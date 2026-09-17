---
title: H2Mappable<WT, A, R>
description: API reference for H2Mappable, the Rank-2 functor interface mapping over binary type constructors Higher2 in Foomp.
---

`H2Mappable<WT, A, R>` represents the **Rank-2 Functor** contract in Foomp for binary higher-kinded type constructors (`Higher2<WT, A, R>`).

It defines the standard `map` transformation over the primary type parameter `A`, while preserving the second type parameter `R` (such as a return type or environment/state).

---

## Interface Definition

```java
package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher2;
import org.quurz.foomp.higher.WitnessType;

import java.util.function.Function;

@FunctionalInterface
public interface H2Mappable<WT extends WitnessType, A, R> {

    <B> @NonNull Higher2<WT, B, R> map(
        final @NonNull Function<? super A, ? extends B> transformation
    );
}
```

---

## Type Parameters

| Parameter | Description |
| :--- | :--- |
| `<WT>` | The witness type representing the binary higher-kinded constructor (must extend `WitnessType`). |
| `<A>` | The primary type parameter being mapped. |
| `<R>` | The preserved second type parameter (e.g., answer/result type or state type). |

---

## Functor Laws

Any lawful implementation of `H2Mappable` obeys the two standard Functor Laws for a fixed context `R`:

1. **Identity Law:**  
   Mapping the identity function leaves the container unchanged:
   $$m.\text{map}(x \to x) \equiv m$$

2. **Composition Law:**  
   Mapping two functions sequentially is identical to mapping their composition:
   $$m.\text{map}(f).\text{map}(g) \equiv m.\text{map}(g \circ f)$$

---

## Core Methods

### `map`

```java
<B> @NonNull Higher2<WT, B, R> map(
    final @NonNull Function<? super A, ? extends B> transformation
);
```

* **Purpose:** Applies the transformation `A -> B` to the contained active value and produces a new `Higher2<WT, B, R>` with the exact same secondary type `R`.
* **Null Safety:** `transformation` must not be `null`, and its returned value must not be `null`.
* **Covariance / Contravariance:** Supports `? super A` for function arguments and `? extends B` for return values, enabling smooth subtyping.

---

## Implementations in Foomp

* **`Continuation<R, A>`:** Transforms the intermediate value inside a Continuation-Passing Style workflow before the final continuation is supplied.
* **`Stateful<S, A>`:** Transforms the computation result produced by the state transition function while leaving the underlying state transition semantics intact.

---

## Related Types

* [`H2Bindable`](/reference/base/types/h2bindable/) — Rank-2 Monadic Bind (`flatMap`) over binary type constructors.
* [`H2Appliable`](/reference/base/types/h2appliable/) — Rank-2 Applicative Functor (`applyTo`).
* [`H2Monadic`](/reference/base/types/h2monadic/) — Unified Rank-2 Monad interface.
* [`Mappable`](/reference/base/types/mappable/) — Rank-1 Functor interface for unary type constructors (`Higher1<WT, A>`).
