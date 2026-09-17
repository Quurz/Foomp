---
title: Nothing
description: API reference for Nothing, a singleton unit type representation in Foomp.
---

`Nothing` represents the unit type (an empty tuple or single-state value) in Foomp.

Because Java lacks a built-in `Unit` type and `null` is unsafe, `Nothing` provides a concrete, non-null singleton value representing "no data" or "nothingness".

---

## Type Signature

```java
package org.quurz.foomp.base.util;

public final class Nothing
        implements Transmogrifyable<Nothing>
```

---

## Constant / Singleton Instance

```java
public static final Nothing nothing = new Nothing();
```

---

## Method Summary

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <T> T` | `transmogrify(@NonNull Function<? super Nothing, ? extends T> transmogrifier)` | Applies a transformation function to this `Nothing` instance (*ex nihilo* value generation). |
| `boolean` | `equals(Object obj)` | Returns `true` if `obj` is an instance of `Nothing` (since `Nothing` is a singleton). |
| `int` | `hashCode()` | Returns `0`. |
| `String` | `toString()` | Returns `"Nothing"`. |

---

## See Also

* [`Maybe.None`](/reference/base/util/maybe/) – Typed representation of an absent value.
* [`Transmogrifyable<A>`](/reference/base/types/transmogrifyable/) – Contract for transforming objects via higher-order functions.
