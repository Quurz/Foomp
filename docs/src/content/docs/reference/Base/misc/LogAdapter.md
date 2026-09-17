---
title: LogAdapter
description: Formal API reference, delegation model, no-op fallbacks, and logging integration layer in Foomp.
---

`org.quurz.foomp.base.misc.LogAdapter`

`LogAdapter` is a functional abstraction and decoupling layer for logging in the Foomp ecosystem. It enables libraries, components, and applications to emit structured diagnostic logs without binding to any specific third-party logging backend (such as SLF4J, Logback, Log4j2, or `java.util.logging`).

```java
public interface LogAdapter
```

All methods in `LogAdapter` enforce strict non-null contracts for message and argument inputs.

---

## Architecture & Factory Methods

`LogAdapter` provides two primary factory implementations:

### 1. `noOpLogAdapter()` / `noOpLogger()`
```java
static LogAdapter noOpLogAdapter()
static @NonNull LogAdapter noOpLogger()
```
Returns a thread-safe singleton no-op logger instance (`NoOpLogAdapter`).
* Discards all log statements silently.
* Performs fast non-null validation on `message` and `args` to maintain strict contract compliance.
* Ideal as a default fallback or for unit testing environments where log output is not required.

### 2. `delegatingLogAdapter(...)`
```java
static @NonNull LogAdapter delegatingLogAdapter(
    @NonNull BiConsumer<String, Object[]> debugDelegator,
    @NonNull BiConsumer<String, Object[]> infoDelegator,
    @NonNull BiConsumer<String, Object[]> warnDelegator,
    @NonNull BiConsumer<String, Object[]> errorDelegator
)
```
Creates a delegating logger adapter that forwards log calls directly to the provided `BiConsumer` handlers.
* **Throws:** `NullPointerException` if any delegator is `null`.
* **Argument Forwarding:** Unparameterized log calls (e.g. `logger.info("Ready")`) automatically forward an empty array (`new Object[0]`) to the corresponding delegator.

---

## Log Level Methods

Each logging level provides an unparameterized method and a varargs-parameterized method.

### Debug Level

Used for fine-grained diagnostic and troubleshooting information during development.

```java
void debug(@NonNull String message);
void debug(@NonNull String message, Object... args);
```

### Info Level

Used for coarse-grained messages highlighting normal system progress, significant state transitions, or startup milestones.

```java
void info(@NonNull String message);
void info(@NonNull String message, Object... args);
```

### Warning Level

Used for unexpected or potentially harmful situations that do not prevent the system from functioning (e.g., fallback actions, deprecated usage, non-critical timeouts).

```java
void warn(@NonNull String message);
void warn(@NonNull String message, Object... args);
```

### Error Level

Used for critical operational failures and unexpected exceptions that prevented an operation from completing.

```java
void error(@NonNull String message);
void error(@NonNull String message, Object... args);
```

:::note[Exception Handling Convention]
By convention across logging frameworks (such as SLF4J), if the last argument passed in `args` is a `Throwable` / `Exception`, backend adapters should treat it as an exception and print its stack trace.
:::

---

## Contract & Guarantees

1. **Non-Null Inputs:** Passing `null` as a `message` or `args` array immediately throws a `NullPointerException` formatted via `BaseMessages.nullValue(...)`.
2. **Framework Agnostic:** Formatting syntax (such as `{}` placeholders in SLF4J or `%s` in `String.format`) is entirely delegated to the target logging framework's consumer.
3. **Thread Safety:** `NoOpLogAdapter` is inherently thread-safe and stateless. The thread safety of `DelegatingLogAdapter` depends on the provided delegating consumers.

---

## See Also

* [Guide: Integrating Logging Frameworks with LogAdapter](/guides/base/misc/logadapter/) – Practical examples with SLF4J, Log4j2, and `System.Logger`.
* [`ShutdownHookRegistry` Reference](/reference/base/misc/shutdownhookregistry/) – System service utilizing `LogAdapter` for shutdown event logging.
* [Full JavaDoc: `LogAdapter`](/api/base/foomp.base/org/quurz/foomp/base/misc/LogAdapter.html)
