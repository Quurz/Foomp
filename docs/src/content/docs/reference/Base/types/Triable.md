---
title: Triable<A>
description: Functional abstraction for lazy or repeatable computations producing a value of type A or returning an Exception wrapped in XorValue.
---

`Triable<A>` is a functional interface in Foomp representing a computation that either successfully produces a value of type `A` or fails with an `Exception`.

## Overview

Unlike standard `Supplier<A>` (which cannot handle checked exceptions cleanly) or `Callable<A>` (which throws `Exception`), `Triable<A>` guarantees that its execution method **never throws**. Instead, failures are safely returned as an `XorValue<Exception, A>` where the left branch holds the `Exception` and the right branch holds the computed result `A`.

```java
package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
public interface Triable<A> {

    @NonNull XorValue<Exception, A> tryIt();
}
```

---

## Method Contract

### `tryIt()`
```java
@NonNull XorValue<Exception, A> tryIt();
```
* **Return Value:** A non-null `XorValue<Exception, A>`:
  * `Left(Exception)` if execution encounters any failure or thrown exception.
  * `Right(A)` if execution completes successfully.
* **Contract:** 
  * The method itself **must never throw** any checked or unchecked exceptions out of its call stack.
  * The returned `XorValue` must not be `null`.
  * Concrete implementations should document whether evaluation is lazy, eager, or cached/idempotent.

---

## Comparison with Similar Functional Types

| Interface | Throws Exceptions? | Return Type | Primary Purpose |
| :--- | :--- | :--- | :--- |
| **`Triable<A>`** | **Never throws** | `XorValue<Exception, A>` | Safe computation returning either failure or success. |
| **`Supplier<A>`** | Unchecked only | `A` | Standard value supplier. |
| **`UnsafeProvider<A>`** | `throws Exception` | `A` | Supplier throwing checked exceptions. |
| **`Attempt<A>`** | **Never throws** | `Attempt<A>` (evaluates to `Result<A>`) | Monadic, lazy failure-encapsulating workflow container. |

---

## Relationship with `Attempt<A>`

In Foomp, `Attempt<A>` implements `Triable<A>` alongside `Monadic<Attempt.µ, A>`. Calling `tryIt()` on an `Attempt` evaluates the underlying computation and yields the `XorValue` (or `Result`).
