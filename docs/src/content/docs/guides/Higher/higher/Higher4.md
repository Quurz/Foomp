---
title: Higher4 Guide
description: Practical guide and design patterns for Rank-4 Higher-Kinded Types (HKTs) in Foomp.
---

import { Aside } from '@astrojs/starlight/components';

`Higher4<WT, A, B, C, D>` represents **Rank-4 higher-kinded types** ($* \to * \to * \to * \to *$) in Foomp. It enables type-level modeling and abstraction over 4-ary type constructors such as 4-tuples, 4-element records, and quaternary applicative functors.

---

## The Rank-4 Type Constructor Pattern

Quaternary type constructors take four generic arguments:

$$\text{Type Constructor } F : * \to * \to * \to * \to *$$

In Foomp, quaternary types implement `Higher4<WT, A, B, C, D>`:

```java title="QuaternaryContainerWithHkt.java"
import org.quurz.foomp.higher.Higher4;
import org.quurz.foomp.higher.WitnessType;
import org.quurz.foomp.base.annotations.NonNull;

import java.util.Objects;
import java.util.function.Function;

public final class Quad<A, B, C, D> implements Higher4<Quad.µ, A, B, C, D> {

    // 1. Witness marker
    public static final class µ implements WitnessType {
        private µ() {}
    }

    private final A a;
    private final B b;
    private final C c;
    private final D d;

    public Quad(@NonNull A a, @NonNull B b, @NonNull C c, @NonNull D d) {
        this.a = Objects.requireNonNull(a);
        this.b = Objects.requireNonNull(b);
        this.c = Objects.requireNonNull(c);
        this.d = Objects.requireNonNull(d);
    }

    public A getA() { return a; }
    public B getB() { return b; }
    public C getC() { return c; }
    public D getD() { return d; }

    // 2. Element-wise mapping
    public <A2, B2, C2, D2> Quad<A2, B2, C2, D2> map(
            Function<? super A, ? extends A2> fA,
            Function<? super B, ? extends B2> fB,
            Function<? super C, ? extends C2> fC,
            Function<? super D, ? extends D2> fD) {
        return new Quad<>(fA.apply(a), fB.apply(b), fC.apply(c), fD.apply(d));
    }

    // 3. Narrowing helper
    @SuppressWarnings("unchecked")
    public static <A, B, C, D> Quad<A, B, C, D> narrow(
            @NonNull Higher4<? extends Quad.µ, A, B, C, D> wide) {
        return (Quad<A, B, C, D>) Objects.requireNonNull(wide, "wide must not be null");
    }
}
```

---

## Practical Examples with `Tuple4` and `Record4`

Foomp provides built-in Rank-4 product types:
* [`Tuple4<A1, A2, A3, A4>`](/guides/base/util/tuple4/) – Lazy, supplier-backed 4-element tuple.
* [`Record4<A1, A2, A3, A4>`](/guides/base/util/record4/) – Eager, immutable 4-element record.

```java title="Higher4Example.java"
import org.quurz.foomp.higher.Higher4;
import org.quurz.foomp.base.util.Tuple4;
import org.quurz.foomp.base.util.Record4;

import static org.quurz.foomp.base.util.Tuple4.tuple4;
import static org.quurz.foomp.base.util.Record4.record4;

public class Higher4Example {
    public static void main(String[] args) {
        Tuple4<String, Integer, Double, Boolean> tuple =
                tuple4("Server-01", 8080, 0.95, true);

        Record4<String, Integer, Double, Boolean> record =
                record4("Server-02", 8081, 0.42, true);

        // HKT representations
        Higher4<Tuple4.µ, String, Integer, Double, Boolean> hTuple = tuple;
        Higher4<Record4.µ, String, Integer, Double, Boolean> hRecord = record;

        // Reify back to concrete types
        Tuple4<String, Integer, Double, Boolean> nTuple = Tuple4.narrow(hTuple);
        Record4<String, Integer, Double, Boolean> nRecord = Record4.narrow(hRecord);

        System.out.println("Tuple host: " + nTuple.get1() + ", port: " + nTuple.get2());
        System.out.println("Record host: " + nRecord.get1() + ", port: " + nRecord.get2());
    }
}
```

---

## Best Practices

:::tip[Applicative Functor Operations]
Use [`Appliable4<WT, A1, A2, A3, A4>`](/reference/base/types/appliable4/) to perform 4-way applicative transformations in parallel across all four components of a `Higher4`.
:::
