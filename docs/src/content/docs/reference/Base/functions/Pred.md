---
title: Pred<A>
description: Formal API reference and specifications for the Pred<A> functional predicate interface in Foomp.
---

`org.quurz.foomp.base.functions.Pred<A>`

`Pred<A>` is an enhanced boolean predicate interface describing conditions over values of type `A` ($f: A \to \{\text{true}, \text{false}\}$). It extends Java's standard [`java.util.function.Predicate<A>`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/Predicate.html) with complete propositional logic operators (AND, OR, XOR, NAND, NOR, NOT), multi-predicate varargs aggregations, lazy supplier combinators, and seamless memoization via [`MemoisingPred<A>`](/reference/base/functions/memoisingpred/).

```java
@FunctionalInterface
public interface Pred<A>
    extends Predicate<A>
```

---

## Type Parameters

* `<A>` – The input type evaluated by this predicate.

---

## Static Factory Methods

### `alwaysTrue`
```java
static <A> Pred<A> alwaysTrue()
```
Returns a predicate that always yields `true`, while still enforcing strict non-null checks on its input argument.
* **Returns:** A `Pred<A>` returning `true` for any non-null input.
* **Throws:** `NullPointerException` if a `null` argument is passed to `test`.

### `alwaysFalse`
```java
static <A> Pred<A> alwaysFalse()
```
Returns a predicate that always yields `false`, while still enforcing strict non-null checks on its input argument.
* **Returns:** A `Pred<A>` returning `false` for any non-null input.
* **Throws:** `NullPointerException` if a `null` argument is passed to `test`.

### `pred`
```java
static <A> Pred<A> pred(@NonNull final Predicate<A> predicate)
```
Wraps any standard Java `Predicate<A>` into a `Pred<A>`.
* **Parameters:** `predicate` – The predicate to adapt; must not be `null`.
* **Returns:** A `Pred<A>` delegating to `predicate`.
* **Throws:** `NullPointerException` if `predicate` is `null`.

### `not`
```java
static <A> Pred<A> not(@NonNull final Predicate<A> predicate)
```
Returns the logical negation ($\neg P$) of the given predicate.
* **Parameters:** `predicate` – The predicate to negate; must not be `null`.
* **Returns:** A new `Pred<A>` returning `!predicate.test(a)`.
* **Throws:** `NullPointerException` if `predicate` is `null`.

### `and` (Varargs Conjunction)
```java
@SafeVarargs
static <A> Pred<A> and(
    @NonNull final Pred<A> first,
    @NonNull final Pred<A> second,
    @NonNull final Pred<A>... others
)
```
Returns the logical conjunction ($P_1 \land P_2 \land \dots \land P_n$) of two or more predicates. Evaluation short-circuits from left to right.
* **Throws:** `NullPointerException` if any predicate parameter or array element is `null`.

### `or` (Varargs Disjunction)
```java
@SafeVarargs
static <A> Pred<A> or(
    @NonNull final Pred<A> first,
    @NonNull final Pred<A> second,
    @NonNull final Pred<A>... others
)
```
Returns the logical disjunction ($P_1 \lor P_2 \lor \dots \lor P_n$) of two or more predicates. Evaluation short-circuits from left to right.
* **Throws:** `NullPointerException` if any predicate parameter or array element is `null`.

### `xor` (Varargs Exclusive Disjunction)
```java
@SafeVarargs
static <A> Pred<A> xor(
    @NonNull final Pred<A> first,
    @NonNull final Pred<A> second,
    @NonNull final Pred<A>... others
)
```
Returns the exclusive OR across all supplied predicates by computing a left-to-right fold ($(((P_1 \oplus P_2) \oplus P_3) \dots)$).
* **Throws:** `NullPointerException` if any predicate parameter or array element is `null`.

---

## Abstract Methods

### `test`
```java
@Pure
boolean test(@NonNull final A a)
```
Evaluates the predicate condition for the given input argument `a`.
* **Parameters:** `a` – The input argument; must not be `null`.
* **Returns:** `true` if the input matches the condition, otherwise `false`.

---

## Default Methods & Logical Combinators

| Operator | Method Signature | Logic / Description |
| :--- | :--- | :--- |
| **NOT** | `default Pred<A> negate()` | Returns $\neg \text{this}$. |
| **AND** | `default Pred<A> and(Pred<A> other)`<br/>`default Pred<A> and(BooleanSupplier s)`<br/>`default Pred<A> and(Supplier<Boolean> s)` | $P \land Q$. Short-circuits if `this` is `false`. |
| **OR** | `default Pred<A> or(Pred<A> other)`<br/>`default Pred<A> or(BooleanSupplier s)`<br/>`default Pred<A> or(Supplier<Boolean> s)` | $P \lor Q$. Short-circuits if `this` is `true`. |
| **NAND** | `default Pred<A> nand(Pred<A> other)`<br/>`default Pred<A> nand(BooleanSupplier s)`<br/>`default Pred<A> nand(Supplier<Boolean> s)` | $\neg (P \land Q)$. Negated conjunction. |
| **NOR** | `default Pred<A> nor(Pred<A> other)`<br/>`default Pred<A> nor(BooleanSupplier s)`<br/>`default Pred<A> nor(Supplier<Boolean> s)` | $\neg (P \lor Q)$. Negated disjunction. |
| **XOR** | `default Pred<A> xor(Pred<A> other)`<br/>`default Pred<A> xor(BooleanSupplier s)`<br/>`default Pred<A> xor(Supplier<Boolean> s)` | $(P \lor Q) \land \neg(P \land Q)$. Exclusive OR. |
| **MEMOISE** | `default MemoisingPred<A> memoise()` | Returns a thread-safe caching predicate wrapper. |

:::note[Short-Circuit Semantics]
All binary logical combinators (`and`, `or`, `nand`, `nor`, `xor`) evaluating `BooleanSupplier` or `Supplier<Boolean>` preserve standard boolean short-circuiting rules: lazy suppliers are only called when necessary.
:::

---

## Key Contracts & Design

### 1. Complete Propositional Logic
Standard Java `Predicate` only provides `and`, `or`, and `negate`. `Pred<A>` completes the boolean algebra by including `nand`, `nor`, and `xor`, enabling clear, declarative rule definitions without nested inverted clauses.

### 2. Strict Null Defenses
Every static factory method and combinator validates non-null inputs. If a `Supplier<Boolean>` yields `null`, `Pred` detects the contract violation immediately and throws a meaningful `NullPointerException`.

### 3. Memoization
Calling `.memoise()` converts the predicate into a [`MemoisingPred<A>`](/reference/base/functions/memoisingpred/), caching results in a `ConcurrentHashMap` for repeated evaluations of expensive pure predicates.

---

## Type Hierarchy

```
   java.util.function.Predicate<A>
                ▲
                │
             Pred<A>
                ▲
                │
        MemoisingPred<A>
```

---

## See Also

* [Guide: Predicates in Practice](/guides/base/functions/pred/) – Practical recipes, gate composition, and filtering pipelines.
* [`Pred2<A1, A2>` Reference](/reference/base/functions/pred2/) – Binary predicate interface.
* [`Pred3<A1, A2, A3>` Reference](/reference/base/functions/pred3/) – Ternary predicate interface.
* [`MemoisingPred<A>` Reference](/reference/base/functions/memoisingpred/) – Caching predicate wrapper.
* [Full JavaDoc: `Pred`](/api/base/foomp.base/org/quurz/foomp/base/functions/Pred.html)
