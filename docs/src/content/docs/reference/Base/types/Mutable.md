---
title: "@Mutable"
description: Formal API reference and design intent for the @Mutable type annotation in Foomp.
---

import { Aside, Tabs, TabItem } from '@astrojs/starlight/components';

`@Mutable` is a source-level documentation marker indicating that an annotated class or interface encapsulates mutable state.

Because Foomp emphasizes immutability, persistent data structures, and referential transparency, types that maintain mutable state (such as mutable accumulators, builder types, or caching buffers) are explicitly annotated with `@Mutable` to inform readers, code reviewers, and static analysis tools.

```java
package org.quurz.foomp.base.types;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
public @interface Mutable {}
```

---

## Annotation Metadata

| Attribute | Value | Description |
| :--- | :--- | :--- |
| **Retention Policy** | `RetentionPolicy.SOURCE` | Discarded during compilation; zero runtime performance overhead. |
| **Target** | `ElementType.TYPE` | Applicable to classes, interfaces, records, and enums. |
| **Documented** | `@Documented` | Included in generated Javadoc and API documentation. |

---

## Contract & Semantics

1. **Design Intent Communication:**
   * Declares that instances of the annotated type contain fields or internal structures that can change after instantiation.
2. **No Concurrency Guarantees:**
   * `@Mutable` does **not** imply thread-safety, synchronization, or volatile visibility. Thread-safety guarantees must be documented independently by the implementation.
3. **Complementary to `@MutatingOperation`:**
   * Individual methods within a `@Mutable` type that alter state or produce side effects should be marked with [`@MutatingOperation`](/reference/base/types/mutatingoperation/).

---

## Contrast: Immutability vs. Mutability

| Aspect | Default / Immutable Types (e.g. `Record2`, `Tuple3`, `AVLTree`) | `@Mutable` Types (e.g. Iterators, Buffers, Builders) |
| :--- | :--- | :--- |
| **State Mutation** | All state changes return new instances. | Internal state modified in place. |
| **Thread Safety** | Inherently thread-safe (safe sharing). | Caller must ensure synchronization if shared across threads. |
| **Referential Transparency** | Preserved. | May change return values across repeated invocations. |
| **Annotation** | Unannotated (default assumption in Foomp). | Annotated with `@Mutable`. |

---

## Related Annotations

* [`@MutatingOperation`](/reference/base/types/mutatingoperation/) – Marks specific methods that cause side effects or mutate state.
* [`Copyable<SELF>`](/reference/base/types/copyable/) – Type-safe value copying interface.
