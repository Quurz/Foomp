---
title: Monadic<WT, A>
description: Formal API reference and contract for Rank-1 monadic containers in Foomp.
---

import { Aside, Tabs, TabItem } from '@astrojs/starlight/components';

`Monadic<WT extends WitnessType, A>` is the primary Rank-1 Monad interface in Foomp. It consolidates the full functorial progression:

$$\text{Functor (Mappable)} \longrightarrow \text{Applicative (Appliable)} \longrightarrow \text{Monad (Bindable)}$$

By implementing `Monadic`, a container supports covariant value mapping (`map`), contextual function application (`applyTo`), and monadic sequencing/composition (`flatMap`), while preserving its higher-kinded constructor shape.

```java
package org.quurz.foomp.base.types;

import org.quurz.foomp.higher.WitnessType;

public interface Monadic<WT extends WitnessType, A>
        extends Mappable<WT, A>,
                Appliable<WT, A>,
                Bindable<WT, A> {}
```

---

## Type Parameters

| Parameter | Bound | Description |
| :--- | :--- | :--- |
| `<WT>` | `extends WitnessType` | The phantom witness type that identifies the concrete container constructor (e.g. `Maybe.µ`, `Attempt.µ`, `Sequence.µ`). |
| `<A>` | Unconstrained | The type of value(s) carried within the monadic context. |

---

## Inherited Capabilities

Because `Monadic` inherits from `Mappable`, `Appliable`, and `Bindable`, every monadic instance provides three core operations:

```
                  ┌──────────────────────┐
                  │    Mappable<WT, A>   │
                  │   map: A -> B        │
                  └──────────┬───────────┘
                             │
                  ┌──────────▼───────────┐
                  │    Appliable<WT, A>  │
                  │ applyTo: F[A -> B]   │
                  └──────────┬───────────┘
                             │
                  ┌──────────▼───────────┐
                  │    Bindable<WT, A>   │
                  │ flatMap: A -> F[B]   │
                  └──────────┬───────────┘
                             │
                  ┌──────────▼───────────┐
                  │    Monadic<WT, A>    │
                  └──────────────────────┘
```

### 1. Covariant Mapping (`map`)
From [`Mappable<WT, A>`](/reference/base/types/mappable/):
```java
<B> @NonNull Higher1<WT, B> map(final @NonNull Fun<A, B> transformation);
```
Transforms the encapsulated value $a \in A$ into $b \in B$ using a pure, non-null function `Fun<A, B>`.

### 2. Applicative Application (`applyTo`)
From [`Appliable<WT, A>`](/reference/base/types/appliable/):
```java
<B> @NonNull Higher1<WT, B> applyTo(final @NonNull Higher1<WT, ? extends Fun<A, B>> transformation);
```
Applies a function wrapped in the same monadic container `Higher1<WT, Fun<A, B>>` to this value.

### 3. Monadic FlatMap (`flatMap`)
From [`Bindable<WT, A>`](/reference/base/types/bindable/):
```java
<B> @NonNull Higher1<WT, B> flatMap(final @NonNull Fun<A, ? extends Higher1<WT, B>> transformation);
```
Chains dependent computations where each step returns another monadic container of the same constructor `WT`, automatically flattening nested layers ($F[F[B]] \to F[B]$).

---

## Monad Laws

Every implementation of `Monadic<WT, A>` obeys the three fundamental monad laws:

### 1. Left Identity
Wrapping a value into a monadic unit and flatMapping a function $f$ over it is identical to directly applying $f(a)$:
$$\text{unit}(a).\text{flatMap}(f) \equiv f(a)$$

### 2. Right Identity
FlatMapping the monadic unit constructor over any monadic instance leaves the instance unchanged:
$$m.\text{flatMap}(\text{unit}) \equiv m$$

### 3. Associativity
Chaining monadic operations is associative regardless of nesting:
$$m.\text{flatMap}(f).\text{flatMap}(g) \equiv m.\text{flatMap}(x \to f(x).\text{flatMap}(g))$$

---

## Key Implementations in Foomp

| Monadic Type | Witness Type | Semantics & Primary Use Case |
| :--- | :--- | :--- |
| [`Maybe<A>`](/reference/base/util/maybe/) | `Maybe.µ` | Optional values without `null` (`Just` / `Nothing`). |
| [`Either<L, R>`](/reference/base/util/either/) | `Either.µ<L>` | Disjoint unions and error representations (`Left` / `Right`). |
| [`Attempt<A>`](/reference/base/util/attempt/) | `Attempt.µ` | Lazy computations capturing checked exceptions safely. |
| [`Sequence<A>`](/reference/base/util/sequence/) | `Sequence.µ` | Lazy, potentially infinite functional streams. |
| [`Task<A>`](/reference/base/util/task/) | `Task.µ` | Asynchronous non-blocking computation workflows. |
| [`Provider<A>`](/reference/base/functions/provider/) | `Provider.µ` | Lazy value evaluation and monadic supplier pipelines. |

---

## Related Types

* [`Mappable<WT, A>`](/reference/base/types/mappable/) – Functor interface providing `map`.
* [`Appliable<WT, A>`](/reference/base/types/appliable/) – Applicative functor interface providing `applyTo`.
* [`Bindable<WT, A>`](/reference/base/types/bindable/) – Monadic bind interface providing `flatMap`.
* [`H2Monadic<WT, A, R>`](/reference/base/types/h2monadic/) – Rank-2 counterpart for binary type constructors ($F[A, R]$).
* [`UnsafeMonadic<WT, A>`](/reference/base/types/unsafemonadic/) – Monadic interface for computations throwing checked exceptions.
