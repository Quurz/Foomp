---
title: Continuation<A, R>
description: API reference for Continuation, modeling continuation-passing style (CPS) computation and call/cc in Foomp.
---

`Continuation<A, R>` represents a deferred computation in Continuation-Passing Style (CPS).

It encapsulates a function of the form:
$$\text{runCont} : (A \to R) \to R$$

This mirrors Haskell's `Cont r a` monad (with type parameter order `<A, R>` where `A` is the intermediate value and `R` is the final computation result). It provides complete Functor, Applicative, and Monad operations, alongside `callCurrentCont` ($\text{call/cc}$) for advanced control-flow mechanisms such as non-local jumps, early exits, and coroutine-like branching.

---

## Type Signature

```java
package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.H2Monadic;
import org.quurz.foomp.higher.Higher2;
import org.quurz.foomp.higher.WitnessType;
import java.util.function.Function;

public class Continuation<A, R>
        implements H2Monadic<Continuation.µ, A, R>,
                   Function<Function<A, R>, R>,
                   Higher2<Continuation.µ, A, R>
```

### Type Parameters
* `A`: The produced value type (passed into the final consumer continuation).
* `R`: The overall final result type of the CPS pipeline.

---

## Higher-Kinded Witness Type & Narrowing

```java
public static final class µ implements WitnessType { private µ() {} }
```

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A, R> Continuation<A, R>` | `narrow(@NonNull Higher2<? extends µ, A, R> higher)` | Safely downcasts a generic `Higher2` container to `Continuation<A, R>`. Throws `NullPointerException` if `higher` is `null`, or `IllegalArgumentException` if not a `Continuation`. |

---

## Static Factory Methods

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <R, A> Continuation<A, R>` | `pureContinuation(@NonNull A value)` | Lifts a plain value into a CPS computation: $a \mapsto (k \mapsto k(a))$. |
| `static <R, A> Continuation<A, R>` | `continuation(@NonNull Function<Function<A, R>, R> runCont)` | Wraps a raw CPS runner function $(A \to R) \to R$ into a `Continuation`. |
| `static <R, A, B> Continuation<B, R>` | `callCurrentCont(@NonNull Function<Function<A, Continuation<B, R>>, Continuation<A, R>> computation)` | Provides **call/cc** ($\text{call-with-current-continuation}$), supplying an escape continuation $k$ that can be invoked to immediately short-circuit execution. |

---

## Method Summary

### Functor, Applicative & Monadic Operations

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <B> Continuation<B, R>` | `map(@NonNull Function<? super A, ? extends B> transformation)` | Functor map: maps $A \to B$ over the produced value. Defined as $\text{map } g = k \mapsto \text{runCont}(a \mapsto k(g(a)))$. |
| `@NonNull <B> Continuation<B, R>` | `applyTo(@NonNull Higher2<? extends µ, Function<A, B>, R> transformation)` | Applicative application: applies a wrapped function continuation $(A \to B)$ to this continuation's value. |
| `@NonNull <B> Continuation<B, R>` | `flatMap(@NonNull Function<A, ? extends Higher2<? extends µ, B, R>> transformation)` | Monadic bind ($\gg=$): sequences CPS computations. Defined as $\text{flatMap } f = k \mapsto \text{runCont}(a \mapsto f(a).\text{runCont}(k))$. |
| `<B> Continuation<B, R>` | `then(@NonNull Continuation<B, R> continuation)` | Sequences this continuation followed by the next, discarding this value. |

### Execution & Materialization

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull R` | `apply(@NonNull Function<A, R> computation)` | Runs the continuation by supplying the final consumer $(A \to R)$, returning the outcome of type $R$. |

---

## See Also

* [`H2Monadic<WT, A, B>`](/reference/base/types/h2monadic/) – Binary higher-kinded monad abstraction.
* [`WitnessType`](/reference/higher/witnesstype/) – Marker type enabling higher-kinded simulations in Java.
* [`Eval<A>`](/reference/base/util/eval/) – Lazy evaluation and stack-safe computation container.
