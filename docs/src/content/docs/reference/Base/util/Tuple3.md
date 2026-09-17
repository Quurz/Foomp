---
title: Tuple3<A1, A2, A3>
description: API reference for Tuple3, an immutable and lazy 3-element tuple container in Foomp.
---

`Tuple3<A1, A2, A3>` is an immutable, deferred 3-tuple holding three non-null values `(A1, A2, A3)` wrapped via `Supplier` instances. Components are evaluated on demand when accessed.

---

## Type Signature

```java
package org.quurz.foomp.base.util;

public final class Tuple3<A1, A2, A3>
        implements Appliable3<Tuple3.µ, A1, A2, A3>,
                   Mappable3<Tuple3.µ, A1, A2, A3>,
                   Unwindable<Tuple3<A1, A2, A3>>,
                   Value3<A1, A2, A3>,
                   Higher3<Tuple3.µ, A1, A2, A3>
```

### Type Parameters
* `A1`: Type of the first value.
* `A2`: Type of the second value.
* `A3`: Type of the third value.

---

## Witness Type & Higher-Kinded Operations

```java
public static final class µ implements WitnessType { private µ() {} }
```

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A1, A2, A3> Tuple3<A1, A2, A3>` | `narrow(@NonNull Higher3<? extends Tuple3.µ, A1, A2, A3> unfixed)` | Narrows a `Higher3` value to a concrete `Tuple3<A1, A2, A3>` instance. Throws `IllegalArgumentException` if not a `Tuple3`. |

---

## Static Factory Methods

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A1, A2, A3> Tuple3<A1, A2, A3>` | `tuple3(@NonNull A1 value1, @NonNull A2 value2, @NonNull A3 value3)` | Creates an immutable, lazy `Tuple3` storing values via suppliers. |

---

## Method Summary

### Value Access & Presence (`@UnwindingOperation`)

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull A1` | `get1()` | Evaluates and returns the first value. |
| `@NonNull A1` | `get()` | Alias for `get1()`. |
| `@NonNull A2` | `get2()` | Evaluates and returns the second value. |
| `@NonNull A3` | `get3()` | Evaluates and returns the third value. |
| `boolean` | `isPresent1()` | Always returns `true`. |
| `boolean` | `isPresent2()` | Always returns `true`. |
| `boolean` | `isPresent3()` | Always returns `true`. |
| `boolean` | `isPresent()` | Always returns `true`. |

### Component Replacement (`with`)

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `<B1> Tuple3<B1, A2, A3>` | `with1(@NonNull B1 newFirst)` | Returns a new tuple with the first value replaced; other values remain lazy. |
| `<B2> Tuple3<A1, B2, A3>` | `with2(@NonNull B2 newSecond)` | Returns a new tuple with the second value replaced; other values remain lazy. |
| `<B3> Tuple3<A1, A2, B3>` | `with3(@NonNull B3 newThird)` | Returns a new tuple with the third value replaced; other values remain lazy. |

### Functor & Mapping Operations

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <B1> Tuple3<B1, A2, A3>` | `map(@NonNull Function<? super A1, ? extends B1> transformation)` | Functor map (alias for `map1`): lazily transforms the first value. |
| `@NonNull <B1> Tuple3<B1, A2, A3>` | `map1(@NonNull Function<? super A1, ? extends B1> transformation)` | Lazily transforms the first component. |
| `@NonNull <B2> Tuple3<A1, B2, A3>` | `map2(@NonNull Function<? super A2, ? extends B2> transformation)` | Lazily transforms the second component. |
| `@NonNull <B3> Tuple3<A1, A2, B3>` | `map3(@NonNull Function<? super A3, ? extends B3> transformation)` | Lazily transforms the third component. |
| `@NonNull <B1, B2, B3> Tuple3<B1, B2, B3>` | `mapAll(@NonNull Function<? super A1, ? extends B1> t1, @NonNull Function<? super A2, ? extends B2> t2, @NonNull Function<? super A3, ? extends B3> t3)` | Lazily transforms all three components with independent functions. |
| `<B1> Tuple3<B1, A2, A3>` | `mapTo1(@NonNull Fun3<? super A1, ? super A2, ? super A3, ? extends B1> mapper)` | Transforms the first component using all three values `(A1, A2, A3) -> B1`. |
| `<B2> Tuple3<A1, B2, A3>` | `mapTo2(@NonNull Fun3<? super A1, ? super A2, ? super A3, ? extends B2> mapper)` | Transforms the second component using all three values `(A1, A2, A3) -> B2`. |
| `<B3> Tuple3<A1, A2, B3>` | `mapTo3(@NonNull Fun3<? super A1, ? super A2, ? super A3, ? extends B3> mapper)` | Transforms the third component using all three values `(A1, A2, A3) -> B3`. |

### Applicative & Structural Operations

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <B1, B2, B3> Tuple3<B1, B2, B3>` | `applyTo(@NonNull Higher3<µ, ? extends Function<? super A1, ? extends B1>, ? extends Function<? super A2, ? extends B2>, ? extends Function<? super A3, ? extends B3>> transformation)` | Applies functions carried by another `Tuple3` component-wise. |
| `<B> B` | `meld(@NonNull Fun3<? super A1, ? super A2, ? super A3, ? extends B> meld)` | Combines all three values into a single result using `meld` (evaluates all components). |

### Realization & Conversion (`@UnwindingOperation`)

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull Tuple3<A1, A2, A3>` | `unwind1()` | Realizes only the first component while leaving others deferred. |
| `@NonNull Tuple3<A1, A2, A3>` | `unwind2()` | Realizes only the second component while leaving others deferred. |
| `@NonNull Tuple3<A1, A2, A3>` | `unwind3()` | Realizes only the third component while leaving others deferred. |
| `@NonNull Tuple3<A1, A2, A3>` | `unwind()` | Realizes all three supplier-backed values. |
| `Record3<A1, A2, A3>` | `toRecord()` | Converts this tuple to an eager, immutable [`Record3<A1, A2, A3>`](/reference/base/util/record3/). |

---

## See Also

* [`Tuple2<A1, A2>`](/reference/base/util/tuple2/) – 2-element lazy immutable tuple.
* [`Tuple4<A1, A2, A3, A4>`](/reference/base/util/tuple4/) – 4-element lazy immutable tuple.
* [`Record3<A1, A2, A3>`](/reference/base/util/record3/) – Eager, immutable 3-value record.
