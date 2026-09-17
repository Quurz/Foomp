---
title: Attempt Guide
description: Practical developer guide for lazy exception-safe functional pipelines using Attempt in Foomp.
---

`Attempt<A>` is Foomp's lazy error-handling container. It wraps computations that may throw exceptions into declarative pipelines, deferring execution until you explicitly ask for the outcome.

---

## When to Use `Attempt<A>`

* **Deferred Execution:** Building computation recipes or retry mechanisms without running them immediately at creation time.
* **Checked Exception Pipelines:** Interoperating with throwing standard Java APIs (such as `java.io`, `java.nio`, SQL, or JSON parsers) without nested `try-catch` blocks.
* **Railway-Oriented Error Handling:** Seamlessly composing operations where any step may fail, with elegant recovery paths via `onFailureRecover`.

---

## Examples

### 1. Basic Throwing Pipeline with `mapUnsafe`

You can start with a known seed value using `Attempt.attempt(...)` and apply throwing operations via `mapUnsafe`:

```java title="AttemptBasicExample.java"
import org.quurz.foomp.base.util.Attempt;
import org.quurz.foomp.base.util.Result;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.quurz.foomp.base.util.Attempt.attempt;

public class AttemptBasicExample {
    public static void main(String[] args) {
        // Build a lazy pipeline: seed path -> read file -> parse integer
        Attempt<Integer> portAttempt = attempt("server-config.txt")
            .map(Path::of)
            .mapUnsafe(Files::readString)
            .map(String::trim)
            .mapUnsafe(Integer::parseInt);

        // Computation hasn't run yet!
        // Now we evaluate it safely with tryIt():
        Result<Integer> outcome = portAttempt.tryIt();

        if (outcome.isSuccess()) {
            System.out.println("Configured port: " + outcome.getValue());
        } else {
            System.err.println("Failed to read port: " + outcome.getException().getMessage());
        }
    }
}
```

---

### 2. Error Recovery with `onFailureRecover`

You can provide fallback suppliers or functions that take the thrown `Exception` and supply an alternate value:

```java title="AttemptRecoveryExample.java"
import org.quurz.foomp.base.util.Attempt;
import org.quurz.foomp.base.util.Result;

import static org.quurz.foomp.base.util.Attempt.attempt;

public class AttemptRecoveryExample {
    public static void main(String[] args) {
        Attempt<Integer> safePort = attempt("invalid-number")
            .mapUnsafe(Integer::parseInt)
            // Recover based on exception type or provide a default fallback
            .onFailureRecover(ex -> {
                System.out.println("Encountered " + ex.getClass().getSimpleName() + ", falling back to default 8080");
                return 8080;
            });

        Result<Integer> result = safePort.tryIt();
        System.out.println("Final Port: " + result.getValue()); // 8080
    }
}
```

---

### 3. Chaining Dependent Computations with `flatMapUnsafe`

When an intermediate step itself returns an `Attempt`, use `flatMap` or `flatMapUnsafe`:

```java title="AttemptFlatMapExample.java"
import org.quurz.foomp.base.util.Attempt;
import org.quurz.foomp.base.util.Result;

import static org.quurz.foomp.base.util.Attempt.attempt;

public class AttemptFlatMapExample {

    static Attempt<String> fetchUserData(String userId) {
        return attempt(userId).mapUnsafe(id -> {
            if ("admin".equals(id)) {
                return "{\"role\": \"ADMIN\", \"access\": 99}";
            }
            throw new IllegalArgumentException("Unknown user ID: " + id);
        });
    }

    static Attempt<Integer> parseAccessLevel(String json) {
        return attempt(json).mapUnsafe(raw -> {
            if (raw.contains("\"access\": 99")) {
                return 99;
            }
            return 0;
        });
    }

    public static void main(String[] args) {
        Attempt<Integer> pipeline = attempt("admin")
            .flatMapUnsafe(AttemptFlatMapExample::fetchUserData)
            .flatMapUnsafe(AttemptFlatMapExample::parseAccessLevel);

        Result<Integer> result = pipeline.tryIt();
        System.out.println("Access Level: " + result.getValue()); // 99
    }
}
```

---

### 4. Observing Failures with `peekFailureLazy`

Use `peekFailureLazy` to attach logging or monitoring side effects without altering or forcing evaluation of the lazy pipeline:

```java title="AttemptLoggingExample.java"
import org.quurz.foomp.base.util.Attempt;

import static org.quurz.foomp.base.util.Attempt.attempt;

public class AttemptLoggingExample {
    public static void main(String[] args) {
        Attempt<String> computation = attempt("data.bin")
            .mapUnsafe(file -> {
                throw new java.io.FileNotFoundException("File missing: " + file);
            })
            .peekFailureLazy(ex -> System.err.println("[LOG] Caught error during processing: " + ex.getMessage()))
            .onFailureRecover(() -> "DEFAULT_BACKUP_DATA");

        // The log only prints when tryIt() is called:
        String outcome = computation.tryIt().getValue();
        System.out.println("Outcome: " + outcome);
    }
}
```

---

## Best Practices

:::tip[Best Practices]
* **`Attempt<A>` vs `Result<A>`:** Use `Attempt<A>` to construct lazy execution pipelines and capture checked exceptions. Use `Result<A>` as the eager value carrier once computation finishes.
* **Keep Side Effects in Recovery / Peek:** Keep transformations in `map` and `mapUnsafe` pure; use `peekFailureLazy` or `peekFailureEager` for non-invasive logging and diagnostics.
:::
