---
title: Mappable<WT, A>
description: API reference for Mappable, the Rank-1 Functor interface mapping transformations over Higher1 containers in Foomp.
---

`Mappable<WT, A>` represents the **Rank-1 Covariant Functor** contract in Foomp for unary higher-kinded type constructors (`Higher1<WT, A>`).

It defines the standard `map` transformation over the carried value type `A`, returning a container of the same constructor shape carrying the transformed value type `B`.

---

## Interface Definition

```java
package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.function.Function;

@FunctionalInterface
public interface Mappable<WT extends WitnessType, A> {

    <B> @NonNull Higher1<? extends WT, B> map(
        final @NonNull Function<? super A, ? extends B> transformation
    );
}
```

---

## Type Parameters

| Parameter | Description |
| :--- | :--- |
| `<WT>` | The witness type representing the unary higher-kinded constructor (must extend `WitnessType`). |
| `<A>` | The type of the value carried by the structure. |

---

## Functor Laws

Every lawful implementation of `Mappable` must satisfy the two fundamental Category Theory Functor Laws:

1. **Identity Law:**  
   Mapping the identity function leaves the structure unchanged:
   $$m.\text{map}(x \to x) \equiv m$$

2. **Composition Law:**  
   Mapping two functions sequentially produces the same result as mapping their composition:
   $$m.\text{map}(f).\text{map}(g) \equiv m.\text{map}(g \circ f)$$

---

## Core Methods

### `map`

```java
<B> @NonNull Higher1<? extends WT, B> map(
    final @NonNull Function<? super A, ? extends B> transformation
);
```

* **Purpose:** Applies the transformation function to the carried value `A` and wraps the result `B` inside the same container constructor.
* **Null Safety:** `transformation` must not be `null`. The returned `Higher1` container must never be `null`.
* **Variance:** Accepts contravariant function input `? super A` and covariant output `? extends B`, returning `Higher1<? extends WT, B>` to support smooth subtyping hierarchies.

---

## Implementations in Foomp

* **[`Maybe<A>`](/reference/base/util/maybe/):** Maps over the present value (`Just`), leaving `Nothing` unmodified.
* **[`Sequence<A>`](/reference/base/util/sequence/):** Lazily maps the transformation across elements in the stream.
* **[`Attempt<A>`](/reference/base/util/attempt/):** Chains transformations over successful computations without executing until evaluated.
* **[`Provider<A>`](/reference/base/functions/provider/):** Lazily transforms supplier output before invocation.

---

## Related Types

* [`Appliable`](/reference/base/types/appliable/) — Rank-1 Applicative Functor applying contextual functions.
* [`Bindable`](/reference/base/types/bindable/) — Rank-1 Monadic Bind (`flatMap`).
* [`Monadic`](/reference/base/types/monadic/) — Unified Rank-1 Monad combining `Mappable`, `Appliable`, and `Bindable`.
* [`H2Mappable`](/reference/base/types/h2mappable/) — Rank-2 Functor for binary higher-kinded containers (`Higher2`).
* [`Mappable2`](/reference/base/types/mappable2/) — Independent/joint mapping over two type parameters.
