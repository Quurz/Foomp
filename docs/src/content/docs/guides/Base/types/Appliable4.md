---
title: Quaternary Applicatives with Appliable4
description: Practical recipes and examples for 4-way simultaneous type transformations using Appliable4 with Tuple4 and Record4 in Foomp.
---

`Appliable4` provides rank-4 applicative functor operations for 4-element structures (`Tuple4`, `Record4`). It applies four functions packaged inside a `Higher4` container to the four values of the target container simultaneously.

---

## 1. Transforming 4-Tuples (e.g. RGBA Color Channels)

`Tuple4.applyTo` applies four functions component-wise:

```java title="Tuple4ApplicativeExample.java"
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.util.Tuple4;
import static org.quurz.foomp.base.util.Tuple4.tuple4;

public class Tuple4Example {
    public static void main(String[] args) {
        // RGBA color tuple (r, g, b, alpha)
        Tuple4<Integer, Integer, Integer, Double> color = tuple4(255, 128, 0, 0.75);

        // Transform channels: hex format RGB, percentage alpha
        Tuple4<Fun<Integer, String>, Fun<Integer, String>, Fun<Integer, String>, Fun<Double, String>> formatters = tuple4(
            r -> String.format("%02X", r),
            g -> String.format("%02X", g),
            b -> String.format("%02X", b),
            a -> String.format("%.0f%%", a * 100)
        );

        Tuple4<String, String, String, String> cssColor = color.applyTo(formatters);

        System.out.println(cssColor); // Tuple4(FF, 80, 00, 75%)
    }
}
```

---

## 2. Transforming 4-Field Named Records

With `Record4`, you can transform 4 heterogeneous fields in a structured record:

```java title="Record4Example.java"
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.util.Record4;
import static org.quurz.foomp.base.util.Record4.record4;

public class Record4Applicative {
    public static void main(String[] args) {
        // User record: (id, username, role, active)
        Record4<Long, String, String, Boolean> user = record4(
            "id", 1001L,
            "username", "admin",
            "role", "superadmin",
            "active", true
        );

        // Transformation functions
        Record4<Fun<Long, String>, Fun<String, String>, Fun<String, String>, Fun<Boolean, String>> serializer = record4(
            "idFn", id -> "USER#" + id,
            "nameFn", String::toUpperCase,
            "roleFn", String::toLowerCase,
            "activeFn", act -> act ? "ENABLED" : "DISABLED"
        );

        Record4<String, String, String, String> serialized = user.applyTo(serializer);

        System.out.println(serialized);
        // Record4(id=USER#1001, username=ADMIN, role=superadmin, active=ENABLED)
    }
}
```

---

## See Also

* [`Appliable4` Reference](/reference/base/types/appliable4/) – Formal method signatures and contract specifications.
* [`Appliable3` Reference](/reference/base/types/appliable3/) – Ternary rank-3 applicative functor interface.
* [`Tuple4` Reference](/reference/base/util/tuple4/) – Immutable 4-element tuple.
