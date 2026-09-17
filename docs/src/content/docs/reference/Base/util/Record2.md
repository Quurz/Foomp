---
title: Record2<A1, A2>
description: API reference for Record2, an immutable, record-based 2-tuple with eager evaluation in Foomp.
---

`Record2<A1, A2>` is an immutable, record-based 2-tuple holding two non-null values.

It provides component accessors (`get1`, `get2`), functional updates (`with1`, `with2`), eager component-wise mappings (`map1`, `map2`, `mapAll`), applicative application (`applyTo`), conversion to `Tuple2`, and fluent transmogrification.

---

## Type Signature

```java
package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.*;
import org.quurz.foomp.higher.Higher2;
import org.quurz.foomp.higher.WitnessType;

public record Record2<A1, A2>(A1 value1, A2 value2)
        implements Appliable2<Record2.µ, A1, A2>,
                   Mappable2<Record2.µ, A1, A2>,
                   Transmogrifyable<Record2<A1, A2>>,
                   Value2<A1, A2>,
                   Higher2<Record2.µ, A1, A2>
```

### Type Parameters
* `A1`: The non-null type of the first component.
* `A2`: The non-null type of the second component.

---

## Witness Type & Higher-Kinded Operations

```java
public static final class µ implements WitnessType { private µ() {} }
```

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A1, A2> Record2<A1, A2>` | `narrow(@NonNull Higher2<? extends Record2.µ, A1, A2> wide)` | Safely downcasts a `Higher2` container to a concrete `Record2<A1, A2>`. |

---

## Static Factory Methods

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A1, A2> Record2<A1, A2>` | `record2(@NonNull A1 value1, @NonNull A2 value2)` | Creates a new `Record2` instance. Throws `NullPointerException` if any component is `null`. |

---

## Method Summary

### Value Access & Presence

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `boolean` | `isPresent()` | Always returns `true`. |
| `boolean` | `isPresent1()` | Always returns `true`. |
| `boolean` | `isPresent2()` | Always returns `true`. |
| `@NonNull A1` | `get()` | Alias for `get1()`. Returns the first component. |
| `@NonNull A1` | `get1()` | Returns the first component. |
| `@NonNull A2` | `get2()` | Returns the second component. |

### Structural Updates (Functional Wither Methods)

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <B1> Record2<B1, A2>` | `with1(@NonNull B1 value)` | Creates a new `Record2` replacing the first component. |
| `@NonNull <B2> Record2<A1, B2>` | `with2(@NonNull B2 value)` | Creates a new `Record2` replacing the second component. |

### Eager Mappings & Applicative Application

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <B1> Record2<B1, A2>` | `map1(@NonNull Function<? super A1, ? extends B1> transformation)` | Eagerly transforms the first component. |
| `@NonNull <B2> Record2<A1, B2>` | `map2(@NonNull Function<? super A2, ? extends B2> transformation)` | Eagerly transforms the second component. |
| `@NonNull <B1, B2> Record2<B1, B2>` | `mapAll(@NonNull Function<? super A1, ? extends B1> t1, @NonNull Function<? super A2, ? extends B2> t2)` | Eagerly transforms both components simultaneously. |
| `@NonNull <B1, B2> Record2<B1, B2>` | `applyTo(@NonNull Higher2<? extends Record2.µ, ? extends Function<? super A1, ? extends B1>, ? extends Function<? super A2, ? extends B2>> transformation)` | Applies functions contained in another `Record2` container to this record's values. |

### Conversions & Transformations

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull Tuple2<A1, A2>` | `toTuple()` | Converts this record into a lazy `Tuple2`. |
| `@NonNull <T> T` | `transmogrify(@NonNull Function<? super Record2<A1, A2>, ? extends T> transmogrifier)` | Applies a transformation function to this record. |

---

## See Also

* [`Tuple2<A1, A2>`](/reference/base/util/tuple2/) – Lazy 2-tuple implementation.
* [`Pair<A1, A2>`](/reference/base/util/pair/) – Mutable 2-tuple with nullable values.
* [`Record3<A1, A2, A3>`](/reference/base/util/record3/) – Record-based 3-tuple.
* [`Record4<A1, A2, A3, A4>`](/reference/base/util/record4/) – Record-based 4-tuple.
