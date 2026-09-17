---
title: Visitor<A>
description: API reference for Visitor, the functional visitor interface in Foomp.
---

The `Visitor<A>` functional interface encapsulates an operation or side effect to be performed on a visited element of type `A`.

It acts as the *Visitor* in the Visitor design pattern and complements [`Visitable<A>`](/reference/base/types/visitable/).

---

## Type Signature

```java
package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
public interface Visitor<A>
```

### Type Parameters
* `A`: The type of elements accepted and processed by this visitor.

---

## Method Summary

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `void` | `visit(@NonNull A visitable)` | Visits the given element and performs the visitor’s operation on it. |

---

## Core Characteristics

### 1. Single Abstract Method (SAM)
`Visitor<A>` is a `@FunctionalInterface` whose single abstract method is `void visit(@NonNull A visitable)`. It can be implemented with lambda expressions, method references, or dedicated stateful classes.

### 2. Side-Effecting & Stateful Operation
Visitors are commonly used for side-effecting operations (such as printing, logging, or writing to streams) or stateful accumulators (such as element counters, statistics aggregators, or node indexers).

### 3. Thread Safety & Idempotency
Unless documented otherwise by a concrete implementation, instances of `Visitor` are not required to be thread-safe or idempotent.

---

## Implementations & Usages in Foomp

* Interacts directly with [`Tree<A>`](/reference/base/types/tree/) and [`BinaryTree<A>`](/reference/base/types/binarytree/) via `welcome(visitor)`.
* Compatible with lambda consumers: `(element) -> process(element)`.

---

## See Also

* [`Visitable<A>`](/reference/base/types/visitable/) – Receiver interface accepting visitors.
* [`Receiver<A>`](/reference/base/functions/receiver/) – Fluent consumer interface.
* [`Tree<A>`](/reference/base/types/tree/) – Tree data structures that accept visitors.
