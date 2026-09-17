---
title: "@UnwindingOperation Guide"
description: Architectural guide on annotating and identifying evaluation boundaries with @UnwindingOperation in Foomp.
---

In modern functional Java applications, code often alternates between **definition** (constructing lazy computations, pipelines, and streams) and **execution** (triggering side effects, running pipelines, and forcing materialization).

The `@UnwindingOperation` annotation provides an explicit, self-documenting contract for methods that trigger execution.

---

## Why Annotate Evaluation Points?

1. **Avoid Unexpected Latency Spikes:** Callers can instantly see which method calls incur actual computation cost or blocking I/O versus cheap lambda composition.
2. **Clarify Side Effect Boundaries:** For stateful monads (such as `Stateful` or `Continuation`), knowing where the state transitions actually execute prevents hidden bugs.
3. **Enhance Code Reviews:** Reviewers can quickly distinguish lazy transformations (`map`, `flatMap`, `filter`) from terminal operations.

---

## Practical Examples

### 1. Annotating Custom Container Evaluation

When creating custom functional types, mark methods that resolve internal suppliers or thunks:

```java title="LazyThunk.java"
import org.quurz.foomp.base.types.UnwindingOperation;
import java.util.function.Supplier;

public class LazyThunk<T> {
    private final Supplier<T> supplier;
    private T cachedValue;

    public LazyThunk(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    // Lazy composition - no annotation needed
    public <R> LazyThunk<R> map(java.util.function.Function<T, R> fn) {
        return new LazyThunk<>(() -> fn.apply(get()));
    }

    // Materialization point - annotated!
    @UnwindingOperation(comment = "Evaluates supplier on first invocation and caches result")
    public synchronized T get() {
        if (cachedValue == null) {
            cachedValue = supplier.get();
        }
        return cachedValue;
    }
}
```

---

### 2. State Monad / Runner Execution

```java title="WorkflowEngine.java"
import org.quurz.foomp.base.types.UnwindingOperation;

public class WorkflowEngine<S, A> {

    // Runs the workflow and computes final state
    @UnwindingOperation(comment = "Executes the compiled workflow against the initial state")
    public A run(S initialState) {
        // ... executes staged workflow steps ...
        return null;
    }
}
```

---

## Difference: `@UnwindingOperation` vs `@MutatingOperation`

| Marker | Focus | Typical Scenario |
| :--- | :--- | :--- |
| **`@UnwindingOperation`** | **Computation / Materialization** | Forcing lazy thunks, running state machines, draining streams |
| **`@MutatingOperation`** | **State Mutation** | Mutating internal mutable state (e.g. `list.add(...)`, `map.put(...)`) |

Both annotations can occasionally appear together if an unwinding operation also mutates internal cache state upon evaluation.

---

## Best Practices

:::tip[Architectural Rules]
* **Do Not Annotate Factory / Deferral Methods:** Methods like `Eval.later(...)` or `Attempt.attempt(...)` set up laziness; do not annotate them.
* **Annotate Terminal Stream / Pipeline Methods:** Methods like `toList()`, `reduce()`, or `runState(...)` should be annotated.
* **Use Meaningful Comments:** Supply the `comment` attribute when the materialization mechanism involves concurrency or external I/O.
:::
