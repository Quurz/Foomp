---
title: "Seq<A>"
description: Complete API reference for the core sequence interface in Foomp, covering deconstruction, merging, partitioning, and higher-kinded types.
---

`Seq<A>` represents an ordered collection of elements of type `A`. It forms the fundamental functional collection abstraction in Foomp, supporting inductive head/tail decomposition, merging, filtering, and partitioning.

:::note[Contract and Invariants]
- **Encounter Order:** Elements follow a deterministic encounter order.
- **Null Safety:** Operations and return values are strictly `@NonNull`; `null` elements are not permitted.
- **Algebraic Merging:** Implements `Mergeable<Seq<A>>` for associative sequence concatenation.
- **Higher-Kinded Support:** Declares witness class `Seq.µ` for integration with `Higher1`.
:::

---

## Type Signature & Definition

```java
package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Seq<A> extends Mergeable<Seq<A>> {

    class µ implements WitnessType { protected µ() {} }

    static <A> Seq<A> narrow(final @NonNull Higher1<? extends µ, A> wide);

    boolean isNotEmpty();

    default boolean isEmpty();

    @NonNull A head() throws NoSuchElementException;

    @NonNull Value<A> headSafe();

    @NonNull Seq<A> tail() throws NoSuchElementException;

    @NonNull Seq<A> cons(final @NonNull A element);

    default @NonNull Seq<A> appendAll(final @NonNull Seq<A> other);

    default @NonNull Seq<A> prependAll(final @NonNull Seq<A> other);

    @NonNull Value2<A, ? extends Seq<A>> decons() throws NoSuchElementException;

    @NonNull Seq<A> filter(final @NonNull Predicate<? super A> pred);

    @NonNull Value2<? extends Seq<A>, ? extends Seq<A>> partition(final @NonNull Predicate<? super A> pred);

    @NonNull Value2<? extends Seq<A>, ? extends Seq<A>> span(final @NonNull Predicate<? super A> pred);

    <C extends Collection<? super A>> @NonNull C toCollection(final @NonNull Supplier<C> init);
}
```

---

## Method Overview

### 1. Structure & Inspection
* **`boolean isNotEmpty()` / `default boolean isEmpty()`**  
  Checks if the sequence contains elements.
* **`@NonNull A head() throws NoSuchElementException`**  
  Returns the first element (Lisp `car`). Throws `NoSuchElementException` if empty.
* **`@NonNull Value<A> headSafe()`**  
  Returns the head wrapped in a `Value<A>`, returning an empty value if the sequence is empty.
* **`@NonNull Seq<A> tail() throws NoSuchElementException`**  
  Returns the remainder of the sequence after the head (Lisp `cdr`).
* **`@NonNull Value2<A, ? extends Seq<A>> decons() throws NoSuchElementException`**  
  Deconstructs the sequence atomically into its head element and tail sequence pair.

### 2. Extension & Combination
* **`@NonNull Seq<A> cons(final @NonNull A element)`**  
  Prepends a single element to the front of the sequence.
* **`default @NonNull Seq<A> appendAll(final @NonNull Seq<A> other)`**  
  Appends `other` to this sequence (`this.merge(other)`).
* **`default @NonNull Seq<A> prependAll(final @NonNull Seq<A> other)`**  
  Prepends `other` to this sequence (`other.merge(this)`).

### 3. Filtering & Splitting
* **`@NonNull Seq<A> filter(final @NonNull Predicate<? super A> pred)`**  
  Retains only elements matching `pred`.
* **`@NonNull Value2<? extends Seq<A>, ? extends Seq<A>> partition(final @NonNull Predicate<? super A> pred)`**  
  Divides the sequence into a pair: `Value2(matchingSeq, nonMatchingSeq)`.
* **`@NonNull Value2<? extends Seq<A>, ? extends Seq<A>> span(final @NonNull Predicate<? super A> pred)`**  
  Splits the sequence into `Value2(longestMatchingPrefix, remainder)`.

### 4. Conversion & Interoperability
* **`<C extends Collection<? super A>> @NonNull C toCollection(final @NonNull Supplier<C> init)`**  
  Materializes the sequence elements into a target collection provided by the supplier (e.g. `ArrayList::new`, `HashSet::new`).
* **`static <A> Seq<A> narrow(final @NonNull Higher1<? extends µ, A> wide)`**  
  Safely casts a higher-kinded container back to `Seq<A>`.
