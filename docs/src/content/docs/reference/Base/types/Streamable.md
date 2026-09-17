---
title: "Streamable<A>"
description: Complete API reference for the Streamable functional interface, enabling lazy JDK Stream creation for data structures in Foomp.
---

`Streamable<A>` is a functional interface for data structures that can be viewed and processed as a standard Java `java.util.stream.Stream<A>`.

:::note[Contract and Characteristics]
- **Lazy Evaluation:** Streams should be created lazily whenever possible.
- **Encounter Order:** The resulting stream preserves the deterministic encounter order of the underlying structure.
- **Null Safety:** The returned stream is strictly `@NonNull`.
- **Resource Management:** If an implementation yields a closeable stream (e.g. backed by I/O), callers are responsible for closing it (e.g., via try-with-resources).
:::

---

## Type Signature & Definition

```java
package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.stream.Stream;

@FunctionalInterface
public interface Streamable<A> {

    /**
     * Returns a Stream that represents the elements of this structure.
     * Implementations should create the stream lazily where possible.
     *
     * @return a non-null Stream over the elements of this structure
     */
    @NonNull Stream<A> stream();
}
```

---

## Architectural Role

`Streamable<A>` acts as a bridge between custom Foomp functional data structures (trees, sequences, persistent collections) and standard Java 8+ Stream pipelines (`map`, `filter`, `reduce`, `collect`).

By implementing `Streamable`, custom types seamlessly gain access to:
* JDK Stream operations (`Stream<A>`).
* Parallel processing via `.stream().parallel()`.
* Collectors (`Collectors.toList()`, `Collectors.groupingBy()`, etc.).
