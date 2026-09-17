---
title: Attempt<A>
description: API reference for Attempt, the lazy monadic computation container capturing exceptions in Foomp.
---

`Attempt<A>` is a lazy error monad designed for deferred computations that may either produce a successful value of type `A` or fail with an `Exception`.

Unlike eager error containers (such as [`Result<A>`](/reference/base/util/result/)), `Attempt<A>` encapsulates computations inside a supplier. Execution is deferred until explicitly triggered (for example via `tryIt()`, `onFailureThrow()`, or `unwind()`).

---

## Type Signature

```java
package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Monadic;
import org.quurz.foomp.base.types.Triable;
import org.quurz.foomp.base.types.UnsafeMonadic;
import org.quurz.foomp.base.types.Unwindable;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

public final class Attempt<A>
        implements Monadic<Attempt.µ, A>,
                   UnsafeMonadic<Attempt.µ, A>,
                   Unwindable<Attempt<A>>,
                   Triable<A>,
                   Higher1<Attempt.µ, A>
```

### Type Parameters
* `A`: The type of the value produced when the computation succeeds.

---

## Higher-Kinded Witness Type & Narrowing

```java
public static final class µ implements WitnessType { private µ() {} }
```

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A> Attempt<A>` | `narrow(@NonNull Higher1<? extends µ, A> wide)` | Safely casts a generic `Higher1` container back to `Attempt<A>`. Throws `NullPointerException` if `wide` is `null`, or `IllegalArgumentException` if not an instance of `Attempt`. |

---

## Static Factory Methods

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A> Attempt<A>` | `attempt(@NonNull A value)` | Creates a new successful `Attempt` with the given constant value. Evaluation is deferred and returns `Result.success(value)`. |

---

## Method Summary

### Evaluation & Materialization

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull Result<A>` | `tryIt()` | **Unwinding Operation.** Evaluates the deferred computation and returns the outcome as a [`Result<A>`](/reference/base/util/result/) (`Success` or `Failure`). Never throws checked exceptions. |
| `Attempt<A>` | `onFailureThrow() throws Exception` | **Unwinding Operation.** Evaluates the computation; returns `this` if successful, or throws the stored exception if failed. |
| `@NonNull Attempt<A>` | `unwind()` | **Unwinding Operation.** Evaluates the computation immediately and returns a new `Attempt` that consistently yields the materialized result. |

### Error Handling & Recovery

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `Attempt<A>` | `onFailureRecover(@NonNull Supplier<A> recover)` | Defines a lazy fallback supplier. If evaluated and failed, invokes `recover` to produce a success value. If the supplier throws, the original exception is preserved as a suppressed exception. |
| `Attempt<A>` | `onFailureRecover(@NonNull Function<? super Exception, ? extends A> recover)` | Defines a lazy recovery function mapping the caught exception to a fallback value. |
| `Attempt<A>` | `peekFailureLazy(@NonNull Consumer<? super Exception> failureConsumer)` | Registers a lazy consumer invoked only if evaluation fails. Preserves laziness. |
| `Attempt<A>` | `peekFailureEager(@NonNull Consumer<? super Exception> failureConsumer)` | **Unwinding Operation.** Evaluates immediately and executes the consumer if currently in a failed state. |

### Monadic & Applicative Transformations

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <B> Attempt<B>` | `map(@NonNull Function<? super A, ? extends B> transformation)` | Lazily transforms the success value using a standard Java `Function`. Thrown runtime exceptions are captured as failures. |
| `@NonNull <B> Attempt<B>` | `mapUnsafe(@NonNull Applicable<? super A, ? extends B> transformation)` | Lazily transforms the value using an [`Applicable`](/reference/base/functions/applicable/) function. Checked exceptions are automatically captured as failures. |
| `@NonNull <B> Attempt<B>` | `applyTo(@NonNull Higher1<? extends µ, ? extends Function<? super A, ? extends B>> transformation)` | Applicative functor application over an `Attempt` containing a function. |
| `@NonNull <B> Attempt<B>` | `applyToUnsafe(@NonNull Higher1<? extends µ, ? extends Applicable<? super A, ? extends B>> transformation)` | Unsafe applicative functor application with checked exception capture. |
| `@NonNull <B> Attempt<B>` | `flatMap(@NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation)` | Monadic bind ($\gg=$) chaining computations returning higher-kinded `Attempt`s. |
| `@NonNull <B> Attempt<B>` | `flatMapUnsafe(@NonNull Applicable<? super A, ? extends Higher1<? extends µ, B>> transformation)` | Monadic bind accepting throwing [`Applicable`](/reference/base/functions/applicable/) functions. |

---

## Core Characteristics

### 1. Lazy Execution
Unlike standard try-catch blocks or immediate containers like `Result<A>`, creating an `Attempt` or chaining transformations (`map`, `mapUnsafe`, `flatMap`) does not execute the underlying code immediately. Execution is triggered only when unwinding operations such as `tryIt()`, `onFailureThrow()`, or `unwind()` are invoked.

### 2. Comprehensive Exception Capturing
Every step in an `Attempt` pipeline—including functions passed to `mapUnsafe`, `flatMapUnsafe`, or `onFailureRecover`—is protected. Thrown checked and unchecked exceptions are intercepted and converted into `Result.Failure`, preventing unhandled crashes.

### 3. Railway-Oriented Programming (ROP)
If any step in an `Attempt` chain fails, all subsequent mapping and binding steps short-circuit until an `onFailureRecover` handler is encountered.

---

## See Also

* [`Result<A>`](/reference/base/util/result/) – The eager outcome container produced by `Attempt.tryIt()`.
* [`Applicable<X, Y>`](/reference/base/functions/applicable/) – Checked exception-throwing function used in `mapUnsafe`.
* [`Triable<A>`](/reference/base/types/triable/) – The interface implemented by `Attempt` providing `tryIt()`.
* [`UnsafeMonadic<WT, A>`](/reference/base/types/unsafemonadic/) – The unsafe monadic interface supported by `Attempt`.
