---
title: Working with Mappable2
description: Practical examples and bifunctor transformation patterns using Mappable2 in Foomp.
---

import { Aside, Tabs, TabItem } from '@astrojs/starlight/components';

`Mappable2<WT, A1, A2>` provides bifunctor capabilities, enabling you to transform two independent type parameters separately or together in a single operation.

---

## The Bifunctor Concept

Unlike standard unary functors where only one parameter is mapped, a bifunctor operates over two type channels:

```
  Mappable2<A1, A2> ─── map1(A1 -> B1) ───> Mappable2<B1, A2>
  Mappable2<A1, A2> ─── map2(A2 -> B2) ───> Mappable2<A1, B2>
  Mappable2<A1, A2> ─── mapAll(f, g)   ───> Mappable2<B1, B2>
```

---

## Practical Examples

### 1. Mapping Pairs (`Pair<A, B>`)

Transform individual elements of key-value pairs or coordinates:

```java title="PairMappingExample.java"
import org.quurz.foomp.base.util.Pair;

public class PairMappingExample {

    public static void main(String[] args) {
        Pair<String, Integer> entry = Pair.of("port", 8080);

        // Map only the first element (Key)
        Pair<String, Integer> upperKey = entry.map1(String::toUpperCase);
        System.out.println(upperKey); // Pair(PORT, 8080)

        // Map only the second element (Value)
        Pair<String, Integer> securePort = entry.map2(p -> p + 443);
        System.out.println(securePort); // Pair(port, 8523)

        // Map both elements simultaneously
        Pair<String, String> formatted = entry.mapAll(
            key -> key + "_config",
            val -> "port:" + val
        );
        System.out.println(formatted); // Pair(port_config, port:8080)
    }
}
```

---

### 2. Disjoint Union Transformations (`Either<L, R>`)

With `Either<L, R>`, `Mappable2` allows mapping error and success channels independently:

```java title="EitherMappingExample.java"
import org.quurz.foomp.base.util.Either;

public class EitherMappingExample {

    public static void main(String[] args) {
        Either<Integer, String> rightSuccess = Either.right("hello world");
        Either<Integer, String> leftError = Either.left(404);

        // mapAll transforms Left or Right depending on which branch is populated
        Either<String, String> mappedSuccess = rightSuccess.mapAll(
            errCode -> "Error: " + errCode,
            str -> str.toUpperCase()
        );
        System.out.println(mappedSuccess); // Right(HELLO WORLD)

        Either<String, String> mappedError = leftError.mapAll(
            errCode -> "Error code: " + errCode,
            str -> str.toUpperCase()
        );
        System.out.println(mappedError); // Left(Error code: 404)
    }
}
```

---

## Comparison of Mapping Methods

| Method | Target | Other Parameter | Use Case |
| :--- | :--- | :--- | :--- |
| `map1(f)` / `map(f)` | `A1` | Kept unchanged (`A2`) | Updating keys, error types, or first coordinate |
| `map2(g)` | `A2` | Kept unchanged (`A1`) | Updating payload values, success cases, or second coordinate |
| `mapAll(f, g)` | `A1` and `A2` | Both transformed ($B_1, B_2$) | Complete structural data mapping in a single atomic pass |

---

## Best Practices

:::tip[Performance with mapAll]
When you need to transform both elements of a pair/tuple, prefer `mapAll(f, g)` over `.map1(f).map2(g)` to avoid creating an intermediate object.
:::

:::note[Null Safety]
Transformation functions supplied to `map1`, `map2`, or `mapAll` must never return `null`.
:::
