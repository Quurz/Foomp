---
title: Applicable<X, Y>
description: Reference documentation for the Applicable functional interface in Foomp Base.
---

`org.quurz.foomp.base.functions.Applicable<X, Y>`

`Applicable` is the fundamental single-argument functional interface in Foomp designed to execute an operation on an input of type `X` and produce a result of type `Y`. 

Unlike standard Java's `java.util.function.Function<T, R>`, **`Applicable` allows checked exceptions** to be thrown during execution, making it the primary bridge between standard Java I/O / reflection / legacy APIs and modern functional programming.

```java
@FunctionalInterface
public interface Applicable<X, Y>
```

---

## Type Parameters

| Parameter | Description |
| :--- | :--- |
| `<X>` | The type of the input argument. |
| `<Y>` | The type of the resulting value. |

---

## Method Summary

### Core Execution

#### `apply`
```java
Y apply(@NonNull final X x) throws Exception
```
Executes the operation with the provided input argument `x`.
* **Throws:** `Exception` if the underlying computation fails.
* **Contract:** Neither `x` nor the returned result may be `null`. Passing `null` or returning `null` from an implementation causes a `NullPointerException`.
* **Purity:** Implementations may perform side effects; purity/referential transparency is **not** required.

---

### Factory & Helper Methods

#### `applicable` (Static Factory)
```java
static <X, Y> Applicable<X, Y> applicable(final @NonNull Function<X, Y> function)
```
Wraps a standard `java.util.function.Function<X, Y>` into an `Applicable<X, Y>`. Enforces non-null input and non-null output checks at runtime.

#### `identity` (Static Factory)
```java
static <X> Applicable<X, X> identity()
```
Returns an identity operation that returns its input parameter unchanged.

---

### Safe Transformation

#### `safe`
```java
default Fun<X, ? extends XorValue<Exception, Y>> safe()
```
Transforms this throwing `Applicable` into a pure, non-throwing `Fun<X, XorValue<Exception, Y>>`.
* If `apply(x)` succeeds, the returned function produces `XorValue.right(Y)`.
* If `apply(x)` throws an `Exception`, the exception is caught and returned as `XorValue.left(Exception)`.
* Underlying side effects still occur when evaluated.

---

## Contract & Semantics

1. **Null Safety Contract:**
   All inputs and returned values must be `@NonNull`. Foomp strictly enforces non-null contracts to eliminate silent `NullPointerException` bugs.

2. **Checked Exception Handling:**
   Standard Java streams and lambdas reject operations throwing checked exceptions (e.g., `IOException`, `SQLException`, `ParseException`). `Applicable` natively declares `throws Exception`, allowing seamless lambda expressions without verbose `try-catch` blocks inside lambdas.

3. **Referential Transparency & Side Effects:**
   `Applicable` does not require operations to be pure. If an `Applicable` is pure, it can be safely wrapped with `MemoisingApplicable` to cache results.

---

## Hierarchy & Related Types

```
                    ┌─────────────────────────┐
                    │    Applicable<X, Y>     │
                    │  (throws Exception)     │
                    └────────────┬────────────┘
                                 │
           ┌─────────────────────┴─────────────────────┐
           ▼                                           ▼
┌─────────────────────────┐                 ┌─────────────────────────┐
│       Fun<X, Y>         │                 │ MemoisingApplicable<X,Y>│
│ (extends Function<X,Y>, │                 │   (Thread-safe cache    │
│  no checked exceptions) │                 │  for results/exceptions)│
└─────────────────────────┘                 └─────────────────────────┘
```

* **`Fun<X, Y>`:** Extends both `Applicable<X, Y>` and `java.util.function.Function<X, Y>`. `Fun` refines `apply` to guarantee that no checked exceptions are thrown, and adds combinators like `compose`, `andThen`, and `async()`.
* **`MemoisingApplicable<X, Y>`:** Caches both successful return values and thrown exceptions in a thread-safe `ConcurrentHashMap`.
* **`UnsafeProvider<A>`:** Specialization representing a zero-argument supplier permitted to throw exceptions, extending `Applicable<Nothing, A>`.
* **`Attempt<A>` / `Result<A>`:** Monadic containers in `foomp.base.util` providing `mapUnsafe` and `flatMapUnsafe` accepting `Applicable`.

---

## See Also

* [Guide: Working with Applicable](/guides/base/functions/applicable/) – Practical recipes and usage examples.
* [`Fun<X, Y>` Reference](/reference/base/functions/fun/) – Exception-free functional abstraction.
* [`MemoisingApplicable<X, Y>` Reference](/reference/base/functions/memoisingapplicable/) – Thread-safe result caching.
* [Full JavaDoc: `org.quurz.foomp.base.functions.Applicable`](/api/base/foomp.base/org/quurz/foomp/base/functions/Applicable.html)
