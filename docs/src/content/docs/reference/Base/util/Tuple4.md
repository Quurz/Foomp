---
title: Tuple4<A1, A2, A3, A4>
description: API reference for Tuple4, an immutable and lazy 4-element tuple container in Foomp.
---

`Tuple4<A1, A2, A3, A4>` is an immutable, lazy 4-tuple holding four non-null values `(A1, A2, A3, A4)`. Values are stored using `Supplier` functions and evaluated on demand when accessed.

---

## Type Signature

```java
package org.quurz.foomp.base.util;

public final class Tuple4<A1, A2, A3, A4>
        implements Appliable4<Tuple4.µ, A1, A2, A3, A4>,
                   Mappable4<Tuple4.µ, A1, A2, A3, A4>,
                   Unwindable<Tuple4<A1, A2, A3, A4>>,
                   Value4<A1, A2, A3, A4>,
                   Higher4<Tuple4.µ, A1, A2, A3, A4>
```

### Type Parameters
* `A1`: Type of the first value.
* `A2`: Type of the second value.
* `A3`: Type of the third value.
* `A4`: Type of the fourth value.

---

## Witness Type & Higher-Kinded Operations

```java
public static final class µ implements WitnessType { private µ() {} }
```

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A1, A2, A3, A4> Tuple4<A1, A2, A3, A4>` | `narrow(@NonNull Higher4<? extends Tuple4.µ, A1, A2, A3, A4> unfixed)` | Narrows a `Higher4` value to a concrete `Tuple4<A1, A2, A3, A4>` instance. Throws `IllegalArgumentException` if not a `Tuple4`. |

---

## Static Factory Methods

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A1, A2, A3, A4> Tuple4<A1, A2, A3, A4>` | `tuple4(@NonNull A1 value1, @NonNull A2 value2, @NonNull A3 value3, @NonNull A4 value4)` | Creates an immutable, lazy `Tuple4` storing values via suppliers. |

---

## Method Summary

