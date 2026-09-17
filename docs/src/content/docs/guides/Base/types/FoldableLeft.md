---
title: FoldableLeft Guide
description: Practical recipes and idioms for left-associative folding with FoldableLeft in Foomp.
---

`FoldableLeft<A>` enables left-to-right element aggregation. Left folds are naturally suited for iterative computation, running totals, building reversed sequences, and stream reductions.

---

## The Left Fold Mental Model

In a left fold, the accumulator value is passed along on the left side:

```text
Initial accumulator:  init
Step 1:               acc1 = f(init, item1)
Step 2:               acc2 = f(acc1, item2)
Step 3:               acc3 = f(acc2, item3)
...
Final Result:         accN
```

---

## Practical Examples

### 1. Basic Reductions (Sums, Products, Aggregations)

```java
import org.quurz.foomp.base.types.FoldableLeft;

public static int sum(FoldableLeft<Integer> numbers) {
    return numbers.foldLeft(0, Integer::sum);
}

public static long product(FoldableLeft<Integer> numbers) {
    return numbers.foldLeft(1L, (acc, n) -> acc * n);
}
```

### 2. Building a Custom String / CSV Representation

```java
public static String toCsv(FoldableLeft<String> items) {
    return items.foldLeft(new StringBuilder(), (sb, item) -> {
        if (!sb.isEmpty()) {
            sb.append(", ");
        }
        return sb.append(item);
    }).toString();
}
```

### 3. Reversing / Inverting Order

Because elements are encountered from head to tail and prepended to a functional list, `foldLeft` naturally reverses an inductive sequence:

```java
import java.util.LinkedList;

public static <T> LinkedList<T> reverse(FoldableLeft<T> foldable) {
    return foldable.foldLeft(new LinkedList<T>(), (list, item) -> {
        list.addFirst(item);
        return list;
    });
}
```

### 4. Counting Elements Matching a Condition

```java
import org.quurz.foomp.base.functions.Pred;

public static <T> int countMatches(FoldableLeft<T> foldable, Pred<T> predicate) {
    return foldable.foldLeft(0, (count, item) -> predicate.test(item) ? count + 1 : count);
}
```

---

## Implementing `FoldableLeft` on Custom Data Structures

When implementing `FoldableLeft` on custom collections or trees, ensure that null checks are honored at every step:

```java
import org.quurz.foomp.base.types.FoldableLeft;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.List;

public final class CustomList<A> implements FoldableLeft<A> {
    private final List<A> elements;

    public CustomList(List<A> elements) {
        this.elements = List.copyOf(elements);
    }

    @Override
    public <B> @NonNull B foldLeft(
        final @NonNull B init,
        final @NonNull BiFunction<? super B, ? super A, ? extends B> function
    ) {
        Objects.requireNonNull(init, "init must not be null");
        Objects.requireNonNull(function, "function must not be null");

        B acc = init;
        for (A element : elements) {
            acc = Objects.requireNonNull(
                function.apply(acc, element),
                "foldLeft function must return non-null accumulator"
            );
        }
        return acc;
    }
}
```
