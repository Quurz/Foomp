---
title: Executable Guide
description: Practical developer guide for defining and executing asynchronous computations with Executable in Foomp.
---

`Executable<A>` bridges standard Java asynchronous abstractions ([`Executor`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/Executor.html) and [`CompletableFuture`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/CompletableFuture.html)) with Foomp's functional primitives like `Result<A>` and `Task<A>`.

---

## Why Use `Executable<A>`?

* **Explicit Execution Context:** It defers thread allocation and execution until an `Executor` is explicitly provided.
* **Resilience:** Integrates failure management directly through `CompletableFuture<Result<A>>`, preventing uncaught asynchronous exceptions from escaping silently.
* **Interoperability:** Implements `Fun<Executor, CompletableFuture<Result<A>>>`, allowing it to participate in function compositions, currying, and higher-order piping.

---

## Examples

### 1. Defining a Custom Asynchronous Operation

```java title="CustomExecutableExample.java"
import org.quurz.foomp.base.util.Executable;
import org.quurz.foomp.base.util.Result;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CustomExecutableExample {

    public static Executable<String> fetchBody(URI uri) {
        return (Executor executor) -> CompletableFuture.supplyAsync(() -> {
            try {
                HttpClient client = HttpClient.newBuilder().executor(executor).build();
                HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                return Result.success(response.body());
            } catch (Exception e) {
                return Result.failure(e);
            }
        }, executor);
    }

    public static void main(String[] args) {
        Executor pool = Executors.newFixedThreadPool(4);
        Executable<String> fetchTask = fetchBody(URI.create("https://httpbin.org/get"));

        CompletableFuture<Result<String>> future = fetchTask.execute(pool);

        future.thenAccept(result -> {
            result.peek(System.out::println)
                  .peekFailure(err -> System.err.println("Request failed: " + err.getMessage()));
        }).join();
    }
}
```

---

### 2. Using Executable in `Task<A>`

`Executable<A>` acts as the execution engine behind `Task<A>`:

```java title="TaskExecutableBridgeExample.java"
import org.quurz.foomp.base.util.Executable;
import org.quurz.foomp.base.util.Result;
import org.quurz.foomp.base.util.Task;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

public class TaskExecutableBridgeExample {
    public static void main(String[] args) {
        Executable<Integer> computation = executor ->
            CompletableFuture.supplyAsync(() -> Result.success(10 * 4), executor);

        // Convert Executable into composable Task
        Task<Integer> task = Task.task(computation);

        Task<String> mappedTask = task.map(n -> "Value: " + (n + 2));

        mappedTask.execute(ForkJoinPool.commonPool())
                  .thenAccept(res -> System.out.println(res.getOrThrow()))
                  .join();
    }
}
```

---

## Best Practices

:::tip[Executable Guidelines]
* **Safe Error Handling:** Catch checked and unchecked exceptions within your execution supplier and return `Result.failure(ex)` rather than allowing the future to complete exceptionally.
* **Resource Cleanup:** If using dedicated thread pools or executors, ensure lifecycle management (like shutting down pools) is handled at the application boundary.
:::
