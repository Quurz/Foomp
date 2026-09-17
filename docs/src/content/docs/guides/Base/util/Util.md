---
title: Util Guide
description: Defensive programming, precondition validation, set checks, and path guards with the Util class in Foomp.
---

import { Aside } from '@astrojs/starlight/components';

`Util` is a collection of static defensive programming and validation helpers designed to streamline precondition checking, collection validation, set relationship assertions, reflection guards, and filesystem verification.

---

## Key Features

* **Collection & Map Assertions**: Enforce non-empty collections or maps (`requireNonEmpty`) and ensure no elements/entries are null (`requireNonNullElementsInCollection`, `requireNonNullElementsInArray`).
* **Function Wrapping**: Safeguard function execution against returning `null` values via `requireNonNullResult1` and `requireNonNullResult2`.
* **Set Verification**: Check subset and proper subset relationships (`isSubSet`, `requireSubSet`, `isProperSubSet`, `requireProperSubSet`).
* **Type & Reflection Assertions**: Ensure classes are interfaces or concrete instantiable classes (`requireInterfaceType`, `requireConcreteType`).
* **Filesystem & Path Guards**: Verify paths/files are regular files, directories, readable, or writable with custom exception suppliers.

---

## Practical Examples

### 1. Collection & Array Preconditions

```java title="CollectionValidationExample.java"
import org.quurz.foomp.base.util.Util;

import java.util.List;

public class CollectionValidationExample {
    public static void main(String[] args) {
        List<String> items = List.of("alpha", "beta", "gamma");

        // Ensure collection is not empty
        List<String> validItems = Util.requireNonEmpty(
                items,
                () -> new IllegalArgumentException("Items list cannot be empty")
        );

        // Ensure all elements in the collection are non-null
        Util.requireNonNullElementsInCollection(
                validItems,
                idx -> new NullPointerException("Found null element at index " + idx)
        );

        // Process elements while checking for nulls
        Util.requireNonNullElementsInCollection(
                validItems,
                item -> System.out.println("Processing item: " + item),
                idx -> new IllegalStateException("Null item encountered at " + idx)
        );
    }
}
```

---

### 2. Set Inclusion & Proper Subsets

```java title="SetValidationExample.java"
import org.quurz.foomp.base.util.Util;

import java.util.Set;

public class SetValidationExample {
    public static void main(String[] args) {
        Set<String> allowedRoles = Set.of("ADMIN", "EDITOR", "VIEWER");
        Set<String> userRoles = Set.of("EDITOR", "VIEWER");

        // Check subset relationship
        boolean isSubset = Util.isSubSet(allowedRoles, userRoles);
        System.out.println("Is subset: " + isSubset); // true

        // Enforce subset with custom exception
        Set<String> verifiedRoles = Util.requireSubSet(
                allowedRoles,
                userRoles,
                (sup, sub) -> new SecurityException("User contains unauthorized roles: " + sub)
        );

        // Check proper subset (strict smaller subset)
        boolean isProper = Util.isProperSubSet(allowedRoles, userRoles);
        System.out.println("Is proper subset: " + isProper); // true
    }
}
```

---

### 3. File & Path Safety Checks

```java title="PathGuardExample.java"
import org.quurz.foomp.base.util.Util;

import java.io.FileNotFoundException;
import java.nio.file.Path;

public class PathGuardExample {
    public static void main(String[] args) throws Exception {
        String inputPath = "config/app.properties";

        // Sanitize string to safe filename
        String safeName = Util.toSafeFileName("my config (draft).json");
        System.out.println("Safe file name: " + safeName); // my_config_draft.json

        // Guard against missing files
        Path verifiedPath = Util.requireRegularFile(
                inputPath,
                () -> new FileNotFoundException("Missing configuration file: " + inputPath)
        );

        // Guard against unreadable files
        Util.requireReadable(
                verifiedPath,
                () -> new IllegalStateException("Cannot read configuration file: " + verifiedPath)
        );
    }
}
```

---

## Best Practices

:::tip[Fluent Chaining]
Validation methods like `requireNonEmpty`, `requireSubSet`, and `requireRegularFile` return the validated argument on success, enabling clean fluent assertions in constructor or factory initializers:
```java
this.entries = Util.requireNonEmpty(entries, () -> new IllegalArgumentException("entries empty"));
```
:::
