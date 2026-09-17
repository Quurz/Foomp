---
title: Higher1
description: API reference for Higher1, the base interface for rank-1 higher-kinded types (HKTs) in Foomp.
---

`Higher1<WT, A>` is the foundational interface for **Rank-1 higher-kinded types** ($* \to *$) in Foomp. It provides a lightweight, type-safe encoding to represent generic containers and type constructors that take a single type argument in Java.

---

## Type Signature

```java
package org.quurz.foomp.higher;

public interface Higher1<WT extends WitnessType, A> extends Hkt<WT>
```

### Type Parameters

* **`WT`**: The witness type of the higher-kinded type constructor (must implement [`WitnessType`](/reference/higher/higher/witnesstype/)).
* **`A`**: The generic element/value type carried by this higher-kinded type.

---

## Superinterfaces

* [`Hkt<WT>`](/reference/higher/higher/hkt/) – Base marker interface for all higher-kinded type encodings in Foomp.

---

## Method Summary

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `default int` | `arity()` | Returns the rank (arity) of this higher-kinded type. Always returns `1`. |

---

## Direct Implementations in Foomp

In Foomp, types parameterized by one generic argument implement `Higher1` using an inner witness marker class `µ`:

* [`Maybe<A>`](/reference/base/util/maybe/) implements `Higher1<Maybe.µ, A>`
* [`Attempt<A>`](/reference/base/util/attempt/) implements `Higher1<Attempt.µ, A>`
* [`Box<A>`](/reference/base/util/box/) implements `Higher1<Box.µ, A>`
* [`Eval<A>`](/reference/base/util/eval/) implements `Higher1<Eval.µ, A>`
* [`Task<A>`](/reference/base/util/task/) implements `Higher1<Task.µ, A>`
* [`Provider<A>`](/reference/base/functions/provider/) implements `Higher1<Provider.µ, A>`
* [`Sequence<A>`](/reference/base/util/sequence/) implements `Higher1<Sequence.µ, A>`
* [`SeqList<A>`](/reference/base/util/seqlist/) implements `Higher1<SeqList.µ, A>`
* [`AVLTree<A>`](/reference/base/util/avltree/) implements `Higher1<AVLTree.µ, A>`
* [`RedBlackTree<A>`](/reference/base/util/redblacktree/) implements `Higher1<RedBlackTree.µ, A>`

---

## Narrowing & Reification Pattern

Because Java does not natively support higher-kinded types, concrete classes provide static `narrow` (or `fix`) methods to safely cast generic `Higher1` tokens back to their concrete implementations:

```java
public static <A> Maybe<A> narrow(Higher1<? extends Maybe.µ, A> wide) {
    return (Maybe<A>) Objects.requireNonNull(wide, "wide must not be null");
}
```

---

## See Also

* [`Higher2<WT, A, B>`](/reference/higher/higher/higher2/) – Rank-2 higher-kinded type interface.
* [`Higher3<WT, A, B, C>`](/reference/higher/higher/higher3/) – Rank-3 higher-kinded type interface.
* [`Higher4<WT, A, B, C, D>`](/reference/higher/higher/higher4/) – Rank-4 higher-kinded type interface.
* [`Appliable<WT, A>`](/reference/base/types/appliable/) – Rank-1 applicative functor contract.
* [`Bindable<WT, A>`](/reference/base/types/bindable/) – Rank-1 monadic bind contract.
