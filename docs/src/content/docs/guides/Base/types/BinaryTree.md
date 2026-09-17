---
title: Working with Binary Trees
description: Practical developer guide, tree navigation, safe element extraction, recursive algorithms, and tree merging with BinaryTree in Foomp.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

`BinaryTree<A>` is the fundamental interface for binary search trees and hierarchical structures in Foomp. It provides explicit navigation through `left()` and `right()` subtrees, unified merging via `Mergeable`, and safe unwrapping via `elementSafe()`.

---

## 1. Navigating and Safe Element Extraction

When traversing a `BinaryTree`, nodes can either be internal value-bearing nodes or terminal nodes (empty leaves).

<Tabs>
  <TabItem label="Safe Extraction (Recommended)">
    ```java title="SafeTreeAccess.java"
    import org.quurz.foomp.base.types.BinaryTree;
    import org.quurz.foomp.base.types.Value;
    import org.quurz.foomp.base.util.AVLTree;

    public class SafeTreeAccess {
        public static void printNode(BinaryTree<String> tree) {
            // Returns Value.empty() on terminal/empty nodes without throwing
            Value<String> elemValue = tree.elementSafe();

            elemValue.ifPresent(val -> {
                System.out.println("Node element: " + val);
            });
        }
    }
    ```
  </TabItem>
  <TabItem label="Direct Extraction">
    ```java title="DirectTreeAccess.java"
    import org.quurz.foomp.base.types.BinaryTree;
    import org.quurz.foomp.base.util.AVLTree;
    import java.util.NoSuchElementException;

    public class DirectTreeAccess {
        public static void printNode(BinaryTree<String> tree) {
            if (!tree.isEmpty()) {
                System.out.println("Element: " + tree.element());
                System.out.println("Left child: " + tree.left());
                System.out.println("Right child: " + tree.right());
            }
        }
    }
    ```
  </TabItem>
</Tabs>

---

## 2. Recursive Traversals with BinaryTree

Because `BinaryTree` exposes `left()` and `right()` directly, you can write expressive functional recursive algorithms (pre-order, in-order, post-order).

```java title="TreeTraversals.java"
import org.quurz.foomp.base.functions.Receiver;
import org.quurz.foomp.base.types.BinaryTree;
import org.quurz.foomp.base.util.AVLTree;
import static org.quurz.foomp.base.util.AVLTree.avlTreeOf;

public class TreeTraversals {

    // In-Order Traversal: Left -> Element -> Right (Sorted order in BST)
    public static <A> void inOrder(BinaryTree<A> tree, Receiver<A> consumer) {
        if (!tree.isEmpty()) {
            inOrder(tree.left(), consumer);
            consumer.accept(tree.element());
            inOrder(tree.right(), consumer);
        }
    }

    public static void main(String[] args) {
        BinaryTree<Integer> bst = avlTreeOf(50, 20, 70, 10, 30, 60, 80);

        System.out.print("In-order: ");
        inOrder(bst, elem -> System.out.print(elem + " "));
        // Prints: 10 20 30 50 60 70 80
    }
}
```

---

## 3. Merging Binary Trees

Every `BinaryTree` in Foomp implements `Mergeable<BinaryTree<A>>`. You can combine two independent trees into a unified tree:

```java title="TreeMerging.java"
import org.quurz.foomp.base.types.BinaryTree;
import org.quurz.foomp.base.util.AVLTree;
import static org.quurz.foomp.base.util.AVLTree.avlTreeOf;

public class TreeMerging {
    public static void main(String[] args) {
        BinaryTree<Integer> tree1 = avlTreeOf(10, 30, 50);
        BinaryTree<Integer> tree2 = avlTreeOf(20, 40, 60);

        // Merges tree2 elements into tree1 with automatic rebalancing
        BinaryTree<Integer> merged = tree1.merge(tree2);

        System.out.println("Merged size: " + merged.size()); // 6
        System.out.println("Contains 40? " + merged.contains(40)); // true
    }
}
```

---

## 4. Choosing the Right Concrete Implementation

* Use [`AVLTree<A>`](/reference/base/util/avltree/) when your workload is **read-heavy / search-heavy**, as it maintains stricter balance ($\Delta h \le 1$), leading to slightly shallower trees.
* Use [`RedBlackTree<A>`](/reference/base/util/redblacktree/) when your workload has **frequent insertions and deletions**, as rebalancing during mutations involves fewer tree rotations.

---

## See Also

* [`BinaryTree` Reference](/reference/base/types/binarytree/) – Full API contract and method signatures.
* [`AVLTree` Reference](/reference/base/util/avltree/) – Immutable AVL tree documentation.
* [`RedBlackTree` Reference](/reference/base/util/redblacktree/) – Immutable Red-Black tree documentation.
