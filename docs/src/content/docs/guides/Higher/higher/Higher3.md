---
title: Higher3 Guide
description: Practical guide and design patterns for Rank-3 Higher-Kinded Types (HKTs) in Foomp.
---

import { Aside } from '@astrojs/starlight/components';

`Higher3<WT, A, B, C>` represents **Rank-3 higher-kinded types** ($* \to * \to * \to *$) in Foomp. It allows defining polymorphic operations over ternary type constructors such as 3-tuples, 3-element records, and ternary applicative functors.

---

## The Rank-3 Type Constructor Pattern

Ternary constructors take three generic type parameters:

$$\text{Type Constructor } F : * \to * \to * \to *$$

In Foomp, ternary types implement `Higher3<WT, A, B, C>`:

```java title="TernaryContainerWithHkt.java"
import org.quurz.foomp.higher.Higher3;
import org.quurz.foomp.higher.WitnessType;
import org.quurz.foomp.base.annotations.NonNull;

import java.util.Objects;
import java.util.function.Function;

public final class Triplet<A, B, C> implements Higher3<Triplet.µ, A, B, C> {

    // 1. Witness marker
    public static final class µ implements WitnessType {
        private µ() {}
    }

    private final A first;
    private final B second;
    private final C third;

    public Triplet(@NonNull A first, @NonNull B second, @NonNull C third) {
        this.first = Objects.requireNonNull(first);
        this.second = Objects.requireNonNull(second);
        this.third = Objects.requireNonNull(third);
    }

    public A getFirst() { return first; }
    public B getSecond() { return second; }
    public C getThird() { return third; }

    // 2. Element-wise mapping
    public <A2, B2, C2> Triplet<A2, B2, C2> map(
            Function<? super A, ? extends A2> fA,
            Function<? super B, ? extends B2> fB,
            Function<? super C, ? extends C2> fC) {
        return new Triplet<>(fA.apply(first), fB.apply(second), fC.apply(third));
    }

    // 3. Narrowing helper
    @SuppressWarnings("unchecked")
    public static <A, B, C> Triplet<A, B, C> narrow(
            @NonNull Higher3<? extends Triplet.µ, A, B, C> wide) {
        return (Triplet<A, B, C>) Objects.requireNonNull(wide, "wide must not be null");
    }
}
```

---

## Practical Examples with `Tuple3` and `Record3`

Foomp provides two primary Rank-3 product types:
* [`Tuple3<A1, A2, A3>`](/guides/base/util/tuple3/) – Lazy, supplier-backed 3-element tuple.
* [`Record3<A1, A2, A3>`](/guides/base/util/record3/) – Eager, immutable 3-element record.

```java title="Higher3Example.java"
import org.quurz.foomp.higher.Higher3;
import org.quurz.foomp.base.util.Tuple3;
import org.quurz.foomp.base.util.Record3;

import static org.quurz.foomp.base.util.Tuple3.tuple3;
import static org.quurz.foomp.base.util.Record3.record3;

public class Higher3Example {
    public static void main(String[] args) {
        Tuple3<String, Integer, Boolean> tuple = tuple3("Alice", 30, true);
        Record3<String, Integer, Boolean> record = record3("Bob", 25, false);

        // HKT representations
        Higher3<Tuple3.µ, String, Integer, Boolean> hTuple = tuple;
        Higher3<Record3.µ, String, Integer, Boolean> hRecord = record;

        // Reify back
        Tuple3<String, Integer, Boolean> nTuple = Tuple3.narrow(hTuple);
        Record3<String, Integer, Boolean> nRecord = Record3.narrow(hRecord);

        System.out.println("Tuple: " + nTuple.get1() + ", " + nTuple.get2());
        System.out.println("Record: " + nRecord.get1() + ", " + nRecord.get2());
    }
}
```

---

## Best Practices

:::tip[Applicative Functors with Rank-3]
When performing parallel 3-way transformations, use [`Appliable3<WT, A1, A2, A3>`](/reference/base/types/appliable3/) which provides `applyTo` accepting a `Higher3` carrying three transformation functions simultaneously.
:::
