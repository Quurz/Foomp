---
title: Working with UnsafeProvider
description: Practical recipes for handling checked exceptions in suppliers, deferred I/O, safe adaptation with XorValue, and retry strategies using UnsafeProvider in Foomp.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

Standard Java's `java.util.function.Supplier<A>` does not allow checked exceptions to be thrown from its `get()` method. In real-world enterprise applications, however, lazy operations frequently interact with file systems, networks, database queries, and reflection.

`UnsafeProvider<A>` bridges this gap by defining a single abstract method `A get() throws Exception` while integrating with Foomp's [`Applicable<Nothing, A>`](/reference/base/functions/applicable/) hierarchy.

---

## 1. Defining Throwing Suppliers

With `UnsafeProvider<A>`, you can write clean lambdas or method references without nested `try-catch` blocks or artificial unchecked wrappers:

```java title="UnsafeProviderExamples.java"
import org.quurz.foomp.base.functions.UnsafeProvider;
import java.nio.file.Files;
import java.nio.file.Path;

Path configPath = Path.of("config.json");

// Clean throwing supplier
UnsafeProvider<String> configReader = () -> {
    if (Files.notExists(configPath)) {
        throw new NoSuchFileException(configPath.toString());
    }
    return Files.readString(configPath);
};

// Direct invocation (caller must handle or declare Exception)
try {
    String config = configReader.get();
} catch (Exception e) {
    logger.error("Failed to read configuration", e);
}
```

---

## 2. Converting to Safe Values (`.safe()`)

Because `UnsafeProvider<A>` extends `Applicable<Nothing, A>`, you can convert throwing providers into safe, pure functions returning an `XorValue<Exception, A>`:

```java title="SafeAdaptation.java"
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.functions.UnsafeProvider;
import org.quurz.foomp.base.types.XorValue;
import org.quurz.foomp.base.util.Nothing;

UnsafeProvider<byte[]> downloadPayload = () -> httpClient.fetchBytes(url);

// Safe functional wrapper: Fun<Nothing, XorValue<Exception, byte[]>>
Fun<Nothing, XorValue<Exception, byte[]>> safeDownload = downloadPayload.safe();

XorValue<Exception, byte[]> result = safeDownload.apply(Nothing.nothing);

if (result.isRight()) {
    byte[] data = result.getRight();
    processData(data);
} else {
    Exception ex = result.getLeft();
    logger.warn("Download failed: {}", ex.getMessage());
}
```

---

## 3. Integration with Nothing (`apply`)

To participate seamlessly in higher-order functional APIs without requiring special-cased zero-arity interfaces, `UnsafeProvider<A>` implements `apply(Nothing)`:

<Tabs>
  <TabItem label="Direct get()">
    ```java
    UnsafeProvider<Connection> connProvider = dataSource::getConnection;
    Connection conn = connProvider.get();
    ```
  </TabItem>
  <TabItem label="Applicable apply()">
    ```java
    UnsafeProvider<Connection> connProvider = dataSource::getConnection;
    // Treats Nothing as zero-argument indicator
    Connection conn = connProvider.apply(Nothing.nothing);
    ```
  </TabItem>
</Tabs>

---

## 4. Caching with Memoization

If the throwing supplier computes or acquires an expensive resource, you can memoize it. Only successful outcomes are stored in the cache; failures will trigger a re-attempt on the next invocation:

```java title="MemoisedProvider.java"
import org.quurz.foomp.base.functions.UnsafeProvider;
import org.quurz.foomp.base.functions.MemoisingApplicable;
import org.quurz.foomp.base.util.Nothing;

UnsafeProvider<RemoteConfig> fetchConfig = () -> remoteService.fetchLatestConfig();

// Wrap with MemoisingApplicable
MemoisingApplicable<Nothing, RemoteConfig> cachedConfig = fetchConfig.memoise();

// First call: executes remoteService.fetchLatestConfig()
RemoteConfig cfg1 = cachedConfig.apply(Nothing.nothing);

// Subsequent call: returned instantly from cache
RemoteConfig cfg2 = cachedConfig.apply(Nothing.nothing);
```

---

## Summary Comparison

| Feature | Standard `java.util.function.Supplier<A>` | Foomp `UnsafeProvider<A>` |
| :--- | :--- | :--- |
| **Exception Handling** | Runtime exceptions only | `throws Exception` (Checked & Unchecked) |
| **Functional Hierarchy** | Isolated interface | Extends `Applicable<Nothing, A>` |
| **Safe Error Modeling** | None (requires try-catch) | Built-in `.safe()` returning `XorValue` |
| **Memoization Support** | None | `.memoise()` returning `MemoisingApplicable` |
| **Zero-Arity Bridge** | `get()` | `get()` + `apply(Nothing)` |
