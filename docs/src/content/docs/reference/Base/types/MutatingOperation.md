---
title: "@MutatingOperation"
description: Documentation-only marker annotation for methods causing side effects and state mutations in Foomp.
---

`@MutatingOperation` is a source-level documentation annotation applied to methods that produce side effects after object initialization. It highlights mutations, collaborator changes, I/O operations, or modifications of global/thread-local state.

:::note[Retention and Scope]
`@MutatingOperation` has `SOURCE` retention (`@Retention(RetentionPolicy.SOURCE)`) and targets methods (`@Target(ElementType.METHOD)`). It does not alter bytecode, runtime behavior, or enforce concurrency locks.
:::

---

## Type Signature & Definition

```java
package org.quurz.foomp.base.types;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Documented
public @interface MutatingOperation {}
```

---

## Architectural Purpose

In predominantly immutable and functional architectures, unintended side effects and state mutations can introduce subtle bugs. `@MutatingOperation` addresses this by:

1. **Making Side Effects Explicit:** Clearly signals to consumers, code reviewers, and static analysis tooling that invoking a method alters state.
2. **Complementing `@Mutable`:** While `@Mutable` annotates mutable data structures or classes, `@MutatingOperation` specifically pinpoints the mutating methods within those structures.
3. **Differentiating from Object Construction:** Constructors are explicitly not targeted by `@MutatingOperation`, as object initialization is standard lifecycle behavior rather than a mutation of an existing instance.

---

## Relationship with `@Mutable`

| Annotation | Target | Purpose |
| :--- | :--- | :--- |
| **`@Mutable`** | `TYPE`, `FIELD`, `LOCAL_VARIABLE`, `PARAMETER`, etc. | Marks a container or reference holding mutable state. |
| **`@MutatingOperation`** | `METHOD` | Marks a specific method that performs a mutation or side effect. |

---

## Code Example

```java
import org.quurz.foomp.base.types.Mutable;
import org.quurz.foomp.base.types.MutatingOperation;

@Mutable
public class Accumulator {

    private int sum = 0;

    @MutatingOperation
    public void add(int value) {
        this.sum += value;
    }

    @MutatingOperation
    public void reset() {
        this.sum = 0;
    }

    // Pure query: not annotated with @MutatingOperation
    public int getSum() {
        return this.sum;
    }
}
```
