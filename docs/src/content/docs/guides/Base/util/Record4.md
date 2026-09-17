---
title: Record4 Guide
description: Practical developer guide for working with the eager, immutable 4-tuple Record4 in Foomp.
---

`Record4<A1, A2, A3, A4>` is an immutable, record-based 4-tuple holding four non-null values with eager evaluation semantics.

---

## When to Use `Record4<A1, A2, A3, A4>`

* **Quads of Immutable Data:** Storing RGBA color values, 4D vectors, bounding boxes (`x`, `y`, `width`, `height`), or multi-column data records.
* **Eager Multi-Component Transformations:** Transforming individual components or all four simultaneously via `map1`, `map2`, `map3`, `map4`, or `mapAll`.
* **Applicative Pipelines:** Applying quads of transformation functions packaged in higher-kinded containers.

---

## Examples

### 1. Basic Creation and Component Access

```java title="Record4BasicExample.java"
import org.quurz.foomp.base.util.Record4;

import static org.quurz.foomp.base.util.Record4.record4;

public class Record4BasicExample {
    public static void main(String[] args) {
        Record4<Integer, Integer, Integer, Double> rgba = record4(255, 128, 0, 0.85);

        System.out.println("Red:   " + rgba.get1()); // 255
        System.out.println("Green: " + rgba.get2()); // 128
        System.out.println("Blue:  " + rgba.get3()); // 0
        System.out.println("Alpha: " + rgba.get4()); // 0.85
        System.out.println("RGBA:  " + rgba);        // Record4[value1=255, value2=128, value3=0, value4=0.85]
    }
}
```

---

### 2. Component Mapping with `mapAll`

```java title="Record4MappingExample.java"
import org.quurz.foomp.base.util.Record4;

import static org.quurz.foomp.base.util.Record4.record4;

public class Record4MappingExample {
    public static void main(String[] args) {
        Record4<String, Integer, Integer, Integer> box = record4("main-box", 0, 0, 100);

        // Scale coordinates and append suffix
        Record4<String, Integer, Integer, Integer> scaled = box.mapAll(
            name -> name + "-scaled",
            x -> x + 10,
            y -> y + 10,
            size -> size * 2
        );

        System.out.println(scaled); // Record4[value1=main-box-scaled, value2=10, value3=10, value4=200]
    }
}
```

---

### 3. Functional Updates with `with1` to `with4`

```java title="Record4WitherExample.java"
import org.quurz.foomp.base.util.Record4;

import static org.quurz.foomp.base.util.Record4.record4;

public class Record4WitherExample {
    public static void main(String[] args) {
        Record4<String, String, Integer, Boolean> employee = record4("John", "Doe", 101, true);

        // Update active status immutably
        Record4<String, String, Integer, Boolean> deactivated = employee.with4(false);

        System.out.println("Active:      " + employee.get4());    // true
        System.out.println("Deactivated: " + deactivated.get4()); // false
    }
}
```

---

### 4. Conversion to `Tuple4` and Transmogrification

```java title="Record4ToTupleExample.java"
import org.quurz.foomp.base.util.Record4;
import org.quurz.foomp.base.util.Tuple4;

import static org.quurz.foomp.base.util.Record4.record4;

public class Record4ToTupleExample {
    public static void main(String[] args) {
        Record4<String, Integer, String, String> server = record4("prod-server", 443, "https", "active");

        // Convert to lazy Tuple4
        Tuple4<String, Integer, String, String> tuple = server.toTuple();

        // Transmogrify to URL string
        String url = server.transmogrify(s -> s.get3() + "://" + s.get1() + ":" + s.get2());
        System.out.println("URL: " + url); // https://prod-server:443
    }
}
```

---

## Best Practices

:::tip[Record4 Guidelines]
* **Strict Non-Null Contracts:** All 4 components must be non-null. If any component can be missing or null, use [`Tuple4`](/reference/base/util/tuple4/) with `Maybe` or define a dedicated domain class.
* **Tuple4 vs. Record4:** Prefer `Record4` for immediate execution and standard Java record ergonomics; use `Tuple4` when constructing deferred transformation chains.
:::
