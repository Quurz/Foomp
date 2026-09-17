---
title: Higher1 Guide
description: Practical guide and design patterns for Rank-1 Higher-Kinded Types (HKTs) in Foomp.
---

import { Aside } from '@astrojs/starlight/components';

Java's type system natively supports first-order generics (e.g. `List<A>`, `Optional<A>`), but does not support **higher-kinded polymorphism**—the ability to abstract over a generic type constructor $F<_>$ regardless of whether $F$ is a `Maybe`, `Task`, `Attempt`, or `Sequence`.

`Higher1<WT, A>` provides the lightweight type-level encoding that makes higher-kinded abstractions possible in Foomp.

---

## The HKT Encoding Problem in Java

In languages like Haskell or Scala, you can write generic functions over type constructors:

```haskell
-- In Haskell: f is a unary type constructor (* -> *)
mapAll :: Functor f => (a -> b) -> f a -> f b
```

In standard Java, you cannot write `class <F<_>>` or `F<A>`. Foomp solves this using **Lightweight Higher-Kinded Polymorphism** (also known as the Gibbons-Hinze encoding / KindedJ pattern):

1. **Witness Type (`WT`)**: A marker type representing the type constructor (e.g. `Maybe.µ`).
2. **Generic Higher Interface**: `Higher1<WT, A>` represents the type constructor `WT` applied to element type `A`.
3. **Safe Narrowing**: A static `narrow` (or `fix`) method safely casts `Higher1<MyType.µ, A>` back to `MyType<A>`.

---

## Anatomy of a Rank-1 HKT in Foomp

Here is how a unary container is modeled with `Higher1`:

```java title="ContainerWithHkt.java"
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;
import org.quurz.foomp.base.annotations.NonNull;

import java.util.Objects;
import java.util.function.Function;

public final class Box<A> implements Higher1<Box.µ, A> {

    // 1. Define the witness marker type
    public static final class µ implements WitnessType {
        private µ() {}
    }

    private final A value;

    public Box(@NonNull A value) {
        this.value = Objects.requireNonNull(value);
    }

    public A getValue() {
        return value;
    }

    // 2. Functor mapping
    public <B> Box<B> map(Function<? super A, ? extends B> mapper) {
        return new Box<>(mapper.apply(this.value));
    }

    // 3. Safe narrowing helper
    @SuppressWarnings("unchecked")
    public static <A> Box<A> narrow(@NonNull Higher1<? extends Box.µ, A> wide) {
        return (Box<A>) Objects.requireNonNull(wide, "wide must not be null");
    }
}
```

---

## Writing Higher-Order Polymorphic Functions

With `Higher1`, you can write algorithms that operate generically over any Rank-1 container that provides a functor or applicative capability:

```java title="GenericHktOperations.java"
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;
import org.quurz.foomp.base.types.Appliable;
import org.quurz.foomp.base.util.Maybe;
import org.quurz.foomp.base.util.Attempt;

import java.util.function.Function;

public class GenericHktOperations {

    // Generic pipeline step working on any Rank-1 Appliable container
    public static <WT extends WitnessType, A, B> Higher1<WT, B> transform(
            Appliable<WT, A> container,
            Function<A, B> mapper) {
        return container.map(mapper);
    }

    public static void main(String[] args) {
        Maybe<Integer> maybeVal = Maybe.just(21);
        Attempt<Integer> attemptVal = Attempt.success(21);

        // Operates generically across different Rank-1 containers
        Higher1<Maybe.µ, Integer> transformedMaybe = transform(maybeVal, n -> n * 2);
        Higher1<Attempt.µ, Integer> transformedAttempt = transform(attemptVal, n -> n * 2);

        // Narrow back to concrete types
        Maybe<Integer> resultMaybe = Maybe.narrow(transformedMaybe);
        Attempt<Integer> resultAttempt = Attempt.narrow(transformedAttempt);

        System.out.println("Maybe: " + resultMaybe.getValue());     // 42
        System.out.println("Attempt: " + resultAttempt.getValue()); // 42
    }
}
```

---

## Best Practices

:::tip[Arity & Type Parameter Alignment]
* `Higher1` is strictly for **Rank-1** constructors ($* \to *$) carrying a single generic argument.
* For binary constructors ($* \to * \to *$, like `Either<L, R>` or `Tuple2<A, B>`), use [`Higher2`](/guides/higher/higher/higher2/).
* Always annotate static `narrow` helpers with `@NonNull` parameter constraints to prevent runtime `NullPointerException` errors.
:::
