---
title: Box<A>
description: API reference for Box, the fundamental single-value monadic container with lazy transformations in Foomp.
---

`Box<A>` is an immutable single-value monadic container (also known as the Identity Monad).

It always contains a non-null value of type `A` (`isPresent() == true`), supports lazy transformations (`map`, `applyTo`, `flatMap`), pairwise combination (`zip`), deep copying (`copy()`), and materialization (`unwind()`).

---

## Type Signature

```java
package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.*;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

public class Box<A>
        implements Copyable<Box<A>>,
                   Transmogrifyable<Box<A>>,
                   Unwindable<Box<A>>,
                   Value<A>,
                   Monadic<Box.µ, A>,
                   Higher1<Box.µ, A>
```

### Type Parameters
* `A`: The type of the value contained inside the box.

---

## Higher-Kinded Witness Type & Narrowing

```java
public static final class µ implements WitnessType { private µ() {} }
```

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A> Box<A>` | `narrow(@NonNull Higher1<? extends µ, A> wide)` | Safely casts a generic `Higher1` container back to `Box<A>`. Throws `NullPointerException` if `wide` is `null`, or `IllegalArgumentException` if not a `Box`. |

---

## Static Factory Methods

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A> Box<A>` | `box(@NonNull A value)` | Wraps the given non-null value inside a new `Box`. Throws `NullPointerException` if `value` is `null`. |

---

## Method Summary

### Value Access & Presence

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `boolean` | `isPresent()` | Always returns `true` because a `Box` never represents an empty state. |
| `@NonNull A` | `get()` | Evaluates the internal supplier and returns the non-null value. |

### Monadic & Applicative Transformations

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <B> Box<B>` | `map(@NonNull Function<? super A, ? extends B> transformation)` | Lazily transforms the contained value. The transformation is executed when `get()` is called. |
| `@NonNull <B> Box<B>` | `applyTo(@NonNull Higher1<? extends µ, ? extends Function<? super A, ? extends B>> transformation)` | Applies a function wrapped inside another `Box` to this value. |
| `@NonNull <B> Box<B>` | `flatMap(@NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation)` | Monadic bind ($\gg=$) chaining computations returning a `Box`. |
| `@NonNull <B, C> Box<C>` | `zip(@NonNull Box<B> other, @NonNull BiFunction<? super A, ? super B, ? extends C> combiner)` | Combines two boxes into a single box using a binary combiner function. |

### Lifecycle, Copying & Materialization

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull Box<A>` | `copy()` | Creates a copy of this box. If the contained value implements [`Copyable`](/reference/base/types/copyable/), a deep copy is performed; otherwise, the value reference is reused. |
| `@NonNull Box<A>` | `unwind()` | **Unwinding Operation.** Evaluates the current computation chain and freezes the result into an eager constant box. |
| `<T> T` | `transmogrify(Function<? super Box<A>, ? extends T> transmogrifier)` | Fluent hook for converting this box to an arbitrary type `T`. |

---

## Core Characteristics

### 1. Identity Monad Behavior
`Box<A>` represents the canonical Identity Functor / Monad in category theory:
* **Identity Law:** `box(x).map(Function.identity()) == box(x)`
* **Composition Law:** `box(x).map(f).map(g) == box(x).map(f.andThen(g))`

### 2. Deferred Transformation Pipeline
When you call `.map(f)` or `.flatMap(g)` on a `Box`, the function is not invoked immediately. Instead, a new `Box` is returned that chains the transformation lazily. Calling `.unwind()` forces evaluation and caches the result for future access.

---

## See Also

* [`Value<A>`](/reference/base/types/value/) – Base single-value carrier interface implemented by `Box`.
* [`Monadic<WT, A>`](/reference/base/types/monadic/) – Unified monadic operations interface.
* [`Copyable<SELF>`](/reference/base/types/copyable/) – Type-safe copying contract.
* [`Unwindable<SELF>`](/reference/base/types/unwindable/) – Materialization interface.
