---
title: Working with MemoisingApplicable
description: Practical recipes and guide for caching throwing operations and retry handling using MemoisingApplicable in Foomp.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

`MemoisingApplicable` provides thread-safe, transparent caching for operations that can throw checked exceptions. It ensures that expensive computations are executed only once per distinct input argument upon success, while failures (exceptions) are thrown immediately without contaminating the cache.

---

## Why Memoize Throwing Operations?

In I/O-heavy, network, or file-based operations, calculations can be expensive to execute. 

* **Successful Computations:** When an operation successfully completes, its result is cached. Future invocations with identical parameters return the cached result instantly without re-executing the expensive I/O work.
* **Transient Failures & Retries:** If an operation fails (e.g., temporary network timeout, locked file), the exception is propagated directly to the caller and **is not cached**. Subsequent requests with the same argument can immediately retry the operation, allowing the system to recover as soon as the underlying issue is resolved.

---

## 1. Caching File and Resource Parsers

Suppose we have an expensive parser that loads and parses configurations from the file system:

```java title="ConfigLoader.java"
import org.quurz.foomp.base.functions.Applicable;
import org.quurz.foomp.base.functions.MemoisingApplicable;

import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigLoader {

    private final MemoisingApplicable<Path, String> cachedReader =
        Applicable.<Path, String>applicable(path -> Files.readString(path))
                  .memoise();

    public String loadConfig(Path path) throws Exception {
        // Reads from disk only once per unique Path; returns cached content on subsequent calls
        return cachedReader.apply(path);
    }
}
```

---

## 2. Retry Behavior on Failures

Because exceptions are not stored in the cache, transient failures do not prevent subsequent retries from succeeding:

```java title="RetryExample.java"
import org.quurz.foomp.base.functions.Applicable;
import org.quurz.foomp.base.functions.MemoisingApplicable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

AtomicInteger attempts = new AtomicInteger(0);

Applicable<String, String> remoteFetcher = serviceUrl -> {
    int count = attempts.incrementAndGet();
    if (count == 1) {
        throw new IOException("Service temporarily unavailable");
    }
    return "Data from " + serviceUrl;
};

MemoisingApplicable<String, String> cachedFetcher = remoteFetcher.memoise();

// First attempt fails and throws IOException
try {
    cachedFetcher.apply("https://api.example.com");
} catch (IOException e) {
    System.err.println("First call failed: " + e.getMessage());
}

// Second attempt retries the operation, succeeds, and caches the result
String result = cachedFetcher.apply("https://api.example.com");
System.out.println("Result: " + result);

// Third attempt returns the cached result without invoking the fetcher
String cachedResult = cachedFetcher.apply("https://api.example.com");
```

:::tip[Automatic Retry-Readiness]
Because failed attempts are never cached, there is no need to call `.clear()` just to recover from a transient exception. Once the underlying dependency recovers, the very next invocation will succeed and populate the cache.
:::

---

## 3. Cache Invalidation and Refresh

When external resources or data change permanently, invalidate the cache using `.clear()`:

```java title="CacheClearExample.java"
MemoisingApplicable<String, String> cachedFetcher = ...;

// Fetch and cache
String res1 = cachedFetcher.apply("user-42");

// Invalidate all cached data
cachedFetcher.clear();

// Next call performs a fresh evaluation
String res2 = cachedFetcher.apply("user-42");
```

`.clear()` returns `this`, allowing fluent method chaining:
```java
String fresh = cachedFetcher.clear().apply("user-42");
```

---

## 4. Safe Evaluation with `XorValue` / `Attempt`

You can combine `MemoisingApplicable` with Foomp's `.safe()` combinator or `Attempt` container to convert throwing operations into pure value pipelines:

```java title="SafePipeline.java"
import org.quurz.foomp.base.functions.MemoisingApplicable;
import org.quurz.foomp.base.types.XorValue;

MemoisingApplicable<String, Integer> cachedParser = 
    MemoisingApplicable.memoisingApplicable(Integer::parseInt);

// Convert to non-throwing Fun returning XorValue
var safeCached = cachedParser.safe();

XorValue<Exception, Integer> outcome1 = safeCached.apply("123");  // Right(123) - cached!
XorValue<Exception, Integer> outcome2 = safeCached.apply("abc");  // Left(NumberFormatException) - not cached in memo
```

---

## 5. Thread-Safe Concurrency

`MemoisingApplicable` is backed by `ConcurrentHashMap` and uses atomic operations. When multiple threads request the same key concurrently, only one computation runs:

```java title="ConcurrentAccess.java"
MemoisingApplicable<String, HeavyResource> cache = 
    MemoisingApplicable.memoisingApplicable(HeavyResource::load);

// Multiple worker threads safely access the memoized instance
Runnable task = () -> {
    try {
        HeavyResource res = cache.apply("shared-key");
        res.process();
    } catch (Exception e) {
        log.error("Failed", e);
    }
};

executor.submit(task);
executor.submit(task);
```

---

## Best Practices

:::caution[Memoize Pure Operations Only]
Memoization assumes **referential transparency**: calling the operation with the same argument should consistently yield the same outcome for successful computations. Do not memoize operations that generate random values, query the current timestamp, or execute non-idempotent mutations.
:::

:::note[Immutable Keys]
Ensure the input key type `<X>` implements proper `equals` and `hashCode` semantics. Mutable keys whose hash code or equality changes after insertion will corrupt cache lookup.
:::
