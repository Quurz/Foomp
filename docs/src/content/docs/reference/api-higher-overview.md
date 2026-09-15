---
title: Higher-Kinded Types (HKT) Overview
description: A lightweight encoding for higher-kinded types in Java.
---

Higher-Kinded Types (HKTs) are a powerful abstraction from functional programming (common in languages like Haskell or Scala) that allows type constructors to be treated as first-class citizens. Since Java does not natively support HKTs, the `Higher` module provides a lightweight, type-safe encoding using **Witness Types**.

## Core Concept: Witness Types

To represent a type constructor (like `List<A>` or `Maybe<A>`) at the type level, we use a dedicated **Witness Type** (also called a *tag* or *token*). This witness type acts as a unique identifier for the shape of the type constructor.

The module provides the following base components:

- **`WitnessType`**: A marker interface that every witness type must implement.
- **`Hkt<WT>`**: The base interface for all higher-kinded types. It carries the witness type `WT` that encodes the constructor's identity.

## Rank (Arity) Specific Interfaces

Higher-kinded types are categorized by their **rank** (or arity), which specifies how many type parameters the constructor carries. The module provides specialized interfaces for ranks 1 through 4:

| Interface | Rank | Parameters | Description |
| :--- | :--- | :--- | :--- |
| `Higher1<WT, A>` | 1 | `WT, A` | Models unary constructors like `List<A>` or `Maybe<A>`. |
| `Higher2<WT, A, B>` | 2 | `WT, A, B` | Models binary constructors like `Either<A, B>` or `Map<A, B>`. |
| `Higher3<WT, A, B, C>` | 3 | `WT, A, B, C` | Models ternary constructors. |
| `Higher4<WT, A, B, C, D>` | 4 | `WT, A, B, C, D` | Models quaternary constructors. |

## Why use HKTs in Java?

By using these interfaces, you can write generic algorithms that work across different type constructors. For example, you can define a `Functor` or `Monad` interface that works for any `Higher1` type, regardless of whether it's a `List`, `Maybe`, or `Future`.

## Usage Pattern: Fix and Narrow

To bridge the gap between the abstract HKT and the concrete implementation, classes typically provide a static `fix` (or `narrow`) method. This method performs a safe cast from the `HigherN` interface back to the concrete type.

### Usage Example: Maybe

To understand how these pieces fit together, let's look at the `Maybe<A>` class (Foomp's lazy version of `Optional`).

#### 1. The Witness Type
Each HKT-capable class defines a unique **Witness Type**. For `Maybe`, this is a nested class named `µ` (the Greek letter Mu):

```java
public sealed interface Maybe<A> extends Higher1<Maybe.µ, A> {
    /** Witness type for Maybe */
    final class µ implements WitnessType { private µ() {} }
    // ...
}
```

This `µ` acts as a "type-level tag". It tells the compiler: "Whenever you see `Higher1<Maybe.µ, A>`, you are actually dealing with the `Maybe` constructor."

#### 2. The Higher-Kinded Interface
By implementing `Higher1<Maybe.µ, A>`, the `Maybe` class declares that it is a **Rank-1** type constructor. This allows it to be used in generic functional APIs (like `Functor` or `Monad`) that can't know about `Maybe` specifically, but know how to work with any `Higher1`.

#### 3. Reification (Narrowing)

Because Java doesn't know that `Higher1<Maybe.µ, A>` is always a `Maybe<A>`, the class provides a helper method to "narrow" (or "fix") the type back down:

```java
@SuppressWarnings("unchecked")
static <A> Maybe<A> narrow(final Higher1<? extends µ, A> wide) {
    return (Maybe<A>) wide;
}
```

This acts as a "bridge" between the generic HKT world and your concrete implementation.

### Why do we need this?

Without this encoding, you could only write a `map` method for `Maybe`, another for `List`, another for `Attempt`, and so on. You could not write a single generic function that works for **any** "mappable" container. 

With HKTs, you can define an interface like this:

```java
public interface Functor<F extends WitnessType> {
    <A, B> Higher1<F, B> map(Higher1<F, A> fa, Function<A, B> f);
}
```

Now, you can implement a single `Functor<Maybe.µ>` and use it to transform `Maybe` values without the caller ever needing to know the specifics of the `Maybe` implementation – until they decide to `narrow()` it back to the concrete type.
