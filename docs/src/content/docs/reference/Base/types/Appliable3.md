---
title: Appliable3<WT, A1, A2, A3>
description: Formal API reference, rank-3 applicative functor contract, ternary transformations, and Higher3 integration in Foomp.
---

`org.quurz.foomp.base.types.Appliable3<WT, A1, A2, A3>`

`Appliable3` is the **Rank-3 Applicative Functor** contract in Foomp. It defines simultaneous, contextual function application across ternary higher-kinded types (`Higher3<WT, A1, A2, A3>`).

```java
public interface Appliable3<WT extends WitnessType, A1, A2, A3>
```

Conceptually, `Appliable3` applies a triplet of wrapped functions $(A_1 \to B_1, A_2 \to B_2, A_3 \to B_3)$ to three carried values $(A_1, A_2, A_3)$ within the shared higher-kinded context `WT`.

---

## Category Theory & Conceptual Background

Rank-3 applicative functors generalize trifunctor mapping to contextual transformations:

$$\text{applyTo} \colon \mathcal{F}(A_1 \to B_1, \, A_2 \to B_2, \, A_3 \to B_3) \to \mathcal{F}(A_1, A_2, A_3) \to \mathcal{F}(B_1, B_2, B_3)$$

This provides type-safe, component-wise transformation across three heterogeneous fields without unwrapping intermediate values.

---

## Method Specification

### `applyTo`

```java
<B1, B2, B3> @NonNull Higher3<WT, B1, B2, B3> applyTo(
    @NonNull final Higher3<
        WT,
        ? extends Function<? super A1, ? extends B1>,
        ? extends Function<? super A2, ? extends B2>,
        ? extends Function<? super A3, ? extends B3>
    > transformation
);
```

Applies the three functions carried inside `transformation` to the three values in this ternary container.

#### Parameters
* **`transformation`**: A `Higher3` instance carrying functions `A1 -> B1`, `A2 -> B2`, and `A3 -> B3`. Must not be `null`.
* **`<B1>`**: The new type of the first component after transformation.
* **`<B2>`**: The new type of the second component after transformation.
* **`<B3>`**: The new type of the third component after transformation.

#### Return Value
* Returns a `@NonNull Higher3<WT, B1, B2, B3>` containing the transformed values in the same context shape.

#### Exceptions
* `NullPointerException` if `transformation` is `null` or if any contained function is `null`.

---

## Implementing Types in Foomp

`Appliable3` is implemented by core ternary data structures:

| Container | Applicative Behavior |
| :--- | :--- |
| [`Tuple3<A1, A2, A3>`](/reference/base/util/tuple3/) | Applies $f_1, f_2, f_3$ to `first`, `second`, and `third` respectively. |
| [`Record3<A1, A2, A3>`](/reference/base/util/record3/) | Structural ternary record applying three functions component-wise. |

---

## See Also

* [Guide: Ternary Applicatives with Appliable3](/guides/base/types/appliable3/) – Practical recipes and multi-column record transformations.
* [`Appliable2` Reference](/reference/base/types/appliable2/) – Binary rank-2 applicative functor interface.
* [`Appliable4` Reference](/reference/base/types/appliable4/) – Quaternary rank-4 applicative functor interface.
