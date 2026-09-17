---
title: Stateful<A, S>
description: API reference for Stateful, the State monad carrying a state S and producing a value A in Foomp.
---

`Stateful<A, S>` models a classic State monad representing computations that carry an immutable state `S` and produce a resulting value `A`. Internally, it encapsulates a state transition function of the shape `S -> Tuple2<A, S>`.

---

## Type Signature

```java
package org.quurz.foomp.base.util;

public class Stateful<A, S>
        implements H2Monadic<Stateful.µ, A, S>,
                   Higher2<Stateful.µ, A, S>
```

### Type Parameters
* `A`: The type of the value produced by the computation.
* `S`: The type of the state carried and modified across transitions.

---

## Witness Type & Higher-Kinded Operations

```java
public static final class µ implements WitnessType { private µ() {} }
```

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A, S> Stateful<A, S>` | `narrow(@NonNull Higher2<? extends Stateful.µ, A, S> higher)` | Narrows a `Higher2` value to a concrete `Stateful<A, S>` instance. Throws `IllegalArgumentException` if the instance is not a `Stateful`. |
| `static <A, S> Stateful<A, S>` | `flatten(@NonNull Higher2<? extends Stateful.µ, ? extends Higher2<? extends Stateful.µ, A, S>, S> wrapped)` | Flattens a nested `Stateful` computation by one level (monadic join), threading intermediate states through. |

---

## Static Factory & Combinator Methods

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A, S> Stateful<A, S>` | `stateful(@NonNull Function<S, Tuple2<A, S>> runState)` | Creates a `Stateful` computation from an explicit state transition function `S -> Tuple2<A, S>`. |
| `static <A, S> Stateful<A, S>` | `stateOf(@NonNull A value)` | Lifts a plain value into a `Stateful` computation that yields `value` without changing the initial state. |
| `static <S> Stateful<S, S>` | `getState()` | Returns a computation that yields the current state as its produced value without altering it. |
| `static <S> Stateful<Nothing, S>` | `modifyState(@NonNull Function<? super S, ? extends S> modifier)` | Returns a computation that updates the current state via `modifier` and produces `Nothing.nothing`. |
| `static <S> Stateful<Nothing, S>` | `putState(@NonNull S state)` | Returns a computation that overwrites the current state with a new fixed state and produces `Nothing.nothing`. |

---

## Method Summary

### Functor & Monadic Operations

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <B> Stateful<B, S>` | `map(@NonNull Function<? super A, ? extends B> transformation)` | Functor map: transforms the produced value while threading the state through unchanged. |
| `@NonNull <B> Stateful<B, S>` | `applyTo(@NonNull Higher2<? extends µ, Function<A, B>, S> transformation)` | Applicative application: applies a function carried in `transformation` to this stateful value, threading state from left to right. |
| `@NonNull <B> Stateful<B, S>` | `flatMap(@NonNull Function<A, ? extends Higher2<? extends µ, B, S>> transformation)` | Monadic bind: sequences two stateful computations, passing the resulting state of the first into the second. |

### Execution & Evaluation (`@UnwindingOperation`)

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull Tuple2<A, S>` | `runState(@NonNull S state)` | Runs the state transition function starting with `state`, returning both the produced value and the final state. |
| `@NonNull Tuple2<A, S>` | `runStateTuple(@NonNull Tuple2<A, S> stateTuple)` | Runs the state transition function using `stateTuple.get2()` as the input state, returning both the result and new state. |
| `@NonNull A` | `execValue(@NonNull S state)` | Runs the computation starting with `state` and extracts only the final produced value. |
| `@NonNull S` | `execState(@NonNull S state)` | Runs the computation starting with `state` and extracts only the final transformed state. |

---

## See Also

* [`Tuple2<A, B>`](/reference/base/util/tuple2/) – The pair container used for holding value-state results.
* [`Nothing`](/reference/base/util/nothing/) – Unit type returned by state-modifying operations (`modifyState`, `putState`).
* [`Eval<A>`](/reference/base/util/eval/) – Lazy and memoized computation monad.
