---
title: Task<A> Guide
description: Asynchronous computation patterns, monadic task composition, and concurrency in Foomp.
---

import { Aside } from '@astrojs/starlight/components';

`Task<A>` represents a **cold asynchronous computation**. Unlike Java's `CompletableFuture` (which is eager and starts immediately upon instantiation), a `Task` is a pure description of work that can be transformed, composed, sequenced, and zipped before any background thread is actually spawned.

When triggered, a `Task` produces a [`Result<A>`](/reference/base/util/result/), automatically capturing thrown runtime and checked exceptions into `Result.failure` without throwing uncaught exceptions to caller threads.

---

## Why Use Task?

* **Cold / Lazy Execution**: Create and assemble pipelines without running them prematurely.
* **Automatic Error Containment**: Exceptions during execution are caught and wrapped in `Result.failure`.
* **Flexible Thread Pool Binding**: Bind individual sub-tasks to custom thread pools via `executeOn(Executor)` or supply an `Executor` at the trigger site `runAsync(Executor)`.
* **Monadic and Applicative Composition**: Compose pipelines using `map`, `flatMap`, and `zip`.

---

## Practical Examples

### 1. Creating and Running Tasks

```java title="TaskBasicsExample.java"
import org.quurz.foomp.base.util.Result;
import org.quurz.foomp.base.util.Task;

import java.util.concurrent.CompletableFuture;

import static org.quurz.foomp.base.util.Task.task;
import static org.quurz.foomp.base.util.Task.taskFrom;

public class TaskBasicsExample {
    public static void main(String[] args) {
        // Eager completed task
        Task<String> immediate = task("Hello Foomp");

        // Lazy task from supplier (evaluated only when runAsync is called)
        Task<Integer> asyncCalculation = taskFrom(() -> {
            System.out.println("Calculating on thread: " + Thread.currentThread().getName());
            return 42 * 2;
        });

        // Trigger execution
        CompletableFuture<Result<Integer>> future = asyncCalculation.runAsync();

        future.thenAccept(result -> {
            switch (result) {
                case Result.Success<Integer> succ -> System.out.println("Result: " + succ.getValue());
                case Result.Failure<Integer> fail -> System.err.println("Error: " + fail.getException().getMessage());
            }
        }).join();
    }
}
```

---

### 2. Monadic Pipelines with `flatMap`

```java title="SequentialPipelineExample.java"
import org.quurz.foomp.base.util.Result;
import org.quurz.foomp.base.util.Task;

import static org.quurz.foomp.base.util.Task.taskFrom;

public class SequentialPipelineExample {

    record User(String id, String name) {}
    record Order(String orderId, String userId, double total) {}

    public static Task<User> fetchUser(String userId) {
        return taskFrom(() -> new User(userId, "Alice"));
    }

    public static Task<Order> fetchLatestOrder(User user) {
        return taskFrom(() -> new Order("ORD-99", user.id(), 149.95));
    }

    public static void main(String[] args) {
        Task<String> summaryTask = fetchUser("usr_123")
                .flatMap(user -> fetchLatestOrder(user))
                .map(order -> "Order " + order.orderId() + " total: $" + order.total());

        Result<String> result = summaryTask.runAsync().join();
        if (result.isSuccess()) {
            System.out.println(result.getValue());
        }
    }
}
```

---

### 3. Parallel Combination with `zip`

```java title="ParallelZipExample.java"
import org.quurz.foomp.base.util.Result;
import org.quurz.foomp.base.util.Task;

import java.util.concurrent.Executors;

import static org.quurz.foomp.base.util.Task.taskFrom;

public class ParallelZipExample {
    public static void main(String[] args) {
        var ioPool = Executors.newCachedThreadPool();

        Task<String> taskA = taskFrom(() -> {
            Thread.sleep(100);
            return "Profile Data";
        }).executeOn(ioPool);

        Task<Integer> taskB = taskFrom(() -> {
            Thread.sleep(100);
            return 42;
        }).executeOn(ioPool);

        // Run taskA and taskB in parallel, combining results when both finish
        Task<String> combined = taskA.zip(taskB, (profile, count) ->
                profile + " has " + count + " items"
        );

        Result<String> output = combined.runAsync().join();
        System.out.println(output.getValue()); // Profile Data has 42 items

        ioPool.shutdown();
    }
}
```

---

## Best Practices

:::tip[Cold Pipelines]
Keep task definitions declarative and separate from their execution. Pass `Task<A>` instances around freely until the edge of your application (e.g. controller or main entry point) calls `runAsync()`.
:::

:::note[Task vs. CompletableFuture vs. Eval]
* **`Task<A>`**: Asynchronous, cold/lazy evaluation, returns `CompletableFuture<Result<A>>`, built-in exception capture.
* **`CompletableFuture<T>`**: Asynchronous, hot/eager, runs immediately upon creation.
* **`Eval<A>`**: Synchronous, lazy or eager evaluation within the calling thread, supports memoization (`Eval.later()`).
:::
