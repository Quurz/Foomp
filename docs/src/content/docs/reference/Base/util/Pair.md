---
title: Pair<A1, A2>
description: API reference for Pair, a mutable 2-tuple structure with nullable values in Foomp.
---

`Pair<A1, A2>` is a mutable data structure holding two nullable values of arbitrary types.

Unlike immutable tuples or records, `Pair` explicitly supports nullability, in-place mutation (`set1`, `set2`), copy semantics (`copy()`), and functional replacement (`with1`, `with2`). It can also be converted safely into a tuple of optional values via `toTuple()`.

---

## Type Signature

```java
package org.quurz.foomp.base.util;

import org.quurz.foomp.base.types.Copyable;
import org.quurz.foomp.base.types.Mutable;
import org.quurz.foomp.base.types.Value2;

@Mutable
public final class Pair<A1, A2>
        implements Copyable<Pair<A1, A2>>,
                   Value2<A1, A2>
```

### Type Parameters
* `A1`: The type of the first value (may be nullable).
* `A2`: The type of the second value (may be nullable).

---

## Static Factory Methods

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A1, A2> Pair<A1, A2>` | `pair(@Nullable A1 value1, @Nullable A2 value2)` | Creates a new mutable `Pair` holding the specified values. |

---

## Method Summary

### Value Access & Presence

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `boolean` | `isPresent()` | Alias for `isPresent1()`. Returns `true` if `value1` is not `null`. |
| `boolean` | `isPresent1()` | Returns `true` if `value1` is not `null`, `false` otherwise. |
| `boolean` | `isPresent2()` | Returns `true` if `value2` is not `null`, `false` otherwise. |
| `@Nullable A1` | `get()` | Alias for `get1()`. Returns the first value. |
| `@Nullable A1` | `get1()` | Returns the first value. |
| `@Nullable A2` | `get2()` | Returns the second value. |

### In-Place Mutation & Functional Updates

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `void` | `set1(@Nullable A1 value)` | Mutates the first value in place (`@MutatingOperation`). |
| `void` | `set2(@Nullable A2 value)` | Mutates the second value in place (`@MutatingOperation`). |
| `Pair<A1, A2>` | `with1(@Nullable A1 value)` | Functional update: creates a new `Pair` with the given first value and the current second value. |
| `Pair<A1, A2>` | `with2(@Nullable A2 value)` | Functional update: creates a new `Pair` with the current first value and the given second value. |

### Conversions & Object Methods

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull Tuple2<Maybe<A1>, Maybe<A2>>` | `toTuple()` | Converts this `Pair` into an immutable `Tuple2` containing `Maybe<A1>` and `Maybe<A2>`. |
| `@NonNull Pair<A1, A2>` | `copy()` | Creates a copy of this `Pair`. If component values implement [`Copyable`](/reference/base/types/copyable/), a deep copy is performed; otherwise a shallow copy is used. |
| `boolean` | `equals(Object o)` | Compares this pair with another object based on component values (using `Objects.equals`). |
| `int` | `hashCode()` | Returns the hash code computed from both component values. |
| `String` | `toString()` | Returns a string representation formatted as `Pair[value1=..., value2=...]`. |

---

## See Also

* [`Tuple2<A1, A2>`](/reference/base/util/tuple2/) – Immutable 2-tuple with lazy transformations.
* [`Record2<A1, A2>`](/reference/base/util/record2/) – Immutable Java record-based 2-tuple.
* [`Value2<A1, A2>`](/reference/base/types/value2/) – Interface for two-element value containers.
* [`Copyable<SELF>`](/reference/base/types/copyable/) – Interface for deep-copyable structures.
