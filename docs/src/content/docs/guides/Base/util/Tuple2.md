---
title: Tuple2<A1, A2> Guide
description: Working with lazy immutable 2-tuples, structural transformations, and conversions in Foomp.
---

import { Aside } from '@astrojs/starlight/components';

`Tuple2<A1, A2>` is an immutable, deferred 2-tuple. Unlike standard eager pair types (such as `Pair` or `Record2`), `Tuple2` stores components internally using `Supplier` functions. This guarantees that expensive computations, database calls, or remote queries are only evaluated when the data is explicitly read or unwound.

---

## Key Features

* **Lazy Component Storage**: Components are wrapped in `Supplier`s and evaluated on-demand upon calling `get1()`, `get2()`, or `meld()`.
* **Component-wise Mapping**: Independent or cross-dependent mapping via `map1`, `map2`, `mapAll`, `mapTo1`, and `mapTo2`.
* **Structural Operations**: Swapping elements (`swap()`) and non-destructive replacement (`with1`, `with2`).
* **Conversion Interop**: Fast conversion to eager immutable records (`toRecord()`) or mutable pairs (`toPair()`).

---

## Practical Examples

### 1. Basic Instantiation and Lazy Access

```java title="TupleBasicsExample.java"
import org.quurz.foomp.base.util.Tuple2;

import static org.quurz.foomp.base.util.Tuple2.tuple2;

public class TupleBasicsExample {
    public static void main(String[] args) {
        Tuple2<Integer, String> tuple = tuple2(42, "Foomp");

        // Access components on-demand
        System.out.println("First: " + tuple.get1()); // 42
        System.out.println("Second: " + tuple.get2()); // Foomp

        // Swap components lazily
        Tuple2<String, Integer> swapped = tuple.swap();
        System.out.println("Swapped: " + swapped.get1() + ", " + swapped.get2()); // Foomp, 42
    }
}
```

---

### 2. Independent & Cross-Component Mapping

```java title="TupleMappingExample.java"
import org.quurz.foomp.base.util.Tuple2;

import static org.quurz.foomp.base.util.Tuple2.tuple2;

public class TupleMappingExample {
    public static void main(String[] args) {
        Tuple2<Integer, String> point = tuple2(10, "items");

        // Independent transformations
        Tuple2<Integer, String> modified = point.map1(n -> n * 2); // (20, "items")
        Tuple2<String, Integer> transformedAll = point.mapAll(
                n -> "Count: " + n,
                String::length
        ); // ("Count: 10", 5)

        // Cross-dependent mapping (transform first using both components)
        Tuple2<String, String> crossMapped = point.mapTo1((num, label) -> label + "=" + num);
        System.out.println(crossMapped.get1()); // items=10

        // Melding into a single combined value
        String melded = point.meld((num, label) -> num + " " + label);
        System.out.println("Melded: " + melded); // "10 items"
    }
}
```

---

### 3. Realization & Conversion

```java title="TupleConversionExample.java"
import org.quurz.foomp.base.util.Pair;
import org.quurz.foomp.base.util.Record2;
import org.quurz.foomp.base.util.Tuple2;

import static org.quurz.foomp.base.util.Tuple2.tuple2;

public class TupleConversionExample {
    public static void main(String[] args) {
        Tuple2<String, Integer> tuple = tuple2("Alice", 30);

        // Convert to eager Record2
        Record2<String, Integer> record = tuple.toRecord();
        System.out.println("Record: " + record.get1() + ", " + record.get2());

        // Convert to mutable Pair
        Pair<String, Integer> pair = tuple.toPair();
        pair.set1("Bob");
        System.out.println("Pair first: " + pair.get1()); // Bob

        // Unwind to force evaluation
        Tuple2<String, Integer> unwound = tuple.unwind();
        System.out.println("Unwound: " + unwound);
    }
}
```

---

## When to Use `Tuple2` vs. `Record2` vs. `Pair`

| Feature | `Tuple2<A1, A2>` | `Record2<A1, A2>` | `Pair<A, B>` |
| :--- | :--- | :--- | :--- |
| **Evaluation** | **Lazy** (via `Supplier`) | **Eager** | **Eager** |
| **Mutability** | **Immutable** | **Immutable** | **Mutable** |
| **Witness / HKT** | `Higher2<Tuple2.µ, A1, A2>` | `Higher2<Record2.µ, A1, A2>` | `Higher2<Pair.µ, A, B>` |
| **Primary Use Case** | Deferred pipelines & functional transformations | Eager structured data return | Mutable state & legacy interoperability |
