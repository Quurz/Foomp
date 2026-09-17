---
title: Ternary Applicatives with Appliable3
description: Practical recipes and examples for 3-way simultaneous type transformations using Appliable3 with Tuple3 and Record3 in Foomp.
---

`Appliable3` brings rank-3 applicative functor operations to 3-element structures (`Tuple3`, `Record3`). It applies three functions packaged inside a `Higher3` container to the three values of the target container simultaneously.

---

## 1. Transforming 3-Tuples in a Single Step

Instead of manually deconstructing a `Tuple3` into individual components, apply three transformation functions in one step:

```java title="Tuple3ApplicativeExample.java"
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.util.Tuple3;
import static org.quurz.foomp.base.util.Tuple3.tuple3;

public class Tuple3Example {
    public static void main(String[] args) {
        // Value container: (userId, username, accountBalance)
        Tuple3<Integer, String, Double> account = tuple3(42, "carol", 1500.50);

        // Transformation container: 3 functions
        Tuple3<Fun<Integer, String>, Fun<String, String>, Fun<Double, String>> formatters = tuple3(
            id -> "#" + id,
            String::toUpperCase,
            bal -> String.format("$%.2f", bal)
        );

        // Transform all three fields simultaneously
        Tuple3<String, String, String> formatted = account.applyTo(formatters);

        System.out.println(formatted); // Tuple3(#42, CAROL, $1500.50)
    }
}
```

---

## 2. Transforming 3D Points / RGB Records

With `Record3`, field names and types are preserved while applying component transformations:

```java title="Record3Example.java"
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.util.Record3;
import static org.quurz.foomp.base.util.Record3.record3;

public class Record3Applicative {
    public static void main(String[] args) {
        // 3D vector (x, y, z)
        Record3<Double, Double, Double> vec = record3("x", 1.0, "y", 2.0, "z", 3.0);

        // Scaling factors
        Record3<Fun<Double, Double>, Fun<Double, Double>, Fun<Double, Double>> scale = record3(
            "xFn", x -> x * 10,
            "yFn", y -> y * 10,
            "zFn", z -> z * 10
        );

        Record3<Double, Double, Double> scaled = (Record3<Double, Double, Double>) vec.applyTo(scale);

        System.out.println(scaled); // Record3(x=10.0, y=20.0, z=30.0)
    }
}
```

---

## See Also

* [`Appliable3` Reference](/reference/base/types/appliable3/) – Formal API reference and type signatures.
* [Guide: Quaternary Applicatives with Appliable4](/guides/base/types/appliable4/) – 4-field component transformations.
* [`Tuple3` Reference](/reference/base/util/tuple3/) – Immutable 3-element tuple.
