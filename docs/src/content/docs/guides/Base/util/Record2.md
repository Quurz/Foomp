---
title: Record2 Guide
description: Practical developer guide for working with the eager, immutable 2-tuple Record2 in Foomp.
---

`Record2<A1, A2>` is an immutable, record-based 2-tuple with strict non-null contracts and eager evaluation semantics.

---

## When to Use `Record2<A1, A2>`

* **Lightweight Immutable Pairs:** Grouping two related non-null values with canonical Java record semantics.
* **Eager Transformations:** When you want component mapping operations (`map1`, `map2`, `mapAll`) to execute immediately without deferred thunks.
* **Applicative Computations:** Applying pairs of transformation functions wrapped in higher-kinded structures.
* **Structural Equality:** Leveraging built-in value-based equality and compact syntax.

---

## Examples

### 1. Basic Creation and Component Access

```java title="Record2BasicExample.java"
import org.quurz.foomp.base.util.Record2;

import static org.quurz.foomp.base.util.Record2.record2;

public class Record2BasicExample {
    public static void main(String[] args) {
        Record2<String, Integer> user = record2("Alice", 30);

        System.out.println("Name: " + user.get1()); // Alice
        System.out.println("Age:  " + user.get2()); // 30
        System.out.println("User: " + user);        // Record2[value1=Alice, value2=30]
    }
}
```

---

### 2. Component-Wise Mapping

Transform one or both components eagerly:

```java title="Record2MappingExample.java"
import org.quurz.foomp.base.util.Record2;

import static org.quurz.foomp.base.util.Record2.record2;

public class Record2MappingExample {
    public static void main(String[] args) {
        Record2<String, Integer> product = record2("laptop", 999);

        // Map individual components
        Record2<String, Integer> formatted = product.map1(String::toUpperCase);
        Record2<String, Double> withTax = product.map2(price -> price * 1.19);

        // Map both components simultaneously
        Record2<String, String> summary = product.mapAll(
            name -> "Item: " + name,
            price -> "$" + price
        );

        System.out.println(formatted); // Record2[value1=LAPTOP, value2=999]
        System.out.println(withTax);   // Record2[value1=laptop, value2=1188.81]
        System.out.println(summary);   // Record2[value1=Item: laptop, value2=$999]
    }
}
```

---

### 3. Applicative Application with `applyTo`

Apply functions stored in a `Record2` to another `Record2`:

```java title="Record2ApplicativeExample.java"
import org.quurz.foomp.base.util.Record2;

import java.util.function.Function;

import static org.quurz.foomp.base.util.Record2.record2;

public class Record2ApplicativeExample {
    public static void main(String[] args) {
        Record2<String, Integer> data = record2("foomp", 5);

        // Record containing transformations
        Record2<Function<String, Integer>, Function<Integer, Boolean>> transforms = record2(
            String::length,
            n -> n > 0
        );

        // Apply transformations
        Record2<Integer, Boolean> result = data.applyTo(transforms);

        System.out.println(result); // Record2[value1=5, value2=true]
    }
}
```

---

### 4. Conversion to `Tuple2` and Transmogrification

```java title="Record2ConversionExample.java"
import org.quurz.foomp.base.util.Record2;
import org.quurz.foomp.base.util.Tuple2;

import static org.quurz.foomp.base.util.Record2.record2;

public class Record2ConversionExample {
    public static void main(String[] args) {
        Record2<String, Integer> record = record2("config.yaml", 8080);

        // Convert to lazy Tuple2
        Tuple2<String, Integer> tuple = record.toTuple();

        // Fluent transmogrification
        String endpoint = record.transmogrify(r -> r.get1() + ":" + r.get2());
        System.out.println("Endpoint: " + endpoint); // config.yaml:8080
    }
}
```

---

## Best Practices

:::tip[Record2 vs. Tuple2]
* **Record2 (Eager):** Choose `Record2` for lightweight, immediate computations and native Java record interop.
* **Tuple2 (Lazy):** Choose `Tuple2` when constructing deferred transformation pipelines with chaining and unwinding.
:::
