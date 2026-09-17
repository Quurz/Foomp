---
title: Value2 Guide
description: Practical developer guide for working with the Value2 pair-like value carrier interface in Foomp.
---

The `Value2<A1, A2>` interface defines a common abstraction for containers holding two components. It unifies both **product types** (where both components exist, like `Tuple2` and `Pair`) and **sum types** (where one component exists, like `Either` and `XorValue`).

---

## When to Use `Value2<A1, A2>`

* **Polymorphic Pair Processing:** Writing generic helper functions that operate across tuples, key-value pairs, and disjoint unions.
* **Component Extraction:** Safely checking and extracting component values with `isPresent1()`/`get1()` and `isPresent2()`/`get2()`.
* **JDK Integration:** Treating the first component of a pair directly as a `Supplier<A1>` via `get()`.

---

## Examples

### 1. Unified Processing across Tuples and Either

`Value2` allows inspecting both products and sums through a consistent interface:

```java title="Value2InspectionExample.java"
import org.quurz.foomp.base.types.Value2;
import org.quurz.foomp.base.util.Either;
import org.quurz.foomp.base.util.Tuple2;

public class Value2InspectionExample {

    public static <A, B> void inspectValue2(Value2<A, B> carrier) {
        System.out.println("--- Inspecting Value2 Carrier ---");
        System.out.println("Component 1 Present: " + carrier.isPresent1());
        if (carrier.isPresent1()) {
            System.out.println("Component 1 Value:   " + carrier.get1());
        }

        System.out.println("Component 2 Present: " + carrier.isPresent2());
        if (carrier.isPresent2()) {
            System.out.println("Component 2 Value:   " + carrier.get2());
        }
    }

    public static void main(String[] args) {
        // Product Type (both present)
        Value2<String, Integer> tuple = Tuple2.tuple2("Alice", 30);
        inspectValue2(tuple);

        // Sum Type (Left present, Right absent)
        Value2<String, Integer> leftEither = Either.left("Error 404");
        inspectValue2(leftEither);

        // Sum Type (Left absent, Right present)
        Value2<String, Integer> rightEither = Either.right(200);
        inspectValue2(rightEither);
    }
}
```

---

### 2. Using `Value2` with First Component as Default Supplier

```java title="Value2SupplierExample.java"
import org.quurz.foomp.base.types.Value2;
import org.quurz.foomp.base.util.Tuple2;

public class Value2SupplierExample {
    public static void main(String[] args) {
        Value2<String, Double> product = Tuple2.tuple2("Laptop", 1299.99);

        // get() delegates directly to get1()
        String primaryName = product.get();
        System.out.println("Product name: " + primaryName);
        System.out.println("Product price: " + product.get2());
    }
}
```

---

## Best Practices

:::tip[Working with Sum Types]
* **Always Check Component Presence on Sum Types:** When working with `Either` or `XorValue` cast to `Value2`, calling `get1()` or `get2()` without checking `isPresent1()` / `isPresent2()` will throw a `NoSuchElementException` if that branch is not active.
* **Use Specialized Methods on Concrete Types:** For `Tuple2`, both components are guaranteed present, while `Either` provides ergonomic `.match()` / `.fold()` methods.
:::
