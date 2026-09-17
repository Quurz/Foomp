---
title: H2Monadic<WT, A, R>
description: API reference for H2Monadic, the unified Rank-2 monad interface over binary type constructors Higher2 in Foomp.
---

`H2Monadic<WT, A, R>` represents the **Rank-2 Monad** contract in Foomp for binary higher-kinded type constructors (`Higher2<WT, A, R>`).

It combines the capabilities of [`H2Mappable`](/reference/base/types/h2mappable/), [`H2Appliable`](/reference/base/types/h2appliable/), and [`H2Bindable`](/reference/base/types/h2bindable/) into a single, unified monadic interface.

---

## Interface Definition

```java
package org.quurz.foomp.base.types;

import org.quurz.foomp.higher.WitnessType;

public interface H2Monadic<WT extends WitnessType, A, R>
        extends H2Mappable<WT, A, R>,
                H2Appliable<WT, A, R>,
                H2Bindable<WT, A, R> {}
```

---

## Type Hierarchy & Architecture

```
        ┌────────────────────────────────────────┐
        │        H2Monadic<WT, A, R>             │
        └───────────────────┬────────────────────┘
             ▲              ▲              ▲
             │              │              │
    ┌────────┴─────┐ ┌──────┴──────┐ ┌─────┴────────┐
    │  H2Mappable  │ │ H2Appliable │ │  H2Bindable  │
    │  (Functor)   │ │(Applicative)│ │   (Monad)    │
    └──────────────┘ └─────────────┘ └──────────────┘
```

* **Functor (`H2Mappable`):** Enables transforming the active parameter `A` via `map(Function<A, B>)`.
* **Applicative (`H2Appliable`):** Enables applying wrapped functions via `applyTo(Higher2<WT, Function<A, B>, R>)`.
* **Monad (`H2Bindable`):** Enables sequencing and flat-mapping via `flatMap(Function<A, Higher2<WT, B, R>>)`.

---

## Type Parameters

| Parameter | Description |
| :--- | :--- |
| `<WT>` | The witness type identifying the binary type constructor (must extend `WitnessType`). |
| `<A>` | The primary type parameter being manipulated across monadic operations. |
| `<R>` | The preserved second type parameter (e.g., answer/return type in CPS, or state in stateful computations). |

---

## Monad Axioms & Consistency Laws

Implementations of `H2Monadic` must satisfy category theory laws consistently:

1. **Functor Consistency:**  
   $$m.\text{map}(f) \equiv m.\text{flatMap}(x \to \eta(f(x)))$$
2. **Applicative Consistency:**  
   $$m.\text{applyTo}(f_{box}) \equiv f_{box}.\text{flatMap}(f \to m.\text{map}(f))$$
3. **Monad Laws:**  
   Left identity, Right identity, and Associativity of `flatMap`.

---

## Implementations in Foomp

* **`Continuation<R, A>`:** Continuation-passing style monad with answer type `R`.
* **`Stateful<S, A>`:** State monad managing state transitions `S -> Pair<A, S>`.

---

## Related Types

* [`H2Mappable`](/reference/base/types/h2mappable/) — Rank-2 functor interface.
* [`H2Appliable`](/reference/base/types/h2appliable/) — Rank-2 applicative functor interface.
* [`H2Bindable`](/reference/base/types/h2bindable/) — Rank-2 monadic bind interface.
* [`Monadic`](/reference/base/types/monadic/) — Rank-1 monadic interface for unary containers (`Higher1<WT, A>`).
