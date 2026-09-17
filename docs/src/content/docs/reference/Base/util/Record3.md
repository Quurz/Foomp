---
title: Record3<A1, A2, A3>
description: API reference for Record3, an immutable, record-based 3-tuple with eager evaluation in Foomp.
---

`Record3<A1, A2, A3>` is an immutable, record-based 3-tuple holding three non-null values.

It provides component accessors (`get1`, `get2`, `get3`), functional updates (`with1`, `with2`, `with3`), eager component-wise mappings (`map1`, `map2`, `map3`, `mapAll`), applicative application (`applyTo`), conversion to `Tuple3`, and fluent transmogrification.

---

## Type Signature

```java
package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.*;
import org.quurz.foomp.higher.Higher3;
import org.quurz.foomp.higher.WitnessType;

public record Record3<A1, A2, A3>(A1 value1, A2 value2, A3 value3)
        implements Appliable3<Record3.µ, A1, A2, A3>,
                   Mappable3<Record3.µ, A1, A2, A3>,
                   Transmogrifyable<Record3<A1, A2, A3>>,
                   Value3<A1, A2, A3>,
                   Higher3<Record3.µ, A1, A2, A3>
```

### Type Parameters
* `A1`: The non-null type of the first component.
* `A2`: The non-null type of the second component.
* `A3`: The non-null type of the third component.

---

## Witness Type & Higher-Kinded Operations

```java
public static final class µ implements WitnessType { private µ() {} }
```

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A1, A2, A3> Record3<A1, A2, A3>` | `narrow(@NonNull Higher3<? extends Record3.µ, A1, A2, A3> wide)` | Safely downcasts a `Higher3` container to a concrete `Record3<A1, A2, A3>`. |

---

## Static Factory Methods

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A1, A2, A3> Record3<A1, A2, A3>` | `record3(@NonNull A1 value1, @NonNull A2 value2, @NonNull A3 value3)` | Creates a new `Record3` instance. Throws `NullPointerException` if any component is `null`. |

---

## Method Summary

### Value Access & Presence

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `boolean` | `isPresent()` | Always returns `true`. |
| `boolean` | `isPresent1()` | Always returns `true`. |
| `boolean` | `isPresent2()` | Always returns `true`. |
| `boolean` | `isPresent3()` | Always returns `true`. |
| `@NonNull A1` | `get()` | Alias for `get1()`. Returns the first component. |
| `@NonNull A1` | `get1()` | Returns the first component. |
| `@NonNull A2` | `get2()` | Returns the second component. |
| `@NonNull A3` | `get3()` | Returns the third component. |

### Structural Updates (Functional Wither Methods)

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <B1> Record3<B1, A2, A3>` | `with1(@NonNull B1 value)` | Creates a new `Record3` replacing the first component. |
| `@NonNull <B2> Record3<A1, B2, A3>` | `with2(@NonNull B2 value)` | Creates a new `Record3` replacing the second component. |
| `@NonNull <B3> Record3<A1, A2, B3>` | `with3(@NonNull B3 value)` | Creates a new `Record3` replacing the third component. |

### Eager Mappings & Applicative Application

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <B1> Record3<B1, A2, A3>` | `map1(@NonNull Function<? super A1, ? extends B1> transformation)` | Eagerly transforms the first component. |
| `@NonNull <B2> Record3<A1, B2, A3>` | `map2(@NonNull Function<? super A2, ? extends B2> transformation)` | Eagerly transforms the second component. |
| `@NonNull <B3> Record3<A1, A2, B3>` | `map3(@NonNull Function<? super A3, ? extends B3> transformation)` | Eagerly transforms the third component. |
| `@NonNull <B1, B2, B3> Record3<B1, B2, B3>` | `mapAll(@NonNull Function<? super A1, ? extends B1> t1, @NonNull Function<? super A2, ? extends B2> t2, @NonNull Function<? super A3, ? extends B3> t3)` | Eagerly transforms all three components simultaneously. |
| `@NonNull <B1, B2, B3> Record3<B1, B2, B3>` | `applyTo(@NonNull Higher3<? extends Record3.µ, ? extends Function<? super A1, ? extends B1>, ? extends Function<? super A2, ? extends B2>, ? extends Function<? super A3, ? extends B3>> transformation)` | Applies functions contained in another `Record3` container to this record's values. |

### Conversions & Transformations

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull Tuple3<A1, A2, A3>` | `toTuple()` | Converts this record into a lazy `Tuple3`. |
| `@NonNull <T> T` | `transmogrify(@NonNull Function<? super Record3<A1, A2, A3>, ? extends T> transmogrifier)` | Applies a transformation function to this record. |

---

## See Also

* [`Tuple3<A1, A2, A3>`](/reference/base/util/tuple3/) – Lazy 3-tuple implementation.
* [`Record2<A1, A2>`](/reference/base/util/record2/) – Record-based 2-tuple.
* [`Record4<A1, A2, A3, A4>`](/reference/base/util/record4/) – Record-based 4-tuple.
* [`Value3<A1, A2, A3>`](/reference/base/types/value3/) – Interface for three-element value containers.
