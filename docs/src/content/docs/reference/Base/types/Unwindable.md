---
title: Unwindable<SELF>
description: API reference for Unwindable, the contract for materializing deferred or lazy computations.
---

The `Unwindable<SELF>` interface defines a uniform contract for data structures and functional containers that can force (unwind) deferred or lazy computations into a materialized, stable representation of themselves.

---

## Type Signature

```java
package org.quurz.foomp.base.types;

@FunctionalInterface
public interface Unwindable<SELF extends Unwindable<?>>
```

### Type Parameters
* `SELF`: The implementing unwindable type (Curiously Recurring Template Pattern / Self-Type).

---

## Method Summary

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull SELF` | `unwind()` | Unwinds and materializes deferred or lazy computations, returning an evaluated representation. |

---

## Core Characteristics

### 1. Materialization Contract
Calling `unwind()` forces the evaluation of any lazy suppliers, deferred thunks, or pending closures held by the container. The return value is an evaluated, stable version of the container.

### 2. Idempotency Expectation
Implementations should strive for idempotency:
```text
x.unwind().unwind() ≡ x.unwind()
```
Repeated invocations on an already-unwound instance should be cheap and return an equivalent stable result.

### 3. Self-Typing (`SELF`)
Using the self-referential generic parameter `SELF extends Unwindable<?>`, calling `unwind()` returns the exact concrete container type without requiring type casting (e.g. `Box<A>.unwind()` returns `Box<A>`, `Eval<A>.unwind()` returns `Eval<A>`).

### 4. Non-Null Guarantee
`unwind()` is annotated with `@NonNull` and `@UnwindingOperation`. It never returns `null`.

---

## Implementations in Foomp

* [`Box<A>`](/reference/base/util/box/) – Lazily computed single-value container; `unwind()` forces the underlying supplier.
* [`Eval<A>`](/reference/base/util/eval/) – Lazy evaluation monad; `unwind()` resolves `Eval.Later` or `Eval.Always` into `Eval.Now`.
* [`Provider<A>`](/reference/base/functions/provider/) – Functional value supplier; `unwind()` evaluates and captures the current value into a constant provider.
* [`Tuple2<A1, A2>`](/reference/base/util/tuple2/), [`Tuple3`](/reference/base/util/tuple3/), [`Tuple4`](/reference/base/util/tuple4/) – Multi-value product types that recursively unwind lazy components.
* [`Maybe<A>`](/reference/base/util/maybe/), [`Either<L, R>`](/reference/base/util/either/), [`Attempt<A>`](/reference/base/util/attempt/) – Core containers supporting eager materialization.

---

## See Also

* [`@UnwindingOperation`](/reference/base/types/unwindingoperation/) – Architectural marker annotation for methods that trigger materialization.
* [`Provider<A>`](/reference/base/functions/provider/) – Lazy supplier implementing `Unwindable`.
