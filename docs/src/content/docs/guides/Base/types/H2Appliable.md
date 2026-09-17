---
title: H2Appliable Guide
description: Practical recipes and higher-kinded applicative patterns with H2Appliable in Foomp.
---

The `H2Appliable<WT, A, R>` interface brings the power of **Applicative Functors** to binary type constructors (`Higher2`). It lets you apply functions that are themselves wrapped within a computational context (such as stateful computations or continuations) to values wrapped within the same context.

---

## Conceptual Overview

Standard `Appliable<WT, A>` works with unary higher-kinded types (`Higher1<WT, A>`):

$$\text{applyTo} : F(A \to B) \to F(A) \to F(B)$$

`H2Appliable<WT, A, R>` generalizes this to binary higher-kinded types (`Higher2<WT, A, R>`) by keeping the secondary type parameter `R` fixed:

$$\text{applyTo} : F(A \to B, R) \to F(A, R) \to F(B, R)$$

---

## Practical Examples

### 1. Combining Contextual Computations with Continuations

Delimited continuations (`Continuation<A, R>`) implement `H2Appliable` (via `H2Monadic`). This allows you to combine independent suspended operations:

```java
import org.quurz.foomp.base.util.Continuation;
import java.util.function.Function;

// Two suspended continuation computations with fixed answer type String:
Continuation<Integer, String> contA = Continuation.pure(10);
Continuation<Integer, String> contB = Continuation.pure(32);

// Function in continuation context:
Continuation<Function<Integer, Integer>, String> fnCont =
    contA.map(a -> (Integer b) -> a + b);

// Apply function inside continuation context to contB:
Continuation<Integer, String> combined = Continuation.narrow(contB.applyTo(fnCont));

// Run the continuation with identity prompt:
String result = combined.run(Object::toString); // "42"
```

### 2. Contextual Function Application in `Stateful`

With `Stateful<S, A>`, state transitions are automatically threaded through both the function supplier and the value supplier:

```java
import org.quurz.foomp.base.util.Stateful;
import java.util.function.Function;

// Value wrapped in state context:
Stateful<Integer, String> valueState = Stateful.pure("hello");

// Function wrapped in state context (which also modifies state):
Stateful<Integer, Function<String, String>> funcState =
    Stateful.state(counter -> (Function<String, String>) (String str) -> str.toUpperCase() + " #" + (counter + 1));

// Apply funcState to valueState:
Stateful<Integer, String> applied = Stateful.narrow(valueState.applyTo(funcState));
```

---

## Higher-Kinded Type Safety & Narrowing

Because Java does not have native Higher-Kinded Types, Foomp uses witness types (`WT`) and `Higher2<WT, A, R>`. Foomp provides safe narrowing helpers to unpack concrete implementations:

```java
// Given a Higher2 result:
Higher2<? extends Continuation.µ, Integer, String> h2 = contB.applyTo(fnCont);

// Safely narrow back to the concrete type:
Continuation<Integer, String> concrete = Continuation.narrow(h2);
```

---

## Summary of Category Theory Laws for `H2Appliable`

Implementations of `H2Appliable` satisfy the standard Applicative Functor laws (with fixed parameter `R`):

1. **Identity**: `pure(id).applyTo(v) ≡ v`
2. **Homomorphism**: `pure(f).applyTo(pure(x)) ≡ pure(f(x))`
3. **Interchange**: `u.applyTo(pure(y)) ≡ pure(f -> f(y)).applyTo(u)`
4. **Composition**: `pure(compose).applyTo(u).applyTo(v).applyTo(w) ≡ u.applyTo(v.applyTo(w))`
