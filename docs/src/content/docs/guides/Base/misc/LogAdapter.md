---
title: Logging Integration with LogAdapter
description: Practical integration guide and recipes for connecting SLF4J, Log4j2, java.util.logging, and System.Logger with LogAdapter in Foomp.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

`LogAdapter` allows your applications and modular services to log events without tight coupling to a specific logging library. By supplying custom `BiConsumer` callbacks, you can redirect all log output to any backend of your choice.

---

## 1. Connecting Popular Logging Frameworks

<Tabs>
  <TabItem label="SLF4J (Logback / SimpleLogger)">
    ```java title="Slf4jLogAdapterExample.java"
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.quurz.foomp.base.misc.LogAdapter;

    public class LoggingConfig {
        public static LogAdapter forClass(Class<?> clazz) {
            Logger logger = LoggerFactory.getLogger(clazz);
            return LogAdapter.delegatingLogAdapter(
                logger::debug,
                logger::info,
                logger::warn,
                logger::error
            );
        }
    }
    ```
  </TabItem>
  <TabItem label="Java System.Logger (JDK 9+)">
    ```java title="SystemLoggerExample.java"
    import java.lang.System.Logger;
    import java.lang.System.Logger.Level;
    import org.quurz.foomp.base.misc.LogAdapter;

    public class SystemLoggerAdapter {
        public static LogAdapter forName(String name) {
            Logger logger = System.getLogger(name);
            return LogAdapter.delegatingLogAdapter(
                (msg, args) -> logger.log(Level.DEBUG, msg, args),
                (msg, args) -> logger.log(Level.INFO, msg, args),
                (msg, args) -> logger.log(Level.WARNING, msg, args),
                (msg, args) -> logger.log(Level.ERROR, msg, args)
            );
        }
    }
    ```
  </TabItem>
  <TabItem label="Console / Standard Out (Testing)">
    ```java title="ConsoleLogAdapter.java"
    import org.quurz.foomp.base.misc.LogAdapter;

    public class ConsoleLogging {
        public static LogAdapter console() {
            return LogAdapter.delegatingLogAdapter(
                (msg, args) -> System.out.printf("[DEBUG] " + msg + "%n", args),
                (msg, args) -> System.out.printf("[INFO]  " + msg + "%n", args),
                (msg, args) -> System.out.printf("[WARN]  " + msg + "%n", args),
                (msg, args) -> System.err.printf("[ERROR] " + msg + "%n", args)
            );
        }
    }
    ```
  </TabItem>
</Tabs>

---

## 2. Using LogAdapter in Domain Services

When writing services, accept `LogAdapter` as a dependency (or default to `noOpLogAdapter()`) to keep your code cleanly testable:

```java title="OrderService.java"
import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.misc.LogAdapter;
import java.util.Objects;

public class OrderService {
    private final LogAdapter log;

    public OrderService(@NonNull LogAdapter log) {
        this.log = Objects.requireNonNull(log, "log must not be null");
    }

    public void processOrder(String orderId, double amount) {
        log.info("Processing order {} for amount ${}", orderId, amount);

        try {
            // Execution logic...
            log.debug("Payment verified for order {}", orderId);
        } catch (Exception ex) {
            log.error("Order processing failed for {}: {}", orderId, ex.getMessage(), ex);
            throw ex;
        }
    }
}
```

---

## 3. Disabling Logs in Tests with No-Op Adapter

In unit tests, use `LogAdapter.noOpLogAdapter()` to silence logging and keep test output focused and clean:

```java title="OrderServiceTest.java"
import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.misc.LogAdapter;

class OrderServiceTest {

    @Test
    void testOrderExecutionWithoutLogSpam() {
        // LogAdapter.noOpLogAdapter() absorbs all log events silently
        OrderService service = new OrderService(LogAdapter.noOpLogAdapter());
        
        service.processOrder("ORD-42", 99.95);
        // Assertions...
    }
}
```
