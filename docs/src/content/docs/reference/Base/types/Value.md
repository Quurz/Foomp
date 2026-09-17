---
title: Value<A>
description: API reference for Value, the fundamental single-value carrier contract in Foomp.
---

The `Value<A>` interface defines the minimal value-carrier contract for a single value of type `A`. It bridges functional containers with standard Java `Supplier<A>` while adding explicit presence querying capabilities.

---

## Type Signature

```java
package org.quurz.foomp.base.types;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

public interface Value<A> extends Supplier<A>
```

### Type Parameters
* `A`: The contained value type.

---

## Method Summary

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `boolean` | `isPresent()` | Returns whether a value is present and can be retrieved safely. |
| `A` | `get()` | Returns the contained value if present; may throw `NoSuchElementException` if absent. |

---

## Core Characteristics

### 1. `Supplier<A>` Interoperability
`Value<A>` extends `java.util.function.Supplier<A>`. Any instance of `Value` can be passed directly to standard Java APIs accepting suppliers (such as `Optional.orElseGet`, `CompletableFuture.supplyAsync`, etc.).

### 2. Presence Querying
Through `isPresent()`, callers can test if the container actually holds a retrievable value before calling `get()`, avoiding unhandled `NoSuchElementException` failures.

### 3. Implementation-Specific Nullability & Presence Semantics
* **Total Containers (Always Present):** Containers such as `Box<A>`, `Provider<A>`, and `Eval<A>` are unconditionally present (`isPresent() == true`).
* **Optional / Disjoint Containers (Conditionally Present):** Containers such as `Maybe<A>` (`Just` vs. `Nothing`) or `XorValue<L, R>` may represent absence or error states, returning `false` when no value of type `A` is present.

---

## Implementations in Foomp

* [`Maybe<A>`](/reference/base/util/maybe/) – Optional container (`Just<A>` implements present `Value<A>`, `Nothing<A>` implements empty `Value<A>`).
* [`Box<A>`](/reference/base/util/box/) – Eager or lazy single-value container.
* [`Provider<A>`](/reference/base/functions/provider/) – Functional value supplier.
* [`Eval<A>`](/reference/base/util/eval/) – Lazy evaluation container.
* [`Value2<A1, A2>`](/reference/base/types/value2/) – Binary value carrier extending `Value<A1>`.

---

## See Also

* [`Value2<A1, A2>`](/reference/base/types/value2/) – Two-component value carrier.
* [`Value3<A1, A2, A3>`](/reference/base/types/value3/) – Three-component value carrier.
* [`Value4<A1, A2, A3, A4>`](/reference/base/types/value4/) – Four-component value carrier.
* [`Provider<A>`](/reference/base/functions/provider/) – Monadic supplier interface.
