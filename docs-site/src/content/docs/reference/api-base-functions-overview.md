---
title: Base Functions Overview
description: A comprehensive guide to the specialized functional interfaces in the Foomp Base module.
---

The `org.quurz.foomp.base.functions` package provides a suite of advanced functional interfaces that extend standard Java counterparts (like `Function`, `Predicate`, `Supplier`, and `Consumer`). These types integrate seamlessly with Foomp's core concepts such as **laziness**, **monadic composition**, **memoization**, and **checked exception handling**.

## Core Function Types

Foomp's function hierarchy is built on top of `Applicable` and `Fun`, providing more power than the standard `java.util.function` types.

### Applicable and Fun
- **`Applicable<X, Y>`**: The base interface for anything that can be applied to an input to produce an output. It extends `Deferrable` and allows converting the application into a `Callable` or a null-safe function.
- **`Fun<X, Y>`**: Extends `Applicable` and `java.util.function.Function`. It is the primary type for rank-1 functions.
    - **Composition**: Supports `compose` and `andThen` for fluent chaining.
    - **Async Support**: Provides `async()` and `deferredAsync()` to execute functions using `CompletableFuture`.
    - **Memoization**: Can be converted to a `MemoisingFun` to cache results.
- **Arity Variants**: `Fun2`, `Fun3`, and `Fun4` provide similar capabilities for functions with 2, 3, or 4 arguments.

### Specialized Functions
- **`Operator<A>`**: A specialization of `Fun<A, A>` where the input and output types are the same (Unary Operator). Variants include `Operator2` and `Operator3`.
- **`Komparator<A>`**: A functional interface that extends `Comparator<A>` and `Fun2<A, A, Integer>`, making it compatible with Foomp's binary function APIs.

## Predicates (Logic Gates)

Foomp predicates go far beyond simple boolean tests, offering advanced logical composition.

- **`Pred<A>`**: Extends `Predicate<A>`. It provides a rich set of logical operators:
    - **Basic**: `and`, `or`, `negate`.
    - **Advanced**: `xor`, `nand`, `nor`.
    - **Static Helpers**: `alwaysTrue()`, `alwaysFalse()`.
- **`Pred2<A1, A2>` & `Pred3<A1, A2, A3>`**: Binary and Ternary predicates with similar logical composition and partial application support (`partial1`, `partial2`).

## Providers and Receivers

These types handle the production and consumption of values, bridging the gap between eager and lazy execution.

- **`Provider<A>`**: A powerful alternative to `Supplier<A>`.
    - **Monadic**: Implements `Monadic` (map, flatMap, applyTo), allowing transformation of values before they are even produced.
    - **Witness Type**: Serves as a Higher-Kinded Type (HKT) with witness `Provider.µ`.
    - **Laziness**: Inherently lazy; value is produced only when `get()` is called.
- **`CheckedProvider<A>`**: Similar to `Supplier`, but allowed to throw checked exceptions. It integrates with Foomp's `Applicable` model.
- **`Receiver<A>`**: Extends `Consumer<A>`.
    - **Chaining**: Supports `acceptAndContinue` for fluent consumption.
    - **Bulk Processing**: `acceptAllAndContinue` handles iterators, collections, and streams.

## Behavioral Wrappers

- **`MemoisingFun<X, Y>`**: A thread-safe wrapper that caches the results of a function based on its input. Results are stored in an internal `ConcurrentHashMap`. Use it for expensive, pure computations.
- **`MemoisingApplicable<X, Y>`**: Similar to `MemoisingFun`, but for the more general `Applicable` type.

## Summary Table

| Type | Java Equivalent | Foomp Enhancements |
| :--- | :--- | :--- |
| `Fun<X, Y>` | `Function<X, Y>` | Async, Composition, Memoization, Laziness |
| `Pred<A>` | `Predicate<A>` | NAND, NOR, XOR, Partial Application |
| `Provider<A>` | `Supplier<A>` | Monadic (map/flatMap), HKT, Laziness |
| `Receiver<A>` | `Consumer<A>` | Fluent Chaining, Bulk Consumption |
| `CheckedProvider<A>`| - | Checked Exception Support |

## Why use Foomp Functions?

By using Foomp's functional interfaces instead of standard Java types, you gain:
1. **Consistency**: Unified handling of laziness and async execution.
2. **Readability**: Fluent APIs for logical composition and function chaining.
3. **Power**: Support for Higher-Kinded Types (HKTs) and Monadic patterns in standard Java code.
4. **Safety**: Better integration with Foomp's null-safety and exception handling models.
