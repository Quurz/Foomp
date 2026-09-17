---
title: UnsafeProvider<A>
description: Formal API reference and specifications for the UnsafeProvider<A> checked throwing supplier interface in Foomp.
---

`org.quurz.foomp.base.functions.UnsafeProvider<A>`

`UnsafeProvider<A>` is a functional supplier interface yielding values of type `A` that is explicitly permitted to throw checked exceptions ($() \to A \text{ throws Exception}$). It extends [`Applicable<Nothing, A>`](/reference/base/functions/applicable/), integrating checked-exception suppliers into Foomp's functional architecture and allowing seamless transformation into safe, exception-encapsulating types via `.safe()`.

`UnsafeProvider<A>` is commonly used for deferred I/O, file reading, socket communication, database queries, reflection, and resource acquisition.

```java
@FunctionalInterface
public interface UnsafeProvider<A>
    extends Applicable<Nothing, A>
```

---

## Type Parameters

* `<A>` – The type of the result produced by `get()`.

---

## Abstract Methods

### `get`
```java
A get() throws Exception
```
Performs the computation or resource acquisition and returns the result.
* **Returns:** The computed value of type `A`.
* **Throws:** `Exception` if an error occurs during computation.

---

## Functional Unification (`Applicable<Nothing, A>`)

### `apply`
```java
@Override
default A apply(@NonNull final Nothing nothing) throws Exception
```
Applies this supplier to the given [`Nothing`](/reference/base/util/nothing/) unit placeholder by invoking `get()`.
* **Parameters:** `nothing` – The [`Nothing`](/reference/base/util/nothing/) singleton; must not be `null`.
* **Returns:** The computation result from `get()`.
* **Throws:**
  * `NullPointerException` if `nothing` is `null`.
  * `Exception` if `get()` throws an exception.

---

## Inherited Combinators from `Applicable`

Because `UnsafeProvider<A>` extends [`Applicable<Nothing, A>`](/reference/base/functions/applicable/), it inherits all functional adaptation combinators:

### `safe`
```java
default Fun<Nothing, XorValue<Exception, A>> safe()
```
Adapts this throwing provider into an exception-safe pure function returning an [`XorValue<Exception, A>`](/reference/base/util/xorvalue/) (`Left` on exception, `Right` on success).

### `memoise`
```java
default MemoisingApplicable<Nothing, A> memoise()
```
Wraps this provider into a thread-safe caching wrapper [`MemoisingApplicable<Nothing, A>`](/reference/base/functions/memoisingapplicable/), caching successful results.

---

## Key Contracts & Design

### 1. Checked Exception Permissiveness
Unlike standard Java [`Supplier<A>`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/Supplier.html), which disallows throwing checked exceptions inside lambdas, `UnsafeProvider<A>` allows lambdas and method references with `throws Exception` or specific checked subtypes (`IOException`, `SQLException`, etc.) to be written cleanly without boilerplate `try-catch` blocks.

### 2. Functional Adaptation
By implementing `Applicable<Nothing, A>`, `UnsafeProvider` can be passed to higher-order combinators, transformed via `.safe()` into safe functional pipelines, or integrated with container types like `Attempt` or `Task`.

---

## Type Hierarchy

```
         Applicable<Nothing, A>
                   ▲
                   │
           UnsafeProvider<A>
```

---

## See Also

* [Guide: UnsafeProvider in Practice](/guides/base/functions/unsafeprovider/) – Recipes for checked I/O suppliers, safe adaptation, and retry mechanisms.
* [`Provider<A>` Reference](/reference/base/functions/provider/) – Non-throwing, monadic value supplier.
* [`Applicable<X, Y>` Reference](/reference/base/functions/applicable/) – Core throwing function interface.
* [`Nothing` Reference](/reference/base/util/nothing/) – Zero-argument unit placeholder.
* [Full JavaDoc: `UnsafeProvider`](/api/base/foomp.base/org/quurz/foomp/base/functions/UnsafeProvider.html)
