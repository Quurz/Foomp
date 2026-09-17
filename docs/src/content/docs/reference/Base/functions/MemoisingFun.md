---
title: MemoisingFun<X, Y>
description: Reference documentation for the MemoisingFun interface in Foomp Base.
---

`org.quurz.foomp.base.functions.MemoisingFun<X, Y>`

`MemoisingFun` is a specialized, thread-safe decorator for [`Fun<X, Y>`](/reference/base/functions/fun/) that caches evaluation results indexed by input argument. It ensures that expensive or recurring computations are evaluated at most once per distinct input.

Because `MemoisingFun` extends [`Fun<X, Y>`](/reference/base/functions/fun/), it retains full interoperability with `java.util.function.Function<X, Y>` and inherits all functional combinators (`compose`, `andThen`, `async`, `curry`, etc.).

```java
public interface MemoisingFun<X, Y>
        extends Fun<X, Y>
```

---

## Type Parameters

| Parameter | Description |
| :--- | :--- |
| `<X>` | The type of the input argument (used as cache key). |
| `<Y>` | The type of the resulting value. |

---

## Method Summary

### Core Execution

#### `apply`
```java
@NonNull Y apply(@NonNull final X x)
```
Evaluates this function for input `x`, returning the cached result if available or computing, caching, and returning the result on first invocation.
* **Contract:** Neither `x` nor the computed return value may be `null`.
* **Throws:** `NullPointerException` if `x` is `null` or if the wrapped function evaluates to `null`.
* **Concurrency:** Guaranteed thread-safe; concurrent evaluations for identical keys execute atomically.

---

### Factory Methods

#### `memoisingFun` (Static Factory)
```java
static <X, Y> MemoisingFun<X, Y> memoisingFun(final @NonNull Function<X, Y> function)
```
Creates a new memoizing wrapper over the provided `Function<X, Y>`.
* **Throws:** `NullPointerException` if `function` is `null`.
* **Thread-Safety:** The returned instance is backed by a `ConcurrentHashMap`.

:::tip[Fluent Creation via `Fun.memoise()`]
Every `Fun` instance provides a `.memoise()` default method:
```java
Fun<Integer, BigInteger> fib = ...;
MemoisingFun<Integer, BigInteger> cachedFib = fib.memoise();
```
:::

---

### Cache Management

#### `clear`
```java
MemoisingFun<X, Y> clear()
```
Clears all cached entries. Subsequent invocations will recompute values instead of returning previously cached ones.
* **Returns:** `this` instance for method chaining.

---

## Key Contracts & Design

### 1. Thread Safety & Atomic Caching
`MemoisingFun` is backed by `ConcurrentHashMap.computeIfAbsent`. When multiple concurrent threads call `apply(x)` with the same argument `x`:
* The underlying function is executed exactly once.
* All threads receive the identical cached result.

### 2. Equality & Cache Keying
Cache lookup relies strictly on the `equals(Object)` and `hashCode()` contracts of the input type `<X>`.
* Ensure that cache keys are immutable value objects.
* Equal keys produce immediate cache hits regardless of object reference identity (`==`).

### 3. Referential Transparency (Purity)
`MemoisingFun` is intended for pure mathematical functions:
* Given identical input arguments, the function must always return identical output.
* The function should not rely on mutable external state or execute observable side effects.

### 4. Memory Retention
The internal cache is unbounded. For applications with huge key domains or long-running processes, use `.clear()` periodically to reclaim memory or limit the scope of the `MemoisingFun` instance.

---

## Hierarchy & Related Types

```
                    ┌─────────────────────────┐
                    │    Applicable<X, Y>     │
                    └────────────┬────────────┘
                                 │
                    ┌────────────┴────────────┐
                    ▼                         ▼
┌─────────────────────────┐      ┌─────────────────────────┐
│       Fun<X, Y>         │      │MemoisingApplicable<X, Y>│
└────────────┬────────────┘      └─────────────────────────┘
             │
             ▼
┌─────────────────────────┐
│   MemoisingFun<X, Y>    │
└─────────────────────────┘
```

* **[`Fun<X, Y>`](/reference/base/functions/fun/):** Core non-throwing functional interface.
* **[`MemoisingApplicable<X, Y>`](/reference/base/functions/memoisingapplicable/):** Throwing variant that caches both successful results and exceptions.
* **[`MemoisingPred<A>`](/reference/base/functions/memoisingpred/):** Boolean predicate specialization.

---

## See Also

* [Guide: MemoisingFun in Practice](/guides/base/functions/memoisingfun/) – Practical usage patterns and performance optimization recipes.
* [`Fun<X, Y>` Reference](/reference/base/functions/fun/) – Exception-free functional abstraction.
* [`MemoisingApplicable<X, Y>` Reference](/reference/base/functions/memoisingapplicable/) – Exception-caching alternative.
* [Full JavaDoc: `MemoisingFun`](/api/base/foomp.base/org/quurz/foomp/base/functions/MemoisingFun.html)
