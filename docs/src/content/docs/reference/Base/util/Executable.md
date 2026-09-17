---
title: Executable<A>
description: API reference for Executable, a functional interface representing asynchronous executions on an Executor.
---

`Executable<A>` is a functional interface that models an asynchronous computation ready to be dispatched to a provided [`Executor`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/Executor.html).

It extends [`Fun<Executor, CompletableFuture<Result<A>>>`](/reference/base/functions/fun/), integrating directly with Foomp's functional function hierarchies and `Task<A>`.

---

## Type Signature

```java
package org.quurz.foomp.base.util;

@FunctionalInterface
public interface Executable<A>
        extends Fun<Executor, CompletableFuture<Result<A>>>
```

### Type Parameters
* `A`: The result value type produced upon successful execution of the computation.

---

## Method Summary

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull CompletableFuture<Result<A>>` | `execute(@NonNull Executor executor)` | **(Abstract method)** Executes the computation using the specified `Executor` and returns a `CompletableFuture` containing the `Result<A>`. |
| `@NonNull default CompletableFuture<Result<A>>` | `apply(@NonNull Executor executor)` | Applies this function by delegating to `execute(executor)`. |

---

## Functional Interface Usage

As a single-abstract-method (SAM) interface, `Executable<A>` can be defined using lambda expressions or method references:

```java
Executable<String> fetchUser = executor -> CompletableFuture.supplyAsync(() -> {
    return Result.success("User_42");
}, executor);
```

---

## See Also

* [`Task<A>`](/reference/base/util/task/) – Composable asynchronous task monadic container wrapping `Executable`.
* [`Result<A>`](/reference/base/util/result/) – Two-state computation result (`Success` or `Failure`).
* [`Fun<A, B>`](/reference/base/functions/fun/) – Functional single-argument function interface.
* [`Executor`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/Executor.html) – Standard Java asynchronous task executor.
