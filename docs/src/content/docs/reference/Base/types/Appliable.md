---
title: Appliable<WT, A>
description: Formal API reference, category theory background, applicative functor contract, and Higher1 integration for Appliable in Foomp.
---

`org.quurz.foomp.base.types.Appliable<WT, A>`

`Appliable` is the **Rank-1 Applicative Functor** contract in Foomp. It allows applying a function wrapped inside a higher-kinded context (`Higher1<WT, Function<A, B>>`) to a value wrapped inside the same context (`Higher1<WT, A>`), yielding a transformed wrapped value (`Higher1<WT, B>`).

```java
public interface Appliable<WT extends WitnessType, A>
```

`Appliable` forms the core bridge between standard functors ([`Mappable`](/reference/base/types/mappable/)) and full monads ([`Monadic`](/reference/base/types/monadic/)).

---

## Category Theory & Conceptual Background

In functional programming and category theory, an **Applicative Functor** generalizes functors by allowing functions themselves to be contained within an effectful context:

$$\text{applyTo} \colon \mathcal{F}(A \to B) \to \mathcal{F}(A) \to \mathcal{F}(B)$$

While `Mappable.map` applies a plain function $(A \to B)$ to $\mathcal{F}(A)$, `Appliable.applyTo` takes a function that is already encapsulated inside $\mathcal{F}$ and applies it to the encapsulated value $\mathcal{F}(A)$.

### Laws of Applicative Functors

Implementations of `Appliable` should adhere to the standard applicative laws (where $\text{pure}(x)$ lifts a value into the context):

1. **Identity:** $\text{pure}(\text{id}) \circledast v \equiv v$
2. **Homomorphism:** $\text{pure}(f) \circledast \text{pure}(x) \equiv \text{pure}(f(x))$
3. **Interchange:** $u \circledast \text{pure}(y) \equiv \text{pure}(f \mapsto f(y)) \circledast u$
4. **Composition:** $\text{pure}(\circ) \circledast u \circledast v \circledast w \equiv u \circledast (v \circledast w)$

---

## Method Specification

### `applyTo`

```java
<B> @NonNull Higher1<? extends WT, B> applyTo(
    final @NonNull Higher1<? extends WT, ? extends Function<? super A, ? extends B>> transformation
);
```

Applies the function(s) inside the `transformation` context to the value(s) carried by this container.

#### Parameters
* **`transformation`**: A `Higher1` structure carrying the function `A -> B`. Must not be `null`, and must not carry a `null` function reference.
* **`<B>`**: The target type produced by the function transformation.

#### Return Value
* Returns a `@NonNull Higher1<? extends WT, B>` representing the result within the same higher-kinded constructor shape.

#### Exceptions
* `NullPointerException` if `transformation` is `null` or if unwrapping reveals a `null` function.

---

## Contract & Guarantees

1. **Constructor Shape Preservation:** The resulting higher-kinded value always preserves the witness type `WT` of the original container.
2. **Strict Null-Safety:** Neither the `transformation` wrapper nor the carried functions may be `null`.
3. **Variance Flexibility:** The method accepts covariance on the witness type (`? extends WT`), contravariance on the function argument (`? super A`), and covariance on the function result (`? extends B`).

---

## Implementing Containers in Foomp

`Appliable` is directly or indirectly implemented by all major monadic containers in Foomp:

| Container | Short Description |
| :--- | :--- |
| [`Maybe<A>`](/reference/base/util/maybe/) | Applies a wrapped function if both the function and the target value are `Just(v)`. |
| [`Attempt<A>`](/reference/base/util/attempt/) | Evaluates function application safely, capturing thrown exceptions as `Failure`. |
| [`Task<A>`](/reference/base/util/task/) | Asynchronously coordinates the execution of a wrapped function and a wrapped value. |
| [`Eval<A>`](/reference/base/util/eval/) | Lazily evaluates applicative application with memoization support. |
| [`Sequence<A>`](/reference/base/util/sequence/) | Produces the Cartesian product of applying each wrapped function to each element. |
| [`SeqList<A>`](/reference/base/util/seqlist/) | Strict persistent list applicative evaluation. |
| [`Dictionary<K, V>`](/reference/base/util/dictionary/) | Key-wise applicative function merging over map values. |
| [`Box<A>`](/reference/base/util/box/) | Unconditional applicative application in a strict identity container. |
| [`Provider<A>`](/reference/base/functions/provider/) | Supplier-based applicative evaluation. |

---

## See Also

* [Guide: Applicative Programming with Appliable](/guides/base/types/appliable/) – Practical recipes, combining independent computations, and Cartesian transformations.
* [`Appliable2` Reference](/reference/base/types/appliable2/) – Rank-2 applicative functor for binary type constructors.
* [`Mappable` Reference](/reference/base/types/mappable/) – Functor mapping contract.
* [`Bindable` Reference](/reference/base/types/bindable/) – Monadic bind (`flatMap`) contract.
* [`Monadic` Reference](/reference/base/types/monadic/) – Composite interface uniting `Mappable`, `Appliable`, and `Bindable`.
