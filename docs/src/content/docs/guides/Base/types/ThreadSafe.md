---
title: Working with @ThreadSafe
description: Practical patterns and architectural design guidelines for documenting thread-safe types in Foomp.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

The `@ThreadSafe` annotation is used across Foomp and client applications to clearly signal that a class or data structure is designed for concurrent execution without external synchronization.

## Why Document Thread Safety?

In standard Java, determining whether a third-party class or helper is safe for concurrent access often requires digging through source code or trial and error. Adding `@ThreadSafe` eliminates guesswork for API consumers.

---

## Common Patterns

<Tabs>
  <TabItem label="Immutable Types">
    ```java title="ImmutablePoint.java"
    import org.checkerframework.checker.nullness.qual.NonNull;
    import org.quurz.foomp.base.types.ThreadSafe;

    /**
     * An immutable 2D point.
     * <p>
     * This class is thread-safe because all fields are final and deeply immutable.
     */
    @ThreadSafe
    public record ImmutablePoint(int x, int y) {
        public @NonNull ImmutablePoint translate(int dx, int dy) {
            return new ImmutablePoint(this.x + dx, this.y + dy);
        }
    }
    ```
  </TabItem>
  <TabItem label="Concurrent Cache">
    ```java title="ThreadSafeCache.java"
    import org.checkerframework.checker.nullness.qual.NonNull;
    import org.quurz.foomp.base.types.ThreadSafe;
    import java.util.Map;
    import java.util.concurrent.ConcurrentHashMap;
    import java.util.function.Function;

    /**
     * A thread-safe memoizing compute cache backed by ConcurrentHashMap.
     */
    @ThreadSafe
    public class ThreadSafeCache<K, V> {
        private final Map<K, V> storage = new ConcurrentHashMap<>();

        public @NonNull V getOrCompute(@NonNull K key, @NonNull Function<K, V> compute) {
            return storage.computeIfAbsent(key, compute);
        }
    }
    ```
  </TabItem>
  <TabItem label="Pure Stateless Helpers">
    ```java title="MathAlgorithms.java"
    import org.quurz.foomp.base.types.ThreadSafe;

    /**
     * Stateless mathematical helper utilities.
     * <p>
     * Thread-safe because it contains no mutable state.
     */
    @ThreadSafe
    public final class MathAlgorithms {
        private MathAlgorithms() {}

        public static int gcd(int a, int b) {
            return b == 0 ? a : gcd(b, a % b);
        }
    }
    ```
  </TabItem>
</Tabs>

---

## Architectural Guidelines

:::note[Contract Checklist for `@ThreadSafe`]
When marking a class as `@ThreadSafe`, ensure:
1. **No Shared Mutable State:** State is either immutable or wrapped in thread-safe concurrent containers (`AtomicReference`, `ConcurrentHashMap`, etc.).
2. **Safe Publication:** Fields are `final` or assigned before making instances accessible to other threads.
3. **Consistent Subtyping:** Subclasses should maintain the concurrency guarantees of their supertypes.
:::
