---
title: Base Utility Overview
description: A comprehensive guide to all functional containers and utility classes in the Foomp Base module.
---

The `org.quurz.foomp.base.util` package is the heart of the Foomp library's utility layer. It provides a wide array of functional data structures, monadic containers, and helper classes that extend Java's capabilities with patterns found in advanced functional languages.

## Functional Containers (Sum Types & Monads)

These types represent the core "value wrappers" that provide safe alternatives to `null` and `try-catch` blocks.

### Maybe
`Maybe<A>` is a lazy, type-safe alternative to `Optional`. It encapsulates a computation that may or may not produce a value.
- **Some**: A container for a value (computed on demand).
- **None**: Represents the absence of a value.

### Either
`Either<L, R>` represents a value of one of two possible types. It is commonly used for error handling where `Left` is the error channel and `Right` is the success channel.

### Result
`Result<A>` is a specialized `Either<Exception, A>`. It explicitly captures success or failure (as an `Exception`).

### Attempt
`Attempt<A>` is a fully lazy wrapper for operations that might throw exceptions. It defers the execution and the potential failure handling until the result is explicitly requested.

### Eval
`Eval<A>` provides explicit control over evaluation strategies:
- **Now**: Immediate (eager).
- **Later**: Lazy and memoized.
- **Always**: Lazy and recomputed on every access.

### Trampoline
`Trampoline<T>` is a mechanism for stack-safe recursion. It transforms deep recursive calls into a tail-recursive loop to prevent `StackOverflowError`.

### Stateful
`Stateful<A, S>` implements the State Monad, allowing you to pass and update a state `S` through a chain of computations producing `A`.

### Continuation
`Continuation<A, R>` (the Cont Monad) provides advanced control flow by capturing the "rest of the computation."

---

## Data Structures & Records

These classes provide immutable and mutable ways to group data without writing boilerplate.

### Tuples (Tuple2, Tuple3, Tuple4)
Immutable, type-safe containers for fixed-size collections of heterogenous values. They implement many functional behaviors (mapping, applying).

### Records (Record2, Record3, Record4)
A specialized variant of tuples designed for structured data. While similar to tuples, they are often used where a more "named" or structured approach to multiple values is required within the functional pipeline.

### AVLTree
A self-balancing binary search tree. It ensures $O(\log n)$ time complexity for search, insertion, and deletion by maintaining a height balance between subtrees. It is immutable and supports merging with other binary trees.

### RedBlackTree
An immutable, self-balancing search tree based on the Okasaki implementation. It uses colors (Red/Black) to maintain balance and provides guaranteed $O(\log n)$ performance for core operations. Like AVLTree, it is copy-on-write and persistent.

### MutablePair (Pair)
A mutable alternative to `Tuple2`. It implements `Value2`, allowing access via `get1()`/`get2()` and presence checks via `isPresent1()`/`isPresent2()`. It allows updating its values via `set1()`/`set2()`.

### Nothing
`Nothing` is a terminal type often used in generic contexts to represent a value that can never exist (bottom type) or to signify an empty result in certain monadic chains.

---

## Logic, Validation & Batching

Specialized tools for controlling logic flow and processing data streams.

### DecisionTree
`DecisionTree<F, R>` allows modeling complex, branching business logic as a traversable, lazy tree structure based on facts. It is implemented as a sealed interface with internal immutable classes for nodes and leaves.

### Constraint
`Constraint<A, FAILURE>` encapsulates a validation rule. It combines a predicate with a failure result, making it easy to build validation pipelines.

### Bucket
`Bucket<A>` is used for batching elements. It accumulates items until a limit is reached or a manual flush is triggered, then passes them to a consumer.

---

## Utilities

Static helper classes for common operations.

### Util
The primary entry point for contract enforcement and validation.
- **require...**: Methods (like `requireSubSet`, `requireNonEmpty`, `requireConcreteType`, `requireInterfaceType`) that throw exceptions with consistent messaging if conditions are not met.
- **File System Utilities**: Methods (like `requireRegularFile`, `requireDirectory`, `requireReadable`, `requireWriteable`) to validate paths, file types, and permissions.
- **requireNonNullResult**: Wrappers to ensure functions always return non-null values.

### Zipper
A utility for merging and splitting lists.
- **zip**: Combines two lists into a list of `Tuple2`.
- **unzip**: Separates a list of tuples back into two lists.

---

## Summary Table of All Types

| Category | Classes & Interfaces |
| :--- | :--- |
| **Sum Types / Monads** | `Maybe`, `Either`, `Result`, `Eval`, `Attempt`, `Stateful`, `Continuation` |
| **Recursion** | `Trampoline` |
| **Fixed Containers** | `Tuple2`, `Tuple3`, `Tuple4`, `Record2`, `Record3`, `Record4`, `Pair` (Mutable) |
| **Data Structures** | `AVLTree`, `RedBlackTree` |
| **Logic & Validation** | `DecisionTree`, `Constraint` |
| **Stream Processing** | `Bucket` |
| **System Types** | `Nothing` |
| **Utilities** | `Util`, `Zipper` |

---

## Why use these Utilities?

The `base.util` package aims to provide a robust foundation for building maintainable Java applications. By replacing scattered null checks and complex if-else chains with types like `Maybe` and `DecisionTree`, your code becomes more declarative, easier to test, and significantly less prone to runtime errors.
