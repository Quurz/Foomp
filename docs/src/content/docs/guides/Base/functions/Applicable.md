---
title: Working with Applicable
description: A practical guide to handling checked exceptions, wrapping legacy APIs, and building resilient functional pipelines using Applicable.
---

import { Aside, Tabs, TabItem } from '@astrojs/starlight/components';

In standard Java, functional programming often clashes with Java's checked exception model. Passing a method reference that throws `IOException`, `SQLException`, or `ParseException` into `Stream.map()` or `Optional.map()` results in compilation errors, forcing verbose `try-catch` boilerplate.

`Applicable<X, Y>` solves this problem by providing a functional interface that natively embraces `throws Exception`, while offering clean adaptors to convert risky operations into type-safe, non-throwing representations.

---

## The Problem: Checked Exceptions in Functional Java

Consider reading a file or parsing a URI in standard Java:

<Tabs>
  <TabItem label="Standard Java (Cumbersome)">
    ```java
    // ❌ Compilation Error: Unhandled exception java.net.URISyntaxException
    List<URI> uris = urls.stream()
        .map(url -> {
            try {
                return new URI(url);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e); // Boilerplate wrap
            }
        })
        .toList();
    ```
  </TabItem>
  <TabItem label="Foomp Applicable (Clean)">
    ```java
    import org.quurz.foomp.base.functions.Applicable;

    // ✅ Clean declaration allowing checked exceptions
    Applicable<String, URI> parseUri = URI::new;
    ```
  </TabItem>
</Tabs>

---

## Converting Throwing Operations to Safe Values (`safe()`)

Instead of letting exceptions bubble up or converting them into uncontrolled `RuntimeException`s, `Applicable.safe()` transforms any throwing operation into a pure function returning an `XorValue<Exception, Y>`:

```java
import org.quurz.foomp.base.functions.Applicable;
import org.quurz.foomp.base.types.XorValue;

// 1. Define a risky operation (throws checked exception)
Applicable<String, Integer> parseInt = str -> Integer.parseInt(str);

// 2. Convert to a safe function: Fun<String, XorValue<Exception, Integer>>
var safeParser = parseInt.safe();

// 3. Evaluate inputs without try-catch blocks
XorValue<Exception, Integer> valid = safeParser.apply("123");
XorValue<Exception, Integer> invalid = safeParser.apply("abc");

if (valid.isRight()) {
    System.out.println("Parsed number: " + valid.getRight()); // 123
}

if (invalid.isLeft()) {
    System.err.println("Parse failed: " + invalid.getLeft().getMessage());
}
```

:::tip[Functional Either Pattern]
`XorValue.left(Exception)` represents a failure, while `XorValue.right(Value)` represents success. This follows the standard `Either<L, R>` idiom in functional programming.
:::

---

## Integration with Monadic Pipelines (`Attempt` & `Sequence`)

`Applicable` integrates directly with Foomp's container types. For example, `Attempt` provides `mapUnsafe` and `flatMapUnsafe` to compose `Applicable` steps fluently:

```java
import org.quurz.foomp.base.util.Attempt;
import org.quurz.foomp.base.util.Result;
import java.nio.file.Files;
import java.nio.file.Path;

// Chain multiple operations that each may throw checked exceptions
Attempt<String> fileReader = Attempt.tryIt(() -> Path.of("config.json"))
    .mapUnsafe(Files::readString)          // throws IOException
    .mapUnsafe(json -> parseConfig(json)); // throws ParseException

// Execute the chain and receive a Result container
Result<String> result = fileReader.execute();

result.ifSuccess(config -> System.out.println("Loaded: " + config))
      .ifFailure(error -> System.err.println("Failed to load config: " + error.getMessage()));
```

---

## Caching Expensive & Throwing Computations (`MemoisingApplicable`)

When an operation is referentially transparent (pure) but expensive and prone to recoverable errors (e.g., DNS lookup, schema compilation), you can memoize it using `MemoisingApplicable`:

```java
import org.quurz.foomp.base.functions.Applicable;
import org.quurz.foomp.base.functions.MemoisingApplicable;
import java.net.InetAddress;

// 1. Wrap a throwing function
Applicable<String, InetAddress> dnsResolver = 
    MemoisingApplicable.memoisingApplicable(InetAddress::getByName);

// 2. First call performs actual network lookup
InetAddress ip1 = dnsResolver.apply("github.com");

// 3. Subsequent calls return cached value instantly
InetAddress ip2 = dnsResolver.apply("github.com");

// If the lookup throws UnknownHostException, the exception is cached and rethrown
```

:::caution[Memoization & Side Effects]
Only memoize pure operations! Memoizing operations with observable side effects (like writing to a file or database) will cause subsequent calls to skip the side effect.
:::

---

## Choosing the Right Function Type

| Use Case | Recommended Type | Reason |
| :--- | :--- | :--- |
| Single-arg operation throwing **checked exceptions** | `Applicable<X, Y>` | Direct `throws Exception` declaration |
| Pure, non-throwing functional transformations | `Fun<X, Y>` | Extends `Function<X, Y>` with async, compose, currying |
| Zero-argument throwing supplier | `UnsafeProvider<A>` | Extends `Applicable<Nothing, A>` |
| Multi-step exception-safe workflow | `Attempt<A>` / `Task<A>` | Monadic evaluation, async execution |

---

## Summary

* Use `Applicable<X, Y>` when you need a clean, single-argument functional interface that permits checked exceptions.
* Use `.safe()` to convert a throwing `Applicable` into a pure `Fun` yielding `XorValue<Exception, Y>`.
* Combine `Applicable` with `Attempt.mapUnsafe(...)` for declarative, exception-safe data pipelines.
