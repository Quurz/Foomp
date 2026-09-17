---
title: Zipper Guide
description: Combining and separating parallel collections with Zipper in Foomp.
---

import { Aside } from '@astrojs/starlight/components';

`Zipper` provides classic functional list pairing and unpairing operations. It enables you to easily interleave two collections into a list of tuples, and conversely decompose structured pairs back into distinct parallel collections.

---

## Key Concepts

* **Zipping**: Takes two parallel lists `List<A>` and `List<B>` and produces a `List<Tuple2<A, B>>`.
* **Unzipping**: Takes a `List<Tuple2<A, B>>` and returns a `Tuple2<List<A>, List<B>>`.
* **Truncation**: If two lists have unequal sizes, `zip` automatically stops at the shortest list's end without throwing an exception.

---

## Practical Examples

### 1. Pairing Parallel Data (`zip`)

```java title="ZipperZipExample.java"
import org.quurz.foomp.base.util.Tuple2;
import org.quurz.foomp.base.util.Zipper;

import java.util.List;

public class ZipperZipExample {
    public static void main(String[] args) {
        List<String> users = List.of("Alice", "Bob", "Charlie");
        List<Integer> scores = List.of(95, 82, 88, 100); // 4th element ignored

        List<Tuple2<String, Integer>> leaderboard = Zipper.zip(users, scores);

        leaderboard.forEach(entry ->
                System.out.println(entry.get1() + " -> " + entry.get2())
        );
        // Output:
        // Alice -> 95
        // Bob -> 82
        // Charlie -> 88
    }
}
```

---

### 2. Separating Composite Tuples (`unzip`)

```java title="ZipperUnzipExample.java"
import org.quurz.foomp.base.util.Tuple2;
import org.quurz.foomp.base.util.Zipper;

import java.util.List;

import static org.quurz.foomp.base.util.Tuple2.tuple2;

public class ZipperUnzipExample {
    public static void main(String[] args) {
        List<Tuple2<String, Double>> products = List.of(
                tuple2("Keyboard", 49.99),
                tuple2("Mouse", 29.99),
                tuple2("Monitor", 199.99)
        );

        Tuple2<List<String>, List<Double>> separated = Zipper.unzip(products);

        List<String> names = separated.get1();
        List<Double> prices = separated.get2();

        System.out.println("Names: " + names);   // [Keyboard, Mouse, Monitor]
        System.out.println("Prices: " + prices); // [49.99, 29.99, 199.99]
    }
}
```

---

## When to Use `Zipper`

* **Indexed Processing**: Pair items with their numerical indices: `Zipper.zip(items, indices)`.
* **Table/Column Decomposition**: Transform row-based tuple datasets into separate column lists for reporting or plotting.
* **Correlated Processing**: Combine distinct event streams or coordinate lists before processing them in functional pipelines.
