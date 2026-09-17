---
title: UnsafeMonadic Guide
description: Practical guide to using UnsafeMonadic transformations with checked exceptions in Foomp.
---

The `UnsafeMonadic<WT, A>` interface bridges the monadic composition world with Java's checked exception model. It allows monadic pipelines to incorporate throwing lambdas (`Applicable`) directly without wrapping them manually in `try-catch` blocks.

---

## When to Use `UnsafeMonadic`

* **I/O & Network Operations:** When mapping data over files, sockets, or databases where checked exceptions (`IOException`, `SQLException`) occur naturally.
* **Parsing & Reflection:** When converting input formats using throwing third-party libraries.
* **Railway Pipelines with Early Exit:** Composing operations that abort when an intermediate step fails.

---

## Examples

### 1. Exception-Throwing `mapUnsafe`

```java title="MapUnsafeExample.java"
import org.quurz.foomp.base.util.Attempt;
import java.nio.file.Files;
import java.nio.file.Path;

public class MapUnsafeExample {
    public static void main(String[] args) throws Exception {
        Attempt<Path> pathAttempt = Attempt.attempt(() -> Path.of("config.json"));

        // Read string directly with a throwing lambda (Files.readString throws IOException)
        Attempt<String> contentAttempt = Attempt.narrow(
            pathAttempt.mapUnsafe(Files::readString)
        );

        System.out.println("Result: " + contentAttempt.tryIt());
    }
}
```

---

### 2. Monadic Chaining with `flatMapUnsafe`

`flatMapUnsafe` chains multiple throwing computations where each step returns another monadic container:

```java title="FlatMapUnsafeExample.java"
import org.quurz.foomp.base.util.Attempt;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FlatMapUnsafeExample {

    public static Attempt<String> fetchRemoteConfig(String urlStr) throws Exception {
        return Attempt.narrow(
            Attempt.attempt(() -> URI.create(urlStr))
                .flatMapUnsafe(uri -> {
                    HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
                    HttpResponse<String> response = HttpClient.newHttpClient()
                        .send(request, HttpResponse.BodyHandlers.ofString());
                    return Attempt.attempt(response::body);
                })
        );
    }
}
```

---

## Comparison: `Monadic` vs. `UnsafeMonadic`

| Feature | `Monadic<WT, A>` | `UnsafeMonadic<WT, A>` |
| :--- | :--- | :--- |
| **Function Argument** | `Fun<A, B>` (pure, non-throwing) | `Applicable<A, B>` (allows `throws Exception`) |
| **Method Signatures** | Does not throw checked exceptions | Declares `throws Exception` |
| **Error Handling Model** | Encapsulated in monadic value | Propagated via standard Java exception mechanism |
| **Typical Target** | `Maybe`, `Either`, `Sequence` | `Attempt`, I/O workflows |

---

## Best Practices

:::tip[Safety Recommendations]
* **Prefer Pure `Monadic` for Domain Logic:** Use standard `Monadic` and `Fun` for pure transformations that cannot fail.
* **Use `Attempt.narrow()`:** Since `mapUnsafe` and `flatMapUnsafe` return `Higher1<? extends WT, B>`, use `Attempt.narrow(...)` to cast back to concrete monadic instances safely.
* **Encapsulate at System Boundaries:** Use `UnsafeMonadic` when interacting with external I/O boundaries, then convert to `Result<T>` or `Either<L, R>` for downstream domain processing.
:::
