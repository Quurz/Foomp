---
title: Mappable4<WT, A1, A2, A3, A4>
description: API reference for Mappable4, providing independent and simultaneous functor mappings across 4 type parameters in Foomp.
---

`Mappable4<WT, A1, A2, A3, A4>` represents a **Quadrifunctor-like** interface in Foomp for structures carrying four distinct type parameters (`A1`, `A2`, `A3`, and `A4`) within a context identified by witness type `WT`.

It provides independent methods to transform each individual component (`map1`, `map2`, `map3`, `map4`) or map all four components simultaneously (`mapAll`).

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
public interface Mappable4<WT extends WitnessType, A1, A2, A3, A4> {

    default <B1> @NonNull Mappable4<WT, B1, A2, A3, A4> map1(
        final @NonNull Function<? super A1, ? extends B1> transformation
    ) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return this.mapAll(transformation, Fun.identity(), Fun.identity(), Fun.identity());
    }

    default <B2> @NonNull Mappable4<WT, A1, B2, A3, A4> map2(
        final @NonNull Function<? super A2, ? extends B2> transformation
    ) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return this.mapAll(Fun.identity(), transformation, Fun.identity(), Fun.identity());
    }

    default <B3> @NonNull Mappable4<WT, A1, A2, B3, A4> map3(
        final @NonNull Function<? super A3, ? extends B3> transformation
    ) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return this.mapAll(Fun.identity(), Fun.identity(), transformation, Fun.identity());
    }

    default <B4> @NonNull Mappable4<WT, A1, A2, A3, B4> map4(
        final @NonNull Function<? super A4, ? extends B4> transformation
    ) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return this.mapAll(Fun.identity(), Fun.identity(), Fun.identity(), transformation);
    }

    <B1, B2, B3, B4> @NonNull Mappable4<WT, B1, B2, B3, B4> mapAll(
        final @NonNull Function<? super A1, ? extends B1> transformation1,
        final @NonNull Function<? super A2, ? extends B2> transformation2,
        final @NonNull Function<? super A3, ? extends B3> transformation3,
        final @NonNull Function<? super A4, ? extends B4> transformation4
    );
}
```

---

## Type Parameters

| Parameter | Description |
| :--- | :--- |
| `<WT>` | The witness type identifying the 4-parameter structure context (must extend `WitnessType`). |
| `<A1>` | The first mappable type parameter. |
| `<A2>` | The second mappable type parameter. |
| `<A3>` | The third mappable type parameter. |
| `<A4>` | The fourth mappable type parameter. |

---

## Core Methods

### `map1`

```java
default <B1> @NonNull Mappable4<WT, B1, A2, A3, A4> map1(
    final @NonNull Function<? super A1, ? extends B1> transformation
);
```

* **Purpose:** Maps $A_1 \to B_1$. `A2`, `A3`, `A4` remain unchanged.

---

### `map2`

```java
default <B2> @NonNull Mappable4<WT, A1, B2, A3, A4> map2(
    final @NonNull Function<? super A2, ? extends B2> transformation
);
```

* **Purpose:** Maps $A_2 \to B_2$. `A1`, `A3`, `A4` remain unchanged.

---

### `map3`

```java
default <B3> @NonNull Mappable4<WT, A1, A2, B3, A4> map3(
    final @NonNull Function<? super A3, ? extends B3> transformation
);
```

* **Purpose:** Maps $A_3 \to B_3$. `A1`, `A2`, `A4` remain unchanged.

---

### `map4`

```java
default <B4> @NonNull Mappable4<WT, A1, A2, A3, B4> map4(
    final @NonNull Function<? super A4, ? extends B4> transformation
);
```

* **Purpose:** Maps $A_4 \to B_4$. `A1`, `A2`, `A3` remain unchanged.

---

### `mapAll`

```java
<B1, B2, B3, B4> @NonNull Mappable4<WT, B1, B2, B3, B4> mapAll(
    final @NonNull Function<? super A1, ? extends B1> transformation1,
    final @NonNull Function<? super A2, ? extends B2> transformation2,
    final @NonNull Function<? super A3, ? extends B3> transformation3,
    final @NonNull Function<? super A4, ? extends B4> transformation4
);
```

* **Purpose:** The SAM of `Mappable4`. Applies all 4 transformation functions in a single atomic operation.
* **Null Safety:** None of the transformation functions may be `null`. Implementations must return a non-null instance.

---

## Implementations in Foomp

* **[`Tuple4<A1, A2, A3, A4>`](/reference/base/util/tuple4/):** 4-element heterogenous product type.
* **[`Record4<A1, A2, A3, A4>`](/reference/base/util/record4/):** 4-field immutable record structure (e.g. RGBA color vectors, 4D spacetime coordinates, audit logs).

---

## Related Types

* [`Mappable3`](/reference/base/types/mappable3/) — Functor interface for 3 type parameters.
* [`Appliable4`](/reference/base/types/appliable4/) — Rank-4 Applicative Functor.
