---
title: Continuation Guide
description: Practical developer guide for Continuation-Passing Style (CPS) and non-local control flow using Continuation and call/cc in Foomp.
---

`Continuation<A, R>` represents a deferred computation in Continuation-Passing Style (CPS). It enables expressive control flow, inversion of control, and early exits through $\text{call/cc}$ (`callCurrentCont`).

---

## When to Use `Continuation<A, R>`

* **Continuation-Passing Style (CPS):** Passing computations explicitly as functions expecting a remainder-of-computation callback.
* **Non-Local Control Flow / Short-Circuiting:** Implementing advanced escape routes or abort operations without throwing exceptions using `callCurrentCont`.
* **Asynchronous or Generator Patterns:** Suspending, composing, and resuming execution branches in pure functional code.

---

## Examples

### 1. Basic CPS Pipeline with `map` and `flatMap`

In standard computation, operations return values immediately. In CPS, each step passes intermediate results to the next continuation:

```java title="ContinuationBasicExample.java"
import org.quurz.foomp.base.util.Continuation;

import static org.quurz.foomp.base.util.Continuation.pureContinuation;

public class ContinuationBasicExample {
    public static void main(String[] args) {
        // Build a CPS pipeline: 5 -> (* 2) -> (+ 1) -> (* 10)
        Continuation<Integer, Integer> pipeline =
            Continuation.<Integer, Integer>pureContinuation(5)
                .map(i -> i * 2)                                 // 10
                .flatMap(i -> pureContinuation(i + 1))           // 11
                .map(i -> i * 10);                               // 110

        // Supply the final continuation consumer (x -> x)
        int finalResult = pipeline.apply(x -> x);
        System.out.println("Result: " + finalResult); // 110
    }
}
```

---

### 2. Early Exit with `callCurrentCont` (call/cc)

`callCurrentCont` captures the current execution context and provides an escape function `k`. Invoking `k.apply(value)` aborts the rest of the surrounding continuation chain and immediately delivers `value` to the top-level receiver:

```java title="ContinuationCallCcExample.java"
import org.quurz.foomp.base.util.Continuation;

import static org.quurz.foomp.base.util.Continuation.callCurrentCont;
import static org.quurz.foomp.base.util.Continuation.pureContinuation;

public class ContinuationCallCcExample {

    static Continuation<String, String> processOrder(int quantity) {
        return callCurrentCont(k -> {
            if (quantity <= 0) {
                // Short-circuit! Returns immediately, skipping all downstream steps
                return k.apply("Order aborted: Quantity must be greater than zero");
            }
            if (quantity > 100) {
                // Short-circuit for bulk order review
                return k.apply("Order redirected: Bulk orders require manager approval");
            }

            // Normal processing branch
            return pureContinuation("Order accepted for quantity " + quantity)
                .map(msg -> msg + " (Standard Delivery)");
        });
    }

    public static void main(String[] args) {
        System.out.println(processOrder(0).apply(x -> x));    // Order aborted: Quantity must be greater than zero
        System.out.println(processOrder(500).apply(x -> x));  // Order redirected: Bulk orders require manager approval
        System.out.println(processOrder(5).apply(x -> x));    // Order accepted for quantity 5 (Standard Delivery)
    }
}
```

---

### 3. Sequencing with `.then(...)`

You can sequence two independent continuations where the result of the first step is ignored:

```java title="ContinuationThenExample.java"
import org.quurz.foomp.base.util.Continuation;

import static org.quurz.foomp.base.util.Continuation.pureContinuation;

public class ContinuationThenExample {
    public static void main(String[] args) {
        Continuation<String, String> step1 = pureContinuation("Initial Step");
        Continuation<String, String> step2 = pureContinuation("Final Step");

        Continuation<String, String> combined = step1.then(step2);

        System.out.println("Combined result: " + combined.apply(x -> x)); // Final Step
    }
}
```

---

## Best Practices

:::tip[CPS Guidance]
* **`apply(x -> x)` for Direct Extraction:** When the value type `A` matches the result type `R`, running the continuation with the identity function `Function.identity()` directly unwraps the computed value.
* **Avoid Unnecessary CPS for Simple Synchronous Logic:** Continuations add abstraction overhead. Use standard functional pipelines or `Attempt`/`Result` unless you specifically require non-local control flow (`callCurrentCont`) or custom callback suspension.
:::
