---
title: Working with @Mutable
description: Practical guidelines and best practices for documenting mutable state and side effects in Foomp.
---

import { Aside, Tabs, TabItem } from '@astrojs/starlight/components';

In modern functional Java development, immutability is the default expectation. When mutability is required (e.g., for local performance optimizations, stateful accumulators, or I/O streaming), the `@Mutable` annotation clearly signals this architectural deviation.

---

## When to Use `@Mutable`

Use `@Mutable` when authoring classes or interfaces that:
1. **Hold Reassignable or Mutable Fields:** Classes with non-final fields or mutable collections that can change after construction.
2. **Encapsulate In-Place Buffers or Accumulators:** Custom data structures designed for batch mutations before freezing into an immutable representation.
3. **Wrap External Stateful Resources:** Wrappers around JDBC connections, network sockets, or file handles.

---

## Code Example: Annotating Mutable Structures

Combine `@Mutable` at the class level with `@MutatingOperation` on state-changing methods to make side-effect boundaries explicit:

```java title="FastBuffer.java"
import org.quurz.foomp.base.types.Mutable;
import org.quurz.foomp.base.types.MutatingOperation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mutable
public class FastBuffer<T> {

    private final List<T> items = new ArrayList<>();

    @MutatingOperation
    public void append(T item) {
        this.items.add(item);
    }

    @MutatingOperation
    public void clear() {
        this.items.clear();
    }

    public List<T> snapshot() {
        return Collections.unmodifiableList(new ArrayList<>(this.items));
    }
}
```

---

## Best Practices

:::tip[Keep Mutation Local]
Mutate internally for performance within local method scopes, then return immutable structures (such as `Dict`, `Sequence`, or unmodifiable collections) to callers.
:::

:::note[Constructors are Excluded]
Constructors initialize state rather than mutate existing state after construction. Neither constructors nor static factory methods require `@MutatingOperation`.
:::

:::caution[Concurrency Reminder]
`@Mutable` is a source marker for documentation and static analysis. It does not provide runtime locking or thread synchronization.
:::
