---
title: RedBlackTree Guide
description: Practical developer guide for working with the persistent RedBlackTree in Foomp.
---

`RedBlackTree<A>` is an immutable, persistent Red-Black search tree. It maintains logarithmic height ($O(\log n)$) and rebalances purely functionally during updates, providing thread-safe operations with zero synchronization overhead.

---

## When to Use `RedBlackTree<A>`

* **Persistent Ordered Sets:** Maintaining sorted sets across state snapshots without defensive copies.
* **Predictable logarithmic access:** Guaranteeing worst-case $O(\log n)$ bounds for lookups, insertions, and deletions.
* **Concurrent/Multi-threaded Sharing:** Safely sharing ordered structures across threads without locks.
* **Custom Sort Orders & Strategies:** Controlling whether duplicates are ignored (`Discard`) or overwritten (`Replace`).

---

## Examples

### 1. Creating and Inserting Elements

```java title="RedBlackTreeCreation.java"
import org.quurz.foomp.base.util.RedBlackTree;

import static org.quurz.foomp.base.util.RedBlackTree.redBlackTree;

public class RedBlackTreeCreation {
    public static void main(String[] args) {
        // Create an empty tree using natural order
        RedBlackTree<Integer> t0 = redBlackTree();

        // Insert elements (returns a new persistent tree)
        RedBlackTree<Integer> t1 = t0.insert(50).insert(20).insert(80).insert(10);

        System.out.println("Size: " + t1.size()); // 4
        System.out.println("Contains 20? " + t1.contains(20)); // true
        System.out.println("Min: " + t1.min().orElse(-1)); // 10
        System.out.println("Max: " + t1.max().orElse(-1)); // 80
    }
}
```

---

### 2. Custom Comparator and Duplicate Strategy

```java title="CustomStrategyExample.java"
import org.quurz.foomp.base.types.Tree.InsertionStrategy;
import org.quurz.foomp.base.util.RedBlackTree;

import java.util.Comparator;

import static org.quurz.foomp.base.util.RedBlackTree.redBlackTree;

public class CustomStrategyExample {
    public static void main(String[] args) {
        // Tree ordered by string length, replacing duplicates
        RedBlackTree<String> tree = redBlackTree(
                InsertionStrategy.Replace,
                Comparator.comparingInt(String::length)
        );

        RedBlackTree<String> updated = tree.insert("apple").insert("grape");
        // "grape" has same length (5) and replaces "apple"
        System.out.println("Elements: " + updated.toList()); // [grape]
    }
}
```

---

### 3. Stream Accumulation & Traversals

```java title="TraversalExample.java"
import org.quurz.foomp.base.types.Tree.TreeTraversal;
import org.quurz.foomp.base.util.RedBlackTree;

import java.util.List;
import java.util.stream.Stream;

import static org.quurz.foomp.base.util.RedBlackTree.toRedBlackTree;

public class TraversalExample {
    public static void main(String[] args) {
        // Collect stream into RedBlackTree
        RedBlackTree<String> tree = Stream.of("gamma", "alpha", "beta")
                .collect(toRedBlackTree());

        // In-Order traversal (alphabetical)
        List<String> inOrder = tree.stream(TreeTraversal.IN_ORDER).toList();
        System.out.println("In-order: " + inOrder); // [alpha, beta, gamma]

        // Breadth-First / Level-Order traversal
        List<String> levelOrder = tree.stream(TreeTraversal.LEVEL_ORDER).toList();
        System.out.println("Level-order: " + levelOrder);
    }
}
```

---

## Best Practices

:::tip[Immutability & Structural Sharing]
`RedBlackTree` methods like `insert()` and `delete()` do not alter the existing tree; they return a newly balanced tree sharing unmodified subtrees with the original.
:::

:::note[RedBlackTree vs. AVLTree]
* `AVLTree` enforces stricter balance factor constraints, resulting in slightly shorter tree heights and faster lookups.
* `RedBlackTree` performs fewer rotations during insertions/deletions on average, making it optimal for write-heavy workloads.
:::
