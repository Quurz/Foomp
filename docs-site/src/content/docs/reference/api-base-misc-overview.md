---
title: Base Misc Overview
description: A guide to the utility and miscellaneous classes in the Foomp Base module.
---

The `org.quurz.foomp.base.misc` package contains a collection of utility classes and specialized tools that handle cross-cutting concerns such as versioning, logging abstraction, and JVM shutdown orchestration.

## Semantic Versioning

The `SemVer` class provides a robust implementation of the [Semantic Versioning 2.0.0](https://semver.org/) specification. It is designed to be immutable, thread-safe, and integrates with Foomp's functional types.

### Key Features
- **Parsing**: Safely parse version strings into `SemVer` objects.
- **Builders**: Use `SemVerBuilder` for fluent construction of version objects.
- **Immutability**: Methods like `incrementMajor()`, `withPreRelease()`, or `withoutBuildMetadata()` return new instances.
- **Comparison**: Implements `Comparable<SemVer>` following the precedence rules of the SemVer spec (where build metadata is ignored for comparison).
- **Integration**: Implements `Echo` for standardized string representation.

## Logging Abstraction

Foomp provides a lightweight logging abstraction through the `LogAdapter` interface, allowing the library to remain independent of specific logging frameworks while still providing diagnostic information.

- **`LogAdapter`**: A functional-friendly interface providing `debug`, `info`, `warn`, and `error` methods with support for message formatting (varargs).
- **Implementations**:
    - **`NoOpLogAdapter`**: A silent adapter that discards all log messages (useful as a default).
    - **`DelegatingLogAdapter`**: Redirects log calls to existing logging frameworks (like SLF4J or Log4j) via `BiConsumer` delegators.
- **Factory Methods**: `noOpLogAdapter()` and `delegatingLogAdapter()` provide easy access to these implementations.

## Shutdown Orchestration

Managing complex shutdown sequences in Java can be challenging. The `ShutdownHookRegistry` provides a structured way to handle JVM termination.

- **`ShutdownHookRegistry`**: Manages a prioritized list of tasks to be executed when the JVM shuts down.
- **`ShutdownHook`**: Represents a single task with:
    - **Priority**: Lower values execute first, allowing for ordered teardown of dependent services.
    - **Timeout**: Each hook has a maximum execution time to prevent the shutdown process from hanging indefinitely.
- **Builder Pattern**: `ShutdownHookRegistryBuilder` allows for easy registration of multiple hooks and configuration of a `LogAdapter` for monitoring the shutdown process.
- **Execution**: Hooks are executed sequentially in priority order. If a hook exceeds its timeout, it is interrupted, and the next hook proceeds.

## Summary Table

| Class | Purpose | Key Concept |
| :--- | :--- | :--- |
| `SemVer` | Version management | Full SemVer 2.0.0 compliance |
| `LogAdapter` | Logging abstraction | Framework-agnostic diagnostics |
| `ShutdownHookRegistry` | JVM Shutdown | Prioritized and timed teardown |

## Why use these Utilities?

These classes provide standardized solutions for common infrastructure tasks:
1. **Compliance**: `SemVer` ensures your project follows industry standards for versioning.
2. **Flexibility**: `LogAdapter` lets you integrate Foomp into any logging environment without dependency conflicts.
3. **Reliability**: `ShutdownHookRegistry` guarantees a clean and ordered exit for your application, even in the event of unexpected termination.
