---
title: BinaryTree<A>
description: Formal API reference, structural properties, navigation methods, safe access, and Mergeable contract for BinaryTree in Foomp.
---

`org.quurz.foomp.base.types.BinaryTree<A>`

`BinaryTree` is a specialized [`Tree`](/reference/base/types/tree/) interface representing a hierarchical binary structure where each node carries a single element and has at most two child branches: [`left()`](#left) and [`right()`](#right).

```java
public interface BinaryTree<A>
    extends Tree<A>,
            Mergeable<BinaryTree<A>>
```

`BinaryTree` serves as the foundational interface for balanced persistent trees in Foomp, such as [`AVLTree`](/reference/base/util/avltree/) and [`RedBlackTree`](/reference/base/util/redblacktree/).

---

## Key Characteristics & Design

* **Dual-Branch Structure:** Unlike the general n-ary `Tree` interface, a `BinaryTree` explicitly defines left and right subtrees.
* **Single Element per Node:** Each node stores an `@NonNull` element of type `A`.
* **Safe & Throwing Access:** Provides direct access via `element()`, `left()`, and `right()`, as well as exception-safe access via `elementSafe()`.
* **Tree Merging:** Implements `Mergeable<BinaryTree<A>>` to support merging independent binary trees according to the concrete tree's balancing and ordering rules.

---

## Method Specification

### `element`

```java
@NonNull A element();
```

Returns the element held by this node.

#### Return Value
* Returns the `@NonNull A` element stored at the current tree node.

#### Exceptions
* `NoSuchElementException` if invoked on an empty or terminal tree node.

---

### `elementSafe`

```java
@NonNull Value<A> elementSafe();
```

Safely returns the element held by this node wrapped in a [`Value`](/reference/base/types/value/) container without throwing exceptions.

#### Return Value
* Returns a `@NonNull Value<A>` containing the element if present, or an empty `Value` if the node is empty or terminal.

---

### `left`

```java
@NonNull BinaryTree<A> left();
```

Returns the left child subtree of this node.

#### Return Value
* Returns the `@NonNull BinaryTree<A>` representing the left branch.

#### Exceptions
* `NoSuchElementException` if the node is a terminal node.

---

### `right`

```java
@NonNull BinaryTree<A> right();
```

Returns the right child subtree of this node.

#### Return Value
* Returns the `@NonNull BinaryTree<A>` representing the right branch.

#### Exceptions
* `NoSuchElementException` if the node is a terminal node.

---

### Inherited Methods

* From [`Mergeable<BinaryTree<A>>`](/reference/base/types/mergeable/):
  * `merge(BinaryTree<A> other)`: Merges another binary tree into this tree.
* From [`Tree<A>`](/reference/base/types/tree/):
  * `isEmpty()`, `isLeaf()`, `size()`, `height()`, `contains(A)`

---

## Concrete Implementations

| Implementation | Description | Balancing Strategy |
| :--- | :--- | :--- |
| [`AVLTree<A>`](/reference/base/util/avltree/) | Immutable, strictly balanced binary search tree. | Maintains height difference between child branches $\le 1$. |
| [`RedBlackTree<A>`](/reference/base/util/redblacktree/) | Immutable, self-balancing binary search tree. | Node coloring invariants guaranteeing $O(\log n)$ search and insertion. |

---

## See Also

* [Guide: Working with Binary Trees](/guides/base/types/binarytree/) – Practical traversal, safe navigation, and merging recipes.
* [`Tree` Reference](/reference/base/types/tree/) – General tree contract.
* [`Mergeable` Reference](/reference/base/types/mergeable/) – Contract for algebraic merging operations.
* [`AVLTree` Reference](/reference/base/util/avltree/) – Concrete AVL tree implementation.
* [`RedBlackTree` Reference](/reference/base/util/redblacktree/) – Concrete Red-Black tree implementation.
