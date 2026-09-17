---
title: SeqList Guide
description: Practical developer guide for working with the eager, random-access functional SeqList in Foomp.
---

`SeqList<A>` bridges the gap between Java's standard `java.util.List` and pure functional programming. It provides an unmodifiable list with $O(1)$ indexed lookups while supporting functional recursion (`head`, `tail`, `cons`, `decons`), higher-order combinators (`map`, `filter`, `foldLeft`), and monadic composition.

---

## When to Use `SeqList<A>`

* **Interoperability with Standard Java Collections:** Passing functional sequences to existing APIs that require `java.util.List`.
* **Frequent Random Index Access:** Fast $O(1)$ indexing (`get(int)`) when traversing or picking items by position.
* **Eager Evaluation:** Ensuring all transformations and elements are computed up front.
* **Recursive List Deconstruction:** Pattern matching over list heads and tails with `decons()`.

---

## Examples

### 1. Creation and Standard List Access

```java title="SeqListCreation.java"
import org.quurz.foomp.base.util.SeqList;

import java.util.List;

import static org.quurz.foomp.base.util.SeqList.seqList;

public class SeqListCreation {
    public static void main(String[] args) {
        // Create an eager sequence list
        SeqList<String> fruits = seqList("apple", "banana", "cherry");

        // Use standard java.util.List methods
        System.out.println("Size: " + fruits.size()); // 3
        System.out.println("Index 1: " + fruits.get(1)); // banana

        // Works anywhere List<T> is accepted
        List<String> javaList = fruits;
        System.out.println("Contains cherry? " + javaList.contains("cherry")); // true
    }
}
```

---

### 2. Functional Operations (`cons`, `tail`, `decons`)

```java title="FunctionalListOps.java"
import org.quurz.foomp.base.util.SeqList;

import static org.quurz.foomp.base.util.SeqList.seqList;

public class FunctionalListOps {
    public static void main(String[] args) {
        SeqList<Integer> numbers = seqList(2, 3, 4);

        // Prepend an element
        SeqList<Integer> withOne = numbers.cons(1);
        System.out.println(withOne); // [1, 2, 3, 4]

        // Safe head and tail
        System.out.println("Head: " + withOne.head().orElse(0)); // 1
        System.out.println("Tail: " + withOne.tail()); // [2, 3, 4]

        // Recursive deconstruction with decons()
        withOne.decons().ifPresent(tuple -> {
            Integer head = tuple.get1();
            SeqList<Integer> tail = tuple.get2();
            System.out.println("Deconstructed: head=" + head + ", tail=" + tail);
        });
    }
}
```

---

### 3. Transformations, Partitioning & Folding

```java title="TransformationsExample.java"
import org.quurz.foomp.base.util.SeqList;
import org.quurz.foomp.base.util.Tuple2;

import static org.quurz.foomp.base.util.SeqList.seqList;

public class TransformationsExample {
    public static void main(String[] args) {
        SeqList<Integer> numbers = seqList(1, 2, 3, 4, 5, 6);

        // Partition into even and odd lists
        Tuple2<SeqList<Integer>, SeqList<Integer>> parts =
                numbers.partition(n -> n % 2 == 0);

        System.out.println("Evens: " + parts.get1()); // [2, 4, 6]
        System.out.println("Odds:  " + parts.get2()); // [1, 3, 5]

        // Fold left to sum
        int sum = numbers.foldLeft(0, Integer::sum);
        System.out.println("Sum: " + sum); // 21
    }
}
```

---

## Best Practices

:::tip[Use `SeqList` for Index-Heavy Workloads]
When you need fast random element lookups (`get(i)`) or seamless integration into Java collections frameworks, prefer `SeqList<A>`.
:::

:::note[SeqList vs. Sequence]
* `SeqList` evaluates all elements eagerly and stores them internally for $O(1)$ random indexing.
* [`Sequence`](/reference/base/util/sequence/) uses a segmented structure with lazy transformation fusion, optimizing pipelines of chained `map` operations without intermediate allocations.
:::
