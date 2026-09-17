---
title: Graceful JVM Termination with ShutdownHookRegistry
description: Practical guide and recipes for orchestrating ordered teardown, database shutdowns, and timeout bounds using ShutdownHookRegistry in Foomp.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

Standard Java `Runtime.getRuntime().addShutdownHook(Thread)` launches all shutdown threads concurrently without ordering guarantees. If your web server shuts down while your database connection pool is closing, active requests will experience unpredictable errors.

`ShutdownHookRegistry` solves this by introducing **priority-ordered, sequential shutdown** where each step is guarded by an individual timeout.

---

## 1. Setting Up a Multi-Stage Shutdown Sequence

In typical applications, services should terminate in reverse order of initialization:
1. **HTTP / Ingress Server** (Priority 100) – Stop accepting new requests first.
2. **Background Consumers & Workers** (Priority 80) – Finish in-flight jobs.
3. **Cache Flush / State Serialization** (Priority 50) – Persist cached data.
4. **Database & Connection Pools** (Priority 10) – Close connections last.

```java title="ApplicationLifecycle.java"
import org.quurz.foomp.base.misc.LogAdapter;
import org.quurz.foomp.base.misc.ShutdownHookRegistry;
import java.time.Duration;

public class ApplicationLifecycle {

    public static void configureShutdown(
        HttpServer server,
        JobQueue workerQueue,
        CacheManager cache,
        DatabasePool db,
        LogAdapter logger
    ) {
        ShutdownHookRegistry.shutdownHookRegistry()
            .withLogAdapter(logger)
            // 1. Ingress traffic stops first (higher priority = runs first)
            .register("HTTP Server", 100, server::stop, Duration.ofSeconds(5))
            // 2. In-flight jobs drain
            .register("Worker Queue", 80, workerQueue::drain, Duration.ofSeconds(10))
            // 3. Cache flush
            .register("Cache Flush", 50, cache::flushToDisk, Duration.ofSeconds(3))
            // 4. Close database pool last
            .register("Database Pool", 10, db::close, Duration.ofSeconds(5))
            .build()
            .install();
    }
}
```

---

## 2. Pre-Constructed Hooks & Builder API

You can construct individual hooks independently and register them into the builder:

```java title="ModularRegistration.java"
import org.quurz.foomp.base.misc.ShutdownHookRegistry;
import static org.quurz.foomp.base.misc.ShutdownHookRegistry.shutdownHook;
import static org.quurz.foomp.base.misc.ShutdownHookRegistry.shutdownHookRegistry;
import java.time.Duration;

public class ModularRegistryExample {
    public static void init() {
        var metricsHook = shutdownHook("Metrics Publisher", 30, () -> {
            System.out.println("Flushing final metrics...");
        }, Duration.ofSeconds(2));

        var registry = shutdownHookRegistry()
            .register(metricsHook)
            .register("Temp Dir Cleanup", 5, () -> {
                System.out.println("Cleaning temp files...");
            }, Duration.ofSeconds(1))
            .build();

        registry.install();
    }
}
```

---

## 3. Resilience and Timeout Isolation

If a third-party service hangs during shutdown, `ShutdownHookRegistry` guarantees the JVM will not deadlock indefinitely:

```java title="ResilientTeardown.java"
import org.quurz.foomp.base.misc.ShutdownHookRegistry;
import java.time.Duration;

public class ResilienceExample {
    public static void main(String[] args) {
        ShutdownHookRegistry.shutdownHookRegistry()
            .register("Hanging Task", 50, () -> {
                try {
                    // Simulates an unresponsive service
                    Thread.sleep(60_000);
                } catch (InterruptedException e) {
                    System.out.println("Hanging task interrupted cleanly.");
                }
            }, Duration.ofSeconds(2)) // Will be interrupted after 2 seconds
            .register("Critical Teardown", 10, () -> {
                // This will still execute after "Hanging Task" times out!
                System.out.println("Critical teardown completed successfully.");
            }, Duration.ofSeconds(3))
            .build()
            .install();
    }
}
```

:::tip[Best Practices]
* Always assign conservative timeouts (2–10 seconds) based on expected teardown workloads.
* Ensure cleanup actions properly handle `InterruptedException` if they involve sleeping or blocking I/O.
:::
