---
title: Working with Comparer
description: A practical guide to type-safe comparisons, exhaustive switch expressions, and functional binary ordering with Comparer.
---

import { Aside, Tabs, TabItem } from '@astrojs/starlight/components';

In standard Java, `java.util.Comparator<T>` produces a raw `int` whose sign represents the comparison outcome (`< 0`, `== 0`, `> 0`). While performant, this integer-based convention is error-prone when used in conditional branching and cannot be checked for exhaustiveness by the Java compiler.

`Comparer<A>` elevates comparisons into the type system by returning an explicit [`Comparer.Relation`](/reference/base/functions/comparer/#relation-enum) enum (`LESS`, `EQUAL`, `GREATER`). This enables clean, exhaustive `switch` expressions and turns comparison into a composable binary function (`Fun2<A, A, Relation>`).

---

## Why Use `Comparer` Over `Comparator`?

<Tabs>
  <TabItem label="Standard Comparator (Integer-based)">
    ```java
    Comparator<String> comparator = String::compareTo;
    int diff = comparator.compare(a, b);

    // ❌ Cumbersome if-else cascade with loose integer semantics
    if (diff < 0) {
        System.out.println("a is smaller");
    } else if (diff > 0) {
        System.out.println("a is larger");
    } else {
        System.out.println("a equals b");
    }
    ```
  </TabItem>
  <TabItem label="Foomp Comparer (Enum & Switch)">
    ```java
    import org.quurz.foomp.base.functions.Comparer;
    import static org.quurz.foomp.base.functions.Comparer.comparer;

    Comparer<String> stringComparer = comparer(String::compareTo);

    // ✅ Clean, exhaustive switch expression
    String result = switch (stringComparer.compare(a, b)) {
        case LESS    -> "a is smaller";
        case EQUAL   -> "a equals b";
        case GREATER -> "a is larger";
    };
    ```
  </TabItem>
</Tabs>

:::tip[Compiler Exhaustiveness]
Because `Relation` is a sealed 3-value enumeration, the compiler verifies that all cases (`LESS`, `EQUAL`, `GREATER`) are covered without requiring a redundant `default` branch.
:::

---

## Creating a `Comparer`

You can create a `Comparer` from any existing `java.util.Comparator` using the static factory method `Comparer.comparer`:

```java
import org.quurz.foomp.base.functions.Comparer;
import java.util.Comparator;

// 1. From natural order
Comparer<Integer> intOrder = Comparer.comparer(Comparator.naturalOrder());

// 2. From custom Comparator
Comparer<User> userByName = Comparer.comparer(Comparator.comparing(User::name));

// 3. Directly via lambda
Comparer<Double> thresholdCompare = (x, y) -> {
    double diff = x - y;
    if (diff < -0.001) return Comparer.Relation.LESS;
    if (diff > 0.001)  return Comparer.Relation.GREATER;
    return Comparer.Relation.EQUAL;
};
```

---

## Functional Superpowers: Currying & Partial Application

Because `Comparer<A>` extends `Fun2<A, A, Comparer.Relation>`, you can curry or partially apply comparisons using `partial1` and `partial2` to classify data against a fixed reference point (such as a pivot in sorting or a threshold in filtering):

```java
import org.quurz.foomp.base.functions.Comparer;
import org.quurz.foomp.base.functions.Fun;
import static org.quurz.foomp.base.functions.Comparer.comparer;

Comparer<Integer> comparer = comparer(Integer::compare);

// 1. Partial application using partial2: compare(x, 50)
// Fixes the second argument to 50 via Supplier -> Fun<Integer, Relation>
Fun<Integer, Comparer.Relation> classifyAgainst50 = comparer.partial2(() -> 50);

System.out.println(classifyAgainst50.apply(25));  // Relation.LESS    (25 vs 50)
System.out.println(classifyAgainst50.apply(50));  // Relation.EQUAL   (50 vs 50)
System.out.println(classifyAgainst50.apply(100)); // Relation.GREATER (100 vs 50)

// 2. Partial application using partial1: compare(50, y)
// Fixes the first argument to 50 via Supplier -> Fun<Integer, Relation>
Fun<Integer, Comparer.Relation> compareFrom50 = comparer.partial1(() -> 50);

System.out.println(compareFrom50.apply(25));  // Relation.GREATER (50 vs 25)

// 3. Currying
Fun<Integer, Fun<Integer, Comparer.Relation>> curried = comparer.curry();
Fun<Integer, Comparer.Relation> curriedCompareWith10 = curried.apply(10);
System.out.println(curriedCompareWith10.apply(20)); // Relation.LESS (10 vs 20)
```

---

## Practical Example: Binary Search Tree Branching

`Comparer` is used internally in Foomp's persistent data structures (`AVLTree` and `RedBlackTree`) to make recursive tree operations clean and readable:

```java
import org.quurz.foomp.base.functions.Comparer;

sealed interface Tree<A> {
    record Leaf<A>() implements Tree<A> {}
    record Node<A>(A value, Tree<A> left, Tree<A> right) implements Tree<A> {}
}

public <A> Tree<A> insert(Tree<A> tree, A element, Comparer<A> cmp) {
    if (tree instanceof Tree.Leaf<A>) {
        return new Tree.Node<>(element, new Tree.Leaf<>(), new Tree.Leaf<>());
    }

    Tree.Node<A> node = (Tree.Node<A>) tree;
    return switch (cmp.compare(element, node.value())) {
        case LESS    -> new Tree.Node<>(node.value(), insert(node.left(), element, cmp), node.right());
        case GREATER -> new Tree.Node<>(node.value(), node.left(), insert(node.right(), element, cmp));
        case EQUAL   -> node; // Element already exists
    };
}
```

---

## Summary

* **Type Safety:** Replaces ambiguous integer sign semantics with the explicit `Relation` enum (`LESS`, `EQUAL`, `GREATER`).
* **Pattern Matching:** Integrates naturally with modern Java `switch` expressions for exhaustive, clear branching.
* **Functional Integration:** Inherits higher-order capabilities from `Fun2`, including currying, partial application, and null safety.
