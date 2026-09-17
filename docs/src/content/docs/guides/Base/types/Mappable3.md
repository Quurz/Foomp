---
title: Working with Mappable3
description: Practical examples and trifunctor transformation patterns using Mappable3 in Foomp.
---

import { Aside, Tabs, TabItem } from '@astrojs/starlight/components';

`Mappable3<WT, A1, A2, A3>` enables functional transformations across three independent type channels. It is the trifunctor interface behind 3-element structures such as `Tuple3` and `Record3`.

---

## The Trifunctor Architecture

With `Mappable3`, you can transform each position individually or apply a combined transformation:

```
  Mappable3<A1, A2, A3> ─── map1(f) ───────────> Mappable3<B1, A2, A3>
  Mappable3<A1, A2, A3> ─── map2(g) ───────────> Mappable3<A1, B2, A3>
  Mappable3<A1, A2, A3> ─── map3(h) ───────────> Mappable3<A1, A2, B3>
  Mappable3<A1, A2, A3> ─── mapAll(f, g, h) ───> Mappable3<B1, B2, B3>
```

---

## Practical Examples

### 1. Mapping Tuples (`Tuple3<A1, A2, A3>`)

Work with heterogeneous 3-tuples, such as database rows, HTTP request metadata, or coordinates:

```java title="Tuple3MappingExample.java"
import org.quurz.foomp.base.util.Tuple3;
import static org.quurz.foomp.base.util.Tuple3.tuple3;

public class Tuple3MappingExample {

    public static void main(String[] args) {
        // A tuple holding (Method, Path, StatusCode)
        Tuple3<String, String, Integer> request = tuple3("get", "/api/v1/users", 200);

        // Normalize method to uppercase
        Tuple3<String, String, Integer> upper = request.map1(String::toUpperCase);
        System.out.println(upper); // Tuple3(GET, /api/v1/users, 200)

        // Increment or adjust status code
        Tuple3<String, String, Integer> adjusted = request.map3(status -> status + 1);
        System.out.println(adjusted); // Tuple3(get, /api/v1/users, 201)

        // Map all 3 values simultaneously
        Tuple3<String, String, String> stringified = request.mapAll(
            String::toUpperCase,
            path -> "https://example.com" + path,
            status -> "HTTP " + status
        );
        System.out.println(stringified);
        // Tuple3(GET, https://example.com/api/v1/users, HTTP 200)
    }
}
```

---

### 2. 3D Coordinates & Vector Transformations

Given a 3D coordinate represented as `Record3<Double, Double, Double>`:

```java title="Vector3MappingExample.java"
import org.quurz.foomp.base.util.Record3;
import static org.quurz.foomp.base.util.Record3.record3;

public class Vector3MappingExample {

    public static void main(String[] args) {
        Record3<Double, Double, Double> point = record3(1.0, 2.0, 3.0);

        // Scale each axis independently
        Record3<Double, Double, Double> scaled = point.mapAll(
            x -> x * 2.0,
            y -> y * 0.5,
            z -> z + 10.0
        );

        System.out.println(scaled); // Record3[value1=2.0, value2=1.0, value3=13.0]
    }
}
```

---

## Summary of Operations

| Method | Target Parameter | Other Parameters |
| :--- | :--- | :--- |
| `map1(f)` | `A1` $\to$ `B1` | `A2`, `A3` preserved |
| `map2(g)` | `A2` $\to$ `B2` | `A1`, `A3` preserved |
| `map3(h)` | `A3` $\to$ `B3` | `A1`, `A2` preserved |
| `mapAll(f, g, h)` | `A1`, `A2`, `A3` $\to$ `B1`, `B2`, `B3` | None (all mapped simultaneously) |

---

## Best Practices

:::tip[Single Pass Evaluation]
Always use `mapAll(f, g, h)` instead of chaining `.map1(f).map2(g).map3(h)` when all three fields need modification to avoid intermediate object allocations.
:::
