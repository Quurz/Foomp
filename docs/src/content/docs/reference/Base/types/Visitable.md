---
title: Visitable<A>
description: API reference for Visitable, the receiver interface in Foomp's Visitor design pattern.
---

The `Visitable<A>` functional interface defines elements or data structures that can be visited by a [`Visitor<? super A>`](/reference/base/types/visitor/).

It enables externalising operations over elements (such as tree traversal, serialization, counting, or formatting) while keeping the element’s internal structure decoupled and immutable.

---

## Type Signature

```java
package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
public interface Visitable<A>
```

### Type Parameters
* `A`: The type of the element being visited.

---

## Method Summary

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <V extends Visitor<? super A>> V` | `welcome(@NonNull V visitor)` | Accepts the given visitor, applies it to this element, and returns the same visitor instance for fluent chaining. |

---

## Core Characteristics

### 1. Classic Visitor Pattern Receiver
`Visitable` serves as the *Element* in the Gang of Four Visitor pattern. An element implementing `Visitable<A>` accepts a visitor via `welcome(visitor)` and invokes `visitor.visit(...)` on itself or its components.

### 2. Fluent Visitor Chaining
`welcome` is generic in the visitor type `<V extends Visitor<? super A>>` and returns the exact visitor passed in (`@NonNull V`). This allows callers to fluently initialize, run, and extract results or side effects from stateful visitors in a single expression:

```java
CountingVisitor visitor = tree.welcome(new CountingVisitor());
int totalNodes = visitor.getCount();
```

### 3. Strict Null-Safety
Passing a `null` visitor to `welcome` results in an immediate `NullPointerException`. The returned visitor instance is guaranteed `@NonNull`.

---

## Implementations in Foomp

* [`Tree<A>`](/reference/base/types/tree/) – Immutable tree hierarchy interface (`AVLTree`, `RedBlackTree`).
* [`BinaryTree<A>`](/reference/base/types/binarytree/) – Dual-branch binary tree nodes.

---

## See Also

* [`Visitor<A>`](/reference/base/types/visitor/) – The complementary visitor operation interface.
* [`Tree<A>`](/reference/base/types/tree/) – Tree data structure implementing `Visitable`.
* [`Receiver<A>`](/reference/base/functions/receiver/) – Consumer interface for side-effecting sinks.
