---
title: Mappable2<WT, A1, A2>
description: API reference for Mappable2, enabling independent and simultaneous functor mappings over two type parameters in Foomp.
---

`Mappable2<WT, A1, A2>` represents a **Bifunctor-like** interface in Foomp for structures carrying two distinct type parameters (`A1` and `A2`) within a context identified by witness type `WT`.

It provides operations to transform the first parameter independently (`map1`), the second parameter independently (`map2`), or both parameters simultaneously in a single step (`mapAll`).

---

## Interface Definition

```java
package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.higher.WitnessType;

import java.util.Objects;
import java.util.function.Function;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

@FunctionalInterface
public interface Mappable2<WT extends WitnessType, A1, A2> {

    default <B1> @NonNull Mappable2<WT, B1, A2> map(final @NonNull Function<? super A1, ? extends B1> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return this.mapAll(transformation, Fun.identity());
    }

    default <B1> @NonNull Mappable2<WT, B1, A2> map1(final @NonNull Function<? super A1, ? extends B1> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return this.map(transformation);
    }

    default <B2> @NonNull Mappable2<WT, A1, B2> map2(final @NonNull Function<? super A2, ? extends B2> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return this.mapAll(Fun.identity(), transformation);
    }

    <B1, B2> @NonNull Mappable2<WT, B1, B2> mapAll(
        final @NonNull Function<? super A1, ? extends B1> transformation1,
        final @NonNull Function<? super A2, ? extends B2> transformation2
    );
}
```

---

## Type Parameters

| Parameter | Description |
| :--- | :--- |
| `<WT>` | The witness type identifying the two-parameter container structure (must extend `WitnessType`). |
| `<A1>` | The first mappable type parameter. |
| `<A2>` | The second mappable type parameter. |

---

## Core Methods

### `map1` (and alias `map`)

```java
default <B1> @NonNull Mappable2<WT, B1, A2> map1(
    final @NonNull Function<? super A1, ? extends B1> transformation
);
```

* **Purpose:** Transforms only the first type parameter $A_1 \to B_1$ while keeping the second parameter $A_2$ intact.
* **Default Implementation:** Delegates to `mapAll(transformation, Fun.identity())`.

---

### `map2`

```java
default <B2> @NonNull Mappable2<WT, A1, B2> map2(
    final @NonNull Function<? super A2, ? extends B2> transformation
);
```

* **Purpose:** Transforms only the second type parameter $A_2 \to B_2$ while keeping the first parameter $A_1$ intact.
* **Default Implementation:** Delegates to `mapAll(Fun.identity(), transformation)`.

---

### `mapAll`

```java
<B1, B2> @NonNull Mappable2<WT, B1, B2> mapAll(
    final @NonNull Function<? super A1, ? extends B1> transformation1,
    final @NonNull Function<? super A2, ? extends B2> transformation2
);
```

* **Purpose:** The single abstract method (SAM) of `Mappable2`. Applies `transformation1` to `A1` and `transformation2` to `A2` simultaneously.
* **Null Safety:** Neither transformation function may be `null`. Implementations must return a non-null instance.

---

## Implementations in Foomp

* **[`Pair<A, B>`](/reference/base/util/pair/):** Transforms product elements $(a, b) \to (f(a), g(b))$.
* **[`Tuple2<A1, A2>`](/reference/base/util/tuple2/):** Maps over 2-ary tuples.
* **[`Record2<A1, A2>`](/reference/base/util/record2/):** Maps over 2-field immutable records.
* **[`Either<L, R>`](/reference/base/util/either/):** Maps over disjoint union values (`map1` for Left, `map2` for Right).

---

## Related Types

* [`Mappable`](/reference/base/types/mappable/) — Rank-1 Functor for single-parameter structures.
* [`Mappable3`](/reference/base/types/mappable3/) — Independent/joint mapping over three type parameters.
* [`Mappable4`](/reference/base/types/mappable4/) — Independent/joint mapping over four type parameters.
* [`Appliable2`](/reference/base/types/appliable2/) — Rank-2 Applicative Functor.
