---
title: Record4<A1, A2, A3, A4>
description: API reference for Record4, an immutable, record-based 4-tuple with eager evaluation in Foomp.
---

`Record4<A1, A2, A3, A4>` is an immutable, record-based 4-tuple holding four non-null values.

It provides component accessors (`get1`, `get2`, `get3`, `get4`), functional updates (`with1`, `with2`, `with3`, `with4`), eager component-wise mappings (`map1`, `map2`, `map3`, `map4`, `mapAll`), applicative application (`applyTo`), conversion to `Tuple4`, and fluent transmogrification.

---

## Type Signature

```java
package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.*;
import org.quurz.foomp.higher.Higher4;
import org.quurz.foomp.higher.WitnessType;

public record Record4<A1, A2, A3, A4>(A1 value1, A2 value2, A3 value3, A4 value4)
        implements Appliable4<Record4.µ, A1, A2, A3, A4>,
                   Mappable4<Record4.µ, A1, A2, A3, A4>,
                   Transmogrifyable<Record4<A1, A2, A3, A4>>,
                   Value4<A1, A2, A3, A4>,
                   Higher4<Record4.µ, A1, A2, A3, A4>
```

### Type Parameters
* `A1`: The non-null type of the first component.
* `A2`: The non-null type of the second component.
* `A3`: The non-null type of the third component.
* `A4`: The non-null type of the fourth component.

---

## Witness Type & Higher-Kinded Operations

```java
public static final class µ implements WitnessType { private µ() {} }
```

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A1, A2, A3, A4> Record4<A1, A2, A3, A4>` | `narrow(@NonNull Higher4<? extends Record4.µ, A1, A2, A3, A4> wide)` | Safely downcasts a `Higher4` container to a concrete `Record4<A1, A2, A3, A4>`. |

---

## Static Factory Methods

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A1, A2, A3, A4> Record4<A1, A2, A3, A4>` | `record4(@NonNull A1 value1, @NonNull A2 value2, @NonNull A3 value3, @NonNull A4 value4)` | Creates a new `Record4` instance. Throws `NullPointerException` if any component is `null`. |

---

## Method Summary

### Value Access & Presence

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `boolean` | `isPresent()` | Always returns `true`. |
| `boolean` | `isPresent1()` | Always returns `true`. |
| `boolean` | `isPresent2()` | Always returns `true`. |
| `boolean` | `isPresent3()` | Always returns `true`. |
| `boolean` | `isPresent4()` | Always returns `true`. |
| `@NonNull A1` | `get()` | Alias for `get1()`. Returns the first component. |
| `@NonNull A1` | `get1()` | Returns the first component. |
| `@NonNull A2` | `get2()` | Returns the second component. |
| `@NonNull A3` | `get3()` | Returns the third component. |
| `@NonNull A4` | `get4()` | Returns the fourth component. |

### Structural Updates (Functional Wither Methods)

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <B1> Record4<B1, A2, A3, A4>` | `with1(@NonNull B1 value)` | Creates a new `Record4` replacing the first component. |
| `@NonNull <B2> Record4<A1, B2, A3, A4>` | `with2(@NonNull B2 value)` | Creates a new `Record4` replacing the second component. |
| `@NonNull <B3> Record4<A1, A2, B3, A4>` | `with3(@NonNull B3 value)` | Creates a new `Record4` replacing the third component. |
| `@NonNull <B4> Record4<A1, A2, A3, B4>` | `with4(@NonNull B4 value)` | Creates a new `Record4` replacing the fourth component. |

### Eager Mappings & Applicative Application

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <B1> Record4<B1, A2, A3, A4>` | `map1(@NonNull Function<? super A1, ? extends B1> transformation)` | Eagerly transforms the first component. |
| `@NonNull <B2> Record4<A1, B2, A3, A4>` | `map2(@NonNull Function<? super A2, ? extends B2> transformation)` | Eagerly transforms the second component. |
| `@NonNull <B3> Record4<A1, A2, B3, A4>` | `map3(@NonNull Function<? super A3, ? extends B3> transformation)` | Eagerly transforms the third component. |
| `@NonNull <B4> Record4<A1, A2, A3, B4>` | `map4(@NonNull Function<? super A4, ? extends B4> transformation)` | Eagerly transforms the fourth component. |
| `@NonNull <B1, B2, B3, B4> Record4<B1, B2, B3, B4>` | `mapAll(@NonNull Function<? super A1, ? extends B1> t1, @NonNull Function<? super A2, ? extends B2> t2, @NonNull Function<? super A3, ? extends B3> t3, @NonNull Function<? super A4, ? extends B4> t4)` | Eagerly transforms all four components simultaneously. |
| `@NonNull <B1, B2, B3, B4> Record4<B1, B2, B3, B4>` | `applyTo(@NonNull Higher4<? extends Record4.µ, ? extends Function<? super A1, ? extends B1>, ? extends Function<? super A2, ? extends B2>, ? extends Function<? super A3, ? extends B3>, ? extends Function<? super A4, ? extends B4>> transformation)` | Applies functions contained in another `Record4` container to this record's values. |

### Conversions & Transformations

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull Tuple4<A1, A2, A3, A4>` | `toTuple()` | Converts this record into a lazy `Tuple4`. |
| `@NonNull <T> T` | `transmogrify(@NonNull Function<? super Record4<A1, A2, A3, A4>, ? extends T> transmogrifier)` | Applies a transformation function to this record. |

---

## See Also

* [`Tuple4<A1, A2, A3, A4>`](/reference/base/util/tuple4/) – Lazy 4-tuple implementation.
* [`Record2<A1, A2>`](/reference/base/util/record2/) – Record-based 2-tuple.
* [`Record3<A1, A2, A3>`](/reference/base/util/record3/) – Record-based 3-tuple.
* [`Value4<A1, A2, A3, A4>`](/reference/base/types/value4/) – Interface for four-element value containers.
