---
title: AVLTree Guide
description: Practical developer guide for using immutable self-balancing AVL Trees in Foomp.
---

`AVLTree<A>` is Foomp's persistent, self-balancing binary search tree. Every insertion or deletion produces a new balanced tree via structural sharing in $O(\log n)$ time, leaving existing trees unmodified and fully thread-safe.

---

## When to Use `AVLTree<A>`

* **Ordered Lookups & Range Searches:** When you need logarithmic lookup, insertion, and deletion while maintaining sorted order.
* **Persistent / Immutable State:** Ensuring safe sharing across threads or concurrency boundaries without defensive copying or locking.
* **Structural Visualisation:** Visualising tree balance factors and node arrangements using `echo()`.

---

## Examples

### 1. Creating and Querying an AVL Tree

```java title="AVLTreeBasicExample.java"
import org.quurz.foomp.base.util.AVLTree;
import org.quurz.foomp.base.types.Maybe;

import static org.quurz.foomp.base.util.AVLTree.avlTreeOf;

public class AVLTreeBasicExample {
    public static void main(String[] args) {
        // Create an AVL tree from elements
        AVLTree<Integer> tree = avlTreeOf(40, 20, 60, 10, 30, 50, 70);

        System.out.println("Tree height: " + tree.height());
        System.out.println("Contains 30: " + tree.contains(30)); // true
        System.out.println("Contains 99: " + tree.contains(99)); // false

        // Safe lookup returning Maybe<T>
        Maybe<Integer> found = tree.searchSafe(50);
        found.ifPresent(val -> System.out.println("Found: " + val));
    }
}
```

---

### 2. Persistent Mutations (Structural Sharing)

Insertions and deletions do not mutate the tree in place; they return a newly rebalanced tree:

```java title="AVLTreeMutationExample.java"
import org.quurz.foomp.base.util.AVLTree;

import static org.quurz.foomp.base.util.AVLTree.avlTree;

public class AVLTreeMutationExample {
    public static void main(String[] args) {
        AVLTree<String> root = avlTree();

        // Progressive insertions
        AVLTree<String> t1 = root.insert("Gamma");
        AVLTree<String> t2 = t1.insert("Alpha");
        AVLTree<String> t3 = t2.insert("Beta"); // Triggers internal rebalancing rotation!

        // The original trees remain unchanged
        System.out.println("t1 height: " + t1.height() + " (root: " + t1.element() + ")");
        System.out.println("t3 height: " + t3.height() + " (root: " + t3.element() + ")");

        // Persistent removal
        AVLTree<String> t4 = t3.remove("Alpha");
        System.out.println("t4 contains Alpha: " + t4.contains("Alpha")); // false
        System.out.println("t3 contains Alpha: " + t3.contains("Alpha")); // true
    }
}
```

---

### 3. Rendering ASCII Tree Visualisations with `echo()`

`AVLTree` implements `Echo`, producing readable tree diagrams:

```java title="AVLTreeEchoExample.java"
import org.quurz.foomp.base.util.AVLTree;

import static org.quurz.foomp.base.util.AVLTree.avlTreeOf;

public class AVLTreeEchoExample {
    public static void main(String[] args) {
        AVLTree<Integer> tree = avlTreeOf(50, 25, 75, 10, 30, 60, 90, 5);

        // Echo renders the tree topology and node balance
        System.out.println(tree.echo());
    }
}
```

---

### 4. Collecting Streams into an AVL Tree

```java title="AVLTreeStreamExample.java"
import org.quurz.foomp.base.util.AVLTree;
import org.quurz.foomp.base.types.Tree.InsertionStrategy;

import java.util.Comparator;
import java.util.stream.Stream;

public class AVLTreeStreamExample {
    public static void main(String[] args) {
        AVLTree<String> sortedNames = Stream.of("Charlie", "Alice", "Bob", "Daniel")
            .collect(AVLTree.collectToAVLTree(InsertionStrategy.Discard, Comparator.naturalOrder()));

        // In-order traversal via Visitor
        sortedNames.welcome(name -> System.out.println("Sorted: " + name));
    }
}
```

---

## Best Practices

:::tip[AVL vs Other Collections]
* **Balanced Guaranteed:** AVL trees are strictly balanced. They are slightly more rigidly balanced than Red-Black trees, making lookup operations faster at the expense of slightly more rotations during frequent insertions/deletions.
* **Duplicate Handling:** Use `InsertionStrategy.Discard` (default) when duplicates should be ignored, or `InsertionStrategy.Replace` to update payloads with equivalent keys.
:::
