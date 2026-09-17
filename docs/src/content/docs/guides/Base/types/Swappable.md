---
title: Working with Swappable
description: Practical recipes for exchanging inner elements of pairs, tuples, and either instances using Swappable in Foomp.
---

import { Aside, Tabs, TabItem } from '@astrojs/starlight/components';

The `Swappable<SELF, A, B>` interface provides a uniform `.swap()` operation across binary product and sum types in Foomp.

---

## 1. Swapping Tuples (`Tuple2`)

When working with coordinate systems, key-value mappings, or transformed results, swapping allows effortless axis inversion or dictionary key-value inversion:

```java title="SwapTupleExample.java"
import org.quurz.foomp.base.util.Tuple2;
import static org.quurz.foomp.base.util.Tuple2.tuple2;

public class SwapTupleExample {

    public static void main(String[] args) {
        Tuple2<String, Integer> score = tuple2("Alice", 100);

        // Swap: (String, Integer) -> (Integer, String)
        Tuple2<Integer, String> inverted = score.swap();

        System.out.println("First: " + inverted.get1());   // 100
        System.out.println("Second: " + inverted.get2()); // "Alice"
    }
}
```

---

## 2. Inverting Either Channels (`Either`)

In Railway-Oriented Programming and error handling, `Either<L, R>` typically treats `Left` as the failure channel and `Right` as the success channel. Swapping inverts the roles:

```java title="SwapEitherExample.java"
import org.quurz.foomp.base.util.Either;
import static org.quurz.foomp.base.util.Either.left;
import static org.quurz.foomp.base.util.Either.right;

public class SwapEitherExample {

    public static void main(String[] args) {
        Either<String, Integer> success = right(42);
        Either<Integer, String> swappedSuccess = success.swap();
        // swappedSuccess is now Left(42)

        Either<String, Integer> failure = left("Error message");
        Either<Integer, String> swappedFailure = failure.swap();
        // swappedFailure is now Right("Error message")
    }
}
```

---

## 3. Generic Swappable Helper

You can write generic functions operating over any `Swappable` type without binding directly to `Tuple2` or `Either`:

```java title="GenericSwapExample.java"
import org.quurz.foomp.base.types.Swappable;

public class GenericSwapExample {

    public static <S extends Swappable<?, B, A>, A, B> S swapContainer(Swappable<S, A, B> container) {
        return container.swap();
    }
}
```
