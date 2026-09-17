---
title: Record3 Guide
description: Practical developer guide for working with the eager, immutable 3-tuple Record3 in Foomp.
---

`Record3<A1, A2, A3>` is an immutable, record-based 3-tuple designed for holding three non-null values with eager evaluation semantics.

---

## When to Use `Record3<A1, A2, A3>`

* **Triples of Immutable Data:** Storing 3D coordinates, RGB color models, database row triplets, or multi-field keys.
* **Eager Multi-Component Transformations:** Transforming one, two, or all three components immediately via `map1`, `map2`, `map3`, or `mapAll`.
* **Applicative Pipelines:** Combining 3-ary transformations packaged in higher-kinded containers.

---

## Examples

### 1. Basic Creation and Component Access

```java title="Record3BasicExample.java"
import org.quurz.foomp.base.util.Record3;

import static org.quurz.foomp.base.util.Record3.record3;

public class Record3BasicExample {
    public static void main(String[] args) {
        Record3<Integer, Integer, Integer> rgb = record3(255, 128, 0);

        System.out.println("Red:   " + rgb.get1()); // 255
        System.out.println("Green: " + rgb.get2()); // 128
        System.out.println("Blue:  " + rgb.get3()); // 0
        System.out.println("RGB:   " + rgb);        // Record3[value1=255, value2=128, value3=0]
    }
}
```

---

### 2. Component Mapping with `mapAll`

```java title="Record3MappingExample.java"
import org.quurz.foomp.base.util.Record3;

import static org.quurz.foomp.base.util.Record3.record3;

public class Record3MappingExample {
    public static void main(String[] args) {
        Record3<String, String, Integer> record = record3("alice", "active", 42);

        // Map all three components simultaneously
        Record3<String, Boolean, String> mapped = record.mapAll(
            String::toUpperCase,
            status -> "active".equalsIgnoreCase(status),
            score -> "Score: " + score
        );

        System.out.println(mapped); // Record3[value1=ALICE, value2=true, value3=Score: 42]
    }
}
```

---

### 3. Functional Updates with `with1`, `with2`, and `with3`

```java title="Record3WitherExample.java"
import org.quurz.foomp.base.util.Record3;

import static org.quurz.foomp.base.util.Record3.record3;

public class Record3WitherExample {
    public static void main(String[] args) {
        Record3<Double, Double, Double> point = record3(1.0, 2.0, 3.0);

        // Update Z coordinate immutably
        Record3<Double, Double, Double> updatedPoint = point.with3(10.5);

        System.out.println("Original: " + point);        // Record3[value1=1.0, value2=2.0, value3=3.0]
        System.out.println("Updated:  " + updatedPoint); // Record3[value1=1.0, value2=2.0, value3=10.5]
    }
}
```

---

### 4. Conversion to `Tuple3`

```java title="Record3ToTupleExample.java"
import org.quurz.foomp.base.util.Record3;
import org.quurz.foomp.base.util.Tuple3;

import static org.quurz.foomp.base.util.Record3.record3;

public class Record3ToTupleExample {
    public static void main(String[] args) {
        Record3<String, Integer, Double> record = record3("CPU", 4, 3.6);

        // Convert to lazy Tuple3 for deferred computations
        Tuple3<String, Integer, Double> tuple = record.toTuple();

        System.out.println("Tuple item 1: " + tuple.get1()); // CPU
    }
}
```

---

## Best Practices

:::tip[Record3 Guidelines]
* **Strict Non-Null Contracts:** `Record3` rejects `null` inputs at construction and in all mapping functions.
* **Tuple3 vs. Record3:** Use `Record3` when you prefer standard Java record features (concise syntax, value identity) and eager mapping; use `Tuple3` when building lazy transformation chains.
:::
