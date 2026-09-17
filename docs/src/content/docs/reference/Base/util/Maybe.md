---
title: Maybe<A>
description: API reference for Maybe, a monadic optional value container with lazy evaluation semantics in Foomp.
---

`Maybe<A>` is a functional container representing optionality (presence or absence of a value). It is a type-safe alternative to `null` and standard Java `Optional`, featuring lazy payload evaluation and monadic/applicative capabilities.

It has two sealed variants:
* **`Maybe.Some<A>`**: Represents the presence of a value of type `A`.
* **`Maybe.None`**: Represents the absence of a value (empty singleton).

---

## Type Signature

```java
package org.quurz.foomp.base.util;

public sealed interface Maybe<A>
        extends Monadic<Maybe.µ, A>,
                Unwindable<Maybe<A>>,
                Transmogrifyable<Maybe<A>>,
                Value<A>,
                Higher1<Maybe.µ, A>
        permits Maybe.Some,
                Maybe.None
```

### Type Parameters
* `A`: The type of the value contained within `Some`.

---

## Witness Type & Higher-Kinded Operations

```java
public static final class µ implements WitnessType { private µ() {} }
```

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A> Maybe<A>` | `narrow(@NonNull Higher1<? extends µ, A> wide)` | Safely downcasts a `Higher1` container to `Maybe<A>`. |
| `static <A> Higher1<µ, A>` | `wide(@NonNull Maybe<A> narrow)` | Wraps a `Maybe` instance as a higher-kinded `Higher1`. |
| `static <A> Maybe<A>` | `flatten(@NonNull Higher1<? extends µ, ? extends Higher1<? extends µ, A>> wrapped)` | Flattens a nested `Maybe` container by one level. |

---

## Static Factory Methods

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A> Maybe<A>` | `some(@NonNull A value)` | Creates a `Some` instance holding the specified non-null value. |
| `static <A> Maybe<A>` | `none()` | Returns the empty singleton `None` instance. |
| `static <A> Maybe<A>` | `maybeOfNullable(@Nullable A value)` | Returns `some(value)` if `value` is non-null; otherwise `none()`. |
| `static <A> Maybe<A>` | `maybeFrom(@NonNull Optional<A> optional)` | Converts a standard `java.util.Optional` to `Maybe`. |

---

## Method Summary

### Value Queries & Extraction

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `boolean` | `isPresent()` / `isSome()` | Returns `true` if this is `Some`, `false` if `None`. |
| `boolean` | `isNone()` | Returns `true` if this is `None`, `false` if `Some`. |
| `@NonNull A` | `get()` | Retrieves the value if `Some`; throws `NoSuchElementException` if `None`. |
| `@NonNull A` | `getOrElse(@NonNull Supplier<A> supplier)` | Returns the value if present, otherwise returns value supplied by `supplier`. |
| `@NonNull <E extends Throwable> A` | `getOrThrow(@NonNull Supplier<E> exceptionSupplier)` | Returns the value if present, otherwise throws the supplied exception. |

### Side Effects & Branching

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `Maybe<A>` | `ifSome(@NonNull Consumer<A> consumer)` | Executes `consumer` with the value if `Some`. Returns `this`. |
| `Maybe<A>` | `ifSome(@NonNull Runnable runnable)` | Executes `runnable` if `Some`. Returns `this`. |
| `Maybe<A>` | `ifNone(@NonNull Supplier<A> supplier)` | If `None`, returns a new `Some` wrapping the supplied fallback value. |
| `Maybe<A>` | `ifNone(@NonNull Runnable runnable)` | Executes `runnable` if `None`. Returns `this`. |
| `@NonNull Maybe<A>` | `ifSomeOrElse(@NonNull Consumer<A> consumer, @NonNull Runnable orElse)` | Executes `consumer` if `Some`, otherwise executes `orElse`. |

### Monadic, Applicative & Functor Operations

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <B> Maybe<B>` | `map(@NonNull Function<? super A, ? extends B> transformation)` | Lazily transforms the contained value if `Some`; preserves `None`. |
| `@NonNull <B> Maybe<B>` | `applyTo(@NonNull Higher1<? extends µ, ? extends Function<? super A, ? extends B>> transformation)` | Applicative apply: applies function inside `Maybe` to this value. |
| `@NonNull <B> Maybe<B>` | `flatMap(@NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation)` | Monadic bind: sequences operations producing `Maybe`. |
| `@NonNull <B> Maybe<Tuple2<A, B>>` | `zip(@NonNull Maybe<B> other)` | Combines two `Maybe` instances into a `Tuple2` if both are `Some`. |
| `@NonNull Maybe<A>` | `filter(@NonNull Predicate<? super A> predicate)` | Retains `Some` if `predicate` holds; converts to `None` otherwise. |
| `@NonNull Maybe<A>` | `unwind()` | Unwinds lazy thunks in `Some` (`@UnwindingOperation`). |

---

## See Also

* [`Result<A>`](/reference/base/util/result/) – Two-track error-handling container (`Success` / `Failure`).
* [`Either<L, R>`](/reference/base/util/either/) – Disjoint union type.
* [`Nothing`](/reference/base/util/nothing/) – Unit / empty singleton representation.
