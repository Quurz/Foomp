---
title: Rank-2 Applicatives with Appliable2
description: Practical recipes and examples for simultaneous dual-type transformations using Appliable2 with Tuple2, Record2, and Either in Foomp.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

`Appliable2` allows you to apply two independent functions to a binary container (`Tuple2`, `Record2`, or `Either`) in a single, type-safe operation.

Instead of manually unpacking the container, modifying each field, and repacking it, `Appliable2.applyTo` encapsulates simultaneous transformations.

---

## 1. Transforming Tuples Component-Wise

When working with `Tuple2<A1, A2>`, `applyTo` applies a tuple of functions `Tuple2<Function<A1, B1>, Function<A2, B2>>` to the values:

```java title="Tuple2ApplicativeExample.java"
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.util.Tuple2;
import static org.quurz.foomp.base.util.Tuple2.tuple2;

public class Tuple2Example {
    public static void main(String[] args) {
        // Value container: (id, name)
        Tuple2<Integer, String> user = tuple2(101, "john_doe");

        // Function container: (int -> String, String -> String)
        Tuple2<Fun<Integer, String>, Fun<String, String>> modifiers = tuple2(
            id -> "ID-" + id,
            String::toUpperCase
        );

        // Apply both functions simultaneously
        Tuple2<String, String> transformed = user.applyTo(modifiers);

        System.out.println(transformed); // Tuple2(ID-101, JOHN_DOE)
    }
}
```

---

## 2. Structural Record Transformations

With `Record2`, `applyTo` works similarly for named binary records:

```java title="Record2Example.java"
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.util.Record2;
import static org.quurz.foomp.base.util.Record2.record2;

public class Record2Applicative {
    public static void main(String[] args) {
        // Coordinate pair (x, y)
        Record2<Double, Double> point = record2("x", 5.0, "y", 12.0);

        // Transformation functions: scale X by 2, scale Y by 3
        Record2<Fun<Double, Double>, Fun<Double, Double>> scale = record2(
            "xFn", x -> x * 2.0,
            "yFn", y -> y * 3.0
        );

        Record2<Double, Double> scaledPoint = point.applyTo(scale);

        System.out.println(scaledPoint); // Record2(x=10.0, y=36.0)
    }
}
```

---

## 3. Bimorphic Branch Transformation in `Either`

In `Either<L, R>`, `applyTo` takes an `Either` carrying functions for both branches:

```java title="EitherApplicativeExample.java"
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.util.Either;
import static org.quurz.foomp.base.util.Either.left;
import static org.quurz.foomp.base.util.Either.right;

public class EitherApplicative {
    public static void main(String[] args) {
        // Function container matching either branch
        Either<Fun<Integer, String>, Fun<Double, String>> formatters = 
            right(val -> String.format("Score: %.2f", val));

        Either<Integer, Double> value = right(94.55);

        // Evaluates the right-side function since both are Right
        Either<String, String> result = value.applyTo(formatters);

        System.out.println(result); // Right("Score: 94.55")
    }
}
```

---

## See Also

* [`Appliable2` Reference](/reference/base/types/appliable2/) – Formal contract and signature specifications.
* [Guide: Ternary Applicatives with Appliable3](/guides/base/types/appliable3/) – 3-field component transformations.
* [`Tuple2` Reference](/reference/base/util/tuple2/) – Immutable 2-element tuple.
