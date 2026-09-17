---
title: Constraint<A, FAILURE>
description: API reference for Constraint, a reusable validation component with failure mapping in Foomp.
---

`Constraint<A, FAILURE>` is a functional validation component that tests values of type `A` against a predicate and produces an optional failure descriptor of type `FAILURE` wrapped in a [`Maybe`](/reference/base/util/maybe/).

It implements `java.util.function.Function<A, Maybe<FAILURE>>`, making it directly compatible with standard functional composition and validation pipelines.

---

## Type Signature

```java
package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import java.util.function.Function;

public final class Constraint<A, FAILURE>
        implements Function<A, Maybe<FAILURE>>
```

### Type Parameters
* `A`: The type of values to be validated.
* `FAILURE`: The type of the failure diagnostic produced when the constraint is violated.

---

## Static Factory Methods

### Creating `Constraint` Instances

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A, FAILURE> Constraint<A, FAILURE>` | `constraint(@NonNull Predicate<? super A> check, @NonNull FAILURE failure)` | Creates a new constraint with a constant failure value returned upon violation. |
| `static <A, FAILURE> Constraint<A, FAILURE>` | `constraint(@NonNull Predicate<? super A> check, @NonNull Function<? super A, ? extends FAILURE> failureFunction)` | Creates a new constraint that computes the failure value dynamically using `failureFunction`. |

### Creating Standalone Functions

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A, FAILURE> Function<A, Maybe<FAILURE>>` | `constraintFunction(@NonNull Predicate<? super A> check, @NonNull FAILURE failure)` | Constructs a standalone functional validation lambda returning `Maybe.none()` on success or `Maybe.some(failure)` on violation. |
| `static <A, FAILURE> Function<A, Maybe<FAILURE>>` | `constraintFunction(@NonNull Predicate<? super A> check, @NonNull Function<? super A, ? extends FAILURE> failureFunction)` | Constructs a standalone functional validation lambda producing dynamic failures on violation. |

---

## Method Summary

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `boolean` | `isValid(@Nullable A value)` | Returns `true` if the value satisfies the constraint predicate. |
| `boolean` | `isInvalid(@Nullable A value)` | Returns `true` if the value violates the constraint predicate (`!isValid(value)`). |
| `@NonNull Maybe<FAILURE>` | `checkViolation(@Nullable A value)` | Validates the value and returns `Maybe.none()` if valid, or `Maybe.some(failure)` if violated. |
| `@NonNull Maybe<FAILURE>` | `apply(@Nullable A value)` | Implements `Function.apply`. Delegates directly to `checkViolation(value)`. |

---

## See Also

* [`Maybe<A>`](/reference/base/util/maybe/) – Optional container used to represent present validation errors (`Some`) or successful validation (`None`).
* [`Predicate`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/Predicate.html) – Condition tester interface.
* [`Result<A>`](/reference/base/util/result/) – Eager outcome type frequently combined with validation constraints.
