---
title: Transmogrifyable<SELF>
description: Fluent transformation interface enabling objects to transform into arbitrary target representations via user-supplied functions.
---

`Transmogrifyable<SELF>` is a functional interface in Foomp providing a standardized, explicit hook for converting or "transmogrifying" an object into any target type `T` within a fluent pipeline.

## Overview

In object-oriented functional architectures, chaining operations across different types often requires intermediate conversion steps. Rather than cluttering domain classes with specific `.toDTO()`, `.toXml()`, or `.toJson()` methods, `Transmogrifyable` provides a universal conversion hook:

```java
package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.function.Function;

@FunctionalInterface
public interface Transmogrifyable<SELF extends Transmogrifyable<?>> {

    @NonNull <T> T transmogrify(final @NonNull Function<? super SELF, ? extends T> transmogrifier);
}
```

:::note[Origin Note]
The name pays tribute to the beloved cardboard box invention from *Calvin and Hobbes* (Bill Watterson, 1987), where stepping into the box transforms the occupant into another entity entirely!
:::

---

## Method Contract

### `transmogrify(transmogrifier)`
```java
@NonNull <T> T transmogrify(final @NonNull Function<? super SELF, ? extends T> transmogrifier)
```
* **Parameters:** `transmogrifier` — non-null function accepting this instance (or a supertype) and producing a result of type `T`.
* **Return Value:** A non-null value of type `T`.
* **Contract:** The `transmogrifier` function must not be `null` and must not return `null`.

---

## Key Benefits

1. **Fluent Pipeline Continuity:** Allows uninterrupted method chaining even when switching to completely different types outside the class's own API.
2. **Decoupled Conversions:** Keeps domain models clean by delegating serialization, mapping, and external conversions to dedicated mapping functions without polluting class interfaces.
3. **Self-Type Safety (`SELF`):** Uses Foomp's Curiously Recurring Template Pattern (CRTP) so `transmogrify` functions receive the exact implementing subtype.
