---
title: Tuple3<A1, A2, A3> Guide
description: Practical guide to working with lazy immutable 3-tuples, tri-mapping, and conversions in Foomp.
---

import { Aside } from '@astrojs/starlight/components';

`Tuple3<A1, A2, A3>` provides a typed, immutable container for three heterogeneous values. Like all tuples in Foomp, it uses `Supplier` wrappers to support deferred (lazy) evaluation, ensuring that heavy computations or network calls are only executed when values are actually consumed.

---

## Key Features

* **Deferred / Lazy Evaluation**: Components are evaluated on demand when calling `get1()`, `get2()`, `get3()`, or `meld()`.
* **Flexible Transformations**: Map individual elements (`map1`, `map2`, `map3`), all elements together (`mapAll`), or across components (`mapTo1`, `mapTo2`, `mapTo3`).
* **Applicative Application**: Combine 3 functions stored inside another `Tuple3` using `applyTo`.
* **Record Conversion**: Direct conversion to [`Record3`](/reference/base/util/record3/) via `toRecord()`.

---

## Practical Examples

### 1. Creating and Accessing Tuples

```java title="Tuple3BasicsExample.java"
import org.quurz.foomp.base.util.Tuple3;

import static org.quurz.foomp.base.util.Tuple3.tuple3;

public class Tuple3BasicsExample {
    public static void main(String[] args) {
        Tuple3<String, Integer, Boolean> userProfile = tuple3("Alice", 28, true);

        System.out.println("Name: " + userProfile.get1());       // "Alice"
        System.out.println("Age: " + userProfile.get2());        // 28
        System.out.println("Active: " + userProfile.get3());     // true
    }
}
```

---

### 2. Tri-Component Mapping and Melding

```java title="Tuple3MappingExample.java"
import org.quurz.foomp.base.util.Tuple3;

import static org.quurz.foomp.base.util.Tuple3.tuple3;

public class Tuple3MappingExample {
    public static void main(String[] args) {
        Tuple3<String, Integer, Double> product = tuple3("Widget", 5, 19.99);

        // Map all components simultaneously
        Tuple3<String, String, String> formatted = product.mapAll(
                name -> name.toUpperCase(),
                qty -> qty + " pcs",
                price -> "$" + price
        );
        System.out.println(formatted.get1() + " | " + formatted.get2() + " | " + formatted.get3());
        // "WIDGET | 5 pcs | $19.99"

        // Meld into a single summary string
        String summary = product.meld((name, qty, price) ->
                name + ": " + qty + " @ $" + price + " = $" + (qty * price)
        );
        System.out.println(summary); // "Widget: 5 @ $19.99 = $99.95"
    }
}
```

---

### 3. Non-destructive Updates and Conversions

```java title="Tuple3UpdateExample.java"
import org.quurz.foomp.base.util.Record3;
import org.quurz.foomp.base.util.Tuple3;

import static org.quurz.foomp.base.util.Tuple3.tuple3;

public class Tuple3UpdateExample {
    public static void main(String[] args) {
        Tuple3<String, Integer, String> original = tuple3("ID-100", 1, "PENDING");

        // Non-destructive component replacement
        Tuple3<String, Integer, String> updated = original.with3("COMPLETED");
        System.out.println("Original status: " + original.get3()); // PENDING
        System.out.println("Updated status: " + updated.get3());   // COMPLETED

        // Convert to eager Record3
        Record3<String, Integer, String> record = updated.toRecord();
        System.out.println("Record: " + record);
    }
}
```

---

## Best Practices

:::tip[Use with1/with2/with3 for Immutable Updates]
Because `Tuple3` is immutable, `with1`, `with2`, and `with3` produce new tuple instances without evaluating the unchanged deferred suppliers.
:::

:::note[Tuple3 vs Record3]
Use `Tuple3` when you need lazy evaluation, higher-kinded functor/applicative operations (`Higher3`), or cross-component mapping. Use `Record3` when you need an eager, lightweight, nominal data carrier.
:::
