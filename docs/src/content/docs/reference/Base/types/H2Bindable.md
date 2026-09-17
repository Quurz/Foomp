---
title: H2Bindable<WT, A, R>
description: API reference for H2Bindable, the Rank-2 monadic bind contract over binary type constructors Higher2 in Foomp.
---

`H2Bindable<WT, A, R>` represents the **Rank-2 Monadic Bind (flatMap)** contract in Foomp for binary higher-kinded type constructors (`Higher2<WT, A, R>`).

It allows monadic sequencing and chaining of computations over the first type parameter `A`, while preserving the second type parameter `R` (such as a computation return type in `Continuation` or state type in `Stateful`).

---

## Interface Definition

```java
package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher2;
import org.quurz.foomp.higher.WitnessType;

import java.util.function.Function;

@FunctionalInterface
public interface H2Bindable<WT extends WitnessType, A, R> {

    <B> @NonNull Higher2<? extends WT, B, R> flatMap(
        final @NonNull Function<A, ? extends Higher2<? extends WT, B, R>> transformation
    );
}
```

---

## Type Parameters

| Parameter | Description |
| :--- | :--- |
| `<WT>` | The witness type marking the two-parameter higher-kinded constructor (must extend `WitnessType`). |
| `<A>` | The primary type parameter being transformed during monadic binding. |
| `<R>` | The preserved second type parameter (e.g., final computation return type or state type). |

---

## Key Concepts & Category Theory Laws

In category theory, a monad over a binary type constructor $F(-, R)$ acts as a standard monad over the active parameter $A$ for any fixed environment/context $R$.

### Monad Laws

For an instance `m` representing $F(A, R)$ and monadic unit/return function $\eta$:

1. **Left Identity:**  
   $$\eta(a).\text{flatMap}(f) \equiv f(a)$$
2. **Right Identity:**  
   $$m.\text{flatMap}(\eta) \equiv m$$
3. **Associativity:**  
   $$m.\text{flatMap}(f).\text{flatMap}(g) \equiv m.\text{flatMap}(x \to f(x).\text{flatMap}(g))$$

---

## Core Methods

### `flatMap`

```java
<B> @NonNull Higher2<? extends WT, B, R> flatMap(
    final @NonNull Function<A, ? extends Higher2<? extends WT, B, R>> transformation
);
```

* **Purpose:** Monadically sequences computations by passing the unwrapped value `A` to `transformation`, producing a new container `Higher2<WT, B, R>` with the exact same context `R`.
* **Null Safety:** Neither the `transformation` argument nor the returned `Higher2` container may be `null`.
* **Variance:** Accepts upper-bounded type constructors `<? extends WT, B, R>` for maximum flexibility when composing higher-kinded structures.

---

## Implementations in Foomp

* **`Continuation<R, A>`:** Sequences continuation-passing style (CPS) computations with fixed final answer type `R`.
* **`Stateful<S, A>`:** Chains stateful computations that transition state `S` while producing sequential intermediate values `A` and `B`.

---

## Related Types

* [`H2Mappable`](/reference/base/types/h2mappable/) — Rank-2 Functor mapping over the active parameter.
* [`H2Appliable`](/reference/base/types/h2appliable/) — Rank-2 Applicative Functor applying contextual functions.
* [`H2Monadic`](/reference/base/types/h2monadic/) — Unified Rank-2 Monad combining `H2Mappable`, `H2Appliable`, and `H2Bindable`.
* [`Bindable`](/reference/base/types/bindable/) — Rank-1 monadic bind interface for unary containers (`Higher1<WT, A>`).
