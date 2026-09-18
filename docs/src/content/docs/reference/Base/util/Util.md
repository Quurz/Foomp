---
title: Util
description: API reference for Util, a static utility class in Foomp providing validation and defensive helpers.
---

`Util` is a final static utility class containing general-purpose helper methods focused on input validation, collection assertions, set relationships, reflection checks, and filesystem guards in a functional style.

---

## Type Signature

```java
package org.quurz.foomp.base.util;

public final class Util
```

`Util` contains only static methods and cannot be instantiated.

---

## Method Summary

### Numeric & Duration Validation

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <E extends Exception> int` | `requireNonNegativeInt(int value, @NonNull Supplier<E> exceptionSupplier)` | Ensures `value >= 0`; throws the exception supplied by `exceptionSupplier` otherwise. Returns `value`. |
| `static <E extends Exception> int` | `requirePositiveInt(int value, @NonNull Supplier<E> exceptionSupplier)` | Ensures `value > 0`; throws the exception supplied by `exceptionSupplier` otherwise. Returns `value`. |
| `static <E extends Exception> @NonNull Duration` | `requireNonNegativeDuration(@NonNull Duration duration, @NonNull Supplier<E> exceptionSupplier)` | Ensures `duration >= Duration.ZERO`; throws the exception supplied by `exceptionSupplier` otherwise. Returns `duration`. |
| `static <E extends Exception> @NonNull Duration` | `requirePositiveDuration(@NonNull Duration duration, @NonNull Supplier<E> exceptionSupplier)` | Ensures `duration > Duration.ZERO`; throws the exception supplied by `exceptionSupplier` otherwise. Returns `duration`. |

### Collection & Map Validation

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <C extends Collection<A>, A, E extends Exception> C` | `requireNonEmpty(@NonNull C collection, @NonNull Supplier<E> exceptionSupplier)` | Ensures `collection` is not empty; throws the exception supplied by `exceptionSupplier` otherwise. Returns `collection` for fluent chaining. |
| `static <M extends Map<K, V>, K, V, E extends Exception> M` | `requireNonEmpty(@NonNull M map, @NonNull Supplier<E> exceptionSupplier)` | Ensures `map` is not empty; throws the exception supplied by `exceptionSupplier` otherwise. Returns `map` for fluent chaining. |
| `static <A, C extends Collection<A>, E extends Exception> C` | `requireNonNullElementsInCollection(@NonNull C collection, @NonNull Fun<Integer, E> exceptionConstructor)` | Ensures all elements in `collection` are non-null; throws the exception constructed with the failing index otherwise. |
| `static <A, C extends Collection<A>, E extends Exception> C` | `requireNonNullElementsInCollection(@NonNull C collection, @NonNull Receiver<A> andThen, @NonNull Fun<Integer, E> exceptionConstructor)` | Validates that all elements are non-null and passes each element to `andThen` during iteration. |
| `static <A, E extends Exception> A[]` | `requireNonNullElementsInArray(@NonNull A[] array, @NonNull Fun<Integer, E> exceptionConstructor)` | Ensures all elements in `array` are non-null; throws the exception constructed with the failing index otherwise. |
| `static <A, E extends Exception> void` | `requireNonNullElementsInArray(@NonNull A[] array, @NonNull Receiver<A> andThen, @NonNull Fun<Integer, E> exceptionConstructor)` | Validates non-null array elements and executes `andThen` for each element. |

