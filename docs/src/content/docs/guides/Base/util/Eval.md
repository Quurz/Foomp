---
title: Eval Guide
description: Practical developer guide for managing eager, lazy, and memoized computations with Eval in Foomp.
---

`Eval<A>` is a monadic data structure that encapsulates evaluation strategies. It allows developers to control precisely **when** and **how often** an expression is evaluated.

---

## Evaluation Strategies

| Strategy | Factory / Method | Evaluation Timing | Caching / Memoization | Typical Use Case |
| :--- | :--- | :--- | :--- | :--- |
| **Eager** (`Now`) | `Eval.evalNow(value)`, `.now()` | Immediate at construction | Stored directly | Constants, precomputed results |
| **Lazy Memoized** (`Later`) | `Eval.evalLater(value)`, `.later()` | On first access (`get()`) | Cached after first evaluation | Expensive single computations, configuration loading |
| **Lazy Non-Memoized** (`Always`) | `Eval.evalAlways(value)`, `.always()` | On every access (`get()`) | No caching (recomputed each time) | Time-sensitive values, random generators, dynamic sensors |

---

## Examples

### 1. Basic Evaluation Modes

```java title="EvalModesExample.java"
import org.quurz.foomp.base.util.Eval;

import static org.quurz.foomp.base.util.Eval.*;

public class EvalModesExample {
    public static void main(String[] args) {
        // Eager: computed immediately
        Eval<Integer> eager = evalNow(10 + 20);
        System.out.println("Eager value: " + eager.get()); // 30

        // Lazy memoized: computed only once upon first access
        Eval<String> lazyCached = evalLater("expensive-result-" + System.nanoTime());
        System.out.println("First get: " + lazyCached.get());
        System.out.println("Second get (cached): " + lazyCached.get());

        // Lazy non-memoized: recomputed on every access
        Eval<Long> lazyDynamic = evalAlways(System.currentTimeMillis());
        System.out.println("First call: " + lazyDynamic.get());
        System.out.println("Second call: " + lazyDynamic.get());
    }
}
```

---

### 2. Functor & Monadic Composition

`map` and `flatMap` operations preserve the evaluation strategy:

```java title="EvalCompositionExample.java"
import org.quurz.foomp.base.util.Eval;

import static org.quurz.foomp.base.util.Eval.*;

public class EvalCompositionExample {
    public static void main(String[] args) {
        Eval<Integer> base = evalLater(21);

        // Transformation is deferred lazily
        Eval<Integer> doubled = base.map(n -> {
            System.out.println("Doubling " + n);
            return n * 2;
        });

        System.out.println("Before get()");
        // The mapping executes now on first get()
        System.out.println("Result: " + doubled.get()); // 42
        // Subsequent get() returns cached result without re-executing
        System.out.println("Cached: " + doubled.get()); // 42
    }
}
```

---

### 3. Converting Between Strategies

You can seamlessly change evaluation modes via `.now()`, `.later()`, and `.always()`:

```java title="EvalConversionExample.java"
import org.quurz.foomp.base.util.Eval;

import static org.quurz.foomp.base.util.Eval.*;

public class EvalConversionExample {
    public static void main(String[] args) {
        Eval<String> dynamic = evalAlways("token-" + System.nanoTime());

        // Materialize and fix the value
        Eval<String> snapshot = dynamic.now();

        System.out.println("Dynamic 1: " + dynamic.get());
        System.out.println("Dynamic 2: " + dynamic.get());

        System.out.println("Snapshot 1: " + snapshot.get());
        System.out.println("Snapshot 2: " + snapshot.get()); // identical to Snapshot 1
    }
}
```

---

## Best Practices

:::tip[Eval Best Practices]
* **Lazy Computation Chains:** Use `Eval.Later` when chaining transformations on values that might not be needed (e.g., branching or short-circuiting logic).
* **Side-Effect Free:** Keep mapped and flatMapped functions pure when using memoization (`Later`), as side effects will only run on the initial computation.
* **Stack Safety:** For deeply recursive operations, combine `Eval` with `Trampoline` to prevent stack overflow errors.
:::
