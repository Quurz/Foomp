---
title: Sequence Guide
description: Practical developer guide for working with lazy, persistent Sequences with transformation fusion in Foomp.
---

`Sequence<A>` is an immutable, persistent sequence data structure designed for functional pipelines. It segments data across a doubly linked structure and fuses sequential transformations (`map`, `applyTo`) into an internal spool without immediate data copying.

---

## When to Use `Sequence<A>`

* **Chained Transformations:** Applying multiple `map()` steps in sequence without allocating intermediate lists or array copies.
* **Persistent Collections:** Sharing sequence snapshots across functions with $O(1)$ structural prepending (`cons`).
* **Monadic Workflows:** Combining and flattening nested sequences with `flatMap`.
* **Explicit Materialization:** Deferring evaluation until an unwrap or terminal operation (`unwind()`, `stream()`, `iterator()`, `foldLeft`).

---

## Examples

### 1. Creation and Transformation Fusion

```java title="SequenceFusionExample.java"
import org.quurz.foomp.base.util.Sequence;

import static org.quurz.foomp.base.util.Sequence.sequenceOf;

public class SequenceFusionExample {
    public static void main(String[] args) {
        // Create a sequence
        Sequence<Integer> original = sequenceOf(1, 2, 3, 4, 5);

        // Multiple map calls fuse their spool functions without duplicating node data
        Sequence<String> formatted = original
                .map(n -> n * 10)
                .map(n -> "Item #" + n);

        // Iterate over transformed values
        for (String item : formatted) {
            System.out.println(item);
        }
    }
}
```

---

### 2. Structural Manipulation (`cons`, `head`, `headSafe`, `tail`, `decons`)

```java title="StructuralOps.java"
import org.quurz.foomp.base.util.Maybe;
import org.quurz.foomp.base.util.Sequence;
import org.quurz.foomp.base.util.Tuple2;

import static org.quurz.foomp.base.util.Sequence.sequenceOf;

public class StructuralOps {
    public static void main(String[] args) {
        Sequence<String> words = sequenceOf("World", "!");
        
        // Fast O(1) prepend
        Sequence<String> greeting = words.cons("Hello");

        // Direct head extraction (returns String, throws NoSuchElementException if empty)
        String first = greeting.head();
        System.out.println("Head: " + first); // Hello

        // Safe head extraction wrapped in Maybe<String>
        Maybe<String> safeHead = greeting.headSafe();
        System.out.println("Safe Head: " + safeHead.getOrElse(() -> "")); // Hello

        // Deconstruct into head element and tail sequence
        Tuple2<String, Sequence<String>> deconstructed = greeting.decons();
        System.out.println("Deconstructed Head: " + deconstructed.get1()); // Hello
        System.out.println("Tail has elements? " + deconstructed.get2().isNotEmpty()); // true
    }
}
```

---

### 3. Partitioning and Spanning

```java title="PartitionExample.java"
import org.quurz.foomp.base.util.Sequence;
import org.quurz.foomp.base.util.Tuple2;

import static org.quurz.foomp.base.util.Sequence.sequenceOf;

public class PartitionExample {
    public static void main(String[] args) {
        Sequence<Integer> numbers = sequenceOf(1, 2, 3, 4, 5, 6);

        // Partition into matching (even) and non-matching (odd) sequences
        Tuple2<Sequence<Integer>, Sequence<Integer>> partitioned =
                numbers.partition(n -> n % 2 == 0);

        // Span by condition (prefix matching condition and remainder)
        Tuple2<Sequence<Integer>, Sequence<Integer>> spanned =
                numbers.span(n -> n < 4);
    }
}
```

---

### 4. Materializing Fused Transformations (`unwind`)

```java title="UnwindExample.java"
import org.quurz.foomp.base.util.Sequence;

import static org.quurz.foomp.base.util.Sequence.sequenceOf;

public class UnwindExample {
    public static void main(String[] args) {
        Sequence<Integer> lazySeq = sequenceOf(10, 20, 30)
                .map(x -> x + 5);

        // Explicitly forces evaluation of all fused spool functions into a fresh concrete segment
        Sequence<Integer> evaluatedSeq = lazySeq.unwind();

        // Iterate through evaluated sequence
        evaluatedSeq.iterateOverAllElementsFromLeft(System.out::println);
    }
}
```

---

## Best Practices

:::tip[Transformation Spooling]
`map()` operations are fused into the segment's spool function and evaluated on demand. To force eager evaluation and truncate transformation chains, call `unwind()`.
:::

:::note[Sequence vs. SeqList]
* Use `Sequence<A>` for persistent pipelines that undergo frequent functional chaining, splitting, and structural sharing without requiring index-based lookup or total size tracking.
* Use [`SeqList<A>`](/reference/base/util/seqlist/) when you need an eager `java.util.List` implementation with `size()` and $O(1)$ random index access.
:::
