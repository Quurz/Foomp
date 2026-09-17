---
title: Nothing Guide
description: Practical developer guide for the Nothing singleton and unit type patterns in Foomp.
---

`Nothing` provides a concrete singleton unit value in Java. It is used when a generic type parameter or function signature requires a value, but no meaningful data is returned or needed (analogous to `void`, `Unit`, or `()` in other functional languages).

---

## When to Use `Nothing`

* **Unit Type Replacement:** Use `Nothing` in place of `java.lang.Void` when returning a real, instantiable non-null object is required.
* **Result / Either Without Payload:** Use `Result<Nothing>` or `Either<Error, Nothing>` for operations that signal success without producing output.
* **Ex Nihilo Construction:** Use `Nothing.nothing.transmogrify(n -> ...)` to initiate value generation pipelines.

---

## Examples

### 1. Representing Success Without Payload

```java title="NothingAsUnitExample.java"
import org.quurz.foomp.base.util.Nothing;
import org.quurz.foomp.base.util.Result;

import static org.quurz.foomp.base.util.Nothing.nothing;

public class NothingAsUnitExample {

    public static Result<Nothing> writeLog(String message) {
        try {
            System.out.println("[LOG] " + message);
            return Result.success(nothing);
        } catch (Exception e) {
            return Result.failure(e);
        }
    }

    public static void main(String[] args) {
        Result<Nothing> result = writeLog("Application started");

        result.peek(n -> System.out.println("Operation succeeded with: " + n));
    }
}
```

---

### 2. Transmogrification from `Nothing`

```java title="ExNihiloExample.java"
import org.quurz.foomp.base.util.Nothing;

import static org.quurz.foomp.base.util.Nothing.nothing;

public class ExNihiloExample {
    public static void main(String[] args) {
        // Generating a value starting from Nothing
        String greeting = nothing.transmogrify(n -> "Created out of " + n);

        System.out.println(greeting); // Created out of Nothing
    }
}
```

---

## Best Practices

:::tip[Nothing Best Practices]
* **Singleton Usage:** Always reference the static singleton `Nothing.nothing` instead of attempting instantiation.
* **Avoid `null` for Unit:** Prefer `Nothing` over returning `null` when satisfying generic contracts like `Supplier<T>` or `Fun<A, T>` that perform pure side effects.
:::
