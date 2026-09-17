---
title: Working with H2Mappable
description: Practical guide and functor patterns using H2Mappable over binary higher-kinded structures in Foomp.
---

import { Aside, Tabs, TabItem } from '@astrojs/starlight/components';

`H2Mappable<WT, A, R>` provides the **Rank-2 Functor** interface in Foomp. It allows transforming inner values (`A -> B`) of binary structures (`Higher2<WT, A, R>`) without modifying the accompanying type `R`.

---

## Functor Model for Binary Constructors

In standard functional programming, a functor maps over a single type parameter. For binary containers like `Stateful<S, A>` or `Continuation<R, A>`, `H2Mappable` fixes the auxiliary parameter `R` and treats the container as a covariant functor over `A`:

```
Higher2<WT, A, R>   +   (A -> B)   ===>   Higher2<WT, B, R>
```

---

## Practical Examples

### 1. Value Mapping in Stateful Computations

With `Stateful<S, A>`, `.map(...)` changes the resulting value of the state computation while preserving state transitions unchanged:

```java title="StatefulMapExample.java"
import org.quurz.foomp.base.util.Stateful;

public class StatefulMapExample {

    public static void main(String[] args) {
        // A stateful computation that calculates length of input state (String)
        Stateful<String, Integer> lengthFinder = Stateful.stateful(state ->
            Stateful.transition(state.length(), state.toUpperCase())
        );

        // Map integer length to a formatted string
        Stateful<String, String> formatted = lengthFinder.map(len -> "Length is " + len);

        var result = formatted.run("foomp");
        System.out.println("Computed Value: " + result.getValue()); // "Length is 5"
        System.out.println("New State:      " + result.getState()); // "FOOMP"
    }
}
```

---

### 2. Transforming Continuation Values

With `Continuation<R, A>`, mapping allows modifying the produced value without changing the eventual return type `R`:

```java title="ContinuationMapExample.java"
import org.quurz.foomp.base.util.Continuation;

public class ContinuationMapExample {

    public static void main(String[] args) {
        // A continuation producing integer 50 with final String answer
        Continuation<String, Integer> baseCont = Continuation.pure(50);

        // Map the integer to add 10% tax
        Continuation<String, Double> withTax = baseCont.map(val -> val * 1.10);

        // Execute with terminal observer
        String output = withTax.run(total -> String.format("Total: $%.2f", total));
        System.out.println(output); // "Total: $55.00"
    }
}
```

---

## Comparison: `Mappable` vs `H2Mappable`

| Aspect | `Mappable<WT, A>` | `H2Mappable<WT, A, R>` |
| :--- | :--- | :--- |
| **Arity** | Unary (`Higher1<WT, A>`) | Binary (`Higher2<WT, A, R>`) |
| **Examples** | `Maybe<A>`, `Sequence<A>`, `Attempt<A>` | `Stateful<S, A>`, `Continuation<R, A>` |
| **Preservation** | Witness `WT` | Witness `WT` and second type `R` |
| **Contract** | `A -> B` within `Higher1` | `A -> B` within `Higher2` |

---

## Tips & Best Practices

:::tip[Composition via andThen]
Because `map` produces standard higher-kinded structures, multiple mappings can be chained directly (`comp.map(f).map(g)`) or combined in advance via `f.andThen(g)` for performance optimization.
:::

:::caution[Preserve Purity]
Mapping functions should be referentially transparent and avoid side effects. If side effects or conditional branching are needed, consider monadic operations via `flatMap` (`H2Bindable`).
:::
