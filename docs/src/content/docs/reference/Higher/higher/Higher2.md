---
title: Higher2
description: API reference for Higher2, the base interface for rank-2 higher-kinded types (HKTs) in Foomp.
---

`Higher2<WT, A, B>` is the foundational interface for **Rank-2 higher-kinded types** ($* \to * \to *$) in Foomp. It enables type-level modeling of binary type constructors such as bifunctors, disjoint unions, maps, and pairs in standard Java.

---

## Type Signature

```java
package org.quurz.foomp.higher;

public interface Higher2<WT extends WitnessType, A, B> extends Hkt<WT>
```

### Type Parameters

* **`WT`**: The witness type of the binary type constructor (must implement [`WitnessType`](/reference/api-higher-overview/)).
* **`A`**: The first generic type parameter carried by this higher-kinded type (e.g. left type, key type, or first component).
* **`B`**: The second generic type parameter carried by this higher-kinded type (e.g. right type, value type, or second component).

---

## Superinterfaces

* [`Hkt<WT>`](/reference/api-higher-overview/) – Base marker interface for all higher-kinded type encodings in Foomp.

---

## Method Summary

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `default int` | `arity()` | Returns the rank (arity) of this higher-kinded type. Always returns `2`. |

---

## Direct Implementations in Foomp

Binary data structures and functors in Foomp implement `Higher2` using an inner witness marker class `µ`:

* [`Either<L, R>`](/reference/base/util/either/) implements `Higher2<Either.µ, L, R>`
* [`Pair<A, B>`](/reference/base/util/pair/) implements `Higher2<Pair.µ, A, B>`
* [`Tuple2<A1, A2>`](/reference/base/util/tuple2/) implements `Higher2<Tuple2.µ, A1, A2>`
* [`Record2<A1, A2>`](/reference/base/util/record2/) implements `Higher2<Record2.µ, A1, A2>`
* [`Dictionary<K, V>`](/reference/base/util/dictionary/) implements `Higher2<Dictionary.µ, K, V>`
* [`DecisionTree<A, B>`](/reference/base/util/decisiontree/) implements `Higher2<DecisionTree.µ, A, B>`

---

## Narrowing & Reification Pattern

Concrete binary types provide static `narrow` (or `fix`) methods to reify a generic `Higher2` back to its concrete implementation:

```java
public static <L, R> Either<L, R> narrow(Higher2<? extends Either.µ, L, R> wide) {
    return (Either<L, R>) Objects.requireNonNull(wide, "wide must not be null");
}
```

---

## Partial Application

A binary constructor `Higher2<WT, A, B>` can be partially applied to its first argument `A` to form a unary constructor `Higher1` over `B`. For instance, in Foomp's type hierarchy, [`H2Bindable<WT, A, R>`](/reference/base/types/h2bindable/) and [`H2Appliable<WT, A, R>`](/reference/base/types/h2appliable/) allow monadic binding and applicative operations over the right parameter while fixing the left parameter.

---

## See Also

* [`Higher1<WT, A>`](/reference/higher/higher/higher1/) – Rank-1 higher-kinded type interface.
* [`Higher3<WT, A, B, C>`](/reference/higher/higher/higher3/) – Rank-3 higher-kinded type interface.
* [`Higher4<WT, A, B, C, D>`](/reference/higher/higher/higher4/) – Rank-4 higher-kinded type interface.
* [`Appliable2<WT, A, B>`](/reference/base/types/appliable2/) – Rank-2 applicative functor contract.
* [`H2Bindable<WT, A, R>`](/reference/base/types/h2bindable/) – Rank-2 monadic bind contract.
