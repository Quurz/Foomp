---
title: RedBlackTree<A>
description: API reference for RedBlackTree, a persistent self-balancing Red-Black binary search tree in Foomp.
---

`RedBlackTree<A>` is a persistent, functional, self-balancing binary search tree based on Chris Okasaki's approach.

It guarantees $O(\log n)$ time complexity for lookup, insertion, and deletion operations while maintaining immutability and structural sharing (Copy-on-Write semantics).

---

## Type Signature

```java
package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.BinaryTree;
import org.quurz.foomp.base.types.Echo;
import org.quurz.foomp.base.types.Transmogrifyable;

public abstract sealed class RedBlackTree<A>
        implements BinaryTree<A>,
                   Transmogrifyable<RedBlackTree<A>>,
                   Echo
        permits RedBlackTree.Node,
                RedBlackTree.Leaf
```

### Type Parameters
* `A`: The type of elements maintained by the tree.

---

## Static Factory Methods

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A extends Comparable<A>> RedBlackTree<A>` | `redBlackTree()` | Creates an empty Red-Black Tree using natural order and `Discard` insertion strategy. |
| `static <A extends Comparable<A>> RedBlackTree<A>` | `redBlackTree(InsertionStrategy insertionStrategy)` | Creates an empty Red-Black Tree using natural order and the specified insertion strategy. |
| `static <A> RedBlackTree<A>` | `redBlackTree(Comparator<? super A> comparator)` | Creates an empty Red-Black Tree using the specified comparator and `Discard` insertion strategy. |
| `static <A> RedBlackTree<A>` | `redBlackTree(InsertionStrategy insertionStrategy, Comparator<? super A> comparator)` | Creates an empty Red-Black Tree with the given strategy and comparator. |
| `static <A extends Comparable<A>> RedBlackTree<A>` | `redBlackTree(A... elements)` | Creates a Red-Black Tree containing the specified elements with natural order. |
| `static <A> RedBlackTree<A>` | `redBlackTree(Comparator<? super A> comparator, A... elements)` | Creates a Red-Black Tree containing the specified elements with the given comparator. |
| `static <A extends Comparable<A>> RedBlackTree<A>` | `redBlackTree(Iterable<? extends A> elements)` | Creates a Red-Black Tree populated from an `Iterable`. |
| `static <A> RedBlackTree<A>` | `redBlackTree(Comparator<? super A> comparator, Iterable<? extends A> elements)` | Creates a Red-Black Tree populated from an `Iterable` with the given comparator. |
| `static <A extends Comparable<A>> Collector<A, ?, RedBlackTree<A>>` | `toRedBlackTree()` | Returns a `Collector` that accumulates elements into a `RedBlackTree` with natural order. |
| `static <A> Collector<A, ?, RedBlackTree<A>>` | `toRedBlackTree(Comparator<? super A> comparator)` | Returns a `Collector` that accumulates elements into a `RedBlackTree` using the specified comparator. |

---

## Method Summary

### Tree Queries & Properties

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `boolean` | `isEmpty()` | Returns `true` if this tree contains no elements. |
| `int` | `size()` | Returns the total number of elements in the tree. |
| `int` | `height()` | Returns the height of the tree. |
| `int` | `blackHeight()` | Returns the black height (number of black nodes along any path to a leaf). |
| `boolean` | `contains(@NonNull A value)` | Returns `true` if the specified value is present in the tree. |
| `boolean` | `isValid()` | Verifies that all Red-Black invariants and BST orderings hold. |
| `@NonNull Maybe<A>` | `min()` | Returns the minimum element according to the tree comparator, or `none()` if empty. |
| `@NonNull Maybe<A>` | `max()` | Returns the maximum element according to the tree comparator, or `none()` if empty. |
| `@NonNull Comparer<A>` | `comparer()` | Returns the comparator wrapper used by this tree. |
| `@NonNull InsertionStrategy` | `insertionStrategy()` | Returns the insertion strategy (`Discard` or `Replace`). |

### Persistent Modifications

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull RedBlackTree<A>` | `insert(@NonNull A value)` | Returns a new tree with the element inserted, balancing nodes via the Okasaki algorithm. |
| `@NonNull RedBlackTree<A>` | `insertAll(@NonNull Iterable<? extends A> elements)` | Returns a new tree containing all elements from the iterable. |
| `@NonNull RedBlackTree<A>` | `delete(@NonNull A value)` | Returns a new tree with the specified element removed. |
| `@NonNull RedBlackTree<A>` | `clear()` | Returns an empty tree sharing the same comparator and strategy. |

### Traversal & Iteration

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull Stream<A>` | `stream()` | Returns a sequential stream of elements in in-order traversal (sorted). |
| `@NonNull Stream<A>` | `stream(@NonNull TreeTraversal traversal)` | Returns a stream in the specified traversal order (`IN_ORDER`, `PRE_ORDER`, `POST_ORDER`, `LEVEL_ORDER`). |
| `@NonNull Iterator<A>` | `iterator()` | Returns an in-order iterator over the elements. |
| `@NonNull List<A>` | `toList()` | Converts the tree elements to an unmodifiable `List` in sorted order. |

### Transformations & Transmogrification

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <T> T` | `transmogrify(@NonNull Function<? super RedBlackTree<A>, ? extends T> transmogrifier)` | Fluent transformation operator for chaining external pipelines. |

---

## See Also

* [`AVLTree<A>`](/reference/base/util/avltree/) – Strict height-balanced AVL tree implementation.
* [`DecisionTree<A, B>`](/reference/base/util/decisiontree/) – Functional rule/decision tree classifier.
* [`BinaryTree<A>`](/reference/base/types/binarytree/) – Common binary tree interface.
