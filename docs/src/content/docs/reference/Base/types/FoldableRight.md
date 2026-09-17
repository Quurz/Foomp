---
title: FoldableRight<A>
description: API reference for the FoldableRight interface in Foomp.
---

The `FoldableRight<A>` interface defines the contract for data structures that support right-associative folding (right-to-left reduction).

```java
package org.quurz.foomp.base.types;

import java.util.function.BiFunction;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface FoldableRight<A> {

    <B> @NonNull B foldRight(
        final @NonNull B init,
        final @NonNull BiFunction<? super A, ? super B, ? extends B> function
    );
}
```

---

## Overview & Purpose

In functional programming, a **right fold** (`foldRight` or `foldr`) decomposes a collection from right to left (tail to head), applying a binary combining function to the current element and the rest-of-the-structure's accumulated result:

$$\text{foldRight}(z, \oplus, [x_1, x_2, \dots, x_n]) = (x_1 \oplus (x_2 \oplus \dots (x_n \oplus z)))$$

`FoldableRight<A>` provides the standard interface for right-associative reduction, which is essential for inductive list reconstruction, lazy evaluation, and right-associated algebraic operations.

---

## Method Specification

### `foldRight(init, function)`

```java
<B> @NonNull B foldRight(
    final @NonNull B init,
    final @NonNull BiFunction<? super A, ? super B, ? extends B> function
)
```

Folds the elements from right to left using an initial accumulator value and a binary combining function.

- **Parameters:**
  - `init`: The initial accumulator value of type `B`. Must not be `null`.
  - `function`: The binary reducer function combining the current element (`A`) and the right-hand accumulator (`B`). Must not be `null` and must return a non-null `B`.
- **Type Parameters:**
  - `<B>`: The result/accumulator type.
- **Returns:** The final accumulated value of type `B` (never `null`).
- **Throws:** `NullPointerException` if `init`, `function`, any element, or intermediate accumulation result is `null`.

---

## Key Differences Between `FoldableLeft` and `FoldableRight`

| Aspect | `FoldableLeft<A>` | `FoldableRight<A>` |
| :--- | :--- | :--- |
| **Direction** | Left-to-right ($x_1 \dots x_n$) | Right-to-left ($x_n \dots x_1$) |
| **Reducer Signature** | `BiFunction<B, A, B>` `(acc, item) -> acc` | `BiFunction<A, B, B>` `(item, acc) -> acc` |
| **Grouping / Associativity** | `(((z ⊕ x1) ⊕ x2) ⊕ x3)` | `(x1 ⊕ (x2 ⊕ (x3 ⊕ z)))` |
| **List Construction** | Inverts order without extra steps | Preserves original element order |

---

## Contract & Invariants

1. **Strict Non-Nullity**:
   - `init` must be `@NonNull`.
   - `function` must be `@NonNull`.
   - `function.apply(element, acc)` must return `@NonNull B`.
2. **Evaluation Order**: Elements are reduced starting from the rightmost element back towards the head.
3. **Empty Structure Handling**: If the foldable structure contains no elements, `foldRight` immediately returns `init` without invoking `function`.

---

## Type Characteristics

| Feature | Details |
| :--- | :--- |
| **Package** | `org.quurz.foomp.base.types` |
| **Kind** | Interface |
| **Null-Safety** | Strictly enforces `@NonNull` for inputs, functions, and returned accumulators |
| **Generics** | `A` (element type), `<B>` (accumulator type) |
| **Sub-interfaces** | `Foldable<A>` |
