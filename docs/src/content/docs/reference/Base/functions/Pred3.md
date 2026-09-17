---
title: Pred3<A1, A2, A3>
description: Formal API reference and specifications for the Pred3<A1, A2, A3> ternary predicate interface in Foomp.
---

`org.quurz.foomp.base.functions.Pred3<A1, A2, A3>`

`Pred3<A1, A2, A3>` represents a ternary boolean condition evaluating three arguments ($f: A1 \times A2 \times A3 \to \{\text{true}, \text{false}\}$). Because standard Java provides no ternary predicate interface, `Pred3` fills this gap with complete propositional logic (AND, OR, XOR, NAND, NOR, NOT), short-circuiting supplier combinators, and progressive partial application reducing down to [`Pred2<A, B>`](/reference/base/functions/pred2/) and [`Pred<A>`](/reference/base/functions/pred/).

```java
@FunctionalInterface
public interface Pred3<A1, A2, A3>
```

---

## Type Parameters

* `<A1>` – The type of the first argument.
* `<A2>` – The type of the second argument.
* `<A3>` – The type of the third argument.

---

## Static Factory Methods

### `not`
```java
static <A1, A2, A3> Pred3<A1, A2, A3> not(
    @NonNull final Pred3<? super A1, ? super A2, ? super A3> pred3
)
```
Returns the logical negation ($\neg P$) of the given ternary predicate.
* **Parameters:** `pred3` – The ternary predicate to negate; must not be `null`.
* **Returns:** A new `Pred3<A1, A2, A3>` evaluating `!pred3.test(a1, a2, a3)`.
* **Throws:** `NullPointerException` if `pred3` is `null`.

---

## Abstract Methods

### `test`
```java
@Pure
boolean test(
    @NonNull final A1 a1,
    @NonNull final A2 a2,
    @NonNull final A3 a3
)
```
Evaluates the ternary condition for the given inputs `a1`, `a2`, and `a3`.
* **Parameters:**
  * `a1` – The first input; must not be `null`.
  * `a2` – The second input; must not be `null`.
  * `a3` – The third input; must not be `null`.
* **Returns:** `true` if all inputs satisfy the condition, otherwise `false`.

---

## Default Methods & Logical Combinators

| Operator | Method Signature | Logic / Description |
| :--- | :--- | :--- |
| **NOT** | `default Pred3<A1, A2, A3> negate()` | Returns $\neg \text{this}$. |
| **AND** | `default Pred3<A1, A2, A3> and(Pred3<A1, A2, A3> other)`<br/>`default Pred3<A1, A2, A3> and(BooleanSupplier s)`<br/>`default Pred3<A1, A2, A3> and(Supplier<Boolean> s)` | $P \land Q$. Short-circuits if `this` is `false`. |
| **OR** | `default Pred3<A1, A2, A3> or(Pred3<A1, A2, A3> other)`<br/>`default Pred3<A1, A2, A3> or(BooleanSupplier s)`<br/>`default Pred3<A1, A2, A3> or(Supplier<Boolean> s)` | $P \lor Q$. Short-circuits if `this` is `true`. |
| **NAND** | `default Pred3<A1, A2, A3> nand(Pred3<A1, A2, A3> other)`<br/>`default Pred3<A1, A2, A3> nand(BooleanSupplier s)`<br/>`default Pred3<A1, A2, A3> nand(Supplier<Boolean> s)` | $\neg (P \land Q)$. Negated conjunction. |
| **NOR** | `default Pred3<A1, A2, A3> nor(Pred3<A1, A2, A3> other)`<br/>`default Pred3<A1, A2, A3> nor(BooleanSupplier s)`<br/>`default Pred3<A1, A2, A3> nor(Supplier<Boolean> s)` | $\neg (P \lor Q)$. Negated disjunction. |
| **XOR** | `default Pred3<A1, A2, A3> xor(Pred3<A1, A2, A3> other)`<br/>`default Pred3<A1, A2, A3> xor(BooleanSupplier s)`<br/>`default Pred3<A1, A2, A3> xor(Supplier<Boolean> s)` | $(P \lor Q) \land \neg(P \land Q)$. Exclusive OR. |

---

## Partial Evaluation (Arity Reduction)

### `partial1`
```java
@NonNull
default Pred2<A2, A3> partial1(@NonNull final Supplier<A1> supplier)
```
Partially evaluates this predicate by binding the first argument `a1` via a dynamic supplier, returning a binary [`Pred2<A2, A3>`](/reference/base/functions/pred2/).
* **Parameters:** `supplier` – The supplier providing `a1`; must not be `null`.
* **Returns:** A `Pred2<A2, A3>` evaluating `this.test(supplier.get(), a2, a3)`.
* **Throws:** `NullPointerException` if `supplier` is `null`.

### `partial2`
```java
@NonNull
default Pred2<A1, A3> partial2(@NonNull final Supplier<A2> supplier)
```
Partially evaluates this predicate by binding the second argument `a2` via a dynamic supplier, returning a binary [`Pred2<A1, A3>`](/reference/base/functions/pred2/).
* **Parameters:** `supplier` – The supplier providing `a2`; must not be `null`.
* **Returns:** A `Pred2<A1, A3>` evaluating `this.test(a1, supplier.get(), a3)`.
* **Throws:** `NullPointerException` if `supplier` is `null`.

### `partial3`
```java
@NonNull
default Pred2<A1, A2> partial3(@NonNull final Supplier<A3> supplier)
```
Partially evaluates this predicate by binding the third argument `a3` via a dynamic supplier, returning a binary [`Pred2<A1, A2>`](/reference/base/functions/pred2/).
* **Parameters:** `supplier` – The supplier providing `a3`; must not be `null`.
* **Returns:** A `Pred2<A1, A2>` evaluating `this.test(a1, a2, supplier.get())`.
* **Throws:** `NullPointerException` if `supplier` is `null`.

---

## Key Contracts & Design

### 1. Progressive Arity Reduction
With `partial1`, `partial2`, and `partial3`, a ternary predicate cleanly steps down into a binary [`Pred2`](/reference/base/functions/pred2/), which in turn can step down into a unary [`Pred`](/reference/base/functions/pred/).

### 2. Standardized Logical Combinators
All logical combinators (`and`, `or`, `xor`, `nand`, `nor`, `negate`) maintain identical semantics across `Pred`, `Pred2`, and `Pred3`.

---

## See Also

* [Guide: Pred3 in Practice](/guides/base/functions/pred3/) – Ternary rules, multi-attribute access validation, and partial evaluation.
* [`Pred2<A1, A2>` Reference](/reference/base/functions/pred2/) – Binary predicate interface.
* [`Pred<A>` Reference](/reference/base/functions/pred/) – Unary predicate interface.
* [`Fun3<X1, X2, X3, Y>` Reference](/reference/base/functions/fun3/) – Ternary functional interface.
* [Full JavaDoc: `Pred3`](/api/base/foomp.base/org/quurz/foomp/base/functions/Pred3.html)
