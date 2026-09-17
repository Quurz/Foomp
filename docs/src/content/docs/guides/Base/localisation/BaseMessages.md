---
title: Standardized Exception Messaging with BaseMessages
description: Best practices, practical validation recipes, static import idioms, and consistent exception formatting using BaseMessages in Foomp.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

`BaseMessages` provides standardized, internationalized error and validation messages across the `Foomp-Base` ecosystem. By using `BaseMessages` in your application and library code, you maintain uniform exception diagnostics that match Foomp's built-in containers, combinators, and data structures.

---

## 1. The Static Import Idiom

The most idiomatic way to use `BaseMessages` is via static imports alongside `Objects.requireNonNull` or standard exception constructors:

```java title="StaticImportExample.java"
import static java.util.Objects.requireNonNull;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.localisation.BaseMessages.nonPositiveValue;

public class AccountService {
    public void deposit(String accountId, long amount) {
        requireNonNull(accountId, nullValue("accountId"));
        
        if (amount <= 0) {
            throw new IllegalArgumentException(nonPositiveValue("amount"));
        }
        
        // Processing logic...
    }
}
```

---

## 2. Common Validation Recipes

<Tabs>
  <TabItem label="Null Checks & Preconditions">
    ```java
    import static java.util.Objects.requireNonNull;
    import static org.quurz.foomp.base.localisation.BaseMessages.*;

    public <T> T evaluate(T input, Supplier<T> fallback) {
        requireNonNull(input, nullValue("input"));
        requireNonNull(fallback, nullValue("fallback"));

        T result = fallback.get();
        return requireNonNull(result, nullSuppliedFrom("fallback"));
    }
    ```
  </TabItem>
  <TabItem label="Numeric & Range Guards">
    ```java
    import static org.quurz.foomp.base.localisation.BaseMessages.*;

    public void setPagination(int page, int pageSize) {
        if (page < 0) {
            throw new IllegalArgumentException(negativeValue("page"));
        }
        if (pageSize <= 0) {
            throw new IllegalArgumentException(nonPositiveValue("pageSize"));
        }
    }
    ```
  </TabItem>
  <TabItem label="Collections & Bounds">
    ```java
    import static org.quurz.foomp.base.localisation.BaseMessages.*;

    public <T extends Comparable<T>> void validateRange(T min, T max) {
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException(illegalIntervalBounds(min, max));
        }
    }

    public void processBatch(List<String> items) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException(emptyCollection("items"));
        }
    }
    ```
  </TabItem>
  <TabItem label="Filesystem & Paths">
    ```java
    import java.nio.file.Files;
    import java.nio.file.Path;
    import static org.quurz.foomp.base.localisation.BaseMessages.*;

    public void verifyConfigDirectory(Path configDir) {
        requireNonNull(configDir, nullValue("configDir"));
        
        if (!Files.exists(configDir)) {
            throw new IllegalArgumentException(notFound(configDir));
        }
        if (!Files.isDirectory(configDir)) {
            throw new IllegalArgumentException(notADirectory(configDir));
        }
        if (!Files.isReadable(configDir)) {
            throw new IllegalStateException(notReadable(configDir));
        }
    }
    ```
  </TabItem>
</Tabs>

---

## 3. Container & Optional Unwinding

When building custom monadic or container types, `BaseMessages` provides standard messages matching Foomp's `Maybe` and `Attempt`:

```java title="ContainerUnwinding.java"
import java.util.NoSuchElementException;
import static org.quurz.foomp.base.localisation.BaseMessages.noValuePresent;
import static org.quurz.foomp.base.localisation.BaseMessages.noValuePresentIn;

public class CustomBox<T> {
    private final T value;

    public CustomBox(T value) {
        this.value = value;
    }

    public T getOrThrow() {
        if (this.value == null) {
            throw new NoSuchElementException(noValuePresent());
        }
        return this.value;
    }
}
```

---

## 4. Summary Table of Key Message Patterns

| Category | Typical Exception | BaseMessages Method | Resulting Message |
| :--- | :--- | :--- | :--- |
| **Null Argument** | `NullPointerException` | `nullValue("userId")` | `Argument 'userId' must not be null` |
| **Null Supplier Output** | `NullPointerException` | `nullSuppliedFrom("loader")` | `Supplied value from 'loader' must not be null` |
| **Empty Container** | `NoSuchElementException` | `noValuePresent()` | `No value present` |
| **Missing Key / Item** | `NoSuchElementException` | `notFound("user-123")` | `Element 'user-123' not found` |
| **Negative Number** | `IllegalArgumentException` | `negativeValue("offset")` | `Argument 'offset' must not be negative` |
| **Invalid Directory** | `IllegalArgumentException` | `notADirectory(path)` | `'/etc/app' is not a directory` |
| **Duplicate Item** | `IllegalStateException` | `duplicateElement("admin")` | `Element 'admin' already exists` |
