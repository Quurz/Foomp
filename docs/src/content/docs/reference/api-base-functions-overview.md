---
title: Base Functions Overview
description: A comprehensive guide to the specialized functional interfaces in the Foomp Base module.
---

The `org.quurz.foomp.base.functions` package provides a suite of advanced functional interfaces that extend standard Java counterparts (like `Function`, `Predicate`, `Supplier`, and `Consumer`). These types integrate seamlessly with Foomp's core concepts such as **laziness**, **monadic composition**, **memoization**, and **checked exception handling**.

## Core Function Types

Foomp's function hierarchy is built on top of `Applicable` and `Fun`, providing more power than the standard `java.util.function` types.

### Applicable and Fun
- **`Applicable<X, Y>`**: The base interface for anything that can be applied to an input to produce an output and is permitted to throw checked exceptions. Provides `safe()` to convert the computation into an `XorValue<Exception, Y>` without throwing.
- **`Fun<X, Y>`**: Extends `Applicable` and `java.util.function.Function`. It is the primary type for rank-1 functions with strict non-null contracts.
    - **Composition**: Supports `compose` and `andThen` for fluent chaining.
    - **Async Support**: Provides `async()` and `async(Executor)` to execute functions asynchronously using `CompletableFuture`.
    - **Memoization**: Can be converted to a `MemoisingFun` using `memoise()` to cache results.
    - **Null Safety**: Offers `nullSafe()` and static `fun(Function)` wrappers.
- **Arity Variants**: `Fun2`, `Fun3`, and `Fun4` provide similar capabilities for functions with 2, 3, or 4 arguments.

### Specialized Functions
- **`Operator<A>`**: A specialization of `Fun<A, A>` where the input and output types are the same (Unary Operator). Variants include `Operator2` and `Operator3`.
- **`Comparer<A>`**: A functional interface extending `Fun2<A, A, Comparer.Relation>` for comparing two values. It returns a type-safe `Relation` enum (`LESS`, `EQUAL`, `GREATER`) suitable for switch-expressions and can adapt standard `Comparator<A>` instances via `Comparer.comparer(...)`.

## Predicates (Logic Gates)

Foomp predicates go far beyond simple boolean tests, offering advanced logical composition.

- **`Pred<A>`**: Extends `Predicate<A>`. It provides a rich set of logical operators:
    - **Basic**: `and`, `or`, `negate`.
    - **Advanced**: `xor`, `nand`, `nor`.
    - **Static Helpers**: `alwaysTrue()`, `alwaysFalse()`, `pred()`.
    - **Memoization**: Can be converted to `MemoisingPred` via `memoise()`.
- **`Pred2<A1, A2>` & `Pred3<A1, A2, A3>`**: Binary and Ternary predicates with similar logical composition and partial application support (`partial1`, `partial2`).

## Providers and Receivers

These types handle the production and consumption of values, bridging the gap between eager and lazy execution.

- **`Provider<A>`**: A powerful alternative to `Supplier<A>`.
    - **Monadic**: Implements `Monadic` (map, flatMap, applyTo), allowing transformation of values before they are even produced.
    - **Witness Type**: Serves as a Higher-Kinded Type (HKT) with witness `Provider.µ`.
    - **Laziness**: Inherently lazy; value is produced only when `get()` is called.
- **`UnsafeProvider<A>`**: Similar to `Supplier`, but permitted to throw checked exceptions via `get() throws Exception`. It extends `Applicable<Nothing, A>`.
- **`Receiver<A>`**: Extends `Consumer<A>` and `Fun<A, Nothing>`.
    - **Filtering & Conditions**: Offers `receiver(Consumer, Predicate)`, `filter`, and `when`.
    - **Chaining**: Supports `acceptAndContinue` for fluent consumption.
    - **Bulk Processing**: `acceptAllAndContinue` handles iterators, collections, and streams.

## Behavioral Wrappers

- **`MemoisingFun<X, Y>`**: A thread-safe wrapper that caches the results of a function based on its input using a `ConcurrentHashMap`. Use it for expensive, pure computations.
- **`MemoisingApplicable<X, Y>`**: Similar to `MemoisingFun`, but for the more general `Applicable` type.
- **`MemoisingPred<A>`**: A memoizing wrapper for predicates caching boolean test results.

## Summary Table

| Type | Java Equivalent | Foomp Enhancements |
| :--- | :--- | :--- |
| `Applicable<X, Y>` | - | Checked exceptions, safe `XorValue` evaluation |
| `Fun<X, Y>` | `Function<X, Y>` | Async (`CompletableFuture`), Composition, Memoization, Non-Null contract |
| `Comparer<A>` | `Comparator<A>` | Returns `Relation` enum (`LESS`, `EQUAL`, `GREATER`), `Fun2` compatibility |
| `Pred<A>` | `Predicate<A>` | NAND, NOR, XOR, Partial Application, Memoization |
| `Provider<A>` | `Supplier<A>` | Monadic (map/flatMap), HKT (`Provider.µ`), Laziness |
| `Receiver<A>` | `Consumer<A>` | Filtering, Fluent Chaining, Bulk Consumption |
| `UnsafeProvider<A>` | `Supplier<A>` | Checked Exception Support (`get() throws Exception`), `Applicable` integration |

## Why use Foomp Functions?

By using Foomp's functional interfaces instead of standard Java types, you gain:
1. **Consistency**: Unified handling of laziness, non-null contracts, and async execution.
2. **Readability**: Fluent APIs for logical composition, pattern matching (`Relation`), and function chaining.
3. **Power**: Support for Higher-Kinded Types (HKTs) and Monadic patterns in standard Java code.
4. **Safety**: Better integration with Foomp's null-safety and exception handling models (`XorValue`, `UnsafeProvider`).
