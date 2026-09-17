---
title: Foldable Guide
description: Practical recipes and mental models for left and right folds with Foldable in Foomp.
---

Folding (also known as *reduce*, *accumulate*, or *catamorphism*) is the foundational functional pattern for processing collections into summary values, new data structures, or trees.

---

## Left Fold vs. Right Fold

`Foldable<A>` provides both `foldLeft` and `foldRight`. Understanding the difference in association and parameter ordering is crucial.

### 1. `foldLeft` (Left-Associative)

`foldLeft` processes elements from start to finish (left-to-right).

```
Elements: [1, 2, 3] with init = 0 and (+)

Step 1: (0 + 1) = 1
Step 2: (1 + 2) = 3
Step 3: (3 + 3) = 6

Evaluation: (((0 + 1) + 2) + 3)
```

```java
// foldLeft takes (accumulator, element)
int sum = foldable.foldLeft(0, (acc, x) -> acc + x);
```

### 2. `foldRight` (Right-Associative)

`foldRight` processes elements from end to start (right-to-left).

```
Elements: [1, 2, 3] with init = 0 and (+)

Evaluation: (1 + (2 + (3 + 0)))
```

```java
// foldRight takes (element, accumulator)
int sum = foldable.foldRight(0, (x, acc) -> x + acc);
```

---

## Practical Examples

### Example 1: String Formatting and Concatenation

Left fold is natural for appending items to an accumulator:

```java
import org.quurz.foomp.base.types.Foldable;

public class FoldExamples {
    public static String buildCsv(Foldable<String> items) {
        return items.foldLeft(new StringBuilder(), (sb, item) -> {
            if (!sb.isEmpty()) {
                sb.append(", ");
            }
            sb.append(item);
            return sb;
        }).toString();
    }
}
```

### Example 2: Reversing vs. Preserving Order

Notice the effect of `foldLeft` versus `foldRight` when building a new list:

```java
import java.util.ArrayList;
import java.util.List;

// Reversing order using foldLeft:
List<Integer> reversed = numbers.foldLeft(new ArrayList<>(), (acc, x) -> {
    acc.addFirst(x);
    return acc;
});

// Preserving order using foldRight:
List<Integer> preserved = numbers.foldRight(new ArrayList<>(), (x, acc) -> {
    acc.addFirst(x);
    return acc;
});
```

### Example 3: Counting & Predicate Evaluation

You can derive almost all collection aggregations from folds:

```java
// Count elements
int count = items.foldLeft(0, (acc, item) -> acc + 1);

// All match (without early termination)
boolean allPositive = numbers.foldLeft(true, (acc, x) -> acc && x > 0);
```

---

## Summary Checklist

- Use **`foldLeft`** when:
  - You want eager, linear, iterative accumulation.
  - Tail recursion / loops without deep call stacks are preferred.
  - Reducer takes `(acc, item) -> acc`.
- Use **`foldRight`** when:
  - Building inductive/recursive structures from the right.
  - Association order matters (e.g. `a ⊕ (b ⊕ (c ⊕ init))`).
  - Reducer takes `(item, acc) -> acc`.
