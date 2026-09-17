---
title: Visitable Guide
description: Practical developer guide for making data structures visitable using Foomp's Visitor pattern.
---

The `Visitable<A>` interface allows objects and data structures to accept external operations via `welcome(visitor)`. This decouples the structure of an object graph (e.g. trees, composite nodes, ASTs) from the algorithms operating on it.

---

## When to Use `Visitable<A>`

* **Externalising Algorithms:** Traversing complex structures (like `Tree<A>` or custom AST nodes) without adding dozens of specialized traversal methods to the classes themselves.
* **Stateful Traversals:** Collecting metrics, computing aggregates, or accumulating data during iteration.
* **Fluent Visitor Pipelines:** Taking advantage of `welcome`'s return value to immediately read out visitor results.

---

## Examples

### 1. Visiting a Tree Structure

Trees in Foomp implement `Visitable<A>`. You can pass any `Visitor<A>` to `welcome`:

```java title="TreeVisitorExample.java"
import org.quurz.foomp.base.types.Tree;
import org.quurz.foomp.base.types.Visitor;
import org.quurz.foomp.base.util.AVLTree;

import java.util.ArrayList;
import java.util.List;

public class TreeVisitorExample {

    // Custom visitor collecting elements in traversal order
    public static class CollectorVisitor<T> implements Visitor<T> {
        private final List<T> collected = new ArrayList<>();

        @Override
        public void visit(T element) {
            collected.add(element);
        }

        public List<T> getCollected() {
            return List.copyOf(collected);
        }
    }

    public static void main(String[] args) {
        Tree<Integer> tree = AVLTree.avlTree(10, 5, 20, 15, 30);

        // welcome returns the exact visitor passed in!
        CollectorVisitor<Integer> collector = tree.welcome(new CollectorVisitor<>());

        System.out.println("Visited elements: " + collector.getCollected());
    }
}
```

---

### 2. Implementing `Visitable<A>` on Custom Composite Structures

```java title="DocumentSectionExample.java"
import org.quurz.foomp.base.types.Visitable;
import org.quurz.foomp.base.types.Visitor;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Objects;

public class DocumentSectionExample implements Visitable<String> {

    private final String title;
    private final List<String> paragraphs;

    public DocumentSectionExample(String title, List<String> paragraphs) {
        this.title = Objects.requireNonNull(title);
        this.paragraphs = List.copyOf(paragraphs);
    }

    @Override
    public @NonNull <V extends Visitor<? super String>> V welcome(@NonNull V visitor) {
        Objects.requireNonNull(visitor);
        // Visit the title first, then each paragraph
        visitor.visit(title);
        for (String paragraph : paragraphs) {
            visitor.visit(paragraph);
        }
        return visitor;
    }

    public static void main(String[] args) {
        DocumentSectionExample section = new DocumentSectionExample(
            "Introduction to Foomp",
            List.of("Foomp brings functional programming to Java.", "It supports HKT and monads.")
        );

        // Inline visitor using lambda
        section.welcome(text -> System.out.println("Line: " + text));
    }
}
```

---

## Best Practices

:::tip[Visitor Pattern Tips]
* **Leverage the Return Value:** `welcome(V visitor)` returns `V`. Avoid assigning the visitor to an external local variable beforehand if you only need the result at the end.
* **Keep Structures Free of Traversal Logic:** Prefer passing lightweight visitors rather than embedding custom formatting or aggregation algorithms directly into composite classes.
:::
