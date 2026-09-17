---
title: Mappable3<WT, A1, A2, A3>
description: API reference for Mappable3, providing independent and simultaneous functor mappings across 3 type parameters in Foomp.
---

`Mappable3<WT, A1, A2, A3>` represents a **Trifunctor-like** interface in Foomp for structures carrying three independent type parameters (`A1`, `A2`, and `A3`) within a context identified by witness type `WT`.

It provides operations to transform individual components (`map1`, `map2`, `map3`) or all three components at once (`mapAll`).

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
public interface Mappable3<WT extends WitnessType, A1, A2, A3> {

    default <B1> @NonNull Mappable3<WT, B1, A2, A3> map1(
        final @NonNull Function<? super A1, ? extends B1> transformation
    ) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return this.mapAll(transformation, Fun.identity(), Fun.identity());
    }

    default <B2> @NonNull Mappable3<WT, A1, B2, A3> map2(
        final @NonNull Function<? super A2, ? extends B2> transformation
    ) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return this.mapAll(Fun.identity(), transformation, Fun.identity());
    }

    default <B3> @NonNull Mappable3<WT, A1, A2, B3> map3(
        final @NonNull Function<? super A3, ? extends B3> transformation
    ) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return this.mapAll(Fun.identity(), Fun.identity(), transformation);
    }

    <B1, B2, B3> Mappable3<WT, B1, B2, B3> mapAll(
        final @NonNull Function<? super A1, ? extends B1> transformation1,
        final @NonNull Function<? super A2, ? extends B2> transformation2,
        final @NonNull Function<? super A3, ? extends B3> transformation3
    );
}
```

---

## Type Parameters

| Parameter | Description |
| :--- | :--- |
| `<WT>` | The witness type identifying the 3-parameter structure context (must extend `WitnessType`). |
| `<A1>` | The first mappable type parameter. |
| `<A2>` | The second mappable type parameter. |
| `<A3>` | The third mappable type parameter. |

---

## Core Methods

### `map1`

```java
default <B1> @NonNull Mappable3<WT, B1, A2, A3> map1(
    final @NonNull Function<? super A1, ? extends B1> transformation
);
```

* **Purpose:** Transforms only the first type parameter $A_1 \to B_1$. `A2` and `A3` remain unchanged.
* **Default Implementation:** Calls `mapAll(transformation, Fun.identity(), Fun.identity())`.

---

### `map2`

```java
default <B2> @NonNull Mappable3<WT, A1, B2, A3> map2(
    final @NonNull Function<? super A2, ? extends B2> transformation
);
```

* **Purpose:** Transforms only the second type parameter $A_2 \to B_2$. `A1` and `A3` remain unchanged.
* **Default Implementation:** Calls `mapAll(Fun.identity(), transformation, Fun.identity())`.

---

### `map3`

```java
default <B3> @NonNull Mappable3<WT, A1, A2, B3> map3(
    final @NonNull Function<? super A3, ? extends B3> transformation
);
```

* **Purpose:** Transforms only the third type parameter $A_3 \to B_3$. `A1` and `A2` remain unchanged.
* **Default Implementation:** Calls `mapAll(Fun.identity(), Fun.identity(), transformation)`.

---

### `mapAll`

```java
<B1, B2, B3> Mappable3<WT, B1, B2, B3> mapAll(
    final @NonNull Function<? super A1, ? extends B1> transformation1,
    final @NonNull Function<? super A2, ? extends B2> transformation2,
    final @NonNull Function<? super A3, ? extends B3> transformation3
);
```

* **Purpose:** The SAM of `Mappable3`. Maps all three components simultaneously in one atomic step.
* **Null Safety:** None of the transformation functions may be `null`. Implementations must return a non-null instance.

---

## Implementations in Foomp

* **[`Tuple3<A1, A2, A3>`](/reference/base/util/tuple3/):** Maps across 3-element tuple fields.
* **[`Record3<A1, A2, A3>`](/reference/base/util/record3/):** Maps across 3-field immutable records (e.g. 3D spatial points, RGB color records).

---

## Related Types

* [`Mappable2`](/reference/base/types/mappable2/) — Bifunctor interface for 2 type parameters.
* [`Mappable4`](/reference/base/types/mappable4/) — Functor interface for 4 type parameters.
* [`Appliable3`](/reference/base/types/appliable3/) — Rank-3 Applicative Functor.
