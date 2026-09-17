---
title: Exception Handling with Triable
description: Practical recipes for wrapping risky computations and handling failures safely using Triable and XorValue.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

The `Triable<A>` interface allows you to encapsulate computations that might fail with an exception without crashing or throwing checked exceptions to callers.

## Basic Usage

A `Triable` returns an `XorValue<Exception, A>`. You can inspect whether the outcome is a success (`isRight()`) or a failure (`isLeft()`).

```java title="BasicTriableExample.java"
import org.quurz.foomp.base.types.Triable;
import org.quurz.foomp.base.types.XorValue;

public class BasicTriableExample {
    public static void main(String[] args) {
        // Safe computation parsing an integer
        Triable<Integer> safeParser = () -> {
            try {
                return XorValue.right(Integer.parseInt("42"));
            } catch (Exception e) {
                return XorValue.left(e);
            }
        };

        XorValue<Exception, Integer> result = safeParser.tryIt();
        if (result.isRight()) {
            System.out.println("Parsed value: " + result.getRight()); // 42
        }
    }
}
```

---

## Practical Examples

<Tabs>
  <TabItem label="File Reading & Recovery">
    ```java title="FileReaderExample.java"
    import org.quurz.foomp.base.types.Triable;
    import org.quurz.foomp.base.types.XorValue;
    import java.nio.file.Files;
    import java.nio.file.Path;

    public class FileReaderExample {
        public static Triable<String> readConfig(Path configPath) {
            return () -> {
                try {
                    return XorValue.right(Files.readString(configPath));
                } catch (Exception ex) {
                    return XorValue.left(ex);
                }
            };
        }

        public static void main(String[] args) {
            Triable<String> configLoader = readConfig(Path.of("non_existing_config.json"));
            XorValue<Exception, String> outcome = configLoader.tryIt();

            if (outcome.isLeft()) {
                System.err.println("Config not found: " + outcome.getLeft().getMessage());
            } else {
                System.out.println("Loaded config: " + outcome.getRight());
            }
        }
    }
    ```
  </TabItem>
  <TabItem label="Using with Attempt">
    ```java title="AttemptTriableExample.java"
    import org.quurz.foomp.base.types.Triable;
    import org.quurz.foomp.base.types.XorValue;
    import org.quurz.foomp.base.util.Attempt;

    public class AttemptTriableExample {
        public static void main(String[] args) {
            // Attempt is a monadic Triable
            Triable<Integer> attempt = Attempt.attempt(() -> 100 / 5);

            XorValue<Exception, Integer> outcome = attempt.tryIt();
            System.out.println("Result: " + outcome.getRight()); // 20
        }
    }
    ```
  </TabItem>
</Tabs>

---

## Best Practices

:::tip[Guidelines]
* **Never Throw from `tryIt()`:** Ensure all internal exceptions inside a `Triable` implementation are caught and converted to `XorValue.left(e)`.
* **Use `Attempt` for Pipelines:** For composing multiple error-prone steps together with `map` and `flatMap`, prefer `Attempt<A>` over raw `Triable<A>`.
:::
