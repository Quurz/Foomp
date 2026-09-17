---
title: Pred2<A1, A2>
description: Formal API reference and specifications for the Pred2<A1, A2> binary predicate interface in Foomp.
---

`org.quurz.foomp.base.functions.Pred2<A1, A2>`

`Pred2<A1, A2>` represents a binary boolean condition mapping two arguments of types `A1` and `A2` to a boolean result ($f: A1 \times A2 \to \{\text{true}, \text{false}\}$). It extends Java's standard [`java.util.function.BiPredicate<A1, A2>`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/BiPredicate.html) with propositional logic operators (AND, OR, XOR, NAND, NOR, NOT), dynamic supplier combinators, and arity-reducing partial application yielding unary [`Pred<A>`](/reference/base/functions/pred/).

```java
@FunctionalInterface
public interface Pred2<A1, A2>
    extends BiPredicate<A1, A2>
```

---

## Type Parameters

* `<A1>` – The type of the first argument.
* `<A2>` – The type of the second argument.

---

## Static Factory Methods

### `pred2`
```java
static <A1, A2> Pred2<A1, A2> pred2(@NonNull final BiPredicate<? super A1, ? super A2> biPredicate)
```
Wraps any standard Java `BiPredicate` into a `Pred2<A1, A2>`.
* **Parameters:** `biPredicate` – The binary predicate to wrap; must not be `null`.
* **Returns:** A `Pred2<A1, A2>` delegating to `biPredicate`.
* **Throws:** `NullPointerException` if `biPredicate` is `null`.

### `not`
```java
static <A1, A2> Pred2<A1, A2> not(@NonNull final Pred2<? super A1, ? super A2> pred2)
```
Returns the logical negation ($\neg P$) of the given binary predicate.
* **Parameters:** `pred2` – The binary predicate to negate; must not be `null`.
* **Returns:** A new `Pred2<A1, A2>` evaluating `!pred2.test(a1, a2)`.
* **Throws:** `NullPointerException` if `pred2` is `null`.

---

## Abstract Methods

### `test`
```java
@Pure
boolean test(
    @NonNull final A1 a1,
    @NonNull final A2 a2
)
```
Evaluates the binary condition for the given inputs `a1` and `a2`.
* **Parameters:**
  * `a1` – The first input; must not be `null`.
  * `a2` – The second input; must not be `null`.
* **Returns:** `true` if inputs satisfy the condition, otherwise `false`.

---

## Default Methods & Logical Combinators

| Operator | Method Signature | Logic / Description |
| :--- | :--- | :--- |
| **NOT** | `default Pred2<A1, A2> negate()` | Returns $\neg \text{this}$. |
| **AND** | `default Pred2<A1, A2> and(Pred2<A1, A2> other)`<br/>`default Pred2<A1, A2> and(BooleanSupplier s)`<br/>`default Pred2<A1, A2> and(Supplier<Boolean> s)` | $P \land Q$. Short-circuits if `this` is `false`. |
| **OR** | `default Pred2<A1, A2> or(Pred2<A1, A2> other)`<br/>`default Pred2<A1, A2> or(BooleanSupplier s)`<br/>`default Pred2<A1, A2> or(Supplier<Boolean> s)` | $P \lor Q$. Short-circuits if `this` is `true`. |
| **NAND** | `default Pred2<A1, A2> nand(Pred2<A1, A2> other)`<br/>`default Pred2<A1, A2> nand(BooleanSupplier s)`<br/>`default Pred2<A1, A2> nand(Supplier<Boolean> s)` | $\neg (P \land Q)$. Negated conjunction. |
| **NOR** | `default Pred2<A1, A2> nor(Pred2<A1, A2> other)`<br/>`default Pred2<A1, A2> nor(BooleanSupplier s)`<br/>`default Pred2<A1, A2> nor(Supplier<Boolean> s)` | $\neg (P \lor Q)$. Negated disjunction. |
| **XOR** | `default Pred2<A1, A2> xor(Pred2<A1, A2> other)`<br/>`default Pred2<A1, A2> xor(BooleanSupplier s)`<br/>`default Pred2<A1, A2> xor(Supplier<Boolean> s)` | $(P \lor Q) \land \neg(P \land Q)$. Exclusive OR. |

---

## Partial Evaluation (Arity Reduction)

### `partial1`
```java
@NonNull
default Pred<A2> partial1(@NonNull final Supplier<A1> supplier)
```
Partially evaluates this predicate by binding the first argument `a1` using a dynamic supplier, returning a unary [`Pred<A2>`](/reference/base/functions/pred/).
* **Parameters:** `supplier` – The supplier providing `a1`; must not be `null`.
* **Returns:** A `Pred<A2>` evaluating `this.test(supplier.get(), a2)`.
* **Throws:** `NullPointerException` if `supplier` is `null`.

### `partial2`
```java
@NonNull
default Pred<A1> partial2(@NonNull final Supplier<A2> supplier)
```
Partially evaluates this predicate by binding the second argument `a2` using a dynamic supplier, returning a unary [`Pred<A1>`](/reference/base/functions/pred/).
* **Parameters:** `supplier` – The supplier providing `a2`; must not be `null`.
* **Returns:** A `Pred<A1>` evaluating `this.test(a1, supplier.get())`.
* **Throws:** `NullPointerException` if `supplier` is `null`.

---

## Key Contracts & Design

### 1. Seamless Arity Reduction
Unlike standard Java where fixing an argument requires manual nested lambdas `(a2) -> biPredicate.test(val, a2)`, `Pred2` provides first-class `partial1` and `partial2` combinators that return full-featured [`Pred<A>`](/reference/base/functions/pred/) instances with full propositional logic.

### 2. Strict Non-Null Boundaries
Combinators evaluate arguments and supplier results against nulls immediately, failing fast with informative exception messages if invalid inputs are supplied.

---

## Type Hierarchy

```
   java.util.function.BiPredicate<A1, A2>
                     ▲
                     │
              Pred2<A1, A2>
```

---

## See Also

* [Guide: Pred2 in Practice](/guides/base/functions/pred2/) – Practical binary rules, partial evaluation, and cross-field validation.
* [`Pred<A>` Reference](/reference/base/functions/pred/) – Unary predicate interface.
* [`Pred3<A1, A2, A3>` Reference](/reference/base/functions/pred3/) – Ternary predicate interface.
* [`Fun2<X1, X2, Y>` Reference](/reference/base/functions/fun2/) – General binary functional interface.
* [Full JavaDoc: `Pred2`](/api/base/foomp.base/org/quurz/foomp/base/functions/Pred2.html)
