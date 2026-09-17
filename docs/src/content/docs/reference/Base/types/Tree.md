---
title: Tree<A>
description: Generic, immutable hierarchical tree interface with visitor traversal, balancing, insertion strategies, and safe search.
---

`Tree<A>` is the fundamental interface in Foomp representing generic, immutable, hierarchical tree structures holding elements of type `A`.

## Overview

Unlike mutable collection trees in standard Java libraries, `Tree<A>` is designed around persistent data structure principles: operations like insertion and deletion return new tree instances while leaving original trees untouched. Furthermore, `Tree<A>` implements `Visitable<Tree<A>>`, providing externalized traversals via the Visitor pattern.

```java
package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.List;
import java.util.NoSuchElementException;

public interface Tree<A> extends Visitable<Tree<A>> {

    enum InsertionStrategy { Discard, Replace }

    boolean isNode();
    default boolean isLeaf() { return !this.isNode(); }
    int height();

    @NonNull Tree<A> insert(final @NonNull A element);
    boolean contains(final @NonNull A element);
    @NonNull A search(final @NonNull A element) throws NoSuchElementException;
    @NonNull Value<A> searchSafe(final @NonNull A element);
    @NonNull List<? extends Tree<A>> children();
    @NonNull Tree<A> remove(final @NonNull A element);
}
```

---

## Nested Types: `Tree.InsertionStrategy`

When inserting an element that is considered equal/duplicate according to the tree's comparison logic, `InsertionStrategy` dictates the behavior:

| Strategy | Description |
| :--- | :--- |
| **`Discard`** | Keeps the existing element and discards the new one. |
| **`Replace`** | Replaces the existing element with the newly inserted element. |

---

## Core Methods & Contract

### 1. Structural Inspection
* **`isNode()`**: Returns `true` if this instance contains an element and child subtrees; `false` if it is an empty/terminal leaf.
* **`isLeaf()`**: Default method returning `!isNode()`.
* **`height()`**: Returns the height of the tree (number of nodes on longest path down to a leaf; empty/terminal is 0).
* **`children()`**: Returns a non-null `List<? extends Tree<A>>` of immediate child subtrees (empty for terminal leaves).

### 2. Persistent Mutation Operations
* **`insert(element)`**: Returns a new `Tree<A>` containing the given non-null `element`. Throws `NullPointerException` if `element` is `null`.
* **`remove(element)`**: Returns a new `Tree<A>` without the given `element`. Throws `NullPointerException` if `element` is `null`.

### 3. Search & Lookup
* **`contains(element)`**: Returns `true` if `element` is present in the tree; `false` otherwise.
* **`search(element)`**: Returns the stored element equivalent to the searched `element`. Throws `NoSuchElementException` if not found.
* **`searchSafe(element)`**: Safe lookup returning a `Value<A>` (containing the element if present, or an empty `Value` if absent).

### 4. Visitor Support (`Visitable`)
* **`welcome(visitor)`**: Applies the given `Visitor<? super Tree<A>>` to this tree node and returns the same visitor for fluent chaining.

---

## Common Implementations in Foomp

* **`BinaryTree<A>`**: Binary tree specialization supporting left/right child navigation.
* **`AVLTree<A>`**: Self-balancing binary search tree maintaining AVL balance factors.
* **`RedBlackTree<A>`**: Self-balancing binary search tree with red-black coloring invariants.
