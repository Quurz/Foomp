---
title: Sequence<A>
description: API reference for Sequence, a persistent segmented doubly linked sequence structure with lazy transformation fusion in Foomp.
---

`Sequence<A>` is an immutable, persistent sequence data structure modeled as a segmented doubly linked structure with lazy transformation fusion.

It organizes elements into segments carrying a composable transformation spool. Operations like `map` and `applyTo` compose transformations lazily without copying or modifying underlying nodes (structural sharing).

---

## Type Signature

```java
package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.*;
import org.quurz.foomp.higher.Higher1;

public class Sequence<A>
        implements Iterable<A>,
                   Streamable<A>,
                   Foldable<A>,
                   Monadic<Seq.µ, A>,
                   Seq<A>,
                   Higher1<Seq.µ, A>
```

### Type Parameters
* `A`: The type of elements contained in the sequence.

---

## Witness Type & Higher-Kinded Operations

```java
public static final class µ extends Seq.µ { private µ() {} }
```

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A> Sequence<A>` | `narrow(@NonNull Higher1<? extends Seq.µ, A> wide)` | Safely narrows a `Higher1` sequence representation to a concrete `Sequence<A>`. Throws `IllegalArgumentException` if not a `Sequence`. |

---

## Static Factory Methods

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A> Sequence<A>` | `sequence()` | Returns an empty immutable `Sequence`. |
| `static <A> Sequence<A>` | `sequenceOf(@NonNull A... elements)` | Creates an immutable `Sequence` containing the given non-null elements in order. |
| `static <A> Sequence<A>` | `sequenceFrom(@NonNull Collection<A> collection)` | Creates an immutable `Sequence` from the elements of the specified collection. |
| `static <A> Collector<A, List<A>, Sequence<A>>` | `collectToSequence()` | Returns a `Collector` that accumulates stream elements into a new `Sequence`. |

---

## Method Summary

### Sequence Queries & Element Extraction

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `boolean` | `isNotEmpty()` | Returns `true` if this sequence contains at least one element. |
| `boolean` | `isEmpty()` | Returns `true` if this sequence contains no elements (default method from `Seq`). |
| `@NonNull A` | `head()` | Retrieves the first element of this sequence (applying pending transformations). Throws `NoSuchElementException` if empty (`@UnwindingOperation`). |
| `@NonNull Maybe<A>` | `headSafe()` | Safely retrieves the first element wrapped in `Maybe.some(head)`, or `Maybe.none()` if empty (`@UnwindingOperation`). |
| `@NonNull Sequence<A>` | `tail()` | Returns a sequence of all elements after the first element. Throws `NoSuchElementException` if empty. |
| `@NonNull Tuple2<A, Sequence<A>>` | `decons()` | Deconstructs this sequence into a tuple containing the head element and the tail sequence. Throws `NoSuchElementException` if empty. |

### Structural Modification & Partitioning

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull Sequence<A>` | `cons(@NonNull A element)` | Prepends an element to the front of the sequence in $O(1)$. |
| `@NonNull Sequence<A>` | `prepend(@NonNull A element)` | Alias for `cons(element)`. |
| `@NonNull Sequence<A>` | `append(@NonNull A element)` | Appends an element to the end of the sequence. |
| `@NonNull Sequence<A>` | `prependAll(@NonNull Seq<A> other)` | Prepends another sequence to the front of this sequence. |
| `@NonNull Sequence<A>` | `prependAll(@NonNull Collection<? extends A> elements)` | Prepends a collection of elements to the front of this sequence. |
| `@NonNull Sequence<A>` | `appendAll(@NonNull Seq<A> other)` | Appends another sequence to the end of this sequence. |
| `@NonNull Sequence<A>` | `appendAll(@NonNull Collection<? extends A> elements)` | Appends a collection of elements to the end of this sequence. |
| `@NonNull Sequence<A>` | `merge(@NonNull Seq<A> other)` | Merges another `Seq` into this sequence. |
| `@NonNull Sequence<A>` | `mergeSequence(@NonNull Sequence<A> other)` | Merges another `Sequence` into this sequence. |
| `@NonNull Sequence<A>` | `filter(@NonNull Predicate<? super A> predicate)` | Returns a filtered sequence retaining only elements satisfying the predicate. |
| `@NonNull Tuple2<Sequence<A>, Sequence<A>>` | `partition(@NonNull Predicate<? super A> pred)` | Partitions elements into two sequences: matching elements and non-matching elements. |
| `@NonNull Tuple2<Sequence<A>, Sequence<A>>` | `span(@NonNull Predicate<? super A> pred)` | Splits this sequence into a tuple of prefix satisfying the predicate and remainder. |
| `@NonNull <C extends Collection<? super A>> C` | `toCollection(@NonNull Supplier<C> init)` | Collects all elements into a custom collection instance. |
| `@NonNull Sequence<A>` | `unwind()` | Explicitly forces evaluation of all fused spool transformations into fresh segments (`@UnwindingOperation`). |

### Functional Transformations & Monadic Operations

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <B> Sequence<B>` | `map(@NonNull Function<? super A, ? extends B> transformation)` | Composes the transformation function lazily into the segment spool without copying node data. |
| `@NonNull <B> Sequence<B>` | `applyTo(@NonNull Higher1<? extends Seq.µ, ? extends Function<? super A, ? extends B>> transformation)` | Applicative functor application over transformations. |
| `@NonNull <B> Sequence<B>` | `flatMap(@NonNull Function<? super A, ? extends Higher1<? extends Seq.µ, B>> transformation)` | Monadic bind operation flattening nested sequences. |

### Folds, Streaming & Traversal

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <B> B` | `foldLeft(B init, @NonNull BiFunction<? super B, ? super A, ? extends B> function)` | Folds elements from left to right. |
| `@NonNull <B> B` | `foldRight(B init, @NonNull BiFunction<? super A, ? super B, ? extends B> function)` | Folds elements from right to left. |
| `void` | `iterateOverAllElementsFromLeft(@NonNull Consumer<? super A> consumer)` | Traverses and consumes all elements from left to right. |
| `void` | `iterateOverAllElementsFromRight(@NonNull Consumer<? super A> consumer)` | Traverses and consumes all elements from right to left. |
| `@NonNull Iterator<A>` | `iterator()` | Returns a forward iterator over this sequence. |
| `@NonNull Stream<A>` | `stream()` | Returns a sequential `Stream` over the elements of this sequence. |

---

## See Also

* [`SeqList<A>`](/reference/base/util/seqlist/) – Eager random-access sequence list implementing `java.util.List` with `size()`.
* [`Seq<A>`](/reference/base/types/seq/) – Sequence interface specification.
* [`Foldable<A>`](/reference/base/types/foldable/) – Abstraction for left- and right-foldable containers.
* [`Streamable<A>`](/reference/base/types/streamable/) – Interface for streamable sources.
