---
title: Higher3
description: API reference for Higher3, the base interface for rank-3 higher-kinded types (HKTs) in Foomp.
---

`Higher3<WT, A, B, C>` is the foundational interface for **Rank-3 higher-kinded types** ($* \to * \to * \to *$) in Foomp. It enables type-level modeling of ternary type constructors such as 3-tuples, 3-element records, and ternary applicative functors in Java.

---

## Type Signature

```java
package org.quurz.foomp.higher;

public interface Higher3<WT extends WitnessType, A, B, C> extends Hkt<WT>
```

### Type Parameters

* **`WT`**: The witness type of the ternary type constructor (must implement [`WitnessType`](/reference/higher/higher/witnesstype/)).
* **`A`**: The first generic type parameter carried by this higher-kinded type.
* **`B`**: The second generic type parameter carried by this higher-kinded type.
* **`C`**: The third generic type parameter carried by this higher-kinded type.

---

## Superinterfaces

* [`Hkt<WT>`](/reference/higher/higher/hkt/) – Base marker interface for all higher-kinded type encodings in Foomp.

---

## Method Summary

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `default int` | `arity()` | Returns the rank (arity) of this higher-kinded type. Always returns `3`. |

---

## Direct Implementations in Foomp

Ternary data structures in Foomp implement `Higher3` using an inner witness marker class `µ`:

* [`Tuple3<A1, A2, A3>`](/reference/base/util/tuple3/) implements `Higher3<Tuple3.µ, A1, A2, A3>`
* [`Record3<A1, A2, A3>`](/reference/base/util/record3/) implements `Higher3<Record3.µ, A1, A2, A3>`

---

## Narrowing & Reification Pattern

Concrete ternary types provide static `narrow` (or `fix`) methods to reify a generic `Higher3` token back to its concrete implementation:

```java
public static <A1, A2, A3> Tuple3<A1, A2, A3> narrow(
        Higher3<? extends Tuple3.µ, A1, A2, A3> wide) {
    return (Tuple3<A1, A2, A3>) Objects.requireNonNull(wide, "wide must not be null");
}
```

---

## See Also

* [`Higher1<WT, A>`](/reference/higher/higher/higher1/) – Rank-1 higher-kinded type interface.
* [`Higher2<WT, A, B>`](/reference/higher/higher/higher2/) – Rank-2 higher-kinded type interface.
* [`Higher4<WT, A, B, C, D>`](/reference/higher/higher/higher4/) – Rank-4 higher-kinded type interface.
* [`Appliable3<WT, A1, A2, A3>`](/reference/base/types/appliable3/) – Rank-3 applicative functor contract.
* [`Tuple3<A1, A2, A3>`](/reference/base/util/tuple3/) – Lazy 3-element product container.
* [`Record3<A1, A2, A3>`](/reference/base/util/record3/) – Eager 3-element product record.
