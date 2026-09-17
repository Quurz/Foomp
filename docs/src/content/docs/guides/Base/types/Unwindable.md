---
title: Unwindable Guide
description: Practical developer guide for materializing deferred and lazy computations with Unwindable in Foomp.
---

Lazy evaluation and deferred execution are powerful tools in functional programming for saving CPU cycles and deferring costly operations. However, there are scenarios where you want to explicitly **force** (materialize) computations—such as before crossing network boundaries, serializing objects, or freezing state.

The `Unwindable<SELF>` interface provides the standardized method `.unwind()` for this exact purpose.

---

## When to Use `unwind()`

* **Freezing Dynamic State:** Converting a dynamic or time-sensitive supplier (`Provider<A>`) into a constant value.
* **Pre-computation before Serialization:** Forcing all deferred calculations in a container (like `Box` or `Eval`) so the structure can be safely serialized or logged.
* **Ensuring Thread Safety:** Resolving lazy references on a single thread before passing the materialized container across multiple concurrent threads.

---

## Examples

### 1. Freezing a Lazy `Provider`

```java title="UnwindProviderExample.java"
import org.quurz.foomp.base.functions.Provider;
import java.time.Instant;

public class UnwindProviderExample {
    public static void main(String[] args) throws InterruptedException {
        // Dynamic provider returning current timestamp on each call
        Provider<Instant> clock = Instant::now;

        System.out.println("Dynamic 1: " + clock.get());
        Thread.sleep(50);
        System.out.println("Dynamic 2: " + clock.get()); // Changed!

        // Materialize/Freeze into a constant provider
        Provider<Instant> frozenClock = clock.unwind();

        System.out.println("Frozen 1:  " + frozenClock.get());
        Thread.sleep(50);
        System.out.println("Frozen 2:  " + frozenClock.get()); // Identical!
    }
}
```

---

### 2. Materializing a Lazy `Box`

`Box<A>` holds a lazy value. Calling `.unwind()` computes the value and replaces the internal supplier with a constant representation:

```java title="UnwindBoxExample.java"
import org.quurz.foomp.base.util.Box;

public class UnwindBoxExample {
    public static void main(String[] args) {
        Box<String> lazyGreeting = Box.box(() -> {
            System.out.println("--> Performing heavy calculation...");
            return "Hello, Functional World!";
        });

        System.out.println("Box created, not yet unwound.");

        // Unwind evaluates the computation once
        Box<String> materialized = lazyGreeting.unwind();
        System.out.println("Materialized value: " + materialized.get());

        // Subsequent unwinds or reads are instant
        materialized.unwind();
    }
}
```

---

### 3. Recursive Unwinding in Tuples

When multi-value containers (like `Tuple2`) contain `Unwindable` elements, calling `unwind()` recursively forces every contained lazy component:

```java title="UnwindTupleExample.java"
import org.quurz.foomp.base.functions.Provider;
import org.quurz.foomp.base.util.Tuple2;

public class UnwindTupleExample {
    public static void main(String[] args) {
        Tuple2<Provider<Integer>, String> tuple = Tuple2.tuple2(
            () -> 42 * 2,
            "metadata"
        );

        // Unwinding the tuple unwinds the nested provider
        Tuple2<Provider<Integer>, String> unwoundTuple = tuple.unwind();
        System.out.println("Unwound provider value: " + unwoundTuple.get1().get());
    }
}
```

---

## Best Practices

:::tip[Idempotency and Purity]
* **Keep `unwind()` Idempotent:** Repeated calls to `unwind()` on an already unwound container should have no additional side effects.
* **Retain Immutability:** `.unwind()` should return a stable representation without mutating externally visible state unsafely.
* **Document Evaluation Points:** Annotate custom methods that force computation with [`@UnwindingOperation`](/reference/base/types/unwindingoperation/).
:::
