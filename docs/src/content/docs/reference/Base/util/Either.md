---
title: Either<L, R>
description: API reference for Either, a right-biased disjoint union (sum type) in Foomp.
---

`Either<L, R>` is an algebraic data type representing a disjoint union (sum type) holding either a left value of type `L` (`Either.Left`) or a right value of type `R` (`Either.Right`).

In functional programming, `Either` is commonly used for computations that may fail (`Left`) or return a successful outcome (`Right`). In Foomp, `Either` is **right-biased**: monadic and functor operations such as `map` and `flatMap` operate on `Right`, while `Left` is passed through untouched.

Values in `Left` and `Right` are held lazily via `Supplier` thunks and evaluated upon access (`getLeft`, `getRight`, `unwind`, `toString`, `equals`, `hashCode`).

---

## Type Signature

```java
package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.types.*;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.Higher2;
import org.quurz.foomp.higher.WitnessType;

public sealed interface Either<L, R>
        extends Mappable<Either.µ, R>,
                Appliable2<Either.µ, L, R>,
                Bindable<Either.µ, R>,
                Swappable<Either<R, L>, L, R>,
                Unwindable<Either<L, R>>,
                Transmogrifyable<Either<L, R>>,
                XorValue<L, R>,
                Higher2<Either.µ, L, R>,
                Higher1<Either.µ, R>
        permits Either.Left,
                Either.Right
```

### Type Parameters
* `L`: The type of the left value (typically representing failure or error diagnostics).
* `R`: The type of the right value (typically representing success or primary result).

---

## Higher-Kinded Witness Type & Helpers

```java
public static final class µ implements WitnessType { private µ() {} }
```

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <L, R> Either<L, R>` | `narrow(@NonNull Higher2<? extends µ, L, R> wide)` | Safely downcasts a `Higher2` container to `Either<L, R>`. |
| `static <L, R> Either<L, R>` | `flatten(@NonNull Higher2<µ, ? extends Higher2<? extends µ, L, R>, ? extends Higher2<? extends µ, L, R>> wrapped)` | Flattens a nested `Either` by one layer. |

---

## Static Factory Methods

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <L, R> Either<L, R>` | `left(@NonNull L value)` | Creates an `Either.Left` with the specified left value. |
| `static <L, R> Either<L, R>` | `right(@NonNull R value)` | Creates an `Either.Right` with the specified right value. |

---

## Method Summary

### State & Value Access

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `boolean` | `isLeft()` | Returns `true` if this instance is a `Left`. |
| `boolean` | `isRight()` | Returns `true` if this instance is a `Right`. |
| `boolean` | `isPresent()` | Right-biased presence indicator (`isRight()`). |
| `@NonNull L` | `getLeft()` | Returns the left value or throws `NoSuchElementException` if it is a `Right`. |
| `@NonNull L` | `getLeftOrElse(@NonNull Supplier<L> supplier)` | Returns the left value, or computes the fallback supplier value if a `Right`. |
| `@NonNull Maybe<L>` | `getLeftSafe()` | Returns `Maybe.some(left)` or `Maybe.none()`. |
| `<E extends Exception> L` | `getLeftOrThrow(@NonNull Supplier<E> exceptionSupplier)` | Returns the left value or throws the specified exception. |
| `@NonNull R` | `get()` | Right-biased getter: equivalent to `getRight()`. |
| `@NonNull R` | `getRight()` | Returns the right value or throws `NoSuchElementException` if it is a `Left`. |
| `@NonNull R` | `getRightOrElse(@NonNull Supplier<R> supplier)` | Returns the right value, or computes the fallback supplier value if a `Left`. |
| `@NonNull Maybe<R>` | `getRightSafe()` | Returns `Maybe.some(right)` or `Maybe.none()`. |
| `<E extends Exception> R` | `getRightOrThrow(@NonNull Supplier<E> exceptionSupplier)` | Returns the right value or throws the specified exception. |

### Peeking & Side Effects

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull Either<L, R>` | `ifLeft(@NonNull Consumer<L> consumer)` | Executes `consumer` if this instance is a `Left`. |
| `@NonNull Either<L, R>` | `ifRight(@NonNull Consumer<R> consumer)` | Executes `consumer` if this instance is a `Right`. |
| `@NonNull Either<L, R>` | `ifEither(@NonNull Consumer<L> leftConsumer, @NonNull Consumer<R> rightConsumer)` | Executes `leftConsumer` for `Left` or `rightConsumer` for `Right`. |

### Transformations & Monadic Operations

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <S> Either<L, S>` | `map(@NonNull Function<? super R, ? extends S> transformation)` | Functor map (right-biased): transforms the right value. |
| `@NonNull <S> Either<L, S>` | `mapRight(@NonNull Function<? super R, ? extends S> fMap)` | Transforms the right value. |
| `@NonNull <M> Either<M, R>` | `mapLeft(@NonNull Function<? super L, ? extends M> fMap)` | Transforms the left value. |
| `@NonNull <M, S> Either<M, S>` | `mapEither(@NonNull Fun<? super L, ? extends M> fMapLeft, @NonNull Fun<? super R, ? extends S> fMapRight)` | Bimap: transforms both sides simultaneously using the respective functions. |
| `@NonNull <M, S> Either<M, S>` | `applyTo(@NonNull Higher2<µ, ? extends Function<? super L, ? extends M>, ? extends Function<? super R, ? extends S>> transformation)` | Applies an `Either` of functions to the corresponding left/right branches. |
| `@NonNull <S> Either<L, S>` | `flatMap(@NonNull Function<? super R, ? extends Higher1<? extends µ, S>> transformation)` | Monadic bind ($\gg=$): applies the transformation to the right value. |
| `@NonNull Either<R, L>` | `swap()` | Swaps sides: `Left(x) -> Right(x)` and `Right(y) -> Left(y)`. |
| `@NonNull <T> T` | `transmogrify(@NonNull Function<? super Either<L, R>, ? extends T> transmogrifier)` | Transforms the entire `Either` container into a target type `T`. |
| `@NonNull Either<L, R>` | `unwind()` | Materializes the internal `Supplier` thunk into an eager constant value. |

---

## See Also

* [`Maybe<A>`](/reference/base/util/maybe/) – Optional container for presence or absence of a value.
* [`Result<A>`](/reference/base/util/result/) – Eager outcome type for exception-based results.
* [`XorValue<L, R>`](/reference/base/types/xorvalue/) – Base interface for disjoint sum types.
