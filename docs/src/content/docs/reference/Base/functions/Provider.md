---
title: Provider<A>
description: Formal API reference and specifications for the Provider<A> monadic value supplier interface in Foomp.
---

`org.quurz.foomp.base.functions.Provider<A>`

`Provider<A>` represents a functional value container and supplier yielding non-null values of type `A` ($() \to A$). Combining the capabilities of a lazy value supplier with full monadic and functor semantics (`Monadic`, `Higher1`, `Fun<Nothing, A>`, `Value<A>`, `Unwindable`, `Transmogrifyable`), `Provider<A>` provides a composable functional foundation for deferred computations, environment injection, and higher-kinded pipelines.

```java
@FunctionalInterface
public interface Provider<A>
    extends Monadic<Provider.µ, A>,
            Unwindable<Provider<A>>,
            Transmogrifyable<Provider<A>>,
            Fun<Nothing, A>,
            Value<A>,
            Higher1<Provider.µ, A>
```

---

## Type Parameters & Witness

* `<A>` – The type of the provided value.

### Witness Type (`Provider.µ`)
```java
final class µ implements WitnessType { private µ() {} }
```
Acts as the type-level witness representing the higher-kinded type constructor `Provider<_>` within Foomp's HKT framework (`Higher1<Provider.µ, A>`).

---

## Static Factory Methods

### `provider`
```java
static <A> Provider<A> provider(@NonNull final A value)
```
Creates a constant `Provider` that unconditionally yields `value`.
* **Parameters:** `value` – The value to supply; must not be `null`.
* **Returns:** A `Provider<A>` wrapping `value`.
* **Throws:** `NullPointerException` if `value` is `null`.

### `providerFrom`
```java
static <A> Provider<A> providerFrom(@NonNull final Supplier<A> supplier)
```
Wraps a standard Java [`Supplier<A>`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/Supplier.html) into a `Provider<A>`, enforcing non-null guarantees at evaluation time.
* **Parameters:** `supplier` – The source supplier; must not be `null`.
* **Returns:** A lazy `Provider<A>` invoking `supplier.get()`.
* **Throws:** `NullPointerException` if `supplier` is `null` or yields `null` on evaluation.

### `narrow`
```java
static <A> Provider<A> narrow(@NonNull final Higher1<? extends Provider.µ, A> wide)
```
Safely casts and narrows a generic `Higher1<Provider.µ, A>` token back to its concrete `Provider<A>` representation.
* **Parameters:** `wide` – The higher-kinded value to narrow; must not be `null`.
* **Returns:** The narrowed `Provider<A>`.
* **Throws:**
  * `NullPointerException` if `wide` is `null`.
  * `IllegalArgumentException` if `wide` is not an instance of `Provider`.

---

## Abstract Methods

### `get`
```java
@Override
@UnwindingOperation
@Pure
@NonNull
A get()
```
Evaluates or unwinds the provider and returns the underlying value.
* **Returns:** The provided non-null value of type `A`.

---

## Monadic & Functional Operations

### `map` (Functor)
```java
@Override
@NonNull
default <B> Provider<B> map(@NonNull final Function<? super A, ? extends B> transformation)
```
Transforms the value produced by this provider using `transformation`, returning a new lazy `Provider<B>`.
* **Parameters:** `transformation` – The mapping function; must not be `null` and must not return `null`.
* **Returns:** A new `Provider<B>` yielding the transformed value.
* **Throws:** `NullPointerException` if `transformation` is `null` or returns `null` when evaluated.

### `flatMap` (Monad)
```java
@Override
@NonNull
default <B> Provider<B> flatMap(@NonNull final Function<? super A, ? extends Higher1<? extends µ, B>> transformation)
```
Sequences a monadic computation, applying `transformation` to the provided value and flattening the resulting `Provider<B>`.
* **Parameters:** `transformation` – Monadic transformation function returning a `Higher1<Provider.µ, B>`; must not be `null`.
* **Returns:** A flattened `Provider<B>`.
* **Throws:** `NullPointerException` if `transformation` is `null` or produces a `null` provider/value.

### `applyTo` (Applicative)
```java
@Override
@NonNull
default <B> Provider<B> applyTo(
    @NonNull final Higher1<? extends Provider.µ, ? extends Function<? super A, ? extends B>> transformation
)
```
Lifts a provider carrying a function and applies it to this provider's value.
* **Parameters:** `transformation` – A higher-kinded provider carrying a function.
* **Returns:** A new `Provider<B>` with the function applied.

---

## Utilities & Functional Bridges

### `apply` (`Fun<Nothing, A>`)
```java
@Override
@UnwindingOperation
@Pure
@NonNull
default A apply(@NonNull final Nothing nothing)
```
Evaluates this provider as a zero-arity functional mapping ($() \to A$) using the [`Nothing`](/reference/base/util/nothing/) unit placeholder.
* **Throws:** `NullPointerException` if `nothing` is `null`.

### `isPresent` (`Value<A>`)
```java
@Override
default boolean isPresent()
```
Returns `true` always, as `Provider` by definition guarantees the availability of a value.

### `unwind`
```java
@Override
@UnwindingOperation
@NonNull
default Provider<A> unwind()
```
Eagerly evaluates this provider once and returns a constant `Provider<A>` memoizing the evaluated value.

### `transmogrify`
```java
@Override
@NonNull
default <T> T transmogrify(@NonNull final Function<? super Provider<A>, ? extends T> transmogrifier)
```
Applies an external transformer function directly to `this`, enabling fluent builder pipelines.

---

## Type Hierarchy

```
               Supplier<A>          Higher1<Provider.µ, A>
                   ▲                          ▲
                   │                          │
 Value<A>     Monadic<Provider.µ, A>   Fun<Nothing, A>
     ▲                 ▲                      ▲
     │                 │                      │
     └─────────────────┼──────────────────────┘
                       │
                  Provider<A>
```

---

## See Also

* [Guide: Working with Provider](/guides/base/functions/provider/) – Lazy evaluation, monadic chaining, and dependency injection recipes.
* [`UnsafeProvider<A>` Reference](/reference/base/functions/unsafeprovider/) – Throwing supplier interface for checked exceptions.
* [`Receiver<A>` Reference](/reference/base/functions/receiver/) – Functional consumer counterpart.
* [`Nothing` Reference](/reference/base/util/nothing/) – Zero-argument unit representation.
* [Full JavaDoc: `Provider`](/api/base/foomp.base/org/quurz/foomp/base/functions/Provider.html)
