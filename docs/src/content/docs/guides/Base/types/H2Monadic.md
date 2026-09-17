---
title: Working with H2Monadic
description: Guide and architectural patterns for using and implementing full Rank-2 Monads with H2Monadic in Foomp.
---

import { Aside, Tabs, TabItem } from '@astrojs/starlight/components';

`H2Monadic<WT, A, R>` is the unified abstraction in Foomp for full **Rank-2 Monads** over two-parameter higher-kinded structures (`Higher2<WT, A, R>`).

It seamlessly blends Functor (`map`), Applicative (`applyTo`), and Monad (`flatMap`) semantics while preserving context `R`.

---

## Why Rank-2 Monads?

Many fundamental computational patterns inherently require two type parameters:

1. **State Monad (`Stateful<S, A>`):** Tracks and threads state transitions of type `S` while returning values of type `A`.
2. **Continuation Monad (`Continuation<R, A>`):** Encapsulates non-local control flow and suspended computations producing `A` with an eventual result `R`.

By extending `H2Monadic`, these classes offer a uniform, algebraic API across different computational contexts.

---

## Practical Examples

### 1. Fluent Pipeline Chaining with Stateful

`H2Monadic` allows chaining transformations (`map`) and state updates (`flatMap`) freely in a single expressive flow:

```java title="StatefulPipeline.java"
import org.quurz.foomp.base.util.Stateful;

public class StatefulPipeline {

    public static void main(String[] args) {
        // Increment state and yield previous state
        Stateful<Integer, Integer> step = Stateful.stateful(state ->
            Stateful.transition(state, state + 10)
        );

        // Chain using map and flatMap
        Stateful<Integer, String> computation = step
            .map(val -> "Initial value: " + val)
            .flatMap(msg -> Stateful.stateful(state ->
                Stateful.transition(msg + " | Final State: " + state, state * 2)
            ));

        var outcome = computation.run(5);
        System.out.println("Result: " + outcome.getValue());
        // "Initial value: 5 | Final State: 15"
        System.out.println("State:  " + outcome.getState());
        // 30
    }
}
```

---

### 2. Continuation Transformations

Using `H2Monadic` capabilities with CPS workflows:

```java title="ContinuationPipeline.java"
import org.quurz.foomp.base.util.Continuation;

public class ContinuationPipeline {

    public static void main(String[] args) {
        Continuation<String, Integer> pipeline = Continuation.<String, Integer>pure(10)
            .map(x -> x + 5)
            .flatMap(x -> Continuation.pure(x * 2));

        String finalResult = pipeline.run(res -> "Computed outcome is: " + res);
        System.out.println(finalResult); // "Computed outcome is: 30"
    }
}
```

---

## The Hierarchy at a Glance

| Interface | Primary Method | Algebraic Role |
| :--- | :--- | :--- |
| **[`H2Mappable`](/reference/base/types/h2mappable/)** | `.map(A -> B)` | Functor (Value transformation) |
| **[`H2Appliable`](/reference/base/types/h2appliable/)** | `.applyTo(Higher2<WT, Fun<A, B>, R>)` | Applicative (Contextual application) |
| **[`H2Bindable`](/reference/base/types/h2bindable/)** | `.flatMap(A -> Higher2<WT, B, R>)` | Monad (Sequencing & chaining) |
| **`H2Monadic`** | All above | Complete Rank-2 Monad |

---

## Summary & Best Practices

:::tip[Generic Programming with H2Monadic]
When writing higher-order functional utilities that need to operate generically over binary monads (like `Stateful` or `Continuation`), accept `<M extends H2Monadic<WT, A, R>>` to ensure full functorial and monadic capabilities.
:::

:::note[Witness Types]
All `H2Monadic` instances use witness types (`WT`) to enable safe higher-kinded type simulation in Java without losing static type safety.
:::
