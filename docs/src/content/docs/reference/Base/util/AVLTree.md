---
title: AVLTree<A>
description: API reference for AVLTree, the persistent self-balancing binary search tree in Foomp.
---

`AVLTree<A>` is an immutable, persistent, self-balancing binary search tree.

It maintains the strict AVL invariant (the heights of the two child subtrees of any node differ by at most one), guaranteeing $O(\log n)$ worst-case time complexity for lookup, insertion, and deletion.

---

## Type Signature

```java
package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.BinaryTree;
import org.quurz.foomp.base.types.Echo;
import org.quurz.foomp.base.types.Transmogrifyable;
import org.quurz.foomp.base.types.Tree.InsertionStrategy;

public abstract sealed class AVLTree<A>
        implements BinaryTree<A>,
                   Transmogrifyable<AVLTree<A>>,
                   Echo
        permits AVLTree.Node, AVLTree.Leaf
```

### Type Parameters
* `A`: The element type stored within the tree.

---

## Static Factory Methods

### Empty Tree Creation

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A extends Comparable<A>> AVLTree<A>` | `avlTree()` | Creates an empty AVL tree using natural ordering (`Comparable`). |
| `static <A extends Comparable<A>> AVLTree<A>` | `avlTree(InsertionStrategy insertionStrategy)` | Creates an empty AVL tree with the specified duplicate strategy (`Discard` or `Replace`). |
| `static <A> AVLTree<A>` | `avlTree(Comparator<? super A> comparator)` | Creates an empty AVL tree with a custom comparator. |
| `static <A> AVLTree<A>` | `avlTree(InsertionStrategy strategy, Comparator<? super A> comparator)` | Creates an empty AVL tree with a custom strategy and comparator. |

### Varargs & Collection Factories

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A extends Comparable<A>> AVLTree<A>` | `avlTreeOf(A... elements)` | Creates an AVL tree populated with the given elements in natural order. |
| `static <A> AVLTree<A>` | `avlTreeOf(Comparator<? super A> comparator, A... elements)` | Creates an AVL tree populated with elements using a custom comparator. |
| `static <A extends Comparable<A>> AVLTree<A>` | `avlTreeFrom(Collection<A> elements)` | Creates an AVL tree from a Java `Collection`. |
| `static <A> AVLTree<A>` | `avlTreeFrom(Comparator<? super A> comparator, Collection<A> elements)` | Creates an AVL tree from a collection with a custom comparator. |
| `static <A extends Comparable<A>> AVLTree<A>` | `avlTreeFrom(Stream<A> elements)` | Creates an AVL tree by consuming a `Stream`. |
| `static <A> Collector<A, Set<A>, AVLTree<A>>` | `collectToAVLTree(InsertionStrategy strategy, Comparator<? super A> comparator)` | Returns a Java Stream `Collector` that accumulates elements into an `AVLTree`. |

---

## Method Summary

### Tree Queries & Navigation

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `int` | `height()` | Returns the height of the tree (or subtree). `0` for empty leaf nodes. |
| `boolean` | `isNode()` | Returns `true` if this instance is an internal node (`Node<A>`), or `false` if it is a leaf (`Leaf<A>`). |
| `@NonNull A` | `element()` | Returns the element stored at the root of this node. Throws `NoSuchElementException` on a leaf. |
| `@NonNull Maybe<A>` | `elementSafe()` | Returns `Just(element)` if a node, or `Nothing` on a leaf. |
| `@NonNull AVLTree<A>` | `left()` | Returns the left child subtree. Throws `NoSuchElementException` on a leaf. |
| `@NonNull AVLTree<A>` | `right()` | Returns the right child subtree. Throws `NoSuchElementException` on a leaf. |
| `@NonNull List<AVLTree<A>>` | `children()` | Returns an immutable list containing the non-empty child subtrees. |

### Search & Verification

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `boolean` | `contains(@NonNull A element)` | Returns `true` if the element is present in the tree. |
| `@NonNull A` | `search(@NonNull A element)` | Finds and returns the matching element. Throws `NoSuchElementException` if absent. |
| `@NonNull Maybe<A>` | `searchSafe(@NonNull A element)` | Finds the matching element safely, returning `Just(found)` or `Nothing`. |

### Persistent Mutations & Merging

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull AVLTree<A>` | `insert(@NonNull A element)` | Returns a new balanced `AVLTree` containing the inserted element. |
| `@NonNull AVLTree<A>` | `remove(@NonNull A element)` | Returns a new balanced `AVLTree` with the element removed. |
| `@NonNull AVLTree<A>` | `merge(@NonNull BinaryTree<A> other)` | Merges all elements from another binary tree into a new balanced `AVLTree`. |

### Formatting & Visiting

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull String` | `echo()` | Generates a formatted multi-line ASCII diagram representing the tree structure and heights. |
| `<V extends Visitor<? super A>> V` | `welcome(@NonNull V visitor)` | Traverses the tree in-order, applying the visitor to every element. |
| `<T> T` | `transmogrify(Function<? super AVLTree<A>, ? extends T> transmogrifier)` | Applies an arbitrary transformation function to this tree. |

---

## Invariants & Algorithmic Guarantees

1. **Strict Balance Invariant:** The balance factor ($\text{height}(\text{left}) - \text{height}(\text{right})$) is strictly maintained within $\{-1, 0, 1\}$.
2. **Rebalancing Rotations:** Left, right, left-right, and right-left rotations are performed automatically during `insert` and `remove` operations.
3. **Persistence (Immutability):** All mutating operations use structural sharing, leaving the original tree unmodified and thread-safe.

---

## See Also

* [`Tree<A>`](/reference/base/types/tree/) – Root interface for hierarchical tree structures.
* [`BinaryTree<A>`](/reference/base/types/binarytree/) – Binary tree contract.
* [`Echo`](/reference/base/types/echo/) – Structural formatting interface providing `.echo()`.
* [`Visitable<A>`](/reference/base/types/visitable/) – Visitor acceptor interface implemented by trees.
