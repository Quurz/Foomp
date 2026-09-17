---
title: Fun3<X1, X2, X3, Y>
description: Reference documentation for the ternary functional interface Fun3 and its algebraic combinators in Foomp Base.
---

`org.quurz.foomp.base.functions.Fun3<X1, X2, X3, Y>`

`Fun3<X1, X2, X3, Y>` is Foomp's functional interface for ternary functions (functions taking three non-null arguments and returning a non-null result). It fills the gap in standard Java SE (which stops at binary `BiFunction`), equipping 3-argument functions with strict null safety, forward composition, currying, and partial application returning [`Fun2`](/reference/base/functions/fun2/).

```java
@FunctionalInterface
public interface Fun3<X1, X2, X3, Y>
```

---

## Type Parameters

| Parameter | Description |
| :--- | :--- |
| `<X1>` | The type of the first argument. |
| `<X2>` | The type of the second argument. |
| `<X3>` | The type of the third argument. |
| `<Y>` | The type of the result. |

---

## Method Summary

### Core Invocation

#### `apply`
```java
@Pure
@NonNull
Y apply(@NonNull final X1 x1,
        @NonNull final X2 x2,
        @NonNull final X3 x3)
```
Evaluates this ternary function with the three provided non-null arguments.
* **Contract:** Neither `x1`, `x2`, nor `x3` may be `null`; the returned value must never be `null`.
* **Note:** The `@NonNull` annotations define a formal type-system contract. As `apply` is an abstract interface method, runtime enforcement within custom lambda implementations or concrete classes is the responsibility of the implementer. Built-in combinators (like `andThen`, `curry`, and `partial1..3`) enforce these null checks at runtime boundaries.

---

### Static Factory & Transformation Methods

#### `uncurry`
```java
static <X1, X2, X3, Y> Fun3<X1, X2, X3, Y> uncurry(
    @NonNull final Function<X1, Function<X2, Function<X3, Y>>> curried
)
```
Transforms a 3-level curried higher-order function chain (`Function<X1, Function<X2, Function<X3, Y>>>`) into an equivalent ternary function `Fun3<X1, X2, X3, Y>`.
* Computes `(x1, x2, x3) -> curried.apply(x1).apply(x2).apply(x3)`.
* **Throws:** `NullPointerException` if `curried` is `null` or any intermediate evaluation yields `null`.

---

### Algebraic Combinators & Transformations

#### `curry`
```java
default @NonNull Fun<X1, Fun<X2, Fun<X3, Y>>> curry()
```
Transforms this ternary function into a curried unary function chain (`x1 -> x2 -> x3 -> apply(x1, x2, x3)`).
* Returns nested [`Fun<X, Y>`](/reference/base/functions/fun/) instances, preserving full unary combinator capabilities at each step.
* **Contract:** Neither intermediate functions nor the final result may be `null`.

#### `partial1`
```java
default @NonNull Fun2<X2, X3, Y> partial1(@NonNull final Supplier<X1> supplier)
```
Partially applies this ternary function by binding the **first argument** (`X1`) via a `Supplier<X1>`.
* Returns a binary [`Fun2<X2, X3, Y>`](/reference/base/functions/fun2/).
* **Deferred evaluation:** The `supplier` is invoked **every time** the resulting binary function is executed.
* **Throws:** `NullPointerException` if `supplier` is `null`, if the supplier yields `null`, or if subsequent arguments/results are `null`.

#### `partial2`
```java
default @NonNull Fun2<X1, X3, Y> partial2(@NonNull final Supplier<X2> supplier)
```
Partially applies this ternary function by binding the **second argument** (`X2`) via a `Supplier<X2>`.
* Returns a binary [`Fun2<X1, X3, Y>`](/reference/base/functions/fun2/).
* **Deferred evaluation:** The `supplier` is invoked **every time** the resulting binary function is executed.
* **Throws:** `NullPointerException` if `supplier` is `null`, if the supplier yields `null`, or if subsequent arguments/results are `null`.

#### `partial3`
```java
default @NonNull Fun2<X1, X2, Y> partial3(@NonNull final Supplier<X3> supplier)
```
Partially applies this ternary function by binding the **third argument** (`X3`) via a `Supplier<X3>`.
* Returns a binary [`Fun2<X1, X2, Y>`](/reference/base/functions/fun2/).
* **Deferred evaluation:** The `supplier` is invoked **every time** the resulting binary function is executed.
* **Throws:** `NullPointerException` if `supplier` is `null`, if the supplier yields `null`, or if subsequent arguments/results are `null`.

#### `andThen`
```java
default <Z> @NonNull Fun3<X1, X2, X3, Z> andThen(
    @NonNull final Function<? super Y, ? extends Z> next
)
```
Returns a composed ternary function representing `next ∘ this`. Evaluates `next.apply(this.apply(x1, x2, x3))`.
* **Throws:** `NullPointerException` if `next` is `null` or any evaluation produces `null`.

---

## Contract & Semantics

1. **Non-Null Contract & Responsibility:**  
   `@NonNull` on `apply(...)` defines an API and type-level contract. Because `apply` is an abstract interface method, runtime validation within direct lambda implementations is the responsibility of the caller/implementer. Foomp's built-in combinators (`andThen`, `curry`, `uncurry`, `partial1..3`) systematically enforce non-null boundaries at runtime.
2. **Dynamic Supplier Semantics for Partial Application:**  
   The `partial1`, `partial2`, and `partial3` methods accept dynamic suppliers rather than static values. This enables binding arguments to live system context (such as environment variables, clocks, or session tokens) while yielding a reusable `Fun2`.
3. **Arity Reduction Pipeline:**  
   Partial application on `Fun3` yields a binary [`Fun2`](/reference/base/functions/fun2/), which in turn can be partially applied to produce a unary [`Fun`](/reference/base/functions/fun/), providing a consistent and composable arity-reduction hierarchy.

---

## Hierarchy & Related Types

```
               ┌──────────────────────────────┐
               │     Fun3<X1, X2, X3, Y>      │
               └──────────────┬───────────────┘
                              │
         ┌────────────────────┼────────────────────┐
         ▼                    ▼                    ▼
   .partial1(s)         .partial2(s)         .partial3(s)
         │                    │                    │
         └────────────────────┼────────────────────┘
                              ▼
               ┌──────────────────────────────┐
               │       Fun2<A, B, Y>          │
               └──────────────┬───────────────┘
                              │
                        .partial1/2(s)
                              ▼
               ┌──────────────────────────────┐
               │         Fun<X, Y>            │
               └──────────────────────────────┘
```

* **[`Fun<X, Y>`](/reference/base/functions/fun/):** Unary function interface resulting from full currying or multi-stage partial application.
* **[`Fun2<X1, X2, Y>`](/reference/base/functions/fun2/):** Binary function interface produced when partially applying any single argument of a `Fun3`.
* **[`Fun4<X1, X2, X3, X4, Y>`](/reference/base/functions/fun4/):** Quaternary function interface.

---

## See Also

* [Guide: Working with Fun3](/guides/base/functions/fun3/) – Practical examples for currying 3-argument pipelines, dynamic parameter binding, and composition.
* [Reference: `Fun2<X1, X2, Y>`](/reference/base/functions/fun2/) – Binary functional interface.
* [Reference: `Fun4<X1, X2, X3, X4, Y>`](/reference/base/functions/fun4/) – Quaternary functional interface.
* [JavaDoc: `org.quurz.foomp.base.functions.Fun3`](/api/base/foomp.base/org/quurz/foomp/base/functions/Fun3.html)
