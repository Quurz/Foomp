---
title: Higher4
description: API reference for Higher4, the base interface for rank-4 higher-kinded types (HKTs) in Foomp.
---

`Higher4<WT, A, B, C, D>` is the foundational interface for **Rank-4 higher-kinded types** ($* \to * \to * \to * \to *$) in Foomp. It enables type-level modeling of quaternary type constructors such as 4-tuples, 4-element records, and quaternary applicative functors in standard Java.

---

## Type Signature

```java
package org.quurz.foomp.higher;

public interface Higher4<WT extends WitnessType, A, B, C, D> extends Hkt<WT>
```

### Type Parameters

* **`WT`**: The witness type of the quaternary type constructor (must implement [`WitnessType`](/reference/higher/higher/witnesstype/)).
* **`A`**: The first generic type parameter carried by this higher-kinded type.
* **`B`**: The second generic type parameter carried by this higher-kinded type.
* **`C`**: The third generic type parameter carried by this higher-kinded type.
* **`D`**: The fourth generic type parameter carried by this higher-kinded type.

---

## Superinterfaces

* [`Hkt<WT>`](/reference/higher/higher/hkt/) – Base marker interface for all higher-kinded type encodings in Foomp.

---

## Method Summary

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `default int` | `arity()` | Returns the rank (arity) of this higher-kinded type. Always returns `4`. |

---

## Direct Implementations in Foomp

Quaternary data structures in Foomp implement `Higher4` using an inner witness marker class `µ`:

* [`Tuple4<A1, A2, A3, A4>`](/reference/base/util/tuple4/) implements `Higher4<Tuple4.µ, A1, A2, A3, A4>`
* [`Record4<A1, A2, A3, A4>`](/reference/base/util/record4/) implements `Higher4<Record4.µ, A1, A2, A3, A4>`

---

## Narrowing & Reification Pattern

Concrete quaternary types provide static `narrow` (or `fix`) methods to reify a generic `Higher4` token back to its concrete implementation:

```java
public static <A1, A2, A3, A4> Tuple4<A1, A2, A3, A4> narrow(
        Higher4<? extends Tuple4.µ, A1, A2, A3, A4> wide) {
    return (Tuple4<A1, A2, A3, A4>) Objects.requireNonNull(wide, "wide must not be null");
}
```

---

## See Also

* [`Higher1<WT, A>`](/reference/higher/higher/higher1/) – Rank-1 higher-kinded type interface.
* [`Higher2<WT, A, B>`](/reference/higher/higher/higher2/) – Rank-2 higher-kinded type interface.
* [`Higher3<WT, A, B, C>`](/reference/higher/higher/higher3/) – Rank-3 higher-kinded type interface.
* [`Appliable4<WT, A1, A2, A3, A4>`](/reference/base/types/appliable4/) – Rank-4 applicative functor contract.
* [`Tuple4<A1, A2, A3, A4>`](/reference/base/util/tuple4/) – Lazy 4-element product container.
* [`Record4<A1, A2, A3, A4>`](/reference/base/util/record4/) – Eager 4-element product record.
