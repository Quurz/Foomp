---
title: XorValue Guide
description: Practical developer guide for handling disjoint values and safe throwing adaptations with XorValue in Foomp.
---

The `XorValue<L, R>` interface models a disjoint two-way value carrier containing either a `Left` value of type `L` or a `Right` value of type `R`.

It is widely used across Foomp as the lightweight bridge type for safe execution of operations that might throw exceptions (such as `Applicable.safe()`, `UnsafeProvider.safe()`, and `Triable.tryIt()`).

---

## When to Use `XorValue<L, R>`

* **Two-Way Disjoint Results:** Representing computations that yield either a fallback/error (`Left`) or a success value (`Right`).
* **Adapting Checked Exceptions:** Wrapping throwing calls into total functional expressions returning `XorValue<Exception, R>`.
* **Right-Biased Value Processing:** Treating the `Right` value as the primary output via `isPresent()` and `get()`.

---

## Examples

### 1. Creating and Inspecting `XorValue`

```java title="XorValueBasicExample.java"
import org.quurz.foomp.base.types.XorValue;

public class XorValueBasicExample {
    public static void main(String[] args) {
        XorValue<String, Integer> success = XorValue.right(42);
        XorValue<String, Integer> failure = XorValue.left("Out of bounds error");

        // Inspecting success
        if (success.isRight()) {
            System.out.println("Success value: " + success.getRight()); // 42
            System.out.println("Via get():      " + success.get());      // 42
        }

        // Inspecting failure
        if (failure.isLeft()) {
            System.out.println("Left error:     " + failure.getLeft());  // Out of bounds error
            System.out.println("Is present:     " + failure.isPresent()); // false
        }
    }
}
```

---

### 2. Handling Safe Checked Exceptions with `Applicable.safe()`

```java title="SafeApplicableExample.java"
import org.quurz.foomp.base.functions.Applicable;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.types.XorValue;

import java.net.URI;

public class SafeApplicableExample {
    public static void main(String[] args) {
        // Applicable can throw checked URISyntaxException
        Applicable<String, URI> unsafeParser = URI::new;

        // .safe() adapts to Fun returning XorValue<Exception, URI>
        Fun<String, XorValue<Exception, URI>> safeParser = unsafeParser.safe();

        XorValue<Exception, URI> validResult = safeParser.apply("https://foomp.org");
        XorValue<Exception, URI> invalidResult = safeParser.apply("ht tp://invalid uri");

        if (validResult.isRight()) {
            System.out.println("Parsed URI host: " + validResult.getRight().getHost());
        }

        if (invalidResult.isLeft()) {
            System.out.println("Failed to parse: " + invalidResult.getLeft().getMessage());
        }
    }
}
```

---

## Best Practices

:::tip[Working with XorValue]
* **Right is Value:** Always remember that `XorValue` is right-biased: `isPresent()` corresponds to `isRight()` and `get()` corresponds to `getRight()`.
* **Guard `getLeft()` / `getRight()` Calls:** Avoid calling `getLeft()` or `getRight()` without checking `isLeft()` / `isRight()` first, as calling the getter on an empty branch throws `NoSuchElementException`.
:::
