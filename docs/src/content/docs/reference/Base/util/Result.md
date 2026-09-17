---
title: Result<A>
description: API reference for Result, an explicit success or exception outcome container in Foomp implementing XorValue and Transmogrifyable.
---

`Result<A>` represents the outcome of an operation that either succeeded with a value of type `A` (`Success`), or failed with an `Exception` (`Failure`).

It implements `XorValue<Exception, A>` and `Transmogrifyable<Result<A>>`, where the presence of a right value signifies success and the presence of a left value represents an exception.

---

## Type Signature

```java
package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Transmogrifyable;
import org.quurz.foomp.base.types.XorValue;

public sealed interface Result<A>
        extends Transmogrifyable<Result<A>>,
                XorValue<Exception, A>
        permits Result.Success,
                Result.Failure
```

### Type Parameters
* `A`: The type of the successful outcome value.

---

## Static Factory Methods

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A> Result<A>` | `success(@NonNull A value)` | Creates a successful `Result` holding the specified value. Throws `NullPointerException` if `value` is `null`. |
| `static <A> Result<A>` | `failure(@NonNull Exception exception)` | Creates a failed `Result` holding the specified exception. Throws `NullPointerException` if `exception` is `null`. |

---

## Method Summary

### Status & Presence Queries

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `boolean` | `isSuccess()` | Returns `true` if this `Result` holds a success value (alias for `isRight()`). |
| `boolean` | `isRight()` | Returns `true` if this `Result` is a `Success` (from `XorValue`). |
| `boolean` | `isPresent()` | Returns `true` if a success value is present (from `Value` / `XorValue`). |
| `boolean` | `isFailure()` | Returns `true` if this `Result` holds an exception (alias for `isLeft()`). |
| `boolean` | `isLeft()` | Returns `true` if this `Result` is a `Failure` (from `XorValue`). |

### Value & Exception Extraction

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull A` | `getValue()` | Returns the success value or throws `NoSuchElementException` with the underlying exception as cause if this is a failure (alias for `getRight()`). |
| `@NonNull A` | `getRight()` | Returns the success value or throws `NoSuchElementException` (from `XorValue`). |
| `@NonNull A` | `get()` | Returns the success value or throws `NoSuchElementException` (from `Value`). |
| `@NonNull A` | `getOrDefault(@NonNull Supplier<A> defaultValue)` | Returns the contained value if successful, or evaluates and returns the value supplied by `defaultValue` on failure. |
| `@NonNull Exception` | `getException()` | Returns the contained exception or throws `NoSuchElementException` if successful (alias for `getLeft()`). |
| `@NonNull Exception` | `getLeft()` | Returns the contained exception or throws `NoSuchElementException` if successful (from `XorValue`). |

### Conversions & Interop

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `Either<Exception, A>` | `toEither()` | Converts this `Result` to a lazy `Either<Exception, A>` (`Right` on success, `Left` on failure). |
| `Maybe<A>` | `toMaybe()` | Converts this `Result` to a `Maybe<A>` (`some(value)` on success, `none()` on failure). |

### Transmogrification

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <T> T` | `transmogrify(@NonNull Function<? super Result<A>, ? extends T> transmogrifier)` | Passes this result into a custom transformation function. |

---

## Inner Classes

### `Result.Success<R>`
Represents a successful outcome holding a non-null value of type `R`.

### `Result.Failure<R>`
Represents a failed outcome holding a non-null `Exception`.

---

## See Also

* [`Either<L, R>`](/reference/base/util/either/) – Disjoint union for arbitrary left and right value types.
* [`Maybe<A>`](/reference/base/util/maybe/) – Optional value container representing presence or absence.
* [`XorValue<L, R>`](/reference/base/types/xorvalue/) – Base interface for disjoint two-way value containers.
* [`Transmogrifyable<C>`](/reference/base/types/transmogrifyable/) – Transformation interface for fluent pipeline chaining.
