---
title: Type-Safe Copying with Copyable
description: Practical developer guide, avoiding Cloneable anti-patterns, recursive nested copying, and domain entity replication with Copyable in Foomp.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

Java's native `Cloneable` and `Object.clone()` mechanism is notoriously error-prone due to broken inheritance, lack of generic return types, and unchecked casts. Foomp provides `Copyable<SELF>` as a modern, type-safe alternative.

---

## 1. Implementing `Copyable` on Domain Classes

By implementing `Copyable<MyClass>`, the return type of `.copy()` is strictly specialized to `MyClass`, eliminating all casting.

```java title="Config.java"
import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Copyable;

public class DatabaseConfig implements Copyable<DatabaseConfig> {
    private String host;
    private int port;

    public DatabaseConfig(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public @NonNull DatabaseConfig copy() {
        return new DatabaseConfig(this.host, this.port);
    }

    public void setPort(int port) { this.port = port; }
    public int getPort() { return this.port; }
}
```

Usage is completely type-safe:

```java
DatabaseConfig original = new DatabaseConfig("localhost", 5432);
DatabaseConfig clone = original.copy(); // No cast needed!
clone.setPort(5433);
```

---

## 2. Automatic Deep Copying in Foomp Containers

Containers in Foomp (like `Pair` and `Box`) automatically detect if their contained items implement `Copyable`. When you call `.copy()` on the container:
* If an element implements `Copyable`, its `.copy()` method is called (**deep copy**).
* If an element is a standard object, the existing reference is retained (**shallow copy**).

<Tabs>
  <TabItem label="Deep Copy with Nested Pairs">
    ```java title="DeepCopyExample.java"
    import org.quurz.foomp.base.util.Pair;
    import static org.quurz.foomp.base.util.Pair.pair;

    public class DeepCopyExample {
        public static void main(String[] args) {
            // Nested pair of pairs
            Pair<DatabaseConfig, String> configPair = pair(
                new DatabaseConfig("db.local", 3306),
                "production"
            );

            // Creates a new Pair and calls copy() on DatabaseConfig
            Pair<DatabaseConfig, String> clonedPair = configPair.copy();

            clonedPair.get1().setPort(3307);

            System.out.println(configPair.get1().getPort()); // Still 3306!
            System.out.println(clonedPair.get1().getPort()); // 3307
        }
    }
    ```
  </TabItem>
  <TabItem label="Box Container Replication">
    ```java title="BoxCopyExample.java"
    import org.quurz.foomp.base.util.Box;
    import static org.quurz.foomp.base.util.Box.box;

    public class BoxCopyExample {
        public static void main(String[] args) {
            Box<DatabaseConfig> boxedConfig = box(new DatabaseConfig("127.0.0.1", 8080));
            Box<DatabaseConfig> clonedBox = boxedConfig.copy();

            clonedBox.unwind().setPort(9090);

            System.out.println(boxedConfig.unwind().getPort()); // 8080
            System.out.println(clonedBox.unwind().getPort());    // 9090
        }
    }
    ```
  </TabItem>
</Tabs>

---

## 3. Best Practices

:::tip[Immutable Objects]
If a class is completely immutable (such as a Java record or a value object with only `final` fields), implementing `copy()` by returning `this` is completely safe and avoids unnecessary object allocations.
:::

---

## See Also

* [`Copyable` Reference](/reference/base/types/copyable/) – Formal interface specification.
* [`Pair` Reference](/reference/base/util/pair/) – Pair container reference.
* [`Box` Reference](/reference/base/util/box/) – Box container reference.
