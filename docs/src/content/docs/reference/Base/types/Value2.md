---
title: Value2<A1, A2>
description: API reference for Value2, the binary pair-like value carrier interface in Foomp.
---

The `Value2<A1, A2>` interface defines the value-carrier contract for a pair of values exposing two components, commonly referred to as first (`A1`) and second (`A2`).

It extends [`Value<A1>`](/reference/base/types/value/), treating the first component as the primary value for `Supplier<A1>` operations.

---

## Type Signature

```java
package org.quurz.foomp.base.types;

import java.util.NoSuchElementException;

public interface Value2<A1, A2> extends Value<A1>
```

### Type Parameters
* `A1`: The type of the first component.
* `A2`: The type of the second component.

---

## Method Summary

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `default boolean` | `isPresent()` | Delegating alias to `isPresent1()`. |
| `boolean` | `isPresent1()` | Returns whether the first component is present and retrievable. |
| `boolean` | `isPresent2()` | Returns whether the second component is present and retrievable. |
| `default A1` | `get()` | Delegating alias to `get1()`. |
| `A1` | `get1()` | Returns the first component; throws `NoSuchElementException` if absent. |
| `A2` | `get2()` | Returns the second component; throws `NoSuchElementException` if absent. |

---

## Core Characteristics

### 1. Primary Component Defaulting
By default, `Value2` delegates the base `Value<A1>` methods (`isPresent()` and `get()`) to the first component (`isPresent1()` and `get1()`).

### 2. Independent Component Presence
Presence is queried individually per component via `isPresent1()` and `isPresent2()`.
* For product types like `Pair<A, B>` or `Tuple2<A1, A2>`, both components are typically present simultaneously.
* For sum types like `Either<L, R>` or `XorValue<L, R>`, exactly one of the components is present at any given time.

### 3. Strict Extraction Contracts
Calling `get1()` or `get2()` when the corresponding component is not present throws a `NoSuchElementException`.

---

## Implementations in Foomp

* [`Tuple2<A1, A2>`](/reference/base/util/tuple2/) – Immutable product type containing two values.
* [`Pair<A, B>`](/reference/base/util/pair/) – Classical 2-tuple pair structure.
* [`Either<L, R>`](/reference/base/util/either/) – Disjoint union type holding either `Left` or `Right`.
* [`XorValue<L, R>`](/reference/base/types/xorvalue/) – Throwing computation carrier.
* [`Value3<A1, A2, A3>`](/reference/base/types/value3/) – Ternary value carrier extending `Value2`.

---

## See Also

* [`Value<A>`](/reference/base/types/value/) – Base single-value carrier interface.
* [`Value3<A1, A2, A3>`](/reference/base/types/value3/) – 3-component value carrier.
* [`Value4<A1, A2, A3, A4>`](/reference/base/types/value4/) – 4-component value carrier.
* [`Swappable<SELF, A, B>`](/reference/base/types/swappable/) – Interface for swapping binary components.
