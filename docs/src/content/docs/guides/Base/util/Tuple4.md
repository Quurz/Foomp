---
title: Tuple4<A1, A2, A3, A4> Guide
description: Practical guide to working with lazy immutable 4-tuples, quad-mapping, and conversions in Foomp.
---

import { Aside } from '@astrojs/starlight/components';

`Tuple4<A1, A2, A3, A4>` provides a typed, immutable container for four heterogeneous values. By wrapping component retrieval in `Supplier` instances, `Tuple4` defers evaluation until values are explicitly read or combined, enabling high-performance pipeline composition without unnecessary computation.

---

## Key Features

* **Deferred / Lazy Evaluation**: Components are evaluated only when calling `get1()`, `get2()`, `get3()`, `get4()`, or `meld()`.
* **Flexible Transformations**: Map individual components (`map1` .. `map4`), all four components (`mapAll`), or cross-transform across four inputs (`mapTo1` .. `mapTo4`).
* **Applicative Application**: Combine 4 functions stored inside another `Tuple4` using `applyTo`.
* **Record Interop**: Fast conversion to eager immutable [`Record4`](/reference/base/util/record4/) via `toRecord()`.

---

## Practical Examples

### 1. Basic Instantiation and Access

```java title="Tuple4BasicsExample.java"
import org.quurz.foomp.base.util.Tuple4;

import static org.quurz.foomp.base.util.Tuple4.tuple4;

public class Tuple4BasicsExample {
    public static void main(String[] args) {
        Tuple4<String, Integer, Double, Boolean> config =
                tuple4("Server-1", 8080, 0.75, true);

        System.out.println("Host: " + config.get1());      // Server-1
        System.out.println("Port: " + config.get2());      // 8080
        System.out.println("Load: " + config.get3());      // 0.75
        System.out.println("Active: " + config.get4());    // true
    }
}
```

---

### 2. Quad-Component Mapping and Melding

```java title="Tuple4MappingExample.java"
import org.quurz.foomp.base.util.Tuple4;

import static org.quurz.foomp.base.util.Tuple4.tuple4;

public class Tuple4MappingExample {
    public static void main(String[] args) {
        Tuple4<String, String, Integer, Double> item =
                tuple4("SKU-99", "Keyboard", 2, 49.99);

        // Map all components
        Tuple4<String, String, String, String> formatted = item.mapAll(
                sku -> "[" + sku + "]",
                String::toUpperCase,
                qty -> qty + " units",
                price -> "$" + price
        );

        System.out.println(formatted.get1() + " " + formatted.get2());
        // "[SKU-99] KEYBOARD"

        // Meld into a single formatted invoice line
        String invoiceLine = item.meld((sku, desc, qty, price) ->
                String.format("%s: %s x %d @ $%.2f = $%.2f", sku, desc, qty, price, qty * price)
        );
        System.out.println(invoiceLine);
        // "SKU-99: Keyboard x 2 @ $49.99 = $99.98"
    }
}
```

---

### 3. Non-destructive Updates and Conversions

```java title="Tuple4UpdateExample.java"
import org.quurz.foomp.base.util.Record4;
import org.quurz.foomp.base.util.Tuple4;

import static org.quurz.foomp.base.util.Tuple4.tuple4;

public class Tuple4UpdateExample {
    public static void main(String[] args) {
        Tuple4<String, Integer, Double, String> server =
                tuple4("192.168.1.1", 443, 0.10, "ONLINE");

        // Non-destructively update status and load
        Tuple4<String, Integer, Double, String> updated = server
                .with3(0.85)
                .with4("HIGH_LOAD");

        System.out.println("Updated Load: " + updated.get3());     // 0.85
        System.out.println("Updated Status: " + updated.get4());   // HIGH_LOAD

        // Convert to eager Record4
        Record4<String, Integer, Double, String> record = updated.toRecord();
        System.out.println("Record: " + record);
    }
}
```

---

## Best Practices

:::tip[Deferred vs Eager Updates]
Using `with1` .. `with4` constructs a new `Tuple4` where the modified value is passed in without evaluating the untouched components.
:::

:::note[Tuple4 vs Record4]
Use `Tuple4` when deferred evaluation, higher-kinded functor/applicative composition (`Higher4`), or cross-component transformations are required. Use `Record4` when an eager nominal record is needed for standard POJO / DTO serialization.
:::
