---
title: Visitor Guide
description: Practical developer guide for implementing operations with the Visitor functional interface in Foomp.
---

The `Visitor<A>` interface defines an operation executed against visited elements. Together with [`Visitable<A>`](/reference/base/types/visitable/), it allows writing modular, decoupled traversal routines.

---

## When to Use `Visitor<A>`

* **Traversing Trees or Hierarchies:** Processing elements contained in `Tree<A>` or other visitable collections.
* **Accumulating Traversal State:** Gathering statistics, finding max/min elements, or counting matches without exposing internal node pointers.
* **Separation of Concerns:** Keeping presentation, persistence, or validation logic outside data structures.

---

## Examples

### 1. Anonymous Lambda Visitor

Because `Visitor<A>` is a `@FunctionalInterface`, you can pass a lambda or method reference directly:

```java title="LambdaVisitorExample.java"
import org.quurz.foomp.base.types.Tree;
import org.quurz.foomp.base.util.AVLTree;

public class LambdaVisitorExample {
    public static void main(String[] args) {
        Tree<String> words = AVLTree.avlTree("Functional", "Object-Oriented", "Monadic", "Programming");

        // Visiting each word and printing it
        words.welcome(word -> System.out.println("Visited: " + word));
    }
}
```

---

### 2. Stateful Visitor for Tree Statistics

```java title="TreeStatisticsVisitorExample.java"
import org.quurz.foomp.base.types.Tree;
import org.quurz.foomp.base.types.Visitor;
import org.quurz.foomp.base.util.AVLTree;
import org.checkerframework.checker.nullness.qual.NonNull;

public class TreeStatisticsVisitorExample {

    public static class StatisticsVisitor implements Visitor<Integer> {
        private int count = 0;
        private int sum = 0;
        private int min = Integer.MAX_VALUE;
        private int max = Integer.MIN_VALUE;

        @Override
        public void visit(@NonNull Integer value) {
            count++;
            sum += value;
            min = Math.min(min, value);
            max = Math.max(max, value);
        }

        public double getAverage() {
            return count == 0 ? 0.0 : (double) sum / count;
        }

        public int getCount() { return count; }
        public int getMin() { return count == 0 ? 0 : min; }
        public int getMax() { return count == 0 ? 0 : max; }
    }

    public static void main(String[] args) {
        Tree<Integer> scores = AVLTree.avlTree(88, 92, 79, 95, 84, 100);

        StatisticsVisitor stats = scores.welcome(new StatisticsVisitor());

        System.out.println("Scores processed: " + stats.getCount());
        System.out.println("Average score:    " + stats.getAverage());
        System.out.println("Min / Max:        " + stats.getMin() + " / " + stats.getMax());
    }
}
```

---

## Best Practices

:::tip[Stateful Visitors]
* **Instance Reuse:** Create a new visitor instance for each traversal run unless you intentionally want to aggregate metrics across multiple visitable collections.
* **Avoid Mutating Visited Elements:** Whenever possible, treat the visited element as read-only.
:::
