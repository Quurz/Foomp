---
title: Working with Trees in Foomp
description: Practical guide for manipulating, searching, and traversing immutable tree data structures in Foomp.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

Trees in Foomp are immutable, persistent hierarchical structures that guarantee structural sharing and type safety.

## Core Concepts: Immutability & Persistence

Every operation on a `Tree<A>` (`insert`, `remove`) returns a new tree instance. The original tree remains unchanged, allowing safe sharing across threads without synchronization.

```java title="TreeImmutabilityExample.java"
import org.quurz.foomp.base.types.Tree;
import org.quurz.foomp.base.types.Value;
import org.quurz.foomp.base.util.AVLTree;

public class TreeImmutabilityExample {
    public static void main(String[] args) {
        Tree<Integer> t1 = AVLTree.avlTree();
        Tree<Integer> t2 = t1.insert(50).insert(25).insert(75);

        System.out.println("t1 height: " + t1.height()); // 0
        System.out.println("t2 height: " + t2.height()); // 2

        // Safe search returns Value<Integer>
        Value<Integer> found = t2.searchSafe(25);
        if (found.isPresent()) {
            System.out.println("Found: " + found.get()); // 25
        }
    }
}
```

---

## Practical Examples

<Tabs>
  <TabItem label="Persistent Updates & Search">
    ```java title="TreeSearchExample.java"
    import org.quurz.foomp.base.types.Tree;
    import org.quurz.foomp.base.types.Value;
    import org.quurz.foomp.base.util.RedBlackTree;

    public class TreeSearchExample {
        public static void main(String[] args) {
            Tree<String> words = RedBlackTree.<String>redBlackTree()
                .insert("functional")
                .insert("object-oriented")
                .insert("monadic");

            System.out.println("Contains 'monadic': " + words.contains("monadic")); // true

            // Safe lookup avoiding NoSuchElementException
            Value<String> match = words.searchSafe("haskell");
            System.out.println("Found: " + (match.isPresent() ? match.get() : "Not in tree"));

            // Persistent removal
            Tree<String> reduced = words.remove("monadic");
            System.out.println("After removal: " + reduced.contains("monadic")); // false
        }
    }
    ```
  </TabItem>
  <TabItem label="Traversals with Visitors">
    ```java title="TreeVisitorExample.java"
    import org.checkerframework.checker.nullness.qual.NonNull;
    import org.quurz.foomp.base.types.Tree;
    import org.quurz.foomp.base.types.Visitor;
    import org.quurz.foomp.base.util.AVLTree;
    import java.util.concurrent.atomic.AtomicInteger;

    public class TreeVisitorExample {
        public static void main(String[] args) {
            Tree<Integer> tree = AVLTree.<Integer>avlTree()
                .insert(10).insert(20).insert(30).insert(40);

            // Visitor counting all non-leaf nodes
            NodeCounterVisitor<Integer> counter = tree.welcome(new NodeCounterVisitor<>());
            System.out.println("Total nodes visited: " + counter.getCount());
        }
    }

    class NodeCounterVisitor<A> implements Visitor<Tree<A>> {
        private final AtomicInteger count = new AtomicInteger(0);

        @Override
        public void visit(@NonNull Tree<A> node) {
            if (node.isNode()) {
                count.incrementAndGet();
                for (Tree<A> child : node.children()) {
                    child.welcome(this);
                }
            }
        }

        public int getCount() { return count.get(); }
    }
    ```
  </TabItem>
</Tabs>

---

## Summary of Best Practices

:::tip[Best Practices]
* **Prefer `searchSafe`:** Use `searchSafe` returning `Value<A>` rather than `search` to avoid `NoSuchElementException` when keys might not exist.
* **Leverage `welcome(visitor)`:** When computing aggregations, tree depths, or custom serializations, use the Visitor pattern to keep tree implementations cleanly decoupled from external operations.
:::
