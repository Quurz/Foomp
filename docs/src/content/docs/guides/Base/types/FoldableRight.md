---
title: FoldableRight Guide
description: Practical recipes, list construction, and algebraic idioms with FoldableRight in Foomp.
---

`FoldableRight<A>` provides right-to-left reduction across data structures. It is particularly valuable for inductive list reconstruction, preserving element sequence order, and evaluating right-associative operators.

---

## The Right Fold Mental Model

In a right fold, the initial accumulator `init` replaces the empty terminal node at the far right of the structure:

```text
Collection:           [item1, item2, item3]
Initial accumulator:  init
Step 1:               acc1 = f(item3, init)
Step 2:               acc2 = f(item2, acc1)
Step 3:               acc3 = f(item1, acc2)
Final Result:         acc3
```

Notice that the combining function takes `(currentElement, accumulator)` in that order.

---

## Practical Examples

### 1. Reconstructing a List in Original Order

Unlike `foldLeft` (which naturally reverses an inductive sequence if elements are prepended), `foldRight` naturally preserves the original element order:

```java
import org.quurz.foomp.base.types.FoldableRight;
import java.util.LinkedList;

public static <T> LinkedList<T> copyInOrder(FoldableRight<T> foldable) {
    return foldable.foldRight(new LinkedList<T>(), (item, list) -> {
        list.addFirst(item);
        return list;
    });
}
```

### 2. Filtering Elements into a New Collection

```java
import org.quurz.foomp.base.functions.Pred;
import java.util.LinkedList;

public static <T> LinkedList<T> filter(FoldableRight<T> foldable, Pred<T> predicate) {
    return foldable.foldRight(new LinkedList<T>(), (item, list) -> {
        if (predicate.test(item)) {
            list.addFirst(item);
        }
        return list;
    });
}
```

### 3. Right-Associative Expression Evaluation

For right-associative operations like exponentiation ($2^{3^2} = 2^{(3^2)} = 2^9 = 512$):

```java
// Expression: 2 ^ (3 ^ 2)
// Input foldable: [2, 3] with init 2
FoldableRight<Double> exponents = ...;

double result = exponents.foldRight(2.0, (base, exp) -> Math.pow(base, exp));
```

---

## Implementing `FoldableRight` on Custom Data Structures

Here is an implementation example for a singly-linked immutable functional list:

```java
import org.quurz.foomp.base.types.FoldableRight;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.Objects;
import java.util.function.BiFunction;

public sealed interface FList<A> extends FoldableRight<A> {

    record Nil<A>() implements FList<A> {
        @Override
        public <B> @NonNull B foldRight(
            final @NonNull B init,
            final @NonNull BiFunction<? super A, ? super B, ? extends B> function
        ) {
            Objects.requireNonNull(init, "init must not be null");
            Objects.requireNonNull(function, "function must not be null");
            return init;
        }
    }

    record Cons<A>(@NonNull A head, @NonNull FList<A> tail) implements FList<A> {
        @Override
        public <B> @NonNull B foldRight(
            final @NonNull B init,
            final @NonNull BiFunction<? super A, ? super B, ? extends B> function
        ) {
            Objects.requireNonNull(init, "init must not be null");
            Objects.requireNonNull(function, "function must not be null");

            B tailResult = tail.foldRight(init, function);
            return Objects.requireNonNull(
                function.apply(head, tailResult),
                "foldRight function must return non-null accumulator"
            );
        }
    }
}
```
