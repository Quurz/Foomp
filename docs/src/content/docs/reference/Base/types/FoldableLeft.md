---
title: FoldableLeft<A>
description: API reference for the FoldableLeft interface in Foomp.
---

The `FoldableLeft<A>` interface defines the contract for data structures that support left-associative folding (eager left-to-right reduction).

```java
package org.quurz.foomp.base.types;

import java.util.function.BiFunction;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface FoldableLeft<A> {

    <B> @NonNull B foldLeft(
        final @NonNull B init,
        final @NonNull BiFunction<? super B, ? super A, ? extends B> function
    );
}
```

---

## Overview & Purpose

In functional programming, a **left fold** (`foldLeft` or `foldl`) decomposes a collection by traversing its elements from left to right (head to tail), applying a binary combining function to an accumulator and each successive element:

$$\text{foldLeft}(z, \oplus, [x_1, x_2, \dots, x_n]) = (((z \oplus x_1) \oplus x_2) \dots \oplus x_n)$$

`FoldableLeft<A>` provides the standard interface across Foomp collections and trees for left-associative reductions with strict `@NonNull` guarantees.

---

## Method Specification

### `foldLeft(init, function)`

```java
<B> @NonNull B foldLeft(
    final @NonNull B init,
    final @NonNull BiFunction<? super B, ? super A, ? extends B> function
)
```

Folds the elements from left to right using an initial accumulator value and a binary combining function.

- **Parameters:**
  - `init`: The initial accumulator value of type `B`. Must not be `null`.
  - `function`: The binary reducer function combining the previous accumulator (`B`) and the current element (`A`). Must not be `null` and must return a non-null `B`.
- **Type Parameters:**
  - `<B>`: The result/accumulator type.
- **Returns:** The final accumulated value of type `B` (never `null`).
- **Throws:** `NullPointerException` if `init`, `function`, any element, or intermediate accumulation result is `null`.

---

## Contract & Invariants

1. **Strict Non-Nullity**:
   - `init` must be `@NonNull`.
   - `function` must be `@NonNull`.
   - `function.apply(acc, element)` must return `@NonNull B`.
2. **Evaluation Order**: Elements are visited strictly in encounter order (head to tail / left-to-right).
3. **Empty Structure Handling**: If the foldable structure contains no elements, `foldLeft` immediately returns `init` without invoking `function`.

---

## Relationship to `Foldable<A>` and `FoldableRight<A>`

```
        FoldableLeft<A>       FoldableRight<A>
               ▲                     ▲
               │                     │
               └──────────┬──────────┘
                          │
                     Foldable<A>
```

- `FoldableLeft<A>` focuses exclusively on left-associative reduction.
- Structures supporting both directions implement the composite `Foldable<A>` interface.

---

## Type Characteristics

| Feature | Details |
| :--- | :--- |
| **Package** | `org.quurz.foomp.base.types` |
| **Kind** | Interface |
| **Null-Safety** | Strictly enforces `@NonNull` for inputs, functions, and returned accumulators |
| **Generics** | `A` (element type), `<B>` (accumulator type) |
| **Sub-interfaces** | `Foldable<A>` |
