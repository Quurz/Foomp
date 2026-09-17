---
title: Foldable<A>
description: API reference for Foldable, FoldableLeft, and FoldableRight interfaces in Foomp.
---

The `Foldable<A>` interface defines the contract for data structures whose elements can be reduced into a single summary value via left- and right-associative folds.

```java
package org.quurz.foomp.base.types;

public interface Foldable<A>
        extends FoldableLeft<A>,
                FoldableRight<A> {
}
```

---

## Type Hierarchy & Sub-Interfaces

`Foldable<A>` unifies two complementary folding paradigms:

```
        FoldableLeft<A>       FoldableRight<A>
               ▲                     ▲
               │                     │
               └──────────┬──────────┘
                          │
                     Foldable<A>
```

### 1. `FoldableLeft<A>`

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

- Traverses elements **from left to right** (head to tail).
- Accumulator signature: `(B accumulator, A currentElement) -> B newAccumulator`.
- Ideal for tail-recursive or iterative accumulation without deep stack recursion.

### 2. `FoldableRight<A>`

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

- Traverses elements **from right to left** (tail to head).
- Accumulator signature: `(A currentElement, B accumulator) -> B newAccumulator`.
- Fundamental for constructing inductive lists or preserving right-associative operations.

---

## Method Specifications

### `foldLeft(init, function)`

```java
<B> @NonNull B foldLeft(final @NonNull B init,
                        final @NonNull BiFunction<? super B, ? super A, ? extends B> function)
```

Folds elements from left to right using an initial accumulator.

- **Parameters:**
  - `init`: The non-null initial accumulator value.
  - `function`: The non-null binary reducer taking `(acc, item)`. Must return non-null.
- **Returns:** The accumulated result of type `B` (never `null`).

### `foldRight(init, function)`

```java
<B> @NonNull B foldRight(final @NonNull B init,
                         final @NonNull BiFunction<? super A, ? super B, ? extends B> function)
```

Folds elements from right to left using an initial accumulator.

- **Parameters:**
  - `init`: The non-null initial accumulator value.
  - `function`: The non-null binary reducer taking `(item, acc)`. Must return non-null.
- **Returns:** The accumulated result of type `B` (never `null`).

---

## Contract & Invariants

1. **Null-Safety**: `init`, `function`, elements, and returned accumulator values must be non-null (`@NonNull`).
2. **Associativity Difference**: For non-commutative operations (e.g. string concatenation or list construction), `foldLeft` and `foldRight` produce different results:
   - Left fold: `(((init ⊕ a) ⊕ b) ⊕ c)`
   - Right fold: `(a ⊕ (b ⊕ (c ⊕ init)))`
3. **Empty Collections**: If the foldable structure is empty, both methods immediately return `init`.

---

## Type Characteristics

| Feature | Details |
| :--- | :--- |
| **Package** | `org.quurz.foomp.base.types` |
| **Kind** | Interface |
| **Super-interfaces** | `FoldableLeft<A>`, `FoldableRight<A>` |
| **Generics** | `A` (element type), `<B>` (accumulator type) |
