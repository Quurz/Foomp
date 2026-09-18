---
title: Task<A>
description: API reference for Task, an asynchronous and monadic computation description producing a Result in Foomp.
---

`Task<A>` is a computation description that encapsulates lazy, asynchronous evaluations yielding a [`Result<A>`](/reference/base/util/result/). It implements [`Monadic`](/reference/base/types/monadic/) and [`Higher1`](/reference/higher/higher1/), allowing functional transformations such as `map`, `applyTo`, and `flatMap`.

Unlike a `CompletableFuture` which starts running immediately upon creation, a `Task` represents a cold blueprint: it is evaluated lazily only when `runAsync()` or `runAsync(Executor)` is invoked.

---

## Type Signature

```java
package org.quurz.foomp.base.util;

public final class Task<A>
        implements Monadic<Task.µ, A>,
                   Higher1<Task.µ, A>
```

### Type Parameters
* `A`: The type of the value computed by this task.

---

## Witness Type & Higher-Kinded Operations

```java
public static final class µ implements WitnessType { private µ() {} }
```

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A> Task<A>` | `narrow(@NonNull Higher1<? extends Task.µ, A> wide)` | Safely casts a higher-kinded `Higher1` representation to a concrete `Task<A>`. Throws `IllegalArgumentException` if not a `Task`. |

---

## Inner Types: `Task.Executable<A>`

```java
@FunctionalInterface
public interface Executable<A> extends Fun<Executor, CompletableFuture<Result<A>>>
```

`Task.Executable<A>` represents the underlying asynchronous computation dispatched across an `Executor`. It defines the core execution contract `(Executor) -> CompletableFuture<Result<A>>` wrapped by `Task`.

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `CompletableFuture<Result<A>>` | `execute(@NonNull Executor executor)` | Executes the asynchronous computation on the given executor. |
| `default CompletableFuture<Result<A>>` | `apply(@NonNull Executor executor)` | Implements `Fun<Executor, ...>` by delegating directly to `execute(executor)`. |

---

## Static Factory Methods

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A> Task<A>` | `task(@NonNull A value)` | Creates an eagerly completed `Task` yielding the given value wrapped in a successful `Result`. |
| `static Task<Nothing>` | `task()` | Creates an eagerly completed `Task` yielding `Nothing.nothing` wrapped in a successful `Result`. |
| `static <A> Task<A>` | `taskFrom(@NonNull Supplier<A> supplier)` | Creates a lazy `Task` that evaluates the given `Supplier` asynchronously on execution. Caught exceptions yield a `Result.failure`. |
| `static Task<Nothing>` | `taskFrom(@NonNull Runnable runnable)` | Creates a lazy `Task` executing the given `Runnable` asynchronously. Produces `Nothing.nothing` on success, or a `Result.failure` on error. |
| `static <A> Task<A>` | `taskFrom(@NonNull Executable<A> executable)` | Creates a lazy `Task` wrapping a custom `Task.Executable<A>` computation. |

---

## Method Summary

### Functor & Monadic Combinators

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <B> Task<B>` | `map(@NonNull Function<? super A, ? extends B> transformation)` | Lazily transforms the successful result value produced by this task. If the original task failed, the failure is propagated. |
| `@NonNull <B> Task<B>` | `applyTo(@NonNull Higher1<? extends µ, ? extends Function<? super A, ? extends B>> transformation)` | Applicative application: runs both tasks in parallel on the executor and applies the mapped function to the value. |
| `@NonNull <B> Task<B>` | `flatMap(@NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation)` | Monadic bind: sequences two asynchronous tasks, passing the successful result of this task into the next. |
| `<B, C> Task<C>` | `zip(@NonNull Task<B> other, @NonNull BiFunction<? super A, ? super B, ? extends C> combiner)` | Combines this task with another task concurrently using `combiner` on the invocation executor. |
| `<B, C> Task<C>` | `zip(@NonNull Task<B> other, @NonNull BiFunction<? super A, ? super B, ? extends C> combiner, @NonNull Executor executor)` | Combines this task with another task, scheduling `other` explicitly on the provided `executor`. |
| `Task<A>` | `executeOn(@NonNull Executor executor)` | Configures this task to always run on the specified `Executor`, regardless of the executor passed to `runAsync`. |

### Execution & Unwinding (`@UnwindingOperation`)

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `CompletableFuture<Result<A>>` | `runAsync(@NonNull Executor executor)` | Executes this task asynchronously on the supplied `executor`, returning a `CompletableFuture` that completes with a `Result<A>`. |
| `CompletableFuture<Result<A>>` | `runAsync()` | Executes this task asynchronously using the common `ForkJoinPool.commonPool()`, returning a `CompletableFuture<Result<A>>`. |

---

## See Also

* [`Result<A>`](/reference/base/util/result/) – The outcome container (Success / Failure) produced upon task execution.
* [`Eval<A>`](/reference/base/util/eval/) – Synchronous lazy and memoized evaluation monad.
* [`Trampoline<T>`](/reference/base/util/trampoline/) – Stackless tail-recursion driver.
