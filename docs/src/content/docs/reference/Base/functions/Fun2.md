---
title: Fun2<X1, X2, Y>
description: Reference documentation for the binary functional interface Fun2 and its algebraic combinators in Foomp Base.
---

`org.quurz.foomp.base.functions.Fun2<X1, X2, Y>`

`Fun2<X1, X2, Y>` is Foomp's functional interface for binary functions (functions taking two non-null arguments and returning a non-null result). It extends Java's standard `java.util.function.BiFunction<X1, X2, Y>`, enhancing it with strict null safety, forward composition, argument flipping, currying, and partial application.

```java
@FunctionalInterface
public interface Fun2<X1, X2, Y>
        extends BiFunction<X1, X2, Y>
```

---

## Type Parameters

| Parameter | Description |
| :--- | :--- |
| `<X1>` | The type of the first argument. |
| `<X2>` | The type of the second argument. |
| `<Y>` | The type of the result. |

---

## Method Summary

### Core Invocation

#### `apply`
```java
@Override
@Pure
@NonNull
Y apply(@NonNull final X1 x1, @NonNull final X2 x2)
```
Evaluates this binary function with the two provided non-null arguments.
* **Contract:** Neither `x1` nor `x2` may be `null`; the returned value must never be `null`.
* **Throws:** `NullPointerException` if `x1` or `x2` is `null`, or if the function evaluation produces `null`.

---

### Static Factory & Transformation Methods

#### `fun2`
```java
static <X1, X2, Y> Fun2<X1, X2, Y> fun2(@NonNull final BiFunction<X1, X2, Y> function)
```
Wraps a standard `java.util.function.BiFunction<X1, X2, Y>` into a `Fun2<X1, X2, Y>`, enforcing strict non-null validation on inputs and return values at runtime.
* **Throws:** `NullPointerException` if `function` is `null`.

#### `uncurry`
```java
static <X1, X2, Y> Fun2<X1, X2, Y> uncurry(@NonNull final Fun<X1, Fun<X2, Y>> curried)
```
Transforms a curried higher-order function (`Fun<X1, Fun<X2, Y>>`) into an equivalent binary function `Fun2<X1, X2, Y>`.
* Computes `(x1, x2) -> curried.apply(x1).apply(x2)`.
* **Throws:** `NullPointerException` if `curried` is `null` or any intermediate evaluation yields `null`.

---

### Algebraic Combinators & Transformations

#### `curry`
```java
default @NonNull Fun<X1, Fun<X2, Y>> curry()
```
Transforms this binary function into a curried unary function returning another unary function (`x1 -> x2 -> apply(x1, x2)`).
* Returns a chain of [`Fun<X, Y>`](/reference/base/functions/fun/) interfaces, preserving all unary combinators.
* **Contract:** Neither intermediate unary functions nor the final result may be `null`.

#### `partial1`
```java
default @NonNull Fun<X2, Y> partial1(@NonNull final Supplier<X1> supplier)
```
Partially applies this function by binding the **first argument** (`X1`) via a `Supplier<X1>`.
* Returns a unary [`Fun<X2, Y>`](/reference/base/functions/fun/).
* **Deferred evaluation:** The `supplier` is invoked **every time** the returned unary function is executed.
* **Throws:** `NullPointerException` if `supplier` is `null`, if the supplier yields `null`, or if the second argument/result is `null`.

#### `partial2`
```java
default @NonNull Fun<X1, Y> partial2(@NonNull final Supplier<X2> supplier)
```
Partially applies this function by binding the **second argument** (`X2`) via a `Supplier<X2>`.
* Returns a unary [`Fun<X1, Y>`](/reference/base/functions/fun/).
* **Deferred evaluation:** The `supplier` is invoked **every time** the returned unary function is executed.
* **Throws:** `NullPointerException` if `supplier` is `null`, if the supplier yields `null`, or if the first argument/result is `null`.

#### `flip`
```java
default @NonNull Fun2<X2, X1, Y> flip()
```
Returns a new `Fun2` that reverses the argument order before invoking the underlying function: `(x2, x1) -> apply(x1, x2)`.
* **Throws:** `NullPointerException` if either input is `null`.

#### `andThen`
```java
@Override
default <Z> @NonNull Fun2<X1, X2, Z> andThen(@NonNull final Function<? super Y, ? extends Z> next)
```
Returns a composed binary function representing `next ∘ this`. Evaluates `next.apply(this.apply(x1, x2))`.
* **Throws:** `NullPointerException` if `next` is `null` or any intermediate evaluation returns `null`.

---

## Contract & Semantics

1. **Strict Non-Null Hygiene:**  
   Like all Foomp functional interfaces, `Fun2` guarantees that neither inputs nor computed outputs are `null`. Any `null` values cause immediate and deterministic `NullPointerException`s.
2. **Dynamic Supplier Semantics for Partial Application:**  
   Unlike static currying (`curry().apply(val)`), `partial1` and `partial2` accept a `Supplier`. This allows passing dynamic or context-dependent arguments (such as configuration readers, timestamps, or stateful providers) that are resolved on every invocation.
3. **JDK Compatibility:**  
   Because `Fun2` extends `BiFunction`, instances can be passed directly to standard Java APIs (such as `Map.compute`, `Map.merge`, or `Stream.reduce`).

---

## Hierarchy & Related Types

```
               ┌──────────────────────────────┐
               │ java.util.function.BiFunction│
               └──────────────┬───────────────┘
                              │
                              ▼
               ┌──────────────────────────────┐
               │     Fun2<X1, X2, Y>          │
               └──────────────┬───────────────┘
                              │
         ┌────────────────────┼────────────────────┐
         ▼                    ▼                    ▼
┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐
│  Operator2<A>   │  │   Pred2<A1, A2> │  │   Comparer<A>   │
│ (Fun2<A, A, A>) │  │(Fun2<A1,A2,Bool>│  │(Fun2<A,A,Relat>│
└─────────────────┘  └─────────────────┘  └─────────────────┘
```

* **[`Fun<X, Y>`](/reference/base/functions/fun/):** Unary functional interface returned by `curry()`, `partial1()`, and `partial2()`.
* **[`Operator2<A>`](/reference/base/functions/operator2/):** Homogeneous binary operator specializing `Fun2<A, A, A>`.
* **[`Pred2<A1, A2>`](/reference/base/functions/pred2/):** Binary predicate specializing `Fun2<A1, A2, Boolean>`.
* **[`Comparer<A>`](/reference/base/functions/comparer/):** Three-way comparison function specializing `Fun2<A, A, Relation>`.

---

## See Also

* [Guide: Working with Fun2](/guides/base/functions/fun2/) – Practical examples for currying, argument flipping, and supplier-based partial application.
* [Reference: `Fun<X, Y>`](/reference/base/functions/fun/) – Single-argument functional interface.
* [Reference: `Comparer<A>`](/reference/base/functions/comparer/) – Type-safe three-way comparator based on `Fun2`.
* [JavaDoc: `org.quurz.foomp.base.functions.Fun2`](/api/base/foomp.base/org/quurz/foomp/base/functions/Fun2.html)
