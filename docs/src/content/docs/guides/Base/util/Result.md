---
title: Result Guide
description: Practical developer guide for working with Result as an explicit outcome container in Foomp.
---

`Result<A>` provides explicit, typed representation of an operation's outcome: either a `Success` holding a value of type `A`, or a `Failure` holding an `Exception`.

`Result<A>` implements `XorValue<Exception, A>` and `Transmogrifyable<Result<A>>`. Unlike higher-kinded types or full monadic containers, `Result` is a straightforward outcome model focused on value extraction, conversions, pattern matching, and pipeline transmogrification.

---

## When to Use `Result<A>`

* **Explicit Two-Way Outcomes:** Representing whether a method succeeded or encountered an `Exception`.
* **Pattern Matching:** Using Java pattern matching `switch` on `Result.Success` and `Result.Failure`.
* **Safe Value Extraction:** Safely retrieving values with fallback suppliers via `getOrDefault`.
* **Type Conversion:** Converting outcomes into lazy `Either<Exception, A>` or optional `Maybe<A>`.

---

## Examples

### 1. Creating and Inspecting a `Result`

```java title="ResultInspectionExample.java"
import org.quurz.foomp.base.util.Result;

import static org.quurz.foomp.base.util.Result.failure;
import static org.quurz.foomp.base.util.Result.success;

public class ResultInspectionExample {
    public static void main(String[] args) {
        Result<String> ok = success("Operation completed");
        Result<String> err = failure(new IllegalStateException("Service unreachable"));

        System.out.println("ok isSuccess? " + ok.isSuccess()); // true
        System.out.println("ok value: " + ok.getValue()); // Operation completed

        System.out.println("err isFailure? " + err.isFailure()); // true
        System.out.println("err exception: " + err.getException().getMessage()); // Service unreachable
    }
}
```

---

### 2. Fallbacks with `getOrDefault`

```java title="ResultFallbackExample.java"
import org.quurz.foomp.base.util.Result;

import static org.quurz.foomp.base.util.Result.failure;
import static org.quurz.foomp.base.util.Result.success;

public class ResultFallbackExample {
    public static void main(String[] args) {
        Result<Integer> validPort = success(8080);
        Result<Integer> invalidPort = failure(new IllegalArgumentException("Port missing"));

        int port1 = validPort.getOrDefault(() -> 3000);
        int port2 = invalidPort.getOrDefault(() -> 3000);

        System.out.println("Port 1: " + port1); // 8080
        System.out.println("Port 2: " + port2); // 3000
    }
}
```

---

### 3. Pattern Matching on Sealed Types

Because `Result` is a `sealed interface` permitting only `Result.Success` and `Result.Failure`, exhaustive pattern matching is supported by the Java compiler:

```java title="PatternMatchingExample.java"
import org.quurz.foomp.base.util.Result;

public class PatternMatchingExample {
    public static String describeOutcome(Result<Double> result) {
        return switch (result) {
            case Result.Success<Double> s -> "Success: " + s.getValue();
            case Result.Failure<Double> f -> "Failed: " + f.getException().getMessage();
        };
    }
}
```

---

### 4. Converting to `Either` and `Maybe`

```java title="ConversionExample.java"
import org.quurz.foomp.base.util.Either;
import org.quurz.foomp.base.util.Maybe;
import org.quurz.foomp.base.util.Result;

import static org.quurz.foomp.base.util.Result.failure;
import static org.quurz.foomp.base.util.Result.success;

public class ConversionExample {
    public static void main(String[] args) {
        Result<String> res = success("Hello Foomp");

        // Convert to Maybe (Some on success, None on failure)
        Maybe<String> maybe = res.toMaybe();
        System.out.println("Maybe value: " + maybe.get()); // Hello Foomp

        // Convert to lazy Either (Right on success, Left on failure)
        Either<Exception, String> either = res.toEither();
        System.out.println("Either isRight? " + either.isRight()); // true

        Result<String> failedRes = failure(new RuntimeException("Oops"));
        System.out.println("Failed to Maybe isNone? " + failedRes.toMaybe().isNone()); // true
    }
}
```

---

### 5. Fluent Transmogrification

```java title="TransmogrifyExample.java"
import org.quurz.foomp.base.util.Result;

import static org.quurz.foomp.base.util.Result.success;

public class TransmogrifyExample {
    public static void main(String[] args) {
        Result<Integer> result = success(42);

        // Chain with custom external consumer or mapper function
        String report = result.transmogrify(r ->
                r.isSuccess() ? "OK(" + r.getValue() + ")" : "ERR"
        );

        System.out.println(report); // OK(42)
    }
}
```

---

## Best Practices

:::tip[Use Pattern Matching or Conversions for Transformation]
Since `Result` focuses on being a lightweight `XorValue` rather than a full monad, use `switch` expressions or convert to `Either` / `Maybe` when performing complex monadic chains.
:::

:::note[Result vs. Either]
* `Result<A>` fixes the left type to `Exception`, specifically modeling computational success or failure.
* `Either<L, R>` allows arbitrary types on both sides and supports lazy evaluation and monadic operations.
:::
