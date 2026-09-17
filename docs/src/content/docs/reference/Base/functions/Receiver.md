---
title: Receiver<A>
description: Formal API reference and specifications for the Receiver<A> enhanced consumer interface in Foomp.
---

`org.quurz.foomp.base.functions.Receiver<A>`

`Receiver<A>` is an enhanced consumer interface that processes input values of type `A` with optional pre-filtering and validation. It bridges Java's standard [`java.util.function.Consumer<A>`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/Consumer.html) with Foomp's functional ecosystem by implementing [`Fun<A, Nothing>`](/reference/base/functions/fun/), returning the canonical [`Nothing`](/reference/base/util/nothing/) unit value.

`Receiver<A>` provides fluent processing pipelines, conditional acceptance filters, custom exception triggers for invalid values, and bulk batch ingestion from varargs, iterators, collections, and streams.

```java
@FunctionalInterface
public interface Receiver<A>
    extends Fun<A, Nothing>,
            Consumer<A>
```

---

## Type Parameters

* `<A>` – The type of accepted input values.

---

## Static Factory Methods

### `receiver(Consumer<A>)`
```java
static <A> Receiver<A> receiver(@NonNull final Consumer<A> consumer)
```
Creates a new `Receiver` that unconditionally delegates to the given `Consumer`.
* **Parameters:** `consumer` – The consumer to delegate to; must not be `null`.
* **Returns:** A new `Receiver<A>` wrapping `consumer`.
* **Throws:** `NullPointerException` if `consumer` is `null`.

### `receiver(Consumer<A>, Predicate<A>)`
```java
static <A> Receiver<A> receiver(
    @NonNull final Consumer<A> consumer,
    @NonNull final Predicate<A> filter
)
```
Creates a filtered `Receiver` that only delegates incoming values to `consumer` if they satisfy the given `filter`. Values rejected by the filter are silently dropped.
* **Parameters:**
  * `consumer` – The consumer that processes accepted values; must not be `null`.
  * `filter` – The condition determining whether a value is processed; must not be `null`.
* **Returns:** A new filtered `Receiver<A>`.
* **Throws:** `NullPointerException` if `consumer` or `filter` is `null`.

### `receiver(Consumer<A>, Predicate<A>, Function<...>)`
```java
static <A> Receiver<A> receiver(
    @NonNull final Consumer<A> consumer,
    @NonNull final Predicate<A> filter,
    @NonNull final Function<? super A, ? extends RuntimeException> exceptionBuilder
)
```
Creates a validating `Receiver` that processes values matching `filter` and throws an exception constructed by `exceptionBuilder` when encountering rejected values.
* **Parameters:**
  * `consumer` – The consumer processing accepted values; must not be `null`.
  * `filter` – The acceptance condition; must not be `null`.
  * `exceptionBuilder` – Function building the runtime exception for rejected values; must not be `null` and must not return `null`.
* **Returns:** A new validating `Receiver<A>`.
* **Throws:**
  * `NullPointerException` if any parameter is `null` or if `exceptionBuilder` yields `null`.
  * `RuntimeException` (or subtype) if an incoming value fails the filter.

---

## Abstract Methods

### `accept`
```java
@Override
void accept(@NonNull final A value)
```
Processes the input value `value`. Inherited from [`Consumer<A>`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/Consumer.html).
* **Parameters:** `value` – The value to consume; must not be `null`.

---

## Functional Unification (`Fun<A, Nothing>`)

### `apply`
```java
@Override
@NonNull
default Nothing apply(@NonNull final A value)
```
Applies this receiver as a functional transformation, invoking `accept(value)` and returning the canonical unit instance [`Nothing.nothing`](/reference/base/util/nothing/).
* **Parameters:** `value` – The value to process; must not be `null`.
* **Returns:** The [`Nothing`](/reference/base/util/nothing/) singleton.
* **Throws:** `NullPointerException` if `value` is `null`.

---

## Fluent & Bulk Ingestion Methods

`Receiver<A>` provides methods that return `this` to enable fluent call chaining across single or multiple elements:

### `acceptAndContinue`
```java
@NonNull
default Receiver<A> acceptAndContinue(@NonNull final A value)
```
Consumes `value` and returns `this` for fluent chaining.
* **Throws:** `NullPointerException` if `value` is `null`.

### `acceptAllAndContinue` (Varargs)
```java
@NonNull
default Receiver<A> acceptAllAndContinue(
    @NonNull final A first,
    @NonNull final A... others
)
```
Processes `first` followed by all elements in `others`, returning `this`.
* **Throws:** `NullPointerException` if `first`, `others`, or any element in `others` is `null`.

### `acceptAllAndContinue` (Iterator)
```java
@NonNull
default Receiver<A> acceptAllAndContinue(@NonNull final Iterator<A> iterator)
```
Iterates through all elements in `iterator` and processes each element.
* **Throws:** `NullPointerException` if `iterator` is `null` or yields a `null` element.

### `acceptAllAndContinue` (Collection)
```java
@NonNull
default Receiver<A> acceptAllAndContinue(@NonNull final Collection<A> values)
```
Consumes all elements in `values`.
* **Throws:** `NullPointerException` if `values` is `null` or contains a `null` element.

### `acceptAllAndContinue` (Stream)
```java
@NonNull
default Receiver<A> acceptAllAndContinue(@NonNull final Stream<A> values)
```
Consumes all elements in `values` by executing `values.forEach(this)`.
* **Throws:** `NullPointerException` if `values` is `null` or contains a `null` element.

---

## Key Contracts & Design

### 1. Unified Side-Effect Representation
In pure functional programming, side-effecting operations without a meaningful return value map to $A \to ()$, where $()$ is the Unit type. By implementing `Fun<A, Nothing>`, `Receiver<A>` allows consumers to be passed directly to higher-order functional APIs requiring a `Fun<A, ?>`.

### 2. Guarded Ingestion
Using `Receiver.receiver(consumer, filter)` or `Receiver.receiver(consumer, filter, exceptionBuilder)`, validation and pre-filtering logic is encapsulated at construction time, keeping calling sites clean.

### 3. Strict Non-Null Guarantee
`Receiver` enforces strict non-null checks on all parameters and incoming elements, failing fast with a `NullPointerException` if `null` is encountered.

---

## Type Hierarchy

```
 java.util.function.Consumer<A>      Fun<A, Nothing>
                 ▲                          ▲
                 │                          │
                 └───────────┬──────────────┘
                             │
                        Receiver<A>
```

---

## See Also

* [Guide: Receiver in Practice](/guides/base/functions/receiver/) – Practical examples, event sinks, fluent pipelines, and validation.
* [`Provider<A>` Reference](/reference/base/functions/provider/) – Value supplier and monadic container.
* [`Fun<X, Y>` Reference](/reference/base/functions/fun/) – Unary functional interface.
* [`Nothing` Reference](/reference/base/util/nothing/) – Functional unit singleton.
* [Full JavaDoc: `Receiver`](/api/base/foomp.base/org/quurz/foomp/base/functions/Receiver.html)
