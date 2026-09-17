---
title: Fun4<X1, X2, X3, X4, Y>
description: Reference documentation for the quaternary functional interface Fun4 and its algebraic combinators in Foomp Base.
---

`org.quurz.foomp.base.functions.Fun4<X1, X2, X3, X4, Y>`

`Fun4<X1, X2, X3, X4, Y>` is Foomp's functional interface for quaternary functions (functions taking four non-null arguments and returning a non-null result). It enables high-arity functional transformations in Java SE with strict null validation, forward composition, 4-level currying, and partial application returning [`Fun3`](/reference/base/functions/fun3/).

```java
@FunctionalInterface
public interface Fun4<X1, X2, X3, X4, Y>
```

---

## Type Parameters

| Parameter | Description |
| :--- | :--- |
| `<X1>` | The type of the first argument. |
| `<X2>` | The type of the second argument. |
| `<X3>` | The type of the third argument. |
| `<X4>` | The type of the fourth argument. |
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
        @NonNull final X3 x3,
        @NonNull final X4 x4)
```
Evaluates this quaternary function with the four provided non-null arguments.
* **Contract:** Neither `x1`, `x2`, `x3`, nor `x4` may be `null`; the returned value must never be `null`.
* **Note:** The `@NonNull` annotations specify a formal type-system contract. Because `apply` is an abstract interface method, runtime validation within direct lambda implementations is the responsibility of the implementer. Built-in combinators (`andThen`, `curry`, and `partial1..4`) enforce null checks at runtime boundaries.

---

### Static Factory & Transformation Methods

#### `uncurry`
```java
static <X1, X2, X3, X4, Y> Fun4<X1, X2, X3, X4, Y> uncurry(
    @NonNull final Function<X1, Function<X2, Function<X3, Function<X4, Y>>>> curried
)
```
Transforms a 4-level curried higher-order function chain into an equivalent quaternary function `Fun4<X1, X2, X3, X4, Y>`.
* Computes `(x1, x2, x3, x4) -> curried.apply(x1).apply(x2).apply(x3).apply(x4)`.
* **Throws:** `NullPointerException` if `curried` is `null` or any intermediate evaluation yields `null`.

---

### Algebraic Combinators & Transformations

#### `curry`
```java
default @NonNull Fun<X1, Fun<X2, Fun<X3, Fun<X4, Y>>>> curry()
```
Transforms this quaternary function into a curried unary function chain (`x1 -> x2 -> x3 -> x4 -> apply(x1, x2, x3, x4)`).
* Returns nested [`Fun<X, Y>`](/reference/base/functions/fun/) instances across all 4 stages.
* **Contract:** Neither intermediate unary functions nor the final result may be `null`.

#### `partial1`
```java
default @NonNull Fun3<X2, X3, X4, Y> partial1(@NonNull final Supplier<X1> supplier)
```
Partially applies this function by binding the **first argument** (`X1`) via a `Supplier<X1>`.
* Returns a ternary [`Fun3<X2, X3, X4, Y>`](/reference/base/functions/fun3/).
* **Deferred evaluation:** The `supplier` is invoked **every time** the resulting ternary function is executed.
* **Throws:** `NullPointerException` if `supplier` is `null`, if the supplier yields `null`, or if subsequent arguments/results are `null`.

#### `partial2`
```java
default @NonNull Fun3<X1, X3, X4, Y> partial2(@NonNull final Supplier<X2> supplier)
```
Partially applies this function by binding the **second argument** (`X2`) via a `Supplier<X2>`.
* Returns a ternary [`Fun3<X1, X3, X4, Y>`](/reference/base/functions/fun3/).
* **Deferred evaluation:** The `supplier` is invoked **every time** the resulting ternary function is executed.
* **Throws:** `NullPointerException` if `supplier` is `null`, if the supplier yields `null`, or if subsequent arguments/results are `null`.

#### `partial3`
```java
default @NonNull Fun3<X1, X2, X4, Y> partial3(@NonNull final Supplier<X3> supplier)
```
Partially applies this function by binding the **third argument** (`X3`) via a `Supplier<X3>`.
* Returns a ternary [`Fun3<X1, X2, X4, Y>`](/reference/base/functions/fun3/).
* **Deferred evaluation:** The `supplier` is invoked **every time** the resulting ternary function is executed.
* **Throws:** `NullPointerException` if `supplier` is `null`, if the supplier yields `null`, or if subsequent arguments/results are `null`.

#### `partial4`
```java
default @NonNull Fun3<X1, X2, X3, Y> partial4(@NonNull final Supplier<X4> supplier)
```
Partially applies this function by binding the **fourth argument** (`X4`) via a `Supplier<X4>`.
* Returns a ternary [`Fun3<X1, X2, X3, Y>`](/reference/base/functions/fun3/).
* **Deferred evaluation:** The `supplier` is invoked **every time** the resulting ternary function is executed.
* **Throws:** `NullPointerException` if `supplier` is `null`, if the supplier yields `null`, or if subsequent arguments/results are `null`.

#### `andThen`
```java
default <Z> @NonNull Fun4<X1, X2, X3, X4, Z> andThen(
    @NonNull final Function<? super Y, ? extends Z> next
)
```
Returns a composed quaternary function representing `next ∘ this`. Evaluates `next.apply(this.apply(x1, x2, x3, x4))`.
* **Throws:** `NullPointerException` if `next` is `null` or any evaluation produces `null`.

---

## Contract & Semantics

1. **Non-Null Contract & Responsibility:**  
   `@NonNull` on `apply(...)` defines an API and type-level contract. As `apply` is an abstract interface method, runtime checks inside lambda implementations or custom classes are the responsibility of the implementer. Foomp's built-in combinators (`andThen`, `curry`, `uncurry`, `partial1..4`) enforce strict non-null boundaries at runtime.
2. **Multi-Stage Arity Reduction:**  
   `Fun4` forms the top tier of Foomp's multi-arity function family:
   * Partially applying `Fun4` produces [`Fun3`](/reference/base/functions/fun3/).
   * Partially applying `Fun3` produces [`Fun2`](/reference/base/functions/fun2/).
   * Partially applying `Fun2` produces [`Fun`](/reference/base/functions/fun/).
3. **Dynamic Suppliers:**  
   Like all partial methods in Foomp, `partial1` through `partial4` use lazy `Supplier` bindings, allowing runtime context or stateful values to be resolved on each invocation.

---

## Hierarchy & Arity Flow

```
   ┌─────────────────────────────────────┐
   │     Fun4<X1, X2, X3, X4, Y>         │
   └──────────────────┬──────────────────┘
                      │ .partialX(supplier)
                      ▼
   ┌─────────────────────────────────────┐
   │        Fun3<A, B, C, Y>             │
   └──────────────────┬──────────────────┘
                      │ .partialX(supplier)
                      ▼
   ┌─────────────────────────────────────┐
   │          Fun2<A, B, Y>              │
   └──────────────────┬──────────────────┘
                      │ .partialX(supplier)
                      ▼
   ┌─────────────────────────────────────┐
   │            Fun<A, Y>                │
   └─────────────────────────────────────┘
```

---

## See Also

* [Guide: Working with Fun4](/guides/base/functions/fun4/) – Practical recipes for 4-argument functions, step-by-step currying, and partial configuration pipelines.
* [Reference: `Fun3<X1, X2, X3, Y>`](/reference/base/functions/fun3/) – Ternary functional interface.
* [Reference: `Fun2<X1, X2, Y>`](/reference/base/functions/fun2/) – Binary functional interface.
* [Reference: `Fun<X, Y>`](/reference/base/functions/fun/) – Unary functional interface.
* [JavaDoc: `org.quurz.foomp.base.functions.Fun4`](/api/base/foomp.base/org/quurz/foomp/base/functions/Fun4.html)
