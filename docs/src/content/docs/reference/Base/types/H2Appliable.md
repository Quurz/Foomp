---
title: H2Appliable<WT, A, R>
description: API reference for the H2Appliable higher-kinded applicative functor interface in Foomp.
---

The `H2Appliable<WT, A, R>` interface defines a Rank-2 Applicative Functor contract over a binary type constructor `Higher2<WT, A, R>`. It applies a contextual function to the first type parameter `A` while fixing and preserving the second type parameter `R`.

```java
package org.quurz.foomp.base.types;

import java.util.function.Function;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher2;
import org.quurz.foomp.higher.WitnessType;

@FunctionalInterface
public interface H2Appliable<WT extends WitnessType, A, R> {

    <B> @NonNull Higher2<? extends WT, B, R> applyTo(
        final @NonNull Higher2<? extends WT, Function<A, B>, R> transformation
    );
}
```

---

## Overview & Purpose

In category theory and Higher-Kinded Type (HKT) systems, binary type constructors of kind `* -> * -> *` (such as `Continuation<A, R>` or `Stateful<S, A>`) frequently need to operate as applicative functors.

When treating a binary type constructor as an applicative functor over its primary value type parameter `A`, the secondary type parameter `R` (such as the answer type in continuations or the state type in state transformers) remains fixed:

$$\text{applyTo} : F(A \to B, R) \to F(A, R) \to F(B, R)$$

`H2Appliable` is the foundation of `H2Monadic<WT, A, R>` in Foomp, enabling context-aware function application across binary higher-kinded containers.

---

## Method Specification

### `applyTo(transformation)`

```java
<B> @NonNull Higher2<? extends WT, B, R> applyTo(
    final @NonNull Higher2<? extends WT, Function<A, B>, R> transformation
)
```

Applies a contextual function $A \to B$ wrapped inside `transformation` to the value of type `A` encapsulated in `this`, preserving the fixed context type `R`.

- **Parameters:**
  - `transformation`: A `Higher2` instance carrying the pure transformation function `Function<A, B>` in the same context `WT` and fixed parameter `R`. Must not be `null` and must not contain a `null` function.
- **Type Parameters:**
  - `<B>`: The return type of the transformation function and the new first type parameter of the resulting `Higher2`.
- **Returns:** A non-null `Higher2<? extends WT, B, R>` containing the result of type `B`.
- **Throws:** `NullPointerException` if `transformation` or the function inside it is `null`.

---

## Type Parameters

| Parameter | Bound | Role |
| :--- | :--- | :--- |
| `WT` | `extends WitnessType` | The higher-kinded witness type tagging the binary container family. |
| `A` | Unbounded | The input type encapsulated in this instance and consumed by the applied function. |
| `R` | Unbounded | The preserved/fixed second type parameter (e.g., answer type, state type, or error type). |

---

## Implementations in Foomp

Key binary higher-kinded structures implementing `H2Appliable` (via `H2Monadic`):

1. **`Continuation<A, R>`**: The delimited continuation monad, where `A` is the intermediate result and `R` is the final answer type.
2. **`Stateful<S, A>`**: The functional state monad transformer, passing and threading state transitions `S` while transforming values `A`.

---

## Type Characteristics

| Feature | Details |
| :--- | :--- |
| **Package** | `org.quurz.foomp.base.types` |
| **Kind** | Functional Interface (`@FunctionalInterface`) |
| **HKT Rank** | Rank-2 (`Higher2<WT, A, R>`) |
| **Null-Safety** | Strictly enforces `@NonNull` for inputs, transformation functions, and outputs |
| **Related Interfaces** | `H2Mappable<WT, A, R>`, `H2Bindable<WT, A, R>`, `H2Monadic<WT, A, R>`, `Appliable<WT, A>` |
