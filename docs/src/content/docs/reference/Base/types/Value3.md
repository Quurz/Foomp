---
title: Value3<A1, A2, A3>
description: API reference for Value3, the ternary 3-component value carrier interface in Foomp.
---

The `Value3<A1, A2, A3>` interface defines the value-carrier contract for a container exposing exactly three components.

It extends [`Value2<A1, A2>`](/reference/base/types/value2/) and adds presence checking and retrieval for the third component `A3`.

---

## Type Signature

```java
package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.NoSuchElementException;

public interface Value3<A1, A2, A3> extends Value2<A1, A2>
```

### Type Parameters
* `A1`: The type of the first component.
* `A2`: The type of the second component.
* `A3`: The type of the third component.

---

## Method Summary

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `boolean` | `isPresent1()` | Returns whether the first component is present and retrievable. |
| `boolean` | `isPresent2()` | Returns whether the second component is present and retrievable. |
| `boolean` | `isPresent3()` | Returns whether the third component is present and retrievable. |
| `@NonNull A1` | `get1()` | Returns the first component; throws `NoSuchElementException` if absent. |
| `@NonNull A2` | `get2()` | Returns the second component; throws `NoSuchElementException` if absent. |
| `@NonNull A3` | `get3()` | Returns the third component; throws `NoSuchElementException` if absent. |

---

## Core Characteristics

### 1. Hierarchical Inheritance
`Value3` extends `Value2<A1, A2>`, which in turn extends `Value<A1>`. This allows a `Value3` instance to be used anywhere a `Value2`, `Value`, or `Supplier<A1>` is expected.

### 2. Strict `@NonNull` Accessor Guarantees
All three accessors (`get1()`, `get2()`, `get3()`) guarantee `@NonNull` return values when present.

### 3. Presence Contracts
For total product structures (such as `Tuple3` or `Record3`), `isPresent1()`, `isPresent2()`, and `isPresent3()` unconditionally return `true`. Calling any getter when the component is absent throws `NoSuchElementException`.

---

## Implementations in Foomp

* [`Tuple3<A1, A2, A3>`](/reference/base/util/tuple3/) – Immutable 3-element product type.
* [`Record3<A1, A2, A3>`](/reference/base/util/record3/) – Lightweight nominal/positional 3-tuple container.
* [`Value4<A1, A2, A3, A4>`](/reference/base/types/value4/) – 4-component value carrier extending `Value3`.

---

## See Also

* [`Value<A>`](/reference/base/types/value/) – Single-value carrier.
* [`Value2<A1, A2>`](/reference/base/types/value2/) – Binary value carrier.
* [`Value4<A1, A2, A3, A4>`](/reference/base/types/value4/) – Quaternary value carrier.
* [`Mappable3<WT, A1, A2, A3>`](/reference/base/types/mappable3/) – Trifunctor interface.
