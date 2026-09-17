---
title: Fun<X, Y>
description: Reference documentation for the Fun functional interface and combinators in Foomp Base.
---

`org.quurz.foomp.base.functions.Fun<X, Y>`

`Fun<X, Y>` is Foomp's primary single-argument functional interface. It extends both standard Java's `java.util.function.Function<X, Y>` and Foomp's `Applicable<X, Y>`, combining JDK interoperability with strict non-null contracts, exception safety, and a rich set of functional combinators.

Unlike standard `Function`, `Fun` establishes **strict null-hygiene contracts** (rejecting `null` inputs and `null` returns) and provides first-class methods for **composition**, **memoization**, and **asynchronous execution**.

```java
@FunctionalInterface
public interface Fun<X, Y>
        extends Function<X, Y>,
                Applicable<X, Y>
```

---

## Type Parameters

| Parameter | Description |
| :--- | :--- |
| `<X>` | The type of the input argument. |
| `<Y>` | The type of the resulting value. |

---

## Method Summary

### Core Execution

#### `apply`
```java
@Override
@Pure
@NonNull
Y apply(@NonNull final X x)
```
Executes the function with the provided non-null argument `x`.
* **Contract:** `x` must not be `null`; the returned result must not be `null`.
* **Throws:** `NullPointerException` if `x` is `null` or if the function evaluation yields `null`.
* **Exceptions:** Guarantees that no checked exceptions are thrown (refining the contract inherited from `Applicable`).

---

### Static Factory & Utility Methods

#### `fun`
```java
static <X, Y> Fun<X, Y> fun(@NonNull final Function<X, Y> function)
```
Wraps a standard `java.util.function.Function<X, Y>` into a `Fun<X, Y>`, enforcing strict non-null input and output validation at runtime.
* **Throws:** `NullPointerException` if `function` is `null`.

#### `identity`
```java
static <X> Fun<X, X> identity()
```
Returns a strict identity function. Unlike JDK's `Function.identity()`, `Fun.identity()` strictly rejects `null` inputs with a `NullPointerException`.

---

### Combinators & Composition

#### `compose`
```java
default <W> @NonNull Fun<W, Y> compose(@NonNull final Fun<? super W, ? extends X> first)
```
Returns a composed function representing `this ∘ first`. Computes `this.apply(first.apply(w))`.
* Both `first` and `this` are validated for non-null inputs and results.
* **Throws:** `NullPointerException` if `first` is `null` or any intermediate result is `null`.

#### `andThen`
```java
default <Z> @NonNull Fun<X, Z> andThen(@NonNull final Fun<? super Y, ? extends Z> next)
```
Returns a composed function representing `next ∘ this`. Computes `next.apply(this.apply(x))`.
* **Throws:** `NullPointerException` if `next` is `null` or any intermediate result is `null`.

---

### Caching & Null Safety

#### `memoise`
```java
default @NonNull MemoisingFun<X, Y> memoise()
```
Returns a thread-safe, memoized view of this function backed by a concurrent cache.
* Subsequent calls with equal inputs return cached results without re-evaluating the underlying logic.
* **Contract:** Cache semantics require **referential transparency (purity)**. Memoizing functions with side effects is not recommended.

#### `nullSafe`
```java
default @NonNull Fun<X, Y> nullSafe()
```
Returns a defensive null-checking wrapper around this function, explicitly verifying that the input argument and the computation result are not `null`.

#### `applicable`
```java
default @NonNull Applicable<X, Y> applicable()
```
Exposes this function explicitly as an [`Applicable<X, Y>`](/reference/base/functions/applicable/).

---

### Asynchronous Execution

#### `async` (Default ForkJoinPool)
```java
default @NonNull Fun<X, CompletableFuture<Y>> async()
```
Transforms this synchronous function into an asynchronous function executing on `ForkJoinPool.commonPool()`.
* Returns a `CompletableFuture<Y>`.
* If evaluation throws an exception or returns `null`, the returned future completes exceptionally.

#### `async` (Custom Executor)
```java
default @NonNull Fun<X, CompletableFuture<Y>> async(final @NonNull Executor executor)
```
Transforms this synchronous function into an asynchronous function executing on the specified `Executor`.
* **Throws:** `NullPointerException` if `executor` or the input `x` is `null`.

---

## Contract & Semantics

1. **Strict Non-Null Hygiene:**  
   Standard Java functions frequently allow `null` values, leading to subtle `NullPointerException`s downstream. `Fun` enforces that inputs and outputs are never `null`.
2. **Checked Exception Free:**  
   While `Applicable<X, Y>` permits checked exceptions, `Fun<X, Y>` refines the contract so that `apply` does not declare checked exceptions, making it directly compatible with standard Java APIs (`Stream.map`, `Optional.map`).
3. **Purity vs. Side Effects:**  
   Functions defined with `Fun` are permitted to perform side effects. However, memoization via `.memoise()` assumes referential transparency.

---

## Hierarchy & Related Types

```
         ┌─────────────────────────┐       ┌─────────────────────────┐
         │ java.util.Function<X,Y> │       │    Applicable<X, Y>     │
         └────────────┬────────────┘       └────────────┬────────────┘
                      │                                 │
                      └────────────────┬────────────────┘
                                       │
                                       ▼
                         ┌───────────────────────────┐
                         │         Fun<X, Y>         │
                         └─────────────┬─────────────┘
                                       │
        ┌──────────────────────────────┼──────────────────────────────┐
        ▼                              ▼                              ▼
┌───────────────┐              ┌───────────────┐              ┌───────────────┐
│  Operator<X>  │              │    Pred<X>    │              │MemoisingFun<X>│
│ (Fun<X, X>)   │              │(Fun<X,Boolean>│              │(Thread-safe   │
└───────────────┘              └───────────────┘              │ Caching)      │
                                                              └───────────────┘
```

* **[`Applicable<X, Y>`](/reference/base/functions/applicable/):** Root functional interface allowing checked exceptions.
* **[`Operator<X>`](/reference/base/functions/operator/):** Specialization of `Fun<X, X>` where input and output types are identical.
* **[`Pred<X>`](/reference/base/functions/pred/):** Predicate specialization extending `Fun<X, Boolean>`.
* **[`Fun2<A, B, C>`](/reference/base/functions/fun2/):** Binary function extending `Fun` capabilities with currying and partial application.
* **[`MemoisingFun<X, Y>`](/reference/base/functions/memoisingfun/):** Thread-safe caching wrapper.

---

## See Also

* [Guide: Working with Fun](/guides/base/functions/fun/) – Practical pipelines, async computations, and memoization recipes.
* [Reference: `Applicable<X, Y>`](/reference/base/functions/applicable/) – Checked-exception functional interface.
* [Reference: `Fun2<A, B, C>`](/reference/base/functions/fun2/) – Binary functional interface.
* [JavaDoc: `org.quurz.foomp.base.functions.Fun`](/api/base/foomp.base/org/quurz/foomp/base/functions/Fun.html)
