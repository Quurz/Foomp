---
title: Working with Streamable
description: Practical guide and design patterns for creating and consuming lazy JDK Streams from custom data structures with Streamable in Foomp.
---

import { Aside } from '@astrojs/starlight/components';

The `Streamable<A>` interface allows any data structure or container to provide a standard Java `Stream<A>`. This bridges Foomp data structures directly with Java's standard stream ecosystem.

---

## 1. Implementing Streamable on a Custom Container

Implementing `Streamable` requires providing a single `@NonNull Stream<A> stream()` method:

```java title="CustomBuffer.java"
import org.quurz.foomp.base.types.Streamable;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.stream.Stream;

public class CustomBuffer<A> implements Streamable<A> {

    private final List<A> items;

    public CustomBuffer(List<A> items) {
        this.items = List.copyOf(items);
    }

    @Override
    public @NonNull Stream<A> stream() {
        return this.items.stream();
    }
}
```

---

## 2. Consuming Streamable Containers

Any API method can accept `Streamable<A>` instead of concrete collection types when all it needs is stream-based transformation or aggregation:

```java title="StreamableConsumerExample.java"
import org.quurz.foomp.base.types.Streamable;

import java.util.List;
import java.util.stream.Collectors;

public class StreamableConsumerExample {

    public static <A> List<A> filterAndCollect(Streamable<A> container, java.util.function.Predicate<A> predicate) {
        return container.stream()
            .filter(predicate)
            .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        CustomBuffer<Integer> buffer = new CustomBuffer<>(List.of(1, 2, 3, 4, 5, 6));

        List<Integer> evens = filterAndCollect(buffer, n -> n % 2 == 0);
        System.out.println("Evens: " + evens); // [2, 4, 6]
    }
}
```

---

## 3. Best Practices for Lazy Streams

1. **Defer Evaluation:** Use `StreamSupport.stream(...)` with a custom `Spliterator` or `Stream.generate(...)` / `Stream.iterate(...)` to ensure streams are not eagerly materialized until consumed by a terminal operation.
2. **Deterministic Encounter Order:** Ensure the stream iteration matches the natural ordering of the underlying data structure (e.g. in-order traversal for binary search trees).
3. **Closing Streams:** If your stream wraps external resources (such as I/O channels or database cursors), use `stream.onClose(...)` so consumers can clean up resources via try-with-resources.
