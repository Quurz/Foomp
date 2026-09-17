---
title: WitnessType
description: API reference for WitnessType, the marker interface for type constructor witnesses in Foomp.
---

`WitnessType` is the foundational marker interface used to simulate **higher-kinded types (HKTs)** in Java. It serves as a type-level identifier (a *witness*) representing the shape and identity of an unapplied generic type constructor.

---

## Type Signature

```java
package org.quurz.foomp.higher;

public interface WitnessType
```

---

## What is a Witness Type?

In languages like Haskell or Scala, type constructors (such as `Maybe` or `List`) can be manipulated as first-class type entities of kind $* \to *$. Standard Java only permits concrete or fully applied generic types (e.g., `Maybe<String>`, `List<Integer>`).

A **Witness Type** acts as a compile-time token that uniquely identifies an unapplied type constructor. When paired with the rank-specialized interfaces [`Higher1`](/reference/higher/higher/higher1/), [`Higher2`](/reference/higher/higher/higher2/), [`Higher3`](/reference/higher/higher3/), or [`Higher4`](/reference/higher/higher4/), the witness type parameter `WT` instructs the Java compiler which type constructor is being modeled.

---

## Conventions and Inner Witness Class (`µ`)

By convention across the Foomp library, classes that participate in the higher-kinded type system declare an inner static marker class named `µ` (Greek letter mu, often representing a fixed-point or type constructor token):

```java
public final class Maybe<A> implements Higher1<Maybe.µ, A> {

    // Unique witness marker for the Maybe<_> type constructor
    public static final class µ implements WitnessType {
        private µ() {}
    }

    // ...
}
```

### Key Guidelines

* **One-to-One Identity**: Each distinct type constructor defines exactly one corresponding `WitnessType`.
* **Marker Class**: Witness classes are empty and have private constructors to prevent runtime instantiation.
* **Compile-Time Only**: The witness type exists purely for compiler-level type checking and does not incur any runtime overhead.

---

## Direct Implementations in Foomp

Every functional container and type constructor in Foomp provides an inner `µ` implementing `WitnessType`:

| Data Structure / Constructor | Witness Marker | HKT Representation |
| :--- | :--- | :--- |
| `Maybe<A>` | `Maybe.µ` | `Higher1<Maybe.µ, A>` |
| `Attempt<A>` | `Attempt.µ` | `Higher1<Attempt.µ, A>` |
| `Box<A>` | `Box.µ` | `Higher1<Box.µ, A>` |
| `Eval<A>` | `Eval.µ` | `Higher1<Eval.µ, A>` |
| `Task<A>` | `Task.µ` | `Higher1<Task.µ, A>` |
| `Provider<A>` | `Provider.µ` | `Higher1<Provider.µ, A>` |
| `Sequence<A>` | `Sequence.µ` | `Higher1<Sequence.µ, A>` |
| `SeqList<A>` | `SeqList.µ` | `Higher1<SeqList.µ, A>` |
| `AVLTree<A>` | `AVLTree.µ` | `Higher1<AVLTree.µ, A>` |
| `RedBlackTree<A>` | `RedBlackTree.µ` | `Higher1<RedBlackTree.µ, A>` |
| `Either<L, R>` | `Either.µ` | `Higher2<Either.µ, L, R>` |
| `Pair<A, B>` | `Pair.µ` | `Higher2<Pair.µ, A, B>` |
| `Tuple2<A1, A2>` | `Tuple2.µ` | `Higher2<Tuple2.µ, A1, A2>` |
| `Record2<A1, A2>` | `Record2.µ` | `Higher2<Record2.µ, A1, A2>` |
| `Dictionary<K, V>` | `Dictionary.µ` | `Higher2<Dictionary.µ, K, V>` |
| `DecisionTree<A, B>` | `DecisionTree.µ` | `Higher2<DecisionTree.µ, A, B>` |
| `Tuple3<A1, A2, A3>` | `Tuple3.µ` | `Higher3<Tuple3.µ, A1, A2, A3>` |
| `Record3<A1, A2, A3>` | `Record3.µ` | `Higher3<Record3.µ, A1, A2, A3>` |
| `Tuple4<A1, A2, A3, A4>` | `Tuple4.µ` | `Higher4<Tuple4.µ, A1, A2, A3, A4>` |
| `Record4<A1, A2, A3, A4>` | `Record4.µ` | `Higher4<Record4.µ, A1, A2, A3, A4>` |

---

## See Also

* [`Hkt<WT>`](/reference/higher/higher/hkt/) – Root interface for higher-kinded type representations.
* [`Higher1<WT, A>`](/reference/higher/higher/higher1/) – Rank-1 higher-kinded type interface.
* [`Higher2<WT, A, B>`](/reference/higher/higher/higher2/) – Rank-2 higher-kinded type interface.
* [`Higher3<WT, A, B, C>`](/reference/higher/higher/higher3/) – Rank-3 higher-kinded type interface.
* [`Higher4<WT, A, B, C, D>`](/reference/higher/higher/higher4/) – Rank-4 higher-kinded type interface.