### Function Result Validation

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <X, Y, E extends RuntimeException> Fun<X, Y>` | `requireNonNullResult1(@NonNull Function<X, Y> function, @NonNull Fun<X, E> exceptionConstructor)` | Wraps a unary function to enforce non-null arguments and non-null returned results. |
| `static <X1, X2, Y, E extends RuntimeException> Fun2<X1, X2, Y>` | `requireNonNullResult2(@NonNull BiFunction<X1, X2, Y> function, @NonNull Fun2<X1, X2, E> exceptionConstructor)` | Wraps a binary function to enforce non-null arguments and non-null returned results. |

### Set Relationships

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A> boolean` | `isSubSet(@NonNull Set<A> superSet, @NonNull Set<A> subSet)` | Returns `true` if `subSet` is a subset of `superSet` (`subSet ⊆ superSet`). |
| `static <S extends Set<A>, A, E extends Exception> S` | `requireSubSet(@NonNull S superSet, @NonNull S subSet, @NonNull Fun2<S, S, E> exceptionConstructor)` | Ensures `subSet` is a subset of `superSet`; throws the constructed exception otherwise. |
| `static <A> boolean` | `isProperSubSet(@NonNull Set<A> superSet, @NonNull Set<A> subSet)` | Returns `true` if `subSet` is a proper subset of `superSet` (`subSet ⊂ superSet`). |
| `static <S extends Set<A>, A, E extends Exception> S` | `requireProperSubSet(@NonNull S superSet, @NonNull S subSet, @NonNull Fun2<S, S, E> exceptionConstructor)` | Ensures `subSet` is a proper subset of `superSet`; throws the constructed exception otherwise. |

### Type & Reflection Checks

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A, E extends RuntimeException> Class<A>` | `requireInterfaceType(@NonNull Class<A> clazz, @NonNull Supplier<E> exceptionSupplier)` | Ensures `clazz` is an interface type. |
| `static <A, E extends RuntimeException> Class<A>` | `requireConcreteType(@NonNull Class<A> clazz, @NonNull Supplier<E> exceptionSupplier)` | Ensures `clazz` is a concrete instantiable class (not an interface, abstract class, primitive, or annotation). |

### Filesystem & Path Guards

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <E extends Exception> Path` | `requireRegularFile(@NonNull String path, @NonNull Supplier<E> exceptionSupplier)` | Ensures the path string points to a regular file and returns it as `Path`. |
| `static <E extends Exception> File` | `requireRegularFile(@NonNull File file, @NonNull Supplier<E> exceptionSupplier)` | Ensures the `File` is a regular file. |
| `static <E extends Exception> Path` | `requireRegularFile(@NonNull Path path, @NonNull Supplier<E> exceptionSupplier)` | Ensures the `Path` is a regular file. |
| `static <E extends Exception> Path` | `requireDirectory(@NonNull String path, @NonNull Supplier<E> exceptionSupplier)` | Ensures the path string points to a directory and returns it as `Path`. |
| `static <E extends Exception> File` | `requireDirectory(@NonNull File file, @NonNull Supplier<E> exceptionSupplier)` | Ensures the `File` is an existing directory. |
| `static <E extends Exception> Path` | `requireDirectory(@NonNull Path path, @NonNull Supplier<E> exceptionSupplier)` | Ensures the `Path` is an existing directory. |
| `static <E extends Exception> Path` | `requireReadable(@NonNull String path, @NonNull Supplier<E> exceptionSupplier)` | Ensures the path string points to a readable file/directory and returns `Path`. |
| `static <E extends Exception> File` | `requireReadable(@NonNull File file, @NonNull Supplier<E> exceptionSupplier)` | Ensures the `File` is readable. |
| `static <E extends Exception> Path` | `requireReadable(@NonNull Path path, @NonNull Supplier<E> exceptionSupplier)` | Ensures the `Path` is readable. |
| `static <E extends Exception> Path` | `requireWriteable(@NonNull String path, @NonNull Supplier<E> exceptionSupplier)` | Ensures the path string points to a writable file/directory and returns `Path`. |
| `static <E extends Exception> File` | `requireWriteable(@NonNull File file, @NonNull Supplier<E> exceptionSupplier)` | Ensures the `File` is writable. |
| `static <E extends Exception> Path` | `requireWriteable(@NonNull Path path, @NonNull Supplier<E> exceptionSupplier)` | Ensures the `Path` is writable. |
| `static String` | `toSafeFileName(@NonNull String name)` | Sanitizes a string into a safe file name by replacing whitespace with `_` and stripping disallowed characters. |

---

## See Also

* [`Constraint`](/reference/base/util/constraint/) – Functional predicates and validation combinators.
* [`Attempt`](/reference/base/util/attempt/) – Exception encapsulation and error recovery.
