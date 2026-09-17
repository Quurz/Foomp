---
title: MemoisingApplicable<X, Y>
description: Reference documentation for the MemoisingApplicable interface in Foomp Base.
---

`org.quurz.foomp.base.functions.MemoisingApplicable<X, Y>`

`MemoisingApplicable` is a specialized, thread-safe decorator for [`Applicable<X, Y>`](/reference/base/functions/applicable/) that caches successful evaluation results based on input values. It eliminates redundant computations for repeated inputs while preserving the throwing semantics of `Applicable`.

Unlike caching layers that capture failures, `MemoisingApplicable` **only caches successful outcomes**. When the underlying operation throws an exception, that exception is propagated directly to the caller without being stored in the cache. This ensures that subsequent invocations with the same argument can retry the operation (e.g., when external resources or transient failures resolve).

```java
public interface MemoisingApplicable<X, Y>
        extends Applicable<X, Y>
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
Y apply(@NonNull final X x) throws Exception
```
Evaluates the operation for input `x`, returning the cached result if available or computing, caching, and returning the result on first successful invocation.
* **Throws:** `Exception` if the underlying computation fails. The exception is propagated immediately and **not cached**.
* **Throws:** `NullPointerException` if `x` is `null` or if the underlying computation evaluates to `null`.
* **Caching Semantics:**
  * If the evaluation succeeds with value `y`, `y` is stored in the cache and returned on subsequent calls for key `x`.
  * If the evaluation throws an exception, nothing is stored in the cache, allowing subsequent calls to attempt evaluation again.

---

### Factory Methods

#### `memoisingApplicable` (Static Factory)
```java
static <X, Y> MemoisingApplicable<X, Y> memoisingApplicable(final @NonNull Applicable<X, Y> applicable)
```
Creates a new `MemoisingApplicable` wrapping the supplied `Applicable`.
* **Throws:** `NullPointerException` if `applicable` is `null`.
* **Concurrency:** The returned instance is thread-safe, utilizing a `ConcurrentHashMap` with atomic `computeIfAbsent` insertion.

:::tip[Fluent Creation]
You can also create a memoized instance directly via the default method on [`Applicable`](/reference/base/functions/applicable/):
```java
Applicable<String, Config> loader = ...;
MemoisingApplicable<String, Config> cached = loader.memoise();
```
:::

---

### Cache Management

#### `clear`
```java
@NonNull MemoisingApplicable<X, Y> clear()
```
Clears all entries from the internal cache. Subsequent invocations will recompute results.
* **Returns:** `this` instance for method chaining.

---

## Key Contracts & Design

### 1. Success-Only Caching & Retries
`MemoisingApplicable` guarantees that only valid, non-null return values are stored in the cache:
* **Successful Evaluations:** The result is saved in the internal cache and reused for all subsequent calls with equal input.
* **Failures / Exceptions:** Exceptions are thrown directly to the caller and are never stored. If a service or database was temporarily unreachable, a subsequent call with the same parameter will run the underlying computation again.

### 2. Thread Safety & Atomic Computation
All caching operations are fully concurrent and thread-safe. Cache population is performed atomically through `ConcurrentHashMap.computeIfAbsent`. Multiple concurrent threads requesting the same unseen input key will coordinate so that the computation runs once and, upon success, all waiting threads receive the computed result.

### 3. Key Equality Semantics
Cache keys rely on standard Java equality semantics:
* Input instances are looked up and stored using `Object.hashCode()` and `Object.equals(Object)`.
* Input types `<X>` should properly implement immutable value semantics with consistent `equals` and `hashCode`.

### 4. Referential Transparency (Purity)
`MemoisingApplicable` is intended for operations that are referentially transparent:
* For equal inputs, the wrapped operation should always produce the same result without depending on mutable external state.
* Wrapping operations that have observable side effects can lead to skipped side effects once a successful result has been cached.

### 5. Memory Management
The internal cache is unbounded. If your input key domain is large or infinite, use `.clear()` periodically or manage instance lifecycles to avoid unbounded memory retention.

---

## Hierarchy & Related Types

```
                    ┌─────────────────────────┐
                    │    Applicable<X, Y>     │
                    └────────────┬────────────┘
                                 │
           ┌─────────────────────┴─────────────────────┐
           ▼                                           ▼
┌─────────────────────────┐                 ┌─────────────────────────┐
│       Fun<X, Y>         │                 │MemoisingApplicable<X, Y>│
│  (no checked exception) │                 │ (caches successes only) │
└─────────────────────────┘                 └─────────────────────────┘
```

* **[`Applicable<X, Y>`](/reference/base/functions/applicable/):** Base functional interface allowing checked exceptions.
* **[`MemoisingFun<X, Y>`](/reference/base/functions/memoisingfun/):** Memoizing wrapper for non-throwing functions (`Fun<X, Y>`).
* **[`MemoisingPred<A>`](/reference/base/functions/memoisingpred/):** Memoizing wrapper for boolean predicates (`Pred<A>`).

---

## See Also

* [Guide: MemoisingApplicable in Practice](/guides/base/functions/memoisingapplicable/) – Practical usage patterns and retry workflows.
* [`Applicable<X, Y>` Reference](/reference/base/functions/applicable/) – Core throwing function interface.
* [`MemoisingFun<X, Y>` Reference](/reference/base/functions/memoisingfun/) – Non-throwing memoized functions.
* [Full JavaDoc: `MemoisingApplicable`](/api/base/foomp.base/org/quurz/foomp/base/functions/MemoisingApplicable.html)
