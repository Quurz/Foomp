---
title: Tuple2<A1, A2>
description: API reference for Tuple2, an immutable and lazy 2-element tuple container in Foomp.
---

`Tuple2<A1, A2>` is an immutable, lazy 2-tuple that holds two non-null values `(A1, A2)`. Both components are held internally via `Supplier<T>` instances to enable deferred evaluation: values are evaluated on demand when accessed.

---

## Type Signature

```java
package org.quurz.foomp.base.util;

public final class Tuple2<A1, A2>
        implements Mappable2<Tuple2.µ, A1, A2>,
                   Appliable2<Tuple2.µ, A1, A2>,
                   Swappable<Tuple2<A2, A1>, A1, A2>,
                   Unwindable<Tuple2<A1, A2>>,
                   Value2<A1, A2>,
                   Higher2<Tuple2.µ, A1, A2>
```

### Type Parameters
* `A1`: Type of the first value.
* `A2`: Type of the second value.

---

## Witness Type & Higher-Kinded Operations

```java
public static final class µ implements WitnessType { private µ() {} }
```

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A1, A2> Tuple2<A1, A2>` | `narrow(@NonNull Higher2<? extends Tuple2.µ, A1, A2> unfixed)` | Narrows a `Higher2` value to a concrete `Tuple2<A1, A2>` instance. Throws `IllegalArgumentException` if not a `Tuple2`. |

---

## Static Factory Methods

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A1, A2> Tuple2<A1, A2>` | `tuple2(@NonNull A1 value1, @NonNull A2 value2)` | Creates an immutable, lazy `Tuple2` storing both values via suppliers. |

---

## Method Summary

### Value Access & Presence (`@UnwindingOperation`)

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull A1` | `get1()` | Evaluates and returns the first value. |
| `@NonNull A1` | `get()` | Alias for `get1()`. |
| `@NonNull A2` | `get2()` | Evaluates and returns the second value. |
| `boolean` | `isPresent1()` | Always returns `true`. |
| `boolean` | `isPresent2()` | Always returns `true`. |
| `boolean` | `isPresent()` | Always returns `true`. |

### Component Replacement (`with`)

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `<B1> Tuple2<B1, A2>` | `with1(@NonNull B1 newFirst)` | Returns a new tuple with the first value replaced; the second value remains lazy. |
| `<B2> Tuple2<A1, B2>` | `with2(@NonNull B2 newSecond)` | Returns a new tuple with the second value replaced; the first value remains lazy. |

### Functor & Mapping Operations

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <B1> Tuple2<B1, A2>` | `map(@NonNull Function<? super A1, ? extends B1> transformation)` | Functor map (alias for `map1`): lazily transforms the first value. |
| `@NonNull <B1> Tuple2<B1, A2>` | `map1(@NonNull Function<? super A1, ? extends B1> transformation)` | Lazily transforms the first component using the provided function. |
| `@NonNull <B2> Tuple2<A1, B2>` | `map2(@NonNull Function<? super A2, ? extends B2> transformation)` | Lazily transforms the second component using the provided function. |
| `@NonNull <B1, B2> Tuple2<B1, B2>` | `mapAll(@NonNull Function<? super A1, ? extends B1> t1, @NonNull Function<? super A2, ? extends B2> t2)` | Lazily transforms both components with independent functions. |
| `<B1> Tuple2<B1, A2>` | `mapTo1(@NonNull BiFunction<? super A1, ? super A2, ? extends B1> mapper)` | Transforms the first component using both components `(A1, A2) -> B1`. |
| `<B1> Tuple2<B1, A2>` | `mapTo1(@NonNull Function<? super A2, ? extends B1> mapper)` | Transforms the first component using only the second component `A2 -> B1`. |
| `<B2> Tuple2<A1, B2>` | `mapTo2(@NonNull BiFunction<? super A1, ? super A2, ? extends B2> mapper)` | Transforms the second component using both components `(A1, A2) -> B2`. |
| `<B2> Tuple2<A1, B2>` | `mapTo2(@NonNull Function<? super A1, ? extends B2> mapper)` | Transforms the second component using only the first component `A1 -> B2`. |

### Applicative & Structural Operations

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <B1, B2> Tuple2<B1, B2>` | `applyTo(@NonNull Higher2<µ, ? extends Function<? super A1, ? extends B1>, ? extends Function<? super A2, ? extends B2>> transformation)` | Applies functions carried by another `Tuple2` component-wise. |
| `@NonNull Tuple2<A2, A1>` | `swap()` | Swaps the positions of the two components lazily. |
| `<B> B` | `meld(@NonNull BiFunction<? super A1, ? super A2, ? extends B> meld)` | Combines both values into a single result using `meld` (evaluates both components). |

### Realization & Conversion (`@UnwindingOperation`)

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull Tuple2<A1, A2>` | `unwind1()` | Realizes only the first component while leaving the second deferred. |
| `@NonNull Tuple2<A1, A2>` | `unwind2()` | Realizes only the second component while leaving the first deferred. |
| `@NonNull Tuple2<A1, A2>` | `unwind()` | Realizes both supplier-backed values. |
| `Record2<A1, A2>` | `toRecord()` | Converts this tuple to an eager, immutable [`Record2<A1, A2>`](/reference/base/util/record2/). |
| `Pair<A1, A2>` | `toPair()` | Converts this tuple to a mutable [`Pair<A1, A2>`](/reference/base/util/pair/). |

---

## See Also

* [`Tuple3<A1, A2, A3>`](/reference/base/util/tuple3/) – 3-element lazy immutable tuple.
* [`Tuple4<A1, A2, A3, A4>`](/reference/base/util/tuple4/) – 4-element lazy immutable tuple.
* [`Record2<A1, A2>`](/reference/base/util/record2/) – Eager, immutable 2-value record.
* [`Pair<A, B>`](/reference/base/util/pair/) – Mutable 2-value container.
