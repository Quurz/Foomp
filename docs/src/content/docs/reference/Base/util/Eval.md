---
title: Eval<A>
description: API reference for Eval, a monadic interface for eager and lazy evaluation strategies in Foomp.
---

`Eval<A>` is a monadic container for controlling evaluation strategies, inspired by Cats' `Eval`.

It provides three distinct evaluation modes:
* **`Now<A>`**: Eagerly computed value, available immediately.
* **`Later<A>`**: Lazily computed value, evaluated upon first access and cached (memoized) for subsequent accesses.
* **`Always<A>`**: Lazily computed value, recomputed on every access (non-memoized).

---

## Type Signature

```java
package org.quurz.foomp.base.util;

public sealed interface Eval<A>
        extends Unwindable<Eval<A>>,
                Transmogrifyable<Eval<A>>,
                Monadic<Eval.µ, A>,
                Value<A>,
                Higher1<Eval.µ, A>
        permits Eval.Now,
                Eval.Later,
                Eval.Always
```

### Type Parameters
* `A`: The type of value carried and produced by this evaluation.

---

## Witness Type & Higher-Kinded Operations

```java
public static final class µ implements WitnessType { private µ() {} }
```

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A> Eval<A>` | `narrow(@NonNull Higher1<? extends µ, A> wide)` | Safely casts a higher-kinded `Higher1` container to `Eval<A>`. |
| `static <A> Eval<A>` | `flatten(@NonNull Higher1<? extends µ, ? extends Higher1<? extends µ, A>> wrapped)` | Flattens a nested `Eval` container by one level (monadic join). |

---

## Static Factory Methods

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A> Eval<A>` | `evalNow(@NonNull A value)` | Creates an eager `Eval.Now` instance containing the given value. |
| `static <A> Eval<A>` | `evalLater(@NonNull A value)` | Creates a memoized lazy `Eval.Later` instance wrapping a constant value. |
| `static <A> Eval<A>` | `evalAlways(@NonNull A value)` | Creates a non-memoized lazy `Eval.Always` instance wrapping a constant value. |

---

## Method Summary

### Value Access & Evaluation

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull A` | `get()` | Evaluates or extracts the contained value. Depending on the mode, this retrieves the cached value or executes the computation. |
| `boolean` | `isPresent()` | Always returns `true` since `Eval` always produces a value. |

### Strategy Conversions

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull Eval<A>` | `now()` | Evaluates the value immediately (if not already eager) and returns an eager `Eval.Now<A>` instance (`@UnwindingOperation`). |
| `@NonNull Eval<A>` | `later()` | Converts to a memoized lazy `Eval.Later<A>` instance (`@UnwindingOperation`). |
| `@NonNull Eval<A>` | `always()` | Converts to a non-memoized lazy `Eval.Always<A>` instance (`@UnwindingOperation`). |
| `@NonNull Eval<A>` | `unwind()` | Unwinds the computation while preserving the concrete mode (`@UnwindingOperation`). |

### Monadic & Functor Operations

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <B> Eval<B>` | `map(@NonNull Function<? super A, ? extends B> transformation)` | Functor map that transforms the contained value, preserving the underlying evaluation mode. |
| `@NonNull <B> Eval<B>` | `applyTo(@NonNull Higher1<? extends µ, ? extends Function<? super A, ? extends B>> transformation)` | Applicative apply: applies a function wrapped in an `Eval` container to this value. |
| `@NonNull <B> Eval<B>` | `flatMap(@NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation)` | Monadic bind: sequences computations, keeping the evaluation mode lazy for `Later` and `Always`. |
| `@NonNull <T> T` | `transmogrify(@NonNull Function<? super Eval<A>, ? extends T> transmogrifier)` | Applies a transformation function to this `Eval` instance. |

---

## Subtypes

* **`Eval.Now<A>`**: Immutable eager container holding a materialized value.
* **`Eval.Later<A>`**: Thread-safe memoized thunk evaluated upon first call to `get()`.
* **`Eval.Always<A>`**: Non-memoized supplier evaluated on each invocation of `get()`.

---

## See Also

* [`Task<A>`](/reference/base/util/task/) – Asynchronous and task-oriented computations.
* [`Trampoline<A>`](/reference/base/util/trampoline/) – Stackless tail-recursive evaluations.
* [`Supplier`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/Supplier.html) – Standard supplier used for lazy evaluation thunks.
