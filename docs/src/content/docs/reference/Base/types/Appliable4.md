---
title: Appliable4<WT, A1, A2, A3, A4>
description: Formal API reference, rank-4 applicative functor contract, quaternary transformations, and Higher4 integration in Foomp.
---

`org.quurz.foomp.base.types.Appliable4<WT, A1, A2, A3, A4>`

`Appliable4` is the **Rank-4 Applicative Functor** contract in Foomp. It defines simultaneous, contextual function application across quaternary higher-kinded types (`Higher4<WT, A1, A2, A3, A4>`).

```java
public interface Appliable4<WT extends WitnessType, A1, A2, A3, A4>
```

Conceptually, `Appliable4` applies a 4-tuple of wrapped functions $(A_1 \to B_1, A_2 \to B_2, A_3 \to B_3, A_4 \to B_4)$ to four carried values $(A_1, A_2, A_3, A_4)$ within the shared higher-kinded context `WT`.

---

## Category Theory & Conceptual Background

Rank-4 applicative functors generalize multi-parameter functor mapping to 4-way contextual transformations:

$$\text{applyTo} \colon \mathcal{F}(A_1 \to B_1, \, A_2 \to B_2, \, A_3 \to B_3, \, A_4 \to B_4) \to \mathcal{F}(A_1, A_2, A_3, A_4) \to \mathcal{F}(B_1, B_2, B_3, B_4)$$

This enables clean component-wise transformations across 4-field structures without manual dismantling.

---

## Method Specification

### `applyTo`

```java
<B1, B2, B3, B4> @NonNull Higher4<WT, B1, B2, B3, B4> applyTo(
    @NonNull final Higher4<
        WT,
        ? extends Function<? super A1, ? extends B1>,
        ? extends Function<? super A2, ? extends B2>,
        ? extends Function<? super A3, ? extends B3>,
        ? extends Function<? super A4, ? extends B4>
    > transformation
);
```

Applies the four functions carried inside `transformation` to the four values in this quaternary container.

#### Parameters
* **`transformation`**: A `Higher4` instance carrying functions `A1 -> B1`, `A2 -> B2`, `A3 -> B3`, and `A4 -> B4`. Must not be `null`.
* **`<B1>`**: The new type of the first component.
* **`<B2>`**: The new type of the second component.
* **`<B3>`**: The new type of the third component.
* **`<B4>`**: The new type of the fourth component.

#### Return Value
* Returns a `@NonNull Higher4<WT, B1, B2, B3, B4>` containing the transformed values in the same context shape.

#### Exceptions
* `NullPointerException` if `transformation` is `null` or if any contained function is `null`.

---

## Implementing Types in Foomp

`Appliable4` is implemented by core 4-ary data structures:

| Container | Applicative Behavior |
| :--- | :--- |
| [`Tuple4<A1, A2, A3, A4>`](/reference/base/util/tuple4/) | Applies $f_1, f_2, f_3, f_4$ to `first`, `second`, `third`, and `fourth` respectively. |
| [`Record4<A1, A2, A3, A4>`](/reference/base/util/record4/) | Structural 4-element record applying four functions component-wise. |

---

## See Also

* [Guide: Quaternary Applicatives with Appliable4](/guides/base/types/appliable4/) – Practical recipes, 4-tuple pipelines, and schema transformations.
* [`Appliable3` Reference](/reference/base/types/appliable3/) – Ternary rank-3 applicative functor interface.
* [`Appliable2` Reference](/reference/base/types/appliable2/) – Binary rank-2 applicative functor interface.
* [`Appliable` Reference](/reference/base/types/appliable/) – Unary rank-1 applicative functor interface.
