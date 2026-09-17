---
title: Hkt
description: API reference for Hkt, the root base interface for Higher-Kinded Types in Foomp.
---

`Hkt<WT>` is the foundational root marker interface for all **Higher-Kinded Types** (HKTs) in Foomp. It establishes the type-level relationship between an encoded higher-order type constructor and its identifying witness type (`WT`).

---

## Type Signature

```java
package org.quurz.foomp.higher;

public interface Hkt<WT extends WitnessType>
```

### Type Parameters

* **`WT`**: The witness type marker that encodes the identity and shape of the type constructor (must implement [`WitnessType`](/reference/higher/higher/witnesstype/)).

---

## Method Summary

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `default int` | `arity()` | Returns the arity (rank) of this higher-kinded type. Defaults to `0` on the root interface; overridden by subinterfaces (`Higher1` returns `1`, `Higher2` returns `2`, `Higher3` returns `3`, `Higher4` returns `4`). |

---

## Subinterfaces

`Hkt<WT>` serves as the superinterface for rank-specialized higher-kinded type representations:

* [`Higher1<WT, A>`](/reference/higher/higher/higher1/) – Rank-1 higher-kinded types ($* \to *$, arity `1`).
* [`Higher2<WT, A, B>`](/reference/higher/higher/higher2/) – Rank-2 higher-kinded types ($* \to * \to *$, arity `2`).
* [`Higher3<WT, A, B, C>`](/reference/higher/higher/higher3/) – Rank-3 higher-kinded types ($* \to * \to * \to *$, arity `3`).
* [`Higher4<WT, A, B, C, D>`](/reference/higher/higher/higher4/) – Rank-4 higher-kinded types ($* \to * \to * \to * \to *$, arity `4`).

---

## Design Pattern Overview

In standard Java, generics cannot be parameterized over higher-order type constructors (e.g. `F<_>`). Foomp employs a lightweight higher-kinded type encoding:

1. **Witness Registration**: A type constructor defines a companion witness marker (e.g. `public static final class µ implements WitnessType {}`).
2. **HKT Tagging**: The type implements `Higher1`, `Higher2`, `Higher3`, or `Higher4` parameterized by its witness marker.
3. **Arity Introspection**: The `arity()` method reports the number of type parameters carried by the constructor.
4. **Reification / Narrowing**: Concrete classes provide static `narrow` (or `fix`) methods to reify generic HKT references back to their concrete classes safely.

---

## See Also

* [`WitnessType`](/reference/higher/higher/witnesstype/) – Marker interface for witness types encoding type constructor identities.
* [`Higher1<WT, A>`](/reference/higher/higher/higher1/) – Unary higher-kinded type interface.
* [`Higher2<WT, A, B>`](/reference/higher/higher/higher2/) – Binary higher-kinded type interface.
* [`Higher3<WT, A, B, C>`](/reference/higher/higher/higher3/) – Ternary higher-kinded type interface.
* [`Higher4<WT, A, B, C, D>`](/reference/higher/higher/higher4/) – Quaternary higher-kinded type interface.
