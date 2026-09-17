---
title: Bindable<WT, A>
description: Formal API reference, category theory background, monadic bind (flatMap) contract, and Higher1 integration for Bindable in Foomp.
---

`org.quurz.foomp.base.types.Bindable<WT, A>`

`Bindable` is the **Rank-1 Monadic Bind** contract in Foomp. It enables sequential chaining of operations that produce values of the same higher-kinded shape (`Higher1<WT, B>`) based on the value wrapped inside the current container.

```java
public interface Bindable<WT extends WitnessType, A>
```

`Bindable` represents the core computation sequencing mechanism ($\gg=$ or `flatMap`) of monads in category theory.

---

## Category Theory & Conceptual Background

In category theory and functional programming, a **Monad** provides the ability to chain operations where each step depends on the output of the previous step:

$$\text{flatMap} \ (\gg=) \colon \mathcal{M}(A) \to (A \to \mathcal{M}(B)) \to \mathcal{M}(B)$$

While a functor (`Mappable.map`) transforms $A \to B$ producing $\mathcal{M}(B)$, applying a function $A \to \mathcal{M}(B)$ with `map` would yield a nested container $\mathcal{M}(\mathcal{M}(B))$. `Bindable.flatMap` combines mapping and flattening in one single operation.

### Monad Laws

Every valid `Bindable` implementation alongside a unit constructor ($\text{pure} \colon A \to \mathcal{M}(A)$) satisfies three fundamental laws:

1. **Left Identity:** $\text{pure}(a) \gg= f \equiv f(a)$
2. **Right Identity:** $m \gg= \text{pure} \equiv m$
3. **Associativity:** $(m \gg= f) \gg= g \equiv m \gg= (x \mapsto f(x) \gg= g)$

---

## Method Specification

### `flatMap`

```java
<B> @NonNull Higher1<? extends WT, B> flatMap(
    final @NonNull Function<? super A, ? extends Higher1<? extends WT, B>> transformation
);
```

Applies the monadic transformation function to the carried value and returns a new higher-kinded instance of the same constructor shape.

#### Parameters
* **`transformation`**: A `@NonNull Function` taking the contained value and returning a new `Higher1` structure of witness type `WT`. Must not return `null`.
* **`<B>`**: The type of the value carried in the resulting container.

#### Return Value
* Returns a `@NonNull Higher1<? extends WT, B>` representing the sequenced computation result.

#### Exceptions
* `NullPointerException` if `transformation` is `null` or if `transformation` returns `null`.

---

## Contract & Guarantees

1. **Short-Circuiting Semantics:** For containers representing failure or absence (such as `Maybe.nothing()` or `Attempt.failure()`), `flatMap` skips executing `transformation` and propagates the empty or failed state directly.
2. **Constructor Invariance:** The resulting container is guaranteed to retain the same witness type `WT`.
3. **Null Hygiene:** The transformation function is strictly prohibited from returning `null`.

---

## Implementing Monads in Foomp

`Bindable` is implemented by all monadic containers in Foomp:

| Monad | `flatMap` Semantics |
| :--- | :--- |
| [`Maybe<A>`](/reference/base/util/maybe/) | Chains computations when `Just(v)`; short-circuits on `Nothing`. |
| [`Attempt<A>`](/reference/base/util/attempt/) | Chains computations when `Success(v)`; short-circuits and preserves exceptions on `Failure`. |
| [`Task<A>`](/reference/base/util/task/) | Asynchronously awaits the first task, then invokes and returns the second task. |
| [`Eval<A>`](/reference/base/util/eval/) | Lazily chains deferred computations with tail-call optimization / stack safety. |
| [`Sequence<A>`](/reference/base/util/sequence/) | Lazily maps each element to a new sequence and concatenates the resulting sequences. |
| [`Either<L, R>`](/reference/base/util/either/) | Right-biased monadic bind; preserves `Left` errors. |
| [`Provider<A>`](/reference/base/functions/provider/) | Lazily composes supplier functions. |
| [`Box<A>`](/reference/base/util/box/) | Unwraps identity box value and applies the transformation. |

---

## See Also

* [Guide: Monadic Pipelines with Bindable](/guides/base/types/bindable/) – Practical sequencing examples, error handling workflows, and flattening nested structures.
* [`Mappable` Reference](/reference/base/types/mappable/) – Functor mapping contract.
* [`Appliable` Reference](/reference/base/types/appliable/) – Applicative functor contract.
* [`Monadic` Reference](/reference/base/types/monadic/) – Unified interface combining `Mappable`, `Appliable`, and `Bindable`.
* [`H2Bindable` Reference](/reference/base/types/h2bindable/) – Rank-2 monadic bind targeting fixed secondary types.
