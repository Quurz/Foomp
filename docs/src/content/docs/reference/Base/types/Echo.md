---
title: Echo
description: API reference for the Echo functional interface in Foomp.
---

The `Echo` interface defines a functional contract for producing explicit, formatted string representations of domain objects and data structures.

```java
package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
public interface Echo {
    @NonNull String echo();
}
```

---

## Overview & Purpose

Standard Java provides `Object.toString()` for string conversion. However, in practice, `toString()` is often overloaded with multiple conflicting responsibilities:
- Internal debugging representations (e.g. `ClassName@hashcode` or raw field dumps)
- Diagnostic logging
- User-facing or serialized domain formatting

`Echo` decouples explicit domain or visual string representation from Java's built-in `Object.toString()`. It guarantees type-level formatting control with strict `@NonNull` guarantees.

---

## Method Specification

### `echo()`

```java
@NonNull String echo()
```

Computes and returns the formatted string representation according to the implementation's explicit formatting policy.

- **Returns:** A non-null `String` representing the object.
- **Contract:** Implementations must never return `null`.

---

## Implementation Patterns in Foomp

Several core structures in **Foomp** implement `Echo` to provide specialized structural or domain formatting:

1. **`SemVer`**: Returns the canonical SemVer string (e.g. `"1.4.2-alpha.1+build.42"`), while `toString()` can remain dedicated to debugging.
2. **`AVLTree` & `RedBlackTree`**: Produces a multi-line ASCII tree diagram visualizing tree hierarchy and branch structure.

---

## Type Characteristics

| Feature | Details |
| :--- | :--- |
| **Package** | `org.quurz.foomp.base.types` |
| **Kind** | Functional Interface (`@FunctionalInterface`) |
| **Null-Safety** | Enforces `@NonNull` return value |
| **SAM Method** | `String echo()` |
