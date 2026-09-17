---
title: Box Guide
description: Practical developer guide for the single-value Identity container Box in Foomp.
---

`Box<A>` is Foomp's container for single values, implementing the canonical *Identity Monad*. It wraps a guaranteed non-null value, supporting fluent transformations, applicative combinations, and lazy evaluation pipelines.

---

## When to Use `Box<A>`

* **Identity & Monadic Abstractions:** When working with generic functional APIs that require a `Monadic<WT, A>` context without introducing error handling or optionality.
* **Lazy Computation Chains:** Deferring transformations on a known seed value until the result is explicitly extracted.
* **Pairwise Combinations (`zip`):** Combining two independently computed values using a binary operator.

---

## Examples

### 1. Creating and Mapping Values

```java title="BoxBasicExample.java"
import org.quurz.foomp.base.util.Box;

import static org.quurz.foomp.base.util.Box.box;

public class BoxBasicExample {
    public static void main(String[] args) {
        // Wrap a value in a Box
        Box<String> greeting = box("Hello")
            .map(s -> s + ", World!")
            .map(String::toUpperCase);

        // Value is computed upon retrieval
        System.out.println("Result: " + greeting.get()); // HELLO, WORLD!
    }
}
```

---

### 2. Combining Boxes with `zip`

You can combine two separate boxes into a third box using a binary combiner:

```java title="BoxZipExample.java"
import org.quurz.foomp.base.util.Box;

import static org.quurz.foomp.base.util.Box.box;

public class BoxZipExample {
    public static void main(String[] args) {
        Box<String> firstName = box("Ada");
        Box<String> lastName = box("Lovelace");

        // Combine both boxes into a full name
        Box<String> fullName = firstName.zip(lastName, (first, last) -> first + " " + last);

        System.out.println("Full Name: " + fullName.get()); // Ada Lovelace
    }
}
```

---

### 3. Monadic Chaining with `flatMap`

```java title="BoxFlatMapExample.java"
import org.quurz.foomp.base.util.Box;

import static org.quurz.foomp.base.util.Box.box;

public class BoxFlatMapExample {

    static Box<Integer> calculateTax(int subtotal) {
        return box((int) Math.round(subtotal * 0.19));
    }

    public static void main(String[] args) {
        Box<Integer> total = box(100)
            .flatMap(BoxFlatMapExample::calculateTax)
            .map(tax -> 100 + tax);

        System.out.println("Total with Tax: " + total.get()); // 119
    }
}
```

---

### 4. Deep Copying and Unwinding

```java title="BoxCopyUnwindExample.java"
import org.quurz.foomp.base.util.Box;

import static org.quurz.foomp.base.util.Box.box;

public class BoxCopyUnwindExample {
    public static void main(String[] args) {
        Box<Integer> dynamicBox = box(42).map(n -> n * 2);

        // unwind forces evaluation and caches the result
        Box<Integer> eagerBox = dynamicBox.unwind();

        // copy performs deep copy if payload implements Copyable
        Box<Integer> clonedBox = eagerBox.copy();

        System.out.println("Eager value:  " + eagerBox.get());  // 84
        System.out.println("Cloned value: " + clonedBox.get()); // 84
    }
}
```

---

## Best Practices

:::tip[Identity Monad Tips]
* **Lightweight Container:** `Box<A>` has minimal overhead and guarantees presence (`isPresent() == true`).
* **Use `unwind()` for Expensive Lazy Chains:** If a transformation chain is read repeatedly, call `.unwind()` once to evaluate and cache the constant value.
:::
