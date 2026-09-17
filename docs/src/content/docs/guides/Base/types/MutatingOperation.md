---
title: Working with @MutatingOperation
description: Practical guide to documenting side-effecting methods and managing state mutations with @MutatingOperation in Foomp.
---

import { Aside } from '@astrojs/starlight/components';

In functional programming, pure functions and immutable data structures are favored because they make program execution predictable and thread-safe. However, real-world systems often require controlled local mutation, caches, buffers, or external I/O.

The `@MutatingOperation` annotation serves as an explicit visual cue that a method causes state changes or side effects.

---

## When to Use `@MutatingOperation`

Apply `@MutatingOperation` to any method that:
- Modifies internal state of an object (e.g. updating fields, clearing caches).
- Mutates an argument or passed collaborator (e.g. adding items to an external list).
- Interacts with outside systems (e.g. writing to disk, sending network packets, printing to stdout).
- Modifies global or thread-local state.

<Aside type="tip" title="Self-Documenting APIs">
By annotating mutating methods, consumers immediately see which calls are safe to invoke repeatedly without side effects and which calls change application state.
</Aside>

---

## Practical Example: A Transient State Buffer

The following example demonstrates using `@Mutable` on a buffer class and marking state-modifying operations with `@MutatingOperation`:

```java title="ByteBufferWrapper.java"
import org.quurz.foomp.base.types.Mutable;
import org.quurz.foomp.base.types.MutatingOperation;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mutable
public class ByteBufferWrapper {

    private final List<Byte> buffer = new ArrayList<>();

    @MutatingOperation
    public void append(byte b) {
        this.buffer.add(b);
    }

    @MutatingOperation
    public void appendAll(byte @NonNull [] bytes) {
        for (byte b : bytes) {
            this.buffer.add(b);
        }
    }

    @MutatingOperation
    public void clear() {
        this.buffer.clear();
    }

    // Read-only inspection method: NO @MutatingOperation
    public int size() {
        return this.buffer.size();
    }

    // Read-only export: NO @MutatingOperation
    public @NonNull List<Byte> toList() {
        return Collections.unmodifiableList(new ArrayList<>(this.buffer));
    }
}
```

---

## Best Practices

1. **Keep Mutations Local:** Confine `@MutatingOperation` methods behind clear API boundaries and avoid leaking mutable references across concurrent threads without synchronization.
2. **Distinguish Queries from Commands:** Following the Command-Query Separation (CQS) principle, queries should never be mutating operations, while commands modifying state should be annotated with `@MutatingOperation`.
3. **Prefer Immutable Alternatives:** Whenever possible, prefer returning a new updated instance (e.g., via persistent data structures or `Copyable`) rather than mutating existing objects in place.
