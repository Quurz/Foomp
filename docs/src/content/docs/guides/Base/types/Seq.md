---
title: Working with Seq
description: Practical recipes for constructing, deconstructing, splitting, and materializing functional sequences with Seq in Foomp.
---

import { Aside, Tabs, TabItem } from '@astrojs/starlight/components';

`Seq<A>` is the primary sequence contract in Foomp. It allows functional processing of elements with inductive head/tail decomposition, merging, filtering, and splitting.

---

## 1. Inductive Head/Tail Processing

Sequences support safe and direct recursive processing via `head()`, `tail()`, `headSafe()`, and `decons()`:

```java title="ProcessSequenceExample.java"
import org.quurz.foomp.base.types.Seq;
import org.quurz.foomp.base.types.Value2;

public class ProcessSequenceExample {

    // Recursive sum using head and tail
    public static int sum(Seq<Integer> seq) {
        if (seq.isEmpty()) {
            return 0;
        }
        return seq.head() + sum(seq.tail());
    }

    // Atomic deconstruction into (head, tail)
    public static <A> void printHeadAndTail(Seq<A> seq) {
        if (seq.isNotEmpty()) {
            Value2<A, ? extends Seq<A>> split = seq.decons();
            A head = split.get1();
            Seq<A> tail = split.get2();
            System.out.println("Head: " + head + ", Tail has elements: " + tail.isNotEmpty());
        }
    }
}
```

---

## 2. Partitioning and Spanning

`Seq` provides built-in methods for conditional partitioning and prefix splitting:

```java title="PartitionAndSpanExample.java"
import org.quurz.foomp.base.types.Seq;
import org.quurz.foomp.base.types.Value2;

public class PartitionAndSpanExample {

    public static void demonstrate(Seq<Integer> numbers) {
        // Partition into evens and odds
        Value2<? extends Seq<Integer>, ? extends Seq<Integer>> partitioned =
            numbers.partition(n -> n % 2 == 0);

        Seq<Integer> evens = partitioned.get1();
        Seq<Integer> odds = partitioned.get2();

        // Span: take while smaller than 10, then keep the remainder
        Value2<? extends Seq<Integer>, ? extends Seq<Integer>> spanned =
            numbers.span(n -> n < 10);

        Seq<Integer> smallPrefix = spanned.get1();
        Seq<Integer> remainder = spanned.get2();
    }
}
```

---

## 3. Prepending and Merging

You can construct new sequences incrementally or combine existing sequences:

```java title="ConsAndMergeExample.java"
import org.quurz.foomp.base.types.Seq;

public class ConsAndMergeExample {

    public static <A> Seq<A> combine(Seq<A> prefix, A middle, Seq<A> suffix) {
        // Prepend middle element to suffix
        Seq<A> tailWithMiddle = suffix.cons(middle);

        // Append tailWithMiddle to prefix
        return prefix.appendAll(tailWithMiddle);
    }
}
```

---

## 4. Materializing to Standard Collections

Use `toCollection(Supplier<C>)` to convert any `Seq` into a standard Java collection:

```java title="ToCollectionExample.java"
import org.quurz.foomp.base.types.Seq;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ToCollectionExample {

    public static void convert(Seq<String> words) {
        // Convert to ArrayList
        List<String> list = words.toCollection(ArrayList::new);

        // Convert to HashSet (removing duplicates)
        Set<String> uniqueWords = words.toCollection(HashSet::new);
    }
}
```
