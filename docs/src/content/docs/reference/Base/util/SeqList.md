---
title: SeqList<A>
description: API reference for SeqList, an immutable, eager, random-access sequence list implementing java.util.List and functional Seq in Foomp.
---

`SeqList<A>` is an immutable, eager, random-access sequence extending `AbstractList<A>` and implementing functional sequence interfaces (`Seq<A>`, `Monadic`, `Foldable`, `Streamable`, and `Higher1`).

It offers $O(1)$ random access through standard `List.get(int)` alongside pure functional list transformations such as `cons`, `tail`, `decons`, `partition`, `span`, `map`, `applyTo`, and `flatMap`.

---

## Type Signature

```java
package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.*;
import org.quurz.foomp.higher.Higher1;

import java.util.AbstractList;

public class SeqList<A>
        extends AbstractList<A>
        implements Iterable<A>,
                   Streamable<A>,
                   Foldable<A>,
                   Monadic<Seq.µ, A>,
                   Seq<A>,
                   Higher1<SeqList.µ, A>
```

### Type Parameters
* `A`: The non-null type of elements contained in the list.

---

## Witness Type & Higher-Kinded Operations

```java
public static final class µ extends Seq.µ { private µ() {} }
```

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A> SeqList<A>` | `narrow(@NonNull Higher1<SeqList.µ, A> wide)` | Safely downcasts a `Higher1` sequence list representation to a concrete `SeqList<A>`. |

---

## Static Factory Methods

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A> SeqList<A>` | `seqList()` | Returns an empty `SeqList`. |
| `static <A> SeqList<A>` | `seqList(@NonNull A... elements)` | Creates a `SeqList` from the provided elements. |
| `static <A> SeqList<A>` | `seqList(@NonNull Iterable<? extends A> elements)` | Creates a `SeqList` from an `Iterable`. |
| `static <A> SeqList<A>` | `seqList(@NonNull Collection<? extends A> collection)` | Creates a `SeqList` from a collection (eager copy). |
| `static <A> Collector<A, ?, SeqList<A>>` | `toSeqList()` | Returns a `Collector` that accumulates elements into an immutable `SeqList`. |

---

## Method Summary

### Element Access & List Properties

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `int` | `size()` | Returns the number of elements in the list. |
| `boolean` | `isEmpty()` | Returns `true` if the list contains no elements. |
| `@NonNull A` | `get(int index)` | Returns the element at the specified index ($O(1)$). |
| `@NonNull Maybe<A>` | `head()` | Returns the first element as `some(element)`, or `none()` if empty. |
| `@NonNull SeqList<A>` | `tail()` | Returns all elements except the first element as a new `SeqList`. |
| `@NonNull Maybe<Tuple2<A, SeqList<A>>>` | `decons()` | Deconstructs the list into its head element and tail sublist. |

### Structural Sequence Operations

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull SeqList<A>` | `cons(@NonNull A element)` | Prepends an element to the front of the list. |
| `@NonNull SeqList<A>` | `append(@NonNull A element)` | Appends an element to the end of the list. |
| `@NonNull SeqList<A>` | `concat(@NonNull SeqList<A> other)` | Concatenates another `SeqList` to this list. |
| `@NonNull SeqList<A>` | `reverse()` | Returns a new `SeqList` with elements in reversed order. |
| `@NonNull SeqList<A>` | `take(int n)` | Takes the first `n` elements. |
| `@NonNull SeqList<A>` | `drop(int n)` | Drops the first `n` elements. |
| `@NonNull Tuple2<SeqList<A>, SeqList<A>>` | `splitAt(int index)` | Splits the list into two parts at the given index. |
| `@NonNull Tuple2<SeqList<A>, SeqList<A>>` | `partition(@NonNull Predicate<? super A> predicate)` | Partitions elements into matching and non-matching lists. |
| `@NonNull Tuple2<SeqList<A>, SeqList<A>>` | `span(@NonNull Predicate<? super A> predicate)` | Splits the list into prefix matching the predicate and remainder. |

### Functional Transformations & Monadic Operations

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <B> SeqList<B>` | `map(@NonNull Function<? super A, ? extends B> transformation)` | Eagerly maps all elements with the given function. |
| `@NonNull <B> SeqList<B>` | `flatMap(@NonNull Function<? super A, ? extends Higher1<? extends Seq.µ, ? extends B>> transformation)` | Monadic bind operation flattening nested sequences. |
| `@NonNull <B> SeqList<B>` | `applyTo(@NonNull Higher1<? extends Seq.µ, ? extends Function<? super A, ? extends B>> transformation)` | Applicative functor application. |
| `@NonNull SeqList<A>` | `filter(@NonNull Predicate<? super A> predicate)` | Retains elements matching the given predicate. |
| `@NonNull <B> Tuple2<SeqList<A>, SeqList<B>>` | `unzip(@NonNull Function<? super A, ? extends Tuple2<A, B>> unzipper)` | Splits the list into two paired lists. |

### Reductions & Folds

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <B> B` | `foldLeft(@NonNull B identity, @NonNull BiFunction<B, ? super A, B> accumulator)` | Folds elements from left to right. |
| `@NonNull <B> B` | `foldRight(@NonNull B identity, @NonNull BiFunction<? super A, B, B> accumulator)` | Folds elements from right to left. |

---

## See Also

* [`Sequence<A>`](/reference/base/util/sequence/) – Segmented, doubly linked lazy sequence structure.
* [`Seq<A>`](/reference/base/types/seq/) – Functional sequence interface.
* [`Foldable<A>`](/reference/base/types/foldable/) – Interface for foldable structures.
