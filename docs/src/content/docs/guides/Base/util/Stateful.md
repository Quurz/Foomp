---
title: Stateful<A, S> Guide
description: Practical guide and design patterns for the State Monad in Foomp.
---

import { Aside } from '@astrojs/starlight/components';

`Stateful<A, S>` provides a functional, pure approach to state management by modeling computations as transitions `S -> Tuple2<A, S>`. Instead of mutating shared variables, computations pass an updated immutable state forward explicitly through monadic chaining.

---

## Core Concepts

In pure functional programming, state transitions do not modify data in place. Instead, a stateful computation receives an initial state `s0` and yields both a result `a` and an updated state `s1`:

```
s0 ───► [ Stateful<A, S> ] ───► Tuple2(value: a, state: s1)
```

By using `flatMap`, intermediate states are automatically threaded through sequential steps without manual plumbing:

```
s0 ──► [ Step 1 ] ──► (a, s1) ──► [ Step 2(a) ] ──► (b, s2)
```

---

## Practical Examples

### 1. Basic State Manipulation (`getState`, `modifyState`, `putState`)

```java title="StateBasicsExample.java"
import org.quurz.foomp.base.util.Nothing;
import org.quurz.foomp.base.util.Stateful;
import org.quurz.foomp.base.util.Tuple2;

import static org.quurz.foomp.base.util.Stateful.getState;
import static org.quurz.foomp.base.util.Stateful.modifyState;
import static org.quurz.foomp.base.util.Stateful.putState;

public class StateBasicsExample {
    public static void main(String[] args) {
        // Read current state
        Stateful<Integer, Integer> reader = getState();
        System.out.println("Current state: " + reader.execValue(10)); // 10

        // Increment state by 1
        Stateful<Nothing, Integer> incrementer = modifyState(s -> s + 1);
        System.out.println("New state: " + incrementer.execState(10)); // 11

        // Overwrite state
        Stateful<Nothing, Integer> setter = putState(42);
        System.out.println("Set state: " + setter.execState(10)); // 42
    }
}
```

---

### 2. Monadic Chaining (`flatMap` and `map`)

```java title="RandomGeneratorExample.java"
import org.quurz.foomp.base.util.Stateful;
import org.quurz.foomp.base.util.Tuple2;

import static org.quurz.foomp.base.util.Stateful.stateful;
import static org.quurz.foomp.base.util.Tuple2.tuple2;

public class RandomGeneratorExample {

    // A deterministic pseudo-random number step: Seed -> Tuple2<Value, NextSeed>
    public static Stateful<Integer, Long> nextInt() {
        return stateful(seed -> {
            long nextSeed = (seed * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
            int value = (int) (nextSeed >>> 16);
            return tuple2(value, nextSeed);
        });
    }

    public static void main(String[] args) {
        // Compose two random numbers into a pair
        Stateful<String, Long> pairGenerator = nextInt().flatMap(first ->
            nextInt().map(second -> "Generated: " + first + ", " + second)
        );

        // Run the chained stateful computation starting with seed 12345L
        Tuple2<String, Long> result = pairGenerator.runState(12345L);
        System.out.println(result.get());   // "Generated: <int1>, <int2>"
        System.out.println(result.get2());  // Final updated seed
    }
}
```

---

### 3. Execution Strategies (`runState`, `execValue`, `execState`)

```java title="ExecutionExample.java"
import org.quurz.foomp.base.util.Stateful;
import org.quurz.foomp.base.util.Tuple2;

import static org.quurz.foomp.base.util.Stateful.stateful;
import static org.quurz.foomp.base.util.Tuple2.tuple2;

public class ExecutionExample {
    public static void main(String[] args) {
        Stateful<String, Integer> counter = stateful(s -> tuple2("Count: " + s, s + 1));

        // 1. Full execution: returns Tuple2<Value, State>
        Tuple2<String, Integer> full = counter.runState(5);
        System.out.println(full.get());   // "Count: 5"
        System.out.println(full.get2());  // 6

        // 2. Value-only execution: extracts result
        String val = counter.execValue(5);
        System.out.println(val);          // "Count: 5"

        // 3. State-only execution: extracts final state
        int state = counter.execState(5);
        System.out.println(state);        // 6
    }
}
```

---

## Best Practices

:::tip[Keep State Immutable]
Always use immutable records, values, or primitives as the state type `S`. Mutating an object within `modifyState` defeats purity and breaks referential transparency.
:::

:::note[State vs. Mutating Variables]
Use `Stateful<A, S>` when you need:
1. Deterministic, testable transitions without hidden side effects.
2. Composable pipeline steps that produce values while accumulating context/metrics.
3. Safe backtracking, simulation, or transaction rollbacks by storing earlier states.
:::
