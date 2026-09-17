---
title: Operator3<A>
description: Formal API reference and specifications for the Operator3<A> ternary endomorphism interface in Foomp.
---

`org.quurz.foomp.base.functions.Operator3<A>`

`Operator3<A>` represents a homogeneous ternary operator mapping three arguments of type `A` to a result of type `A` ($f: A \times A \times A \to A$). It extends Foomp's [`Fun3<A, A, A, A>`](/reference/base/functions/fun3/), providing partial application methods that return binary [`Operator2<A>`](/reference/base/functions/operator2/) and post-processing with unary operators.

```java
@FunctionalInterface
public interface Operator3<A>
    extends Fun3<A, A, A, A>
```

---

## Type Parameters

* `<A>` – The operand and return type of the ternary operation.

---

## Abstract Methods

### `apply`
```java
@Pure
@NonNull
A apply(@NonNull final A a1, @NonNull final A a2, @NonNull final A a3)
```
Applies the ternary operator to the three operands.
* **Parameters:**
  * `a1` – The first operand; must not be `null`.
  * `a2` – The second operand; must not be `null`.
  * `a3` – The third operand; must not be `null`.
* **Returns:** The computed result of type `A`; never `null`.
* **Throws:** `NullPointerException` if any operand is `null` or if evaluation yields `null`.

---

## Default Methods & Combinators

### `andThen`
```java
default Operator3<A> andThen(@NonNull UnaryOperator<A> after)
```
Returns a composed ternary operator that evaluates `this.apply(a1, a2, a3)` and then applies `after` to the intermediate result.
* **Parameters:** `after` – The unary post-operator; must not be `null`.
* **Returns:** A composed `Operator3<A>`.
* **Throws:** `NullPointerException` if `after` is `null` or if any argument/intermediate result is `null`.

### `partial1`
```java
@Override
@NonNull default Operator2<A> partial1(@NonNull final Supplier<A> supplier)
```
Partially applies the operator by fixing the first argument `a1` from a dynamic `Supplier<A>`, returning a binary [`Operator2<A>`](/reference/base/functions/operator2/) over the remaining operands `(a2, a3)`.
* **Parameters:** `supplier` – The supplier providing `a1`; must not be `null`.
* **Returns:** An `Operator2<A>` accepting operands `(a2, a3)`.
* **Throws:** `NullPointerException` if `supplier` is `null`, or if the supplier evaluates to `null`.

### `partial2`
```java
@Override
@NonNull default Operator2<A> partial2(@NonNull final Supplier<A> supplier)
```
Partially applies the operator by fixing the second argument `a2` from a dynamic `Supplier<A>`, returning a binary [`Operator2<A>`](/reference/base/functions/operator2/) over operands `(a1, a3)`.
* **Parameters:** `supplier` – The supplier providing `a2`; must not be `null`.
* **Returns:** An `Operator2<A>` accepting operands `(a1, a3)`.
* **Throws:** `NullPointerException` if `supplier` is `null`, or if the supplier evaluates to `null`.

### `partial3`
```java
@Override
@NonNull default Operator2<A> partial3(@NonNull final Supplier<A> supplier)
```
Partially applies the operator by fixing the third argument `a3` from a dynamic `Supplier<A>`, returning a binary [`Operator2<A>`](/reference/base/functions/operator2/) over operands `(a1, a2)`.
* **Parameters:** `supplier` – The supplier providing `a3`; must not be `null`.
* **Returns:** An `Operator2<A>` accepting operands `(a1, a2)`.
* **Throws:** `NullPointerException` if `supplier` is `null`, or if the supplier evaluates to `null`.

---

## Key Contracts & Design

### 1. Arity Reduction Hierarchy
Partial application on `Operator3<A>` cleanly scales down the operator arity while preserving homogeneous typing:
* `Operator3<A>.partialN(...)` $\longrightarrow$ [`Operator2<A>`](/reference/base/functions/operator2/)
* `Operator2<A>.partialN(...)` $\longrightarrow$ [`Operator<A>`](/reference/base/functions/operator/)

### 2. Strict Null-Safety
Combinators (`andThen`, `partial1`, `partial2`, `partial3`) validate arguments and return non-null checks at boundary invocations.

---

## Type Hierarchy

```
   Fun3<A, A, A, A>
          ▲
          │
     Operator3<A>
```

---

## See Also

* [Guide: Operator3 in Practice](/guides/base/functions/operator3/) – Practical recipes, 3-argument geometric/color blends, and clamping.
* [`Operator<A>` Reference](/reference/base/functions/operator/) – Unary endomorphic operator ($A \to A$).
* [`Operator2<A>` Reference](/reference/base/functions/operator2/) – Binary endomorphic operator ($A \times A \to A$).
* [`Fun3<X1, X2, X3, Y>` Reference](/reference/base/functions/fun3/) – General ternary functional interface.
* [Full JavaDoc: `Operator3`](/api/base/foomp.base/org/quurz/foomp/base/functions/Operator3.html)
