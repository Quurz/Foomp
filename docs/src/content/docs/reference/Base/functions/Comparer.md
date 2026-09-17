---
title: Comparer<A>
description: Reference documentation for the Comparer functional interface and Relation enum in Foomp Base.
---

`org.quurz.foomp.base.functions.Comparer<A>`

`Comparer<A>` is a functional interface defining comparison logic between two non-null values of the same type `A`. Instead of returning raw integer values like standard `java.util.Comparator<T>`, `Comparer` returns a type-safe [`Comparer.Relation`](#relation-enum) enum (`LESS`, `EQUAL`, `GREATER`).

By extending [`Fun2<A, A, Comparer.Relation>`](/reference/base/functions/fun2/), `Comparer` bridges object comparison with modern functional programming, enabling exhaustive `switch` expressions and functional combinators such as currying, partial application, and composition.

```java
@FunctionalInterface
public interface Comparer<A>
        extends Fun2<A, A, Comparer.Relation>
```

---

## Type Parameters

| Parameter | Description |
| :--- | :--- |
| `<A>` | The type of values being compared. |

---

## Relation Enum

`org.quurz.foomp.base.functions.Comparer.Relation`

An enumeration representing the trichotomy relationship between two compared values:

| Enum Constant | Description | Sign Semantic |
| :--- | :--- | :--- |
| `LESS` | The first value is strictly less than the second value. | `< 0` |
| `EQUAL` | The first value is equivalent to the second value. | `== 0` |
| `GREATER` | The first value is strictly greater than the second value. | `> 0` |

---

## Method Summary

### Core Operations

#### `compare`
```java
@Pure
@NonNull
Relation compare(@NonNull A first, @NonNull A second)
```
Compares two values and returns a `Relation` describing their relative order.
* **Throws:** `NullPointerException` if `first` or `second` is `null`.
* **Contract:** Must be referentially transparent and consistent with standard total ordering laws (transitivity, antisymmetry).

#### `apply` (Inherited from `Fun2`)
```java
@Override
@Pure
@NonNull
default Relation apply(@NonNull A first, @NonNull A second)
```
Enforces non-null validation on both arguments and delegates directly to [`compare(first, second)`](#compare).

---

### Static Factory Methods

#### `comparer`
```java
static <A> Comparer<A> comparer(@NonNull Comparator<A> comparator)
```
Adapts a standard Java `java.util.Comparator<A>` into a `Comparer<A>` using signum semantics (`Integer.signum`):
* Returns `Relation.LESS` if `comparator.compare(first, second) < 0`.
* Returns `Relation.GREATER` if `comparator.compare(first, second) > 0`.
* Returns `Relation.EQUAL` if `comparator.compare(first, second) == 0`.
* **Throws:** `NullPointerException` if `comparator` is `null`.

---

## Contract & Semantics

1. **Null Hygiene:**  
   `Comparer` strictly rejects `null` inputs. Passing `null` as either `first` or `second` immediately results in a `NullPointerException`.

2. **Exhaustive Pattern Matching:**  
   Returning an explicit enum (`Relation`) rather than an `int` allows Java 17+ `switch` expressions to be completely exhaustive without requiring fallback `default` branches or error-prone range comparisons (`< 0`, `> 0`).

3. **First-Class Function Capabilities:**  
   Because `Comparer<A>` extends `Fun2<A, A, Relation>`, it inherits all higher-order function utilities:
   * **Currying:** `comparer.curry()` creates a `Fun<A, Fun<A, Relation>>`.
   * **Partial Application:** `comparer.partial1(supplier)` or `comparer.partial2(supplier)` yields a unary `Fun<A, Relation>`.
   * **Tuple Application:** Can be applied directly to a `Pair<A, A>`.

---

## Hierarchy & Related Types

```
               ┌─────────────────────────────────────┐
               │  Fun2<A, A, Comparer.Relation>      │
               └──────────────────┬──────────────────┘
                                  │
                                  ▼
               ┌─────────────────────────────────────┐
               │            Comparer<A>              │
               │  (returns Comparer.Relation enum)   │
               └─────────────────────────────────────┘
```

* **`java.util.Comparator<T>`:** Standard Java comparison interface returning `int`. Can be converted to `Comparer<A>` via `Comparer.comparer(...)`.
* **`AVLTree<A>` / `RedBlackTree<A>`:** Persistent self-balancing search trees in `foomp.base.util` that use `Comparer<A>` internally to drive tree navigation and balancing decisions.

---

## See Also

* [Guide: Working with Comparer](/guides/base/functions/comparer/) – Practical recipes and `switch` pattern matching examples.
* [Reference: `Fun2<A, B, C>`](/reference/base/functions/fun2/) – Binary functional interface in Foomp.
* [JavaDoc: `org.quurz.foomp.base.functions.Comparer`](/api/base/foomp.base/org/quurz/foomp/base/functions/Comparer.html)
