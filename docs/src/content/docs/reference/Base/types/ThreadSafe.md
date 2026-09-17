---
title: "@ThreadSafe"
description: Architectural marker annotation indicating that a type is intended to be safe for concurrent use across multiple threads.
---

The `@ThreadSafe` annotation is a source-level architectural marker in Foomp indicating that an annotated class, interface, record, or enum is designed to be safely accessed concurrently by multiple threads without external synchronization.

## Overview

In concurrent Java programming, documenting concurrency contracts and thread-safety invariants is vital for maintainability and correctness. `@ThreadSafe` serves as explicit code-level documentation of design intent.

```java
package org.quurz.foomp.base.types;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
public @interface ThreadSafe {}
```

:::note
Like `@Mutable`, `@ThreadSafe` has **source-level retention** (`RetentionPolicy.SOURCE`). It incurs zero runtime overhead, bytecode changes, or performance costs. It communicates thread-safety intent to developers, code reviewers, and static analysis tools.
:::

---

## Key Characteristics & Semantics

### 1. Architectural Intent
`@ThreadSafe` explicitly states that callers can share and invoke instances across threads without needing additional synchronization wrappers or external locks.

### 2. Implementation Strategies
Annotating a type with `@ThreadSafe` implies that the implementer guarantees thread safety through one or more established concurrency techniques:

* **Deep Immutability:** All fields are `final`, deeply immutable, and safely published (e.g. `SemVer`, `Tuple2`, `Maybe`).
* **Thread-Safe Data Structures / Atomic References:** Internal state is managed via `java.util.concurrent` primitives (e.g. `ConcurrentHashMap` in `MemoisingApplicable` / `MemoisingFun`).
* **Explicit Synchronization / Locks:** Internal state mutations are guarded by locks or synchronized blocks.
* **Stateless / Pure Functions:** The implementation holds no mutable state whatsoever.

### 3. Comparison with Related Annotations

| Annotation | Target | Meaning |
| :--- | :--- | :--- |
| **`@ThreadSafe`** | Type (`TYPE`) | Intended for safe multi-threaded usage and concurrent invocation. |
| **`@Mutable`** | Type (`TYPE`) | Documents that instances contain mutable internal or observable state. |
| **`@MutatingOperation`** | Method (`METHOD`) | Marks individual methods that cause in-place state mutations. |

---

## Best Practices

:::tip[Best Practices]
1. **Document Strategy in Javadoc:** Always describe the specific thread-safety mechanism (e.g. *"This class is thread-safe because it is deeply immutable."*) in the class Javadoc.
2. **Combine with Immutability by Default:** Whenever possible, prefer pure immutability for thread safety rather than complex locking.
3. **Be Cautious with Subclassing:** If a class marked `@ThreadSafe` is non-final, document whether subclasses must also preserve thread-safety invariants.
:::
