---
title: Hkt Guide
description: Architectural overview and design patterns for Higher-Kinded Types (HKTs) in Foomp.
---

import { Aside } from '@astrojs/starlight/components';

Higher-kinded types (HKTs) allow programmers to abstract over type constructors rather than just concrete types. While languages like Haskell and Scala support higher kinds natively, standard Java lacks language-level support for $F<_>$.

Foomp provides a lightweight, zero-overhead encoding for HKTs rooted at the [`Hkt<WT>`](/reference/higher/higher/hkt/) interface.

---

## What is Kind Polymorphism?

In type theory, types are classified by **kinds**:

* **Kind $*$ (Proper Types)**: Concrete types that have values, e.g., `String`, `Integer`, `Maybe<String>`.
* **Kind $* \to *$ (Rank-1 Type Constructors)**: Generic types requiring one type argument, e.g., `Maybe`, `List`, `Task`.
* **Kind $* \to * \to *$ (Rank-2 Type Constructors)**: Generic types requiring two type arguments, e.g., `Either`, `Pair`, `Map`.
* **Kind $* \to * \to * \to *$ (Rank-3 Type Constructors)**: e.g., `Tuple3`, `Record3`.
* **Kind $* \to * \to * \to * \to *$ (Rank-4 Type Constructors)**: e.g., `Tuple4`, `Record4`.

Without HKT support, you cannot write a single function or interface that abstracts over any arbitrary functor (e.g., "map a function over $F<A>$ to produce $F<B>$").

---

## The Foomp HKT Hierarchy

Foomp structures higher-kinded types into rank-specific subinterfaces extending `Hkt<WT>`:

```text
               +----------------------+
               |  Hkt<WT> (arity 0)   |
               +----------------------+
                          |
        +-----------------+-----------------+-----------------+
        |                 |                 |                 |
+---------------+ +---------------+ +---------------+ +---------------+
| Higher1<WT,A> | |Higher2<WT,A,B>| |Higher3<WT,A..>| |Higher4<WT,A..>|
|   arity: 1    | |   arity: 2    | |   arity: 3    | |   arity: 4    |
+---------------+ +---------------+ +---------------+ +---------------+
```

* **`Hkt<WT>`**: Root marker declaring common arity metadata.
* **`Higher1<WT, A>`**: Encodes unary type constructors ($* \to *$).
* **`Higher2<WT, A, B>`**: Encodes binary type constructors ($* \to * \to *$).
* **`Higher3<WT, A, B, C>`**: Encodes ternary type constructors ($* \to * \to * \to *$).
* **`Higher4<WT, A, B, C, D>`**: Encodes quaternary type constructors ($* \to * \to * \to * \to *$).

---

## Arity Inspection

The `Hkt` interface declares the `arity()` method, which can be inspected reflectively or in polymorphic dispatch algorithms:

```java title="ArityInspection.java"
import org.quurz.foomp.higher.Hkt;
import org.quurz.foomp.base.util.Maybe;
import org.quurz.foomp.base.util.Either;
import org.quurz.foomp.base.util.Tuple3;
import org.quurz.foomp.base.util.Tuple4;

public class ArityInspection {
    public static void printArity(Hkt<?> hkt) {
        System.out.println("HKT Arity: " + hkt.arity());
    }

    public static void main(String[] args) {
        printArity(Maybe.just("test"));            // Output: HKT Arity: 1
        printArity(Either.right("success"));        // Output: HKT Arity: 2
        printArity(Tuple3.of("a", 1, true));       // Output: HKT Arity: 3
        printArity(Tuple4.of("a", 1, true, 3.14)); // Output: HKT Arity: 4
    }
}
```

---

## The HKT Lifecycle in Java

Working with Foomp's higher-kinded type encoding follows a three-phase lifecycle:

```text
+-------------------+      Implement HKT       +-------------------------+
|   Concrete Type   | -----------------------> | HigherN<WT, Args...>    |
| e.g. Maybe<A>     |                          | Generalized HKT Form    |
+-------------------+                          +-------------------------+
          ^                                                 |
          |                                                 | Polymorphic API
          |                                                 | (e.g. Functor / Monad)
          |           static narrow(hkt)                    v
          +----------------------------------- +-------------------------+
                    Safe Reification           | HigherN<WT, ResultArgs> |
                                               | Processed HKT Form      |
                                               +-------------------------+
```

### 1. Generalization
A concrete object (such as `Maybe<Integer>`) is implicitly typed as `Higher1<Maybe.µ, Integer>`.

### 2. Polymorphic Processing
A higher-order functional component (such as an applicative pipeline or a generic sequence transformer) operates purely on `Higher1<WT, A>`.

### 3. Reification (Narrowing)
The resulting generic `Higher1<Maybe.µ, B>` is narrowed back to `Maybe<B>` using `Maybe.narrow(...)`.

---

## Best Practices

:::tip[Designing Custom HKT Containers]
1. Always implement the exact rank interface corresponding to the number of type parameters (`Higher1` for 1, `Higher2` for 2, etc.).
2. Define a private-constructor witness class (by convention `public static final class µ implements WitnessType {}`).
3. Provide a static `narrow` (or `fix`) method annotated with `@SuppressWarnings("unchecked")` for zero-cost type reification.
:::
