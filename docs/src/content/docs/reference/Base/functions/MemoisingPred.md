---
title: MemoisingPred<A>
description: Reference documentation for the MemoisingPred interface in Foomp Base.
---

`org.quurz.foomp.base.functions.MemoisingPred<A>`

`MemoisingPred` is a specialized, thread-safe decorator for [`Pred<A>`](/reference/base/functions/pred/) that caches boolean test results keyed by input value. It eliminates redundant re-evaluations of complex or computationally expensive predicates.

Because `MemoisingPred` extends [`Pred<A>`](/reference/base/functions/pred/), it seamlessly implements `java.util.function.Predicate<A>` and inherits Foomp's rich boolean algebra combinators (`and`, `or`, `xor`, `nand`, `nor`, `negate`).

```java
public interface MemoisingPred<A>
        extends Pred<A>
```

---

## Type Parameters

| Parameter | Description |
| :--- | :--- |
| `<A>` | The type of the input argument evaluated by this predicate (used as cache key). |

---

## Method Summary

### Core Execution

#### `test`
```java
boolean test(@NonNull final A a)
```
Evaluates the predicate for input `a`. Returns the cached boolean result if previously computed, or executes the underlying predicate, caches, and returns the result.
* **Throws:** `NullPointerException` if `a` is `null`.
* **Concurrency:** Guaranteed thread-safe; atomic evaluation via `ConcurrentHashMap.computeIfAbsent`.

---

### Factory Methods

#### `memoisingPred` (Static Factory)
```java
static <A> MemoisingPred<A> memoisingPred(final @NonNull Pred<A> pred)
```
Creates a new memoizing wrapper around the given `Pred<A>`.
* **Throws:** `NullPointerException` if `pred` is `null`.
* **Thread-Safety:** Backed by an internal `ConcurrentHashMap<A, Boolean>`.

:::tip[Fluent Creation via `Pred.memoise()`]
Every `Pred` provides a `.memoise()` default method:
```java
Pred<String> isPalindrome = ...;
MemoisingPred<String> cached = isPalindrome.memoise();
```
:::

---

### Cache Management

#### `clear`
```java
MemoisingPred<A> clear()
```
Clears all entries from the internal cache. Subsequent calls will re-evaluate the underlying predicate for any input argument.
* **Returns:** `this` instance for method chaining.

---

## Key Contracts & Design

### 1. Thread Safety & Atomic Resolution
`MemoisingPred` stores evaluated `Boolean` outcomes in a `ConcurrentHashMap`. When multiple threads evaluate the predicate for the same unseen argument concurrently:
* The underlying predicate is tested exactly once.
* All caller threads receive the atomic cached boolean outcome.

### 2. Equality & Keying
Cache lookups rely strictly on `Object.equals(Object)` and `Object.hashCode()` of type `<A>`.
* The input type `<A>` should be immutable with consistent equality semantics.

### 3. Referential Transparency (Purity)
`MemoisingPred` assumes pure predicate evaluation:
* Given an equal input object, the predicate should always return the same boolean value.
* Avoid memoizing predicates that depend on external mutable state (e.g., current time or mutable flags).

### 4. Memory Retention
The cache is unbounded. For large or unboundedly evolving key spaces, call `.clear()` periodically or manage the lifecycle of the `MemoisingPred` instance.

---

## Hierarchy & Related Types

```
                    ┌─────────────────────────┐
                    │  java.util.function.    │
                    │      Predicate<A>       │
                    └────────────┬────────────┘
                                 │
                    ┌────────────┴────────────┐
                    ▼                         ▼
┌─────────────────────────┐      ┌─────────────────────────┐
│        Pred<A>          │      │   Fun<A, Boolean>       │
└────────────┬────────────┘      └─────────────────────────┘
             │
             ▼
┌─────────────────────────┐
│   MemoisingPred<A>      │
└─────────────────────────┘
```

* **[`Pred<A>`](/reference/base/functions/pred/):** Base functional predicate interface with extended boolean combinators.
* **[`MemoisingFun<X, Y>`](/reference/base/functions/memoisingfun/):** Memoized general function returning arbitrary types `<Y>`.
* **[`MemoisingApplicable<X, Y>`](/reference/base/functions/memoisingapplicable/):** Throwing memoized operation caching results and exceptions.

---

## See Also

* [Guide: MemoisingPred in Practice](/guides/base/functions/memoisingpred/) – Practical usage patterns and stream filtering recipes.
* [`Pred<A>` Reference](/reference/base/functions/pred/) – Boolean predicate interface with algebraic combinators.
* [`MemoisingFun<X, Y>` Reference](/reference/base/functions/memoisingfun/) – General memoized function interface.
* [Full JavaDoc: `MemoisingPred`](/api/base/foomp.base/org/quurz/foomp/base/functions/MemoisingPred.html)
