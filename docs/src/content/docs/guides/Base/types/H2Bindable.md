---
title: Working with H2Bindable
description: Practical recipes and monadic workflows using H2Bindable with binary type constructors in Foomp.
---

import { Aside, Tabs, TabItem } from '@astrojs/starlight/components';

`H2Bindable<WT, A, R>` enables monadic composition (`flatMap`) over binary higher-kinded containers (`Higher2<WT, A, R>`). While keeping the second type parameter `R` fixed, computations can be chained seamlessly.

---

## What is H2Bindable?

When working with structures with two type parameters—such as state monads `Stateful<S, A>` or continuation monads `Continuation<R, A>`—we often want to bind over the produced value `A` without changing the underlying environment or answer type (`S` or `R`).

```
Higher2<WT, A, R>   +   (A -> Higher2<WT, B, R>)   ===>   Higher2<WT, B, R>
```

<Aside type="note">
`H2Bindable` is parameterized over `<WT, A, R>`. Notice that the second parameter `R` remains constant across all chained computations in the pipeline.
</Aside>

---

## Practical Examples

### 1. Stateful Monad Sequencing

In `Stateful<S, A>`, computations pass state implicitly from one step to the next while yielding intermediate values:

```java title="StatefulBindExample.java"
import org.quurz.foomp.base.util.Stateful;

public class StatefulBindExample {

    public static void main(String[] args) {
        // Initial stateful step: produces integer 10, updates counter state
        Stateful<Integer, String> step1 = Stateful.stateful(count -> 
            Stateful.transition("First: " + count, count + 1)
        );

        // Monadic bind to step 2 using H2Bindable contract
        Stateful<Integer, String> pipeline = step1.flatMap(msg1 ->
            Stateful.stateful(count ->
                Stateful.transition(msg1 + " -> Second: " + count, count * 2)
            )
        );

        // Run the pipeline with initial state 1
        var result = pipeline.run(1);
        System.out.println("Result Value: " + result.getValue()); // "First: 1 -> Second: 2"
        System.out.println("Final State:  " + result.getState()); // 4
    }
}
```

---

### 2. Continuation Workflows

`Continuation<R, A>` encapsulates computations in Continuation-Passing Style (CPS) where `R` is the final answer type:

```java title="ContinuationBindExample.java"
import org.quurz.foomp.base.util.Continuation;

public class ContinuationBindExample {

    public static void main(String[] args) {
        // Computation that returns 21 in CPS
        Continuation<String, Integer> comp1 = Continuation.pure(21);

        // Monadic chain: doubles the value and converts to uppercase string result
        Continuation<String, Integer> comp2 = comp1.flatMap(val -> 
            Continuation.pure(val * 2)
        );

        // Finalize continuation with answer function (Integer -> String)
        String finalAnswer = comp2.run(ans -> "Computation result: " + ans);
        System.out.println(finalAnswer); // "Computation result: 42"
    }
}
```

---

## Difference Between `Bindable` and `H2Bindable`

| Feature | `Bindable<WT, A>` | `H2Bindable<WT, A, R>` |
| :--- | :--- | :--- |
| **Arity** | Rank-1 (`Higher1<WT, A>`) | Rank-2 (`Higher2<WT, A, R>`) |
| **Context** | Unary container (`Maybe`, `Sequence`) | Binary container (`Stateful`, `Continuation`) |
| **Preservation** | Only `WT` preserved | Both `WT` and `R` preserved |
| **Operation** | `A -> Higher1<WT, B>` | `A -> Higher2<WT, B, R>` |

---

## Best Practices

:::tip[Keep Transformations Pure]
Ensure functions passed to `flatMap` do not introduce side effects outside their encapsulated monadic model (e.g., using `Stateful` for state rather than mutating global variables).
:::

:::caution[Null Checks]
Functions passed to `flatMap` must return valid `Higher2` instances. Returning `null` triggers an immediate `NullPointerException`.
:::
