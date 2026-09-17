---
title: "Swappable<SELF, A, B>"
description: Complete API reference for the Swappable functional interface, enabling component-swapping for pairs, tuples, and either-like structures.
---

`Swappable<SELF, A, B>` is a functional interface for data structures whose two inner components can be exchanged:

$$\text{Swappable}\langle A, B \rangle \xrightarrow{\text{swap()}} \text{Swappable}\langle B, A \rangle$$

:::note[Contract and Type Parameters]
- **`SELF`**: The target implementing type after the swap is performed, bounded by `Swappable<?, B, A>`.
- **`A`**: The first component type before swapping.
- **`B`**: The second component type before swapping.
- **Shape Preservation:** The swap preserves the structural shape and container properties while exchanging the types and positions of its two payload elements.
:::

---

## Type Signature & Definition

```java
package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
public interface Swappable<SELF extends Swappable<?, B, A>, A, B> {

    /**
     * Returns this structure with its two inner components swapped:
     * Swappable<A, B> -> Swappable<B, A>.
     *
     * @return a non-null instance with A and B exchanged
     */
    @NonNull SELF swap();
}
```

---

## Standard Implementations in Foomp

| Type | Before Swap | After Swap (`.swap()`) | Description |
| :--- | :--- | :--- | :--- |
| **`Tuple2<A1, A2>`** | `Tuple2<A1, A2>` | `Tuple2<A2, A1>` | Exposes `(a1, a2)` as `(a2, a1)`. |
| **`Either<L, R>`** | `Either<L, R>` | `Either<R, L>` | Inverts branches: `Left(l) -> Right(l)` and `Right(r) -> Left(r)`. |

---

## Category & Algebraic Properties

1. **Involution (Self-Inverse):** Swapping twice returns an isomorphic structure with original types:
   $$\text{swap}(\text{swap}(x)) \cong x$$
2. **Symmetry:** Facilitates conversions between dual operations (e.g. key-value indexing, left/right error channels).