### Value Access & Presence (`@UnwindingOperation`)

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull A1` | `get1()` | Evaluates and returns the first value. |
| `@NonNull A1` | `get()` | Alias for `get1()`. |
| `@NonNull A2` | `get2()` | Evaluates and returns the second value. |
| `@NonNull A3` | `get3()` | Evaluates and returns the third value. |
| `@NonNull A4` | `get4()` | Evaluates and returns the fourth value. |
| `boolean` | `isPresent1()` | Always returns `true`. |
| `boolean` | `isPresent2()` | Always returns `true`. |
| `boolean` | `isPresent3()` | Always returns `true`. |
| `boolean` | `isPresent4()` | Always returns `true`. |
| `boolean` | `isPresent()` | Always returns `true`. |

### Component Replacement (`with`)

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `<B1> Tuple4<B1, A2, A3, A4>` | `with1(@NonNull B1 newFirst)` | Returns a new tuple with the first value replaced; other values remain lazy. |
| `<B2> Tuple4<A1, B2, A3, A4>` | `with2(@NonNull B2 newSecond)` | Returns a new tuple with the second value replaced; other values remain lazy. |
| `<B3> Tuple4<A1, A2, B3, A4>` | `with3(@NonNull B3 newThird)` | Returns a new tuple with the third value replaced; other values remain lazy. |
| `<B4> Tuple4<A1, A2, A3, B4>` | `with4(@NonNull B4 newFourth)` | Returns a new tuple with the fourth value replaced; other values remain lazy. |

### Functor & Mapping Operations

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <B1> Tuple4<B1, A2, A3, A4>` | `map(@NonNull Function<? super A1, ? extends B1> transformation)` | Functor map (alias for `map1`): lazily transforms the first value. |
| `@NonNull <B1> Tuple4<B1, A2, A3, A4>` | `map1(@NonNull Function<? super A1, ? extends B1> transformation)` | Lazily transforms the first component. |
| `@NonNull <B2> Tuple4<A1, B2, A3, A4>` | `map2(@NonNull Function<? super A2, ? extends B2> transformation)` | Lazily transforms the second component. |
| `@NonNull <B3> Tuple4<A1, A2, B3, A4>` | `map3(@NonNull Function<? super A3, ? extends B3> transformation)` | Lazily transforms the third component. |
| `@NonNull <B4> Tuple4<A1, A2, A3, B4>` | `map4(@NonNull Function<? super A4, ? extends B4> transformation)` | Lazily transforms the fourth component. |
| `@NonNull <B1, B2, B3, B4> Tuple4<B1, B2, B3, B4>` | `mapAll(@NonNull Function<? super A1, ? extends B1> t1, @NonNull Function<? super A2, ? extends B2> t2, @NonNull Function<? super A3, ? extends B3> t3, @NonNull Function<? super A4, ? extends B4> t4)` | Lazily transforms all four components with independent functions. |
| `<B1> Tuple4<B1, A2, A3, A4>` | `mapTo1(@NonNull Fun4<? super A1, ? super A2, ? super A3, ? super A4, ? extends B1> mapper)` | Transforms the first component using all four values `(A1, A2, A3, A4) -> B1`. |
| `<B2> Tuple4<A1, B2, A3, A4>` | `mapTo2(@NonNull Fun4<? super A1, ? super A2, ? super A3, ? super A4, ? extends B2> mapper)` | Transforms the second component using all four values `(A1, A2, A3, A4) -> B2`. |
| `<B3> Tuple4<A1, A2, B3, A4>` | `mapTo3(@NonNull Fun4<? super A1, ? super A2, ? super A3, ? super A4, ? extends B3> mapper)` | Transforms the third component using all four values `(A1, A2, A3, A4) -> B3`. |
| `<B4> Tuple4<A1, A2, A3, B4>` | `mapTo4(@NonNull Fun4<? super A1, ? super A2, ? super A3, ? super A4, ? extends B4> mapper)` | Transforms the fourth component using all four values `(A1, A2, A3, A4) -> B4`. |

### Applicative & Structural Operations

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <B1, B2, B3, B4> Tuple4<B1, B2, B3, B4>` | `applyTo(@NonNull Higher4<µ, ? extends Function<? super A1, ? extends B1>, ? extends Function<? super A2, ? extends B2>, ? extends Function<? super A3, ? extends B3>, ? extends Function<? super A4, ? extends B4>> transformation)` | Applies functions carried by another `Tuple4` component-wise. |
| `<B> B` | `meld(@NonNull Fun4<? super A1, ? super A2, ? super A3, ? super A4, ? extends B> meld)` | Combines all four values into a single result using `meld` (evaluates all components). |

### Realization & Conversion (`@UnwindingOperation`)

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull Tuple4<A1, A2, A3, A4>` | `unwind1()` | Realizes only the first component while leaving others deferred. |
| `@NonNull Tuple4<A1, A2, A3, A4>` | `unwind2()` | Realizes only the second component while leaving others deferred. |
| `@NonNull Tuple4<A1, A2, A3, A4>` | `unwind3()` | Realizes only the third component while leaving others deferred. |
| `@NonNull Tuple4<A1, A2, A3, A4>` | `unwind4()` | Realizes only the fourth component while leaving others deferred. |
| `@NonNull Tuple4<A1, A2, A3, A4>` | `unwind()` | Realizes all four supplier-backed values. |
| `Record4<A1, A2, A3, A4>` | `toRecord()` | Converts this tuple to an eager, immutable [`Record4<A1, A2, A3, A4>`](/reference/base/util/record4/). |

---

## See Also

* [`Tuple2<A1, A2>`](/reference/base/util/tuple2/) – 2-element lazy immutable tuple.
* [`Tuple3<A1, A2, A3>`](/reference/base/util/tuple3/) – 3-element lazy immutable tuple.
* [`Record4<A1, A2, A3, A4>`](/reference/base/util/record4/) – Eager, immutable 4-value record.
