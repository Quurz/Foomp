---
title: UnsafeMonadic<WT, A>
description: API reference for UnsafeMonadic, the monadic interface for transformations throwing checked exceptions.
---

The `UnsafeMonadic<WT, A>` interface represents a monad-like abstraction whose mapping, application, and binding operations are permitted to throw checked exceptions (`throws Exception`).

It complements the standard `Monadic<WT, A>` interface by enabling railway-oriented, exception-aware transformations at compile time using `Applicable` functions.

---

## Type Signature

```java
package org.quurz.foomp.base.types;

public interface UnsafeMonadic<WT extends WitnessType, A>
```

### Type Parameters
* `WT`: The witness type of the higher-kinded monadic container (e.g., `Attempt.µ`).
* `A`: The contained value type.

---

## Method Summary

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `<B> Higher1<? extends WT, B>` | `mapUnsafe(Applicable<? super A, ? extends B> transformation)` | Maps the contained value using a transformation that may throw checked exceptions. |
| `<B> Higher1<? extends WT, B>` | `applyToUnsafe(Higher1<? extends WT, ? extends Applicable<? super A, ? extends B>> transformation)` | Applies a monadic throwing function to this monadic container. |
| `<B> Higher1<? extends WT, B>` | `flatMapUnsafe(Applicable<? super A, ? extends Higher1<? extends WT, B>> transformation)` | Monadically binds a function returning a new monadic container that may throw checked exceptions. |

---

## Core Characteristics

### 1. Checked Exception Propagation
Unlike `Monadic<WT, A>`, where functions are purely functional (`Fun<A, B>`) and must handle exceptions internally or encapsulate them into values, `UnsafeMonadic` methods explicitly declare `throws Exception`.

### 2. Higher-Kinded Witness Integration
All return types are wrapped in `Higher1<? extends WT, B>`, allowing monadic computations to integrate seamlessly with Foomp's higher-kinded type system.

### 3. Null-Safety Contracts
All parameter inputs and returned monadic containers are strictly annotated with `@NonNull`. Supplying `null` transformations or containers causes an immediate runtime check failure (such as `NullPointerException`).

---

## Implementations in Foomp

* [`Attempt<A>`](/reference/base/util/attempt/) – Deferred execution monad encapsulating computations that may fail.

---

## See Also

* [`Monadic<WT, A>`](/reference/base/types/monadic/) – Safe monad interface without checked exceptions.
* [`Applicable<X, Y>`](/reference/base/functions/applicable/) – Core function interface permitting checked exceptions.
* [`Triable<A>`](/reference/base/types/triable/) – Lazy computation converting exceptions into `XorValue`.
