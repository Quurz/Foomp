---
title: "@UnwindingOperation"
description: API reference for the @UnwindingOperation architectural marker annotation in Foomp.
---

`@UnwindingOperation` is a source-level documentation marker annotation used to explicitly flag methods that trigger the immediate evaluation or materialization of deferred or lazy computations.

---

## Type Signature

```java
package org.quurz.foomp.base.types;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Documented
public @interface UnwindingOperation
```

---

## Annotation Elements

| Type | Element | Default | Description |
| :--- | :--- | :--- | :--- |
| `String` | `comment()` | `""` | Optional descriptive note providing additional context about the evaluation or materialization mechanism. |

---

## Core Characteristics

### 1. Source Retention
`@UnwindingOperation` has `RetentionPolicy.SOURCE`. It generates zero runtime overhead and does not affect runtime execution or bytecode performance.

### 2. Explicit Evaluation Points
In functional architectures with lazy sequences, thunks, or state transformers, identifying when a computation actually executes can be non-trivial. This annotation makes evaluation boundaries visually apparent during code reviews, IDE inspections, and static code analysis.

### 3. Evaluation Now vs. Laziness
* **Denotes:** *Evaluation Now* – methods that actively force computations (e.g., `unwind()`, `runState(...)`, `getValue()`).
* **Does NOT Denote:** Lazy definition methods that merely construct or defer computations (such as `attempt()`, `box()`, or `defer()`).

---

## Usage Example

```java
public interface LazyPipeline<A> {

    // Defers transformation - NOT an unwinding operation
    LazyPipeline<A> filter(Pred<? super A> predicate);

    // Forces evaluation of the pipeline - IS an unwinding operation
    @UnwindingOperation(comment = "Executes the filter pipeline and materializes results into an immutable list")
    List<A> toList();
}
```

---

## See Also

* [`Unwindable<SELF>`](/reference/base/types/unwindable/) – Interface standardizing the `unwind()` method.
* [`@MutatingOperation`](/reference/base/types/mutatingoperation/) – Marker annotation for methods that mutate internal state.
* [`@ThreadSafe`](/reference/base/types/threadsafe/) – Architectural marker for thread-safe types.
