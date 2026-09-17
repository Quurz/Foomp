---
title: Higher2 Guide
description: Practical guide and design patterns for Rank-2 Higher-Kinded Types (HKTs) in Foomp.
---

import { Aside } from '@astrojs/starlight/components';

`Higher2<WT, A, B>` represents **Rank-2 higher-kinded types** ($* \to * \to *$) in Foomp. It enables type-safe abstraction over binary type constructors like disjoint unions (`Either`), pairs (`Pair`), tuples (`Tuple2`), records (`Record2`), and persistent maps (`Dictionary`).

---

## The Rank-2 Type Constructor Pattern

A Rank-2 type constructor requires two generic parameters before becoming a concrete type:

$$\text{Type Constructor } F : * \to * \to *$$

In Foomp, this is encoded using the witness type `WT` and `Higher2<WT, A, B>`:

```java title="BinaryContainerWithHkt.java"
import org.quurz.foomp.higher.Higher2;
import org.quurz.foomp.higher.WitnessType;
import org.quurz.foomp.base.annotations.NonNull;

import java.util.Objects;
import java.util.function.Function;

public final class Pair<A, B> implements Higher2<Pair.µ, A, B> {

    // 1. Witness marker
    public static final class µ implements WitnessType {
        private µ() {}
    }

    private final A first;
    private final B second;

    public Pair(@NonNull A first, @NonNull B second) {
        this.first = Objects.requireNonNull(first);
        this.second = Objects.requireNonNull(second);
    }

    public A getFirst() { return first; }
    public B getSecond() { return second; }

    // 2. Bifunctor mapping
    public <C, D> Pair<C, D> bimap(
            Function<? super A, ? extends C> mapFirst,
            Function<? super B, ? extends D> mapSecond) {
        return new Pair<>(mapFirst.apply(first), mapSecond.apply(second));
    }

    // 3. Narrowing helper
    @SuppressWarnings("unchecked")
    public static <A, B> Pair<A, B> narrow(@NonNull Higher2<? extends Pair.µ, A, B> wide) {
        return (Pair<A, B>) Objects.requireNonNull(wide, "wide must not be null");
    }
}
```

---

## Partial Application & Right-Biased Monads

A common functional pattern is fixing the first type parameter of a `Higher2` (such as the error type $E$ in `Either<E, A>`) to treat it as a unary functor or monad over the second type parameter:

```java title="EitherHktExample.java"
import org.quurz.foomp.higher.Higher2;
import org.quurz.foomp.base.util.Either;

public class EitherHktExample {
    public static void main(String[] args) {
        Either<String, Integer> right = Either.right(42);

        // General Higher2 token
        Higher2<Either.µ, String, Integer> h2 = right;

        // Reify back to concrete type safely
        Either<String, Integer> restored = Either.narrow(h2);

        System.out.println("Value: " + restored.getRight()); // 42
    }
}
```

---

## Practical Applications

* **Bifunctors**: Map both channels simultaneously using `Appliable2` or `bimap`.
* **State & Continuation Passing**: Model computations where one parameter represents state or answer types while the other represents the produced value (`Stateful<S, A>`, `Continuation<A, R>`).
* **Product Types**: Model homogenous or heterogenous 2-element tuples (`Tuple2<A, B>`, `Record2<A, B>`).

---

## Best Practices

:::tip[Narrowing Conventions]
* Always declare the narrowing method as `public static <A, B> ConcreteType<A, B> narrow(Higher2<? extends ConcreteType.µ, A, B> wide)`.
* For right-biased transformations where the left parameter remains invariant, leverage `H2Bindable` or `H2Appliable`.
:::
