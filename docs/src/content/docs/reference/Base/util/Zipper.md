---
title: Zipper
description: API reference for Zipper, a utility class in Foomp for zipping and unzipping parallel lists.
---

`Zipper` is a final static utility class that provides functional operations to combine and separate parallel lists using [`Tuple2`](/reference/base/util/tuple2/).

---

## Type Signature

```java
package org.quurz.foomp.base.util;

public final class Zipper
```

`Zipper` contains only static methods and cannot be instantiated.

---

## Method Summary

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A, B> List<Tuple2<A, B>>` | `zip(@NonNull List<A> as, @NonNull List<B> bs)` | Combines two lists element-wise into a list of [`Tuple2<A, B>`](/reference/base/util/tuple2/) pairs. The resulting list is truncated to the length of the shorter input list. |
| `static <A, B> Tuple2<List<A>, List<B>>` | `unzip(@NonNull List<Tuple2<A, B>> tuples)` | Separates a list of `Tuple2` pairs into two parallel lists containing first and second components respectively. Preserves order. |

---

## Semantics & Contracts

* **Element-wise pairing**: `zip` matches corresponding indices `(as[i], bs[i])`.
* **Length handling**: When list lengths differ, `zip` truncates to `Math.min(as.size(), bs.size())`. Elements beyond the shorter list's length are omitted.
* **Invertibility**: `unzip` is the structural inverse of `zip` (for lists of equal size).
* **Null safety**: Inputs must not be `null`.

---

## See Also

* [`Tuple2<A1, A2>`](/reference/base/util/tuple2/) – Immutable lazy 2-element tuple used to represent paired elements.
* [`Sequence<A>`](/reference/base/util/sequence/) – Lazy sequence supporting stream-like operations.
* [`SeqList<A>`](/reference/base/util/seqlist/) – Eager random-access list.
