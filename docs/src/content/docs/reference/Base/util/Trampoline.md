---
title: Trampoline<T>
description: API reference for Trampoline, a stackless recursion container converting recursive calls into heap-safe iterations in Foomp.
---

`Trampoline<T>` is a control abstraction for turning deep recursion into a stack-safe iteration. By converting tail-recursive calls into deferred suspension steps, it allows algorithms to recurse arbitrarily deep without causing a `StackOverflowError`.

---

## Type Signature

```java
package org.quurz.foomp.base.util;

public sealed interface Trampoline<T>
        extends Value<T>
        permits Trampoline.More,
                Trampoline.Done
```

### Type Parameters
* `T`: The type of the final result produced by the trampoline computation.

---

## Static Factory Methods

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <T> Trampoline<T>` | `more(@NonNull Supplier<Trampoline<T>> trampoline)` | Suspends the next recursive step in a thunk, creating a `Trampoline.More` instance. |
| `static <T> Trampoline<T>` | `done(@NonNull T result)` | Completes the recursion with a materialized result value, creating a `Trampoline.Done` instance. |

---

## Method Summary

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull T` | `get()` | Evaluates the trampoline iteratively until a `Trampoline.Done` is reached, returning the final computed value without growing the call stack. |
| `default boolean` | `isPresent()` | Indicates whether a finalized result is immediately present. Returns `true` for `Done` and `false` for `More`. |

---

## Permitted Subtypes

* **`Trampoline.More<T>`**: Represents a pending computation holding a `Supplier<Trampoline<T>>` for the next bounce. When `.get()` is invoked on `More`, it runs an iterative `while` loop unwinding nested steps until `Done` is reached.
* **`Trampoline.Done<T>`**: Represents a completed computation holding the finalized result of type `T`.

---

## Performance Considerations

* Each intermediate recursion step allocates a `Trampoline.More` object and a lambda `Supplier` capturing variables.
* For very deep recursions (e.g. millions of iterations), this trades stack space for heap allocations and garbage collection overhead.
* If a hot loop is performance-critical and straightforward, prefer an explicit Java iterative loop (`for` / `while`).

---

## See Also

* [`Value<T>`](/reference/base/types/value/) – General value container interface implemented by `Trampoline`.
* [`Continuation<A, R>`](/reference/base/util/continuation/) – General continuation-passing monad for non-local control flow.
* [`Eval<A>`](/reference/base/util/eval/) – Lazy and memoized computation container.
