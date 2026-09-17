---
title: Value4<A1, A2, A3, A4>
description: API reference for Value4, the quaternary 4-component value carrier interface in Foomp.
---

The `Value4<A1, A2, A3, A4>` interface defines the value-carrier contract for a container exposing exactly four components.

It extends [`Value3<A1, A2, A3>`](/reference/base/types/value3/) and adds presence checking and retrieval for the fourth component `A4`.

---

## Type Signature

```java
package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.NoSuchElementException;

public interface Value4<A1, A2, A3, A4> extends Value3<A1, A2, A3>
```

### Type Parameters
* `A1`: The type of the first component.
* `A2`: The type of the second component.
* `A3`: The type of the third component.
* `A4`: The type of the fourth component.

---

## Method Summary

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `boolean` | `isPresent1()` | Returns whether the first component is present and retrievable. |
| `boolean` | `isPresent2()` | Returns whether the second component is present and retrievable. |
| `boolean` | `isPresent3()` | Returns whether the third component is present and retrievable. |
| `boolean` | `isPresent4()` | Returns whether the fourth component is present and retrievable. |
| `@NonNull A1` | `get1()` | Returns the first component; throws `NoSuchElementException` if absent. |
| `@NonNull A2` | `get2()` | Returns the second component; throws `NoSuchElementException` if absent. |
| `@NonNull A3` | `get3()` | Returns the third component; throws `NoSuchElementException` if absent. |
| `@NonNull A4` | `get4()` | Returns the fourth component; throws `NoSuchElementException` if absent. |

---

## Core Characteristics

### 1. Hierarchical Inheritance
`Value4` extends `Value3<A1, A2, A3>`, which transitively extends `Value2<A1, A2>` and `Value<A1>`. A `Value4` instance can be treated as a 3-tuple, 2-tuple, or single-value `Supplier<A1>`.

### 2. Strict `@NonNull` Accessor Guarantees
All four accessors (`get1()`, `get2()`, `get3()`, `get4()`) guarantee `@NonNull` return values when present.

### 3. Total Product Structures
For total product types like `Tuple4` or `Record4`, all four `isPresentX()` methods return `true`.

---

## Implementations in Foomp

* [`Tuple4<A1, A2, A3, A4>`](/reference/api-base-util-overview/) – Immutable 4-element product type.
* [`Record4<A1, A2, A3, A4>`](/reference/api-base-util-overview/) – Lightweight nominal/positional 4-tuple container.

---

## See Also

* [`Value<A>`](/reference/base/types/value/) – Base single-value carrier interface.
* [`Value2<A1, A2>`](/reference/base/types/value2/) – Binary value carrier interface.
* [`Value3<A1, A2, A3>`](/reference/base/types/value3/) – Ternary value carrier interface.
* [`Mappable4<WT, A1, A2, A3, A4>`](/reference/base/types/mappable4/) – Quadrifunctor interface.
