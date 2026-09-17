---
title: Operator<A>
description: Formal API reference and specifications for the Operator<A> endomorphism interface in Foomp.
---

`org.quurz.foomp.base.functions.Operator<A>`

`Operator<A>` represents a unary endomorphism mapping a set to itself ($f: A \to A$). It combines JDK's [`java.util.function.UnaryOperator<A>`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/UnaryOperator.html) with Foomp's [`Fun<A, A>`](/reference/base/functions/fun/) to guarantee closed type signatures, null-safety, and seamless functional composition.

```java
@FunctionalInterface
public interface Operator<A>
    extends UnaryOperator<A>,
            Fun<A, A>
```

---

## Type Parameters

* `<A>` – The operand and return type of the unary operation.

---

## Static Factory Methods

### `operator`
```java
static <A> Operator<A> operator(@NonNull final UnaryOperator<A> operator)
```
Wraps any standard Java `UnaryOperator<A>` into an `Operator<A>`, establishing strict null-safety boundaries for both input argument and computed result.
* **Parameters:** `operator` – The unary operator to adapt; must not be `null`.
* **Returns:** A null-safe `Operator<A>` instance.
* **Throws:** `NullPointerException` if `operator` is `null`, or if the resulting operator receives `null` or evaluates to `null`.

### `identity`
```java
static <A> Operator<A> identity()
```
Returns the canonical identity operator that yields its input argument unchanged ($f(a) = a$).
* **Returns:** An `Operator<A>` representing the identity endomorphism.
* **Throws:** `NullPointerException` if the supplied input argument is `null`.

---

## Abstract Methods

### `apply`
```java
@Pure
@NonNull
A apply(@NonNull final A a)
```
Applies the operator to the operand `a`.
* **Parameters:** `a` – The input argument; must not be `null`.
* **Returns:** The result of the unary operation; never `null`.
* **Throws:** `NullPointerException` if `a` is `null` or if custom evaluation evaluates to `null`.

---

## Default Methods & Combinators

### `compose`
```java
default Operator<A> compose(@NonNull final Operator<A> before)
```
Returns a composed operator that applies `before` first and then `this` ($this \circ before$).
* **Parameters:** `before` – The operator to evaluate first; must not be `null`.
* **Returns:** A new composed `Operator<A>` applying `this.apply(before.apply(a))`.
* **Throws:** `NullPointerException` if `before` is `null` or if any intermediate/final result is `null`.

### `andThen`
```java
default Operator<A> andThen(@NonNull final Operator<A> next)
```
Returns a composed operator that applies `this` first and then `next` ($next \circ this$).
* **Parameters:** `next` – The operator to evaluate afterwards; must not be `null`.
* **Returns:** A new composed `Operator<A>` applying `next.apply(this.apply(a))`.
* **Throws:** `NullPointerException` if `next` is `null` or if any intermediate/final result is `null`.

:::note[Type Preservation]
While `Fun.andThen(Function)` allows chaining into arbitrary return types `Fun<A, B>`, `Operator.andThen(Operator)` specifically requires another `Operator<A>` and retains the endomorphic type guarantee, returning an `Operator<A>`.
:::

---

## Key Contracts & Design

### 1. Endomorphism Contract
`Operator<A>` enforces that domain and codomain are identical ($A \to A$). This property allows infinite chaining, state transformation cycles, and fixed-point computations without type transitions.

### 2. Strict Null-Safety
Every operator in Foomp enforces strict non-null contracts:
* Arguments supplied to `apply` must not be `null`.
* Results returned from `apply` must not be `null`.
* The `Operator.operator(...)` factory automatically installs runtime non-null checks.

### 3. Dual Inheritance
Because `Operator<A>` inherits from both `UnaryOperator<A>` and `Fun<A, A>`:
* It can be passed directly to any JDK standard library method expecting `UnaryOperator<A>` or `Function<A, A>`.
* It inherits all functional capabilities from `Fun<A, A>`, such as `.memoise()` and `.async()`.

---

## Type Hierarchy

```
   java.util.function.Function<A, A>
                ▲
                │
   ┌────────────┴────────────┐
   │        Fun<A, A>        │        java.util.function.UnaryOperator<A>
   └────────────┬────────────┘                         ▲
                │                                      │
                └──────────────────┬───────────────────┘
                                   │
                                   ▼
                            Operator<A>
```

---

## See Also

* [Guide: Operator in Practice](/guides/base/functions/operator/) – Practical recipes and usage examples.
* [`Operator2<A>` Reference](/reference/base/functions/operator2/) – Binary endomorphic operators ($A \times A \to A$).
* [`Operator3<A>` Reference](/reference/base/functions/operator3/) – Ternary endomorphic operators ($A \times A \times A \to A$).
* [`Fun<X, Y>` Reference](/reference/base/functions/fun/) – General unary functional interface.
* [Full JavaDoc: `Operator`](/api/base/foomp.base/org/quurz/foomp/base/functions/Operator.html)
