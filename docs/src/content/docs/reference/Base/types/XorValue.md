---
title: XorValue<L, R>
description: API reference for XorValue, the disjoint two-way value carrier interface in Foomp.
---

The `XorValue<L, R>` interface defines a disjoint two-way value container. It holds exactly one value: either on the *left* of type `L` or on the *right* of type `R`.

It extends [`Value<R>`](/reference/base/types/value/), treating the presence of the *right* value as the definition of presence for the container.

---

## Type Signature

```java
package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.NoSuchElementException;

public interface XorValue<L, R> extends Value<R>
```

### Type Parameters
* `L`: The type of the left value.
* `R`: The type of the right value.

---

## Static Factory Methods

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <L, R> XorValue<L, R>` | `left(@NonNull L left)` | Creates a new `XorValue` containing a left value. Throws `NullPointerException` if `left` is `null`. |
| `static <L, R> XorValue<L, R>` | `right(@NonNull R right)` | Creates a new `XorValue` containing a right value. Throws `NullPointerException` if `right` is `null`. |

---

## Method Summary

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `default boolean` | `isLeft()` | Returns `true` if a left value is present (`!isRight()`). |
| `boolean` | `isRight()` | Returns `true` if a right value is present. |
| `default boolean` | `isPresent()` | Overrides `Value.isPresent()`; mirrors `isRight()`. |
| `@NonNull L` | `getLeft()` | Returns the left value; throws `NoSuchElementException` if absent. |
| `@NonNull R` | `getRight()` | Returns the right value; throws `NoSuchElementException` if absent. |
| `default @NonNull R` | `get()` | Overrides `Value.get()`; delegates to `getRight()`. |

---

## Core Characteristics

### 1. Disjoint Sum Semantics
`XorValue` represents an exclusive logical OR ($L \oplus R$). If a container holds `Left`, then `isLeft() == true` and `isRight() == false`. If it holds `Right`, then `isRight() == true` and `isLeft() == false`.

### 2. Right-Biased `Value<R>` Alignment
Because it extends `Value<R>`, `XorValue` treats the `Right` side as the primary carrier. Calling `isPresent()` returns `isRight()`, and calling `get()` returns the right value or throws `NoSuchElementException`.

### 3. Usage in Checked Exception Adaptation
`XorValue<Exception, Y>` is the core bridge type used by `Applicable.safe()` and `UnsafeProvider.safe()` to adapt throwing computations ($() \to Y \text{ throws Exception}$) into total functional containers without throwing at the call site.

---

## See Also

* [`Value<A>`](/reference/base/types/value/) – Base single-value carrier interface.
* [`Applicable<X, Y>`](/reference/base/functions/applicable/) – Throwing function interface with `.safe()`.
* [`UnsafeProvider<A>`](/reference/base/functions/unsafeprovider/) – Throwing supplier interface with `.safe()`.
* [`Triable<A>`](/reference/base/types/triable/) – Computation carrier returning `XorValue<Exception, A>`.
