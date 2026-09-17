---
title: Operator2<A>
description: Formal API reference and specifications for the Operator2<A> binary endomorphism interface in Foomp.
---

`org.quurz.foomp.base.functions.Operator2<A>`

`Operator2<A>` represents a homogeneous binary operator mapping two arguments of type `A` to a result of the same type `A` ($f: A \times A \to A$). It combines JDK's [`java.util.function.BinaryOperator<A>`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/BinaryOperator.html) with Foomp's [`Fun2<A, A, A>`](/reference/base/functions/fun2/), providing partial application returning [`Operator<A>`](/reference/base/functions/operator/), argument flipping, and post-composition.

```java
@FunctionalInterface
public interface Operator2<A>
    extends BinaryOperator<A>,
            Fun2<A, A, A>
```

---

## Type Parameters

* `<A>` – The operand and return type of the binary operation.

---

## Static Factory Methods

### `operator2`
```java
static <A> Operator2<A> operator2(@NonNull final BinaryOperator<A> binaryOperator)
```
Wraps any standard Java `BinaryOperator<A>` into an `Operator2<A>`, enforcing runtime non-null checks on arguments and results.
* **Parameters:** `binaryOperator` – The binary operator to wrap; must not be `null`.
* **Returns:** A null-safe `Operator2<A>` instance.
* **Throws:** `NullPointerException` if `binaryOperator` is `null`, or if the resulting operator receives `null` or evaluates to `null`.

---

## Abstract Methods

### `apply`
```java
@Pure
@NonNull
A apply(@NonNull final A a1, @NonNull final A a2)
```
Applies the binary operator to the operands `a1` and `a2`.
* **Parameters:**
  * `a1` – The first operand; must not be `null`.
  * `a2` – The second operand; must not be `null`.
* **Returns:** The computed result of type `A`; never `null`.
* **Throws:** `NullPointerException` if `a1` or `a2` is `null` or if evaluation yields `null`.

---

## Default Methods & Combinators

### `andThen`
```java
default @NonNull Operator2<A> andThen(@NonNull final UnaryOperator<A> after)
```
Returns a composed binary operator that evaluates `this.apply(a1, a2)` and then applies `after` to the intermediate result.
* **Parameters:** `after` – The unary post-operator; must not be `null`.
* **Returns:** A composed `Operator2<A>`.
* **Throws:** `NullPointerException` if `after` is `null` or if any argument/intermediate result is `null`.

### `partial1`
```java
@Override
default @NonNull Operator<A> partial1(@NonNull final Supplier<A> supplier)
```
Partially applies the operator by fixing the first argument `a1` from a dynamic `Supplier<A>`, reducing the arity from a binary operator to a unary [`Operator<A>`](/reference/base/functions/operator/).
* **Parameters:** `supplier` – The supplier providing `a1`; must not be `null`.
* **Returns:** An `Operator<A>` accepting the remaining second argument `a2`.
* **Throws:** `NullPointerException` if `supplier` is `null`, or if the supplier evaluates to `null`.

### `partial2`
```java
@Override
default @NonNull Operator<A> partial2(@NonNull final Supplier<A> supplier)
```
Partially applies the operator by fixing the second argument `a2` from a dynamic `Supplier<A>`, returning an [`Operator<A>`](/reference/base/functions/operator/).
* **Parameters:** `supplier` – The supplier providing `a2`; must not be `null`.
* **Returns:** An `Operator<A>` accepting the remaining first argument `a1`.
* **Throws:** `NullPointerException` if `supplier` is `null`, or if the supplier evaluates to `null`.

### `flip`
```java
@Override
default @NonNull Operator2<A> flip()
```
Returns a new `Operator2<A>` with the argument order reversed: $(a_1, a_2) \mapsto f(a_2, a_1)$.
* **Returns:** An argument-reversed `Operator2<A>`.

---

## Key Contracts & Design

### 1. Homogeneous Closed Type Guarantee
Because all inputs and the output share type `A`, `Operator2<A>` models classical algebraic structures (such as Semigroups and Monoids when associative). Partial application directly reduces into `Operator<A>`, preserving endomorphic operations.

### 2. Dual Inheritance & Null Safety
Extending both `BinaryOperator<A>` and `Fun2<A, A, A>` means `Operator2<A>` integrates seamlessly into both JDK standard APIs (such as `Stream.reduce(...)`) and Foomp's functional ecosystem.

---

## Type Hierarchy

```
   java.util.function.BiFunction<A, A, A>
                     ▲
                     │
       ┌─────────────┴─────────────┐
       │       Fun2<A, A, A>       │     java.util.function.BinaryOperator<A>
       └─────────────┬─────────────┘                      ▲
                     │                                    │
                     └──────────────────┬─────────────────┘
                                        │
                                        ▼
                                  Operator2<A>
```

---

## See Also

* [Guide: Operator2 in Practice](/guides/base/functions/operator2/) – Practical recipes, algebraic structures, and reductions.
* [`Operator<A>` Reference](/reference/base/functions/operator/) – Unary endomorphic operator ($A \to A$).
* [`Operator3<A>` Reference](/reference/base/functions/operator3/) – Ternary endomorphic operator ($A \times A \times A \to A$).
* [`Fun2<X1, X2, Y>` Reference](/reference/base/functions/fun2/) – General binary functional interface.
* [Full JavaDoc: `Operator2`](/api/base/foomp.base/org/quurz/foomp/base/functions/Operator2.html)
