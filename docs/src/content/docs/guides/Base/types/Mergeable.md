---
title: Working with Mergeable
description: Practical guide and design recipes for combining algebraic structures and persistent collections in Foomp.
---

import { Aside, Tabs, TabItem } from '@astrojs/starlight/components';

`Mergeable<SELF>` provides a uniform, type-safe API for combining data structures of the same type. It is the foundation for joining collections, merging dictionaries, and combining hierarchical trees.

---

## Key Principles

1. **Self-Typing (`SELF`):** The recursive generic bound `SELF extends Mergeable<?>` ensures that merging two instances of type `T` always returns a `T` without requiring manual casts.
2. **Persistent & Pure:** In functional programming, `a.merge(b)` never modifies `a` or `b`. Instead, it computes and returns an updated result.
3. **Semigroup Semantics:** Merging corresponds to the binary semigroup operation ($\oplus$), enabling clean reduction across lists or streams using `foldLeft`.

---

## Practical Examples

### 1. Merging Dictionaries (`Dictionary<K, V>`)

When combining two persistent dictionaries, `merge` produces a new dictionary containing keys and values from both maps:

```java title="MergeDictionaryExample.java"
import org.quurz.foomp.base.util.Dictionary;
import static org.quurz.foomp.base.util.Dictionary.dictionary;
import static org.quurz.foomp.base.util.Dictionary.dictionaryOf;
import static org.quurz.foomp.base.util.Tuple2.tuple2;

public class MergeDictionaryExample {

    public static void main(String[] args) {
        Dictionary<String, Integer> defaults = dictionaryOf(
            tuple2("timeout", 30),
            tuple2("retries", 3)
        );
        Dictionary<String, Integer> overrides = dictionaryOf(
            tuple2("retries", 5),
            tuple2("threads", 8)
        );

        // Merging combines both dictionaries; entries in overrides take precedence on conflict
        Dictionary<String, Integer> merged = defaults.merge(overrides);

        System.out.println("Timeout: " + merged.get("timeout")); // 30
        System.out.println("Retries: " + merged.get("retries")); // 5 (overridden)
        System.out.println("Threads: " + merged.get("threads")); // 8
    }
}
```

---

### 2. Merging Binary Search Trees (`AVLTree` / `RedBlackTree`)

Foomp's binary search trees implement `BinaryTree<A>`, which extends `Mergeable<BinaryTree<A>>`. Merging two balanced search trees integrates all nodes while maintaining balance invariants:

```java title="MergeTreesExample.java"
import org.quurz.foomp.base.types.BinaryTree;
import org.quurz.foomp.base.util.AVLTree;

public class MergeTreesExample {

    public static void main(String[] args) {
        BinaryTree<Integer> leftTree = AVLTree.of(10, 20, 30);
        BinaryTree<Integer> rightTree = AVLTree.of(15, 25, 35);

        // Combines both trees into a balanced unified tree
        BinaryTree<Integer> mergedTree = leftTree.merge(rightTree);

        System.out.println("Contains 25: " + mergedTree.contains(25)); // true
    }
}
```

---

### 3. Aggregating Collections with `foldLeft`

Because `merge` is a binary operation `(SELF, SELF) -> SELF`, you can aggregate multiple mergeable elements into a single instance:

```java title="AggregateMergeableExample.java"
import java.util.List;
import org.quurz.foomp.base.util.Dictionary;
import static org.quurz.foomp.base.util.Dictionary.dictionary;
import static org.quurz.foomp.base.util.Dictionary.dictionaryOf;
import static org.quurz.foomp.base.util.Tuple2.tuple2;

public class AggregateMergeableExample {

    public static void main(String[] args) {
        List<Dictionary<String, String>> configurations = List.of(
            dictionaryOf(tuple2("env", "prod")),
            dictionaryOf(tuple2("region", "eu-central-1")),
            dictionaryOf(tuple2("version", "1.0.0"))
        );

        // Fold all configuration layers into one consolidated dictionary
        Dictionary<String, String> consolidated = configurations.stream()
            .reduce(dictionary(), Dictionary::merge);

        System.out.println("Env: " + consolidated.get("env"));
        System.out.println("Region: " + consolidated.get("region"));
    }
}
```

---

### 4. Implementing Custom Mergeable Records

You can implement `Mergeable` in your domain classes to provide expressive, fluent domain combinators:

```java title="AuditLog.java"
import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Mergeable;
import java.util.List;
import java.util.Objects;

public record AuditLog(List<String> entries) implements Mergeable<AuditLog> {

    @Override
    public @NonNull AuditLog merge(final @NonNull AuditLog other) {
        Objects.requireNonNull(other, "other must not be null");

        var combined = new java.util.ArrayList<>(this.entries);
        combined.addAll(other.entries());
        return new AuditLog(List.copyOf(combined));
    }
}
```

---

## Best Practices

:::tip[Semigroup Associativity]
Whenever you implement `Mergeable`, aim for associative merging: `(a.merge(b)).merge(c)` should produce equivalent state to `a.merge(b.merge(c))`. This ensures deterministic behavior in parallel reductions.
:::

:::caution[Null-Safety]
Always enforce `Objects.requireNonNull(other)` in custom `merge` methods to maintain non-null guarantees across your pipelines.
:::
