---
title: Base Types Overview
description: Foundational type abstractions used across the Foomp API.
---

The `org.quurz.foomp.base.types` package provides the core building blocks—interfaces and marker types—that enable Foomp's functional and composable programming style. These abstractions serve as a consistent contract for value carriers, transformations, and monadic compositions across the entire library.

## Core Abstractions

The types in this package can be grouped into several functional categories:

### 1. Value Carriers
These interfaces define how values are stored, accessed, and identified.

*   **`Value<A>`**: The most basic contract for a single value carrier. It extends `Supplier<A>` and adds `isPresent()` to handle potential absence.
*   **`Value2<A, B>`**, **`Value3<A, B, C>`**, **`Value4<A, B, C, D>`**: Extensions for tuples or multiple value carriers (e.g., used by `Tuple2`, `Tuple3`). These provide `isPresent1()`, `isPresent2()`, etc. to check for component presence and `get1()`, `get2()`, etc. for access. `Value2` also provides a default `get()` and `isPresent()` delegating to the first component.
*   **`XorValue<A, B>`**: Represents a value that can be one of two types, but not both (a "Sum Type" or "Either").
*   **`Dict<K, V>`**: Represents an associative key-value lookup contract supporting value retrieval (`get`, `safeGet`), keys/values views, and conversion to `java.util.Map`.
*   **`Streamable<A>`**: Provides a `stream()` method to effortlessly convert functional structures into standard `java.util.stream.Stream<A>`.
*   **`Seq<A>`**: A contract for sequential, ordered data structures.
*   **`Tree<A>`**: The base interface for hierarchical structures, supporting search, insertion, and removal.
*   **`BinaryTree<A>`**: A specialized tree where each node has at most two children.
*   **`Mergeable<SELF>`**: Represents objects that can be combined with others of the same type.

### 2. Functional Composition (HKTs)
Foomp uses **Higher-Kinded Types (HKT)** to provide generic functional operations while preserving the "shape" of the type constructor.

*   **`Mappable<WT, A>`**: The "Functor" contract. Provides `map(Function)` to transform the inner value.
*   **`Mappable2`, `Mappable3`, `Mappable4`**: Support for mapping types with multiple type parameters.
*   **`Bindable<WT, A>`**: The "Monad" contract. Provides `flatMap(Function)` for sequencing operations.
*   **`Appliable<WT, A>`**: The "Applicative" contract. Allows applying functions wrapped in the same HKT context (`applyTo`).
*   **`Appliable2`, `Appliable3`, `Appliable4`**: Applicative contracts for types with 2, 3, or 4 type parameters.
*   **`Monadic<WT, A>`**: A convenience interface that combines `Mappable`, `Bindable`, and `Appliable`.
*   **`H2Mappable`, `H2Bindable`, `H2Appliable`, `H2Monadic`**: Rank-2 HKT contracts for binary type constructors (e.g., `Either<L, R>`).
*   **`UnsafeMonadic<WT, A>`**: Monadic operations with support for throwing functions and checked exception propagation.
*   **`Foldable<A>`**: Allows reducing a structure (like a list or sequence) into a single value. Includes `FoldableLeft` and `FoldableRight` variants.

### 3. Transformation & Evaluation
Interfaces that handle how types change state, representation, or how their evaluation is triggered.

*   **`Transmogrifyable<SELF>`**: A fluent hook for arbitrary transformations. It allows "transmogrifying" an object into any other type via a function.
*   **`Unwindable<SELF>`**: The contract for **Laziness**. It provides `unwind()`, which materializes (evaluates) deferred computations.
*   **`Eager`**: Marker interface for values or operations that evaluate eagerly (immediately).
*   **`Copyable<SELF>`**: Ensures a type can produce a value-level copy of itself.
*   **`Swappable<SELF>`**: For types that support swapping internal elements (like Tuples).

### 4. Behavior & State Control
Marker interfaces and operational contracts that define how a type behaves or how its state is managed.

*   **`Triable<A>`**: A marker for computations that are "wrapped" in a try-catch-like context (e.g., `Attempt`).
*   **`Mutable`**: A marker interface for types that allow internal state changes.
*   **`ThreadSafe`**: Signals that an implementation is safe to use across multiple threads.
*   **`Visitable<V>`** & **`Visitor<V>`**: Implementation of the Visitor pattern for decoupled operations on structures.

### 5. Meta-Information & Operations
Specialized markers and annotations for metadata and lifecycle.

*   **`Eq<SELF>`**: Provides a type-safe alternative to `Object.equals(Object)`.
*   **`Echo`**: A functional interface for producing string representations (similar to `toString` but often with a formal contract).
*   **`UnwindingOperation`**: Annotation marking methods that trigger the materialization of a lazy value.
*   **`MutatingOperation`**: Annotation marking methods that perform side effects or mutate state.

## Usage Example: The Fluent Pipeline

Most types in Foomp (like `Maybe`, `Eval`, or `Attempt`) implement multiple base types to enable a fluent, functional API.

```java
// Example using Eval (which is Monadic, Unwindable, and Transmogrifyable)
String result = Eval.later(() -> "Hello")    // Lazy evaluation (Unwindable)
    .map(s -> s + " World")                  // Functor transformation (Mappable)
    .flatMap(s -> Eval.now(s.length()))      // Monadic bind (Bindable)
    .transmogrify(eval -> "Length: " + eval.get()); // Arbitrary conversion (Transmogrifyable)

// result is "Length: 11"
```

## Why separate these Interfaces?

By splitting functionality into these fine-grained interfaces, Foomp ensures that:
1.  **Consistency**: Different modules (like `Base` and `Higher`) speak the same "language".
2.  **Generic Algorithms**: You can write utilities that work on any `Mappable` or `Unwindable` type without knowing the concrete implementation.
3.  **Type Safety**: Higher-Kinded Type (HKT) witnesses ensure that a `map` on a `Maybe` always returns a `Maybe`, even when handled via a generic interface.
