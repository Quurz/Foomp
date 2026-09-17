---
title: ShutdownHookRegistry
description: Formal API reference, prioritized lifecycle management, timeout enforcement, and shutdown resilience in Foomp.
---

`org.quurz.foomp.base.misc.ShutdownHookRegistry`

`ShutdownHookRegistry` provides ordered, resilient, and timeout-guarded lifecycle management for application shutdown in the JVM. Unlike standard `Runtime.getRuntime().addShutdownHook(...)` threads (which execute concurrently and non-deterministically), `ShutdownHookRegistry` guarantees priority-ordered sequential execution and bounds each cleanup task by a configurable [`Duration`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/time/Duration.html).

```java
public class ShutdownHookRegistry
```

---

## Architectural Principles

1. **Priority Ordering:** Hooks execute sequentially in descending priority order (higher priority values execute first). When priorities are equal, ties are broken deterministically by hook name.
2. **Timeout Protection:** Every hook is assigned a maximum execution `Duration`. If a hook exceeds its allotted time, its worker thread is interrupted, and the registry logs a warning before advancing to the next hook.
3. **Failure Isolation:** Exceptions thrown by individual shutdown hooks are logged via [`LogAdapter`](/reference/base/misc/logadapter/) but never prevent subsequent hooks from executing.
4. **Controlled Termination:** Once all hooks execute (or fail/time out), the registry shuts down its internal single-thread executor with a 5-second graceful await before issuing a hard cancellation.

---

## Inner Classes

### `ShutdownHookRegistry.ShutdownHook`
```java
public static final class ShutdownHook implements Comparable<ShutdownHook>
```
Represents an individual hook unit:
* `String getName()` – Identifier for logging and diagnostics.
* `int getPriority()` – Non-negative execution priority ($\ge 0$).
* `Runnable getAction()` – The cleanup logic to execute.
* `Duration getTimeout()` – Maximum allowed execution time ($> 0$).

#### Ordering & Equality:
* **`compareTo(ShutdownHook other)`:** Compares `other.priority` with `this.priority` (descending), then `this.name` with `other.name` (ascending).
* **`equals(Object obj)` / `hashCode()`:** Determined strictly by `name` and `priority`.

### `ShutdownHookRegistry.ShutdownHookRegistryBuilder`
```java
public static final class ShutdownHookRegistryBuilder
```
Fluent builder for registering hooks and setting the log adapter.

---

## Factory & Builder Methods

### Factory Methods
```java
public static ShutdownHookRegistryBuilder shutdownHookRegistry()
public static ShutdownHook shutdownHook(
    @NonNull String name,
    int priority,
    @NonNull Runnable action,
    @NonNull Duration timeout
)
```

### Builder API
* `register(@NonNull ShutdownHook hook)` – Adds a pre-constructed `ShutdownHook`.
* `register(@NonNull String name, int priority, @NonNull Runnable action, @NonNull Duration timeout)` – Constructs and registers a hook.
* `withLogAdapter(@NonNull LogAdapter logAdapter)` – Configures the logger (defaults to `LogAdapter.noOpLogAdapter()`).
* `ShutdownHookRegistry build()` – Builds the configured registry.

---

## Lifecycle API

### `install()`
```java
public synchronized void install()
```
Registers the master executor thread with `Runtime.getRuntime().addShutdownHook(...)`.
* **Throws:** `IllegalStateException` if `install()` has already been called on this registry instance.

### Query Methods
* `int size()` – Returns the total number of registered hooks.
* `boolean isInstalled()` – Returns `true` if the registry has been installed into the JVM runtime.

---

## Contract & Preconditions

| Method / Parameter | Precondition | Exception on Violation |
| :--- | :--- | :--- |
| `name`, `action`, `timeout`, `hook`, `logAdapter` | Must not be `null` | `NullPointerException` |
| `priority` | Must be $\ge 0$ | `IllegalArgumentException` |
| `timeout` | Must be strictly positive ($> 0$) | `IllegalArgumentException` |
| `install()` | Called at most once | `IllegalStateException` |

---

## See Also

* [Guide: Graceful JVM Shutdown with ShutdownHookRegistry](/guides/base/misc/shutdownhookregistry/) – Step-by-step setup, database teardown, and cache flushing.
* [`LogAdapter` Reference](/reference/base/misc/logadapter/) – Abstraction layer used for shutdown telemetry.
* [Full JavaDoc: `ShutdownHookRegistry`](/api/base/foomp.base/org/quurz/foomp/base/misc/ShutdownHookRegistry.html)
