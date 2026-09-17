---
title: Appliable2<WT, A1, A2>
description: Formal API reference, rank-2 applicative functor contract, dual-type transformations, and Higher2 integration in Foomp.
---

`org.quurz.foomp.base.types.Appliable2<WT, A1, A2>`

`Appliable2` is the **Rank-2 Applicative Functor** contract in Foomp. It defines simultaneous, contextual function application across binary higher-kinded types (`Higher2<WT, A1, A2>`).

```java
public interface Appliable2<WT extends WitnessType, A1, A2>
```

Conceptually, `Appliable2` applies a pair of wrapped functions $(A_1 \to B_1, A_2 \to B_2)$ to a pair of wrapped values $(A_1, A_2)$ within the shared higher-kinded context `WT`.

---

## Category Theory & Conceptual Background

In higher-order functional programming, rank-2 applicative structures extend the concept of bifunctors:

$$\text{applyTo} \colon \mathcal{F}(A_1 \to B_1, \, A_2 \to B_2) \to \mathcal{F}(A_1, A_2) \to \mathcal{F}(B_1, B_2)$$

This enables independent, component-wise transformations on two carried types simultaneously without unpacking the container.

---

## Method Specification

### `applyTo`

```java
<B1, B2> @NonNull Higher2<WT, B1, B2> applyTo(
    @NonNull final Higher2<
        WT,
        ? extends Function<? super A1, ? extends B1>,
        ? extends Function<? super A2, ? extends B2>
    > transformation
);
```

Applies the two functions carried inside `transformation` to the two values in this binary container.

#### Parameters
* **`transformation`**: A `Higher2` instance carrying functions `A1 -> B1` and `A2 -> B2`. Must not be `null`.
* **`<B1>`**: The new type of the first component after transformation.
* **`<B2>`**: The new type of the second component after transformation.

#### Return Value
* Returns a `@NonNull Higher2<WT, B1, B2>` containing the transformed values in the same context shape.

#### Exceptions
* `NullPointerException` if `transformation` is `null` or if either function contained in the context is `null`.

---

## Implementing Types in Foomp

`Appliable2` is implemented by core binary data structures and tagged containers:

| Container | Applicative Behavior |
| :--- | :--- |
| [`Tuple2<A1, A2>`](/reference/base/util/tuple2/) | Applies $f_1$ to `first` and $f_2$ to `second` to yield `Tuple2<B1, B2>`. |
| [`Record2<A1, A2>`](/reference/base/util/record2/) | Structural binary record applying functions component-wise. |
| [`Either<L, R>`](/reference/base/util/either/) | Applies $f_L$ if `Left`, or $f_R$ if `Right` when given a matching function container. |

---

## See Also

* [Guide: Rank-2 Applicatives with Appliable2](/guides/base/types/appliable2/) – Practical examples, pairing functions, and simultaneous transformations.
* [`Appliable` Reference](/reference/base/types/appliable/) – Unary rank-1 applicative functor interface.
* [`Appliable3` Reference](/reference/base/types/appliable3/) – Ternary rank-3 applicative functor interface.
* [`H2Appliable` Reference](/reference/base/types/h2appliable/) – Rank-2 applicative targeting fixed right-hand types.
