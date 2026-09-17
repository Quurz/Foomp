---
title: Eq<SELF>
description: API reference for the Eq functional interface in Foomp.
---

The `Eq<SELF>` interface defines a contract for type-safe, homogeneous equality checks using the Curiously Recurring Template Pattern (CRTP).

```java
package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
public interface Eq<SELF extends Eq<?>> {
    boolean eq(final @NonNull SELF other);
}
```

---

## Overview & Purpose

Standard Java equality via `Object.equals(Object)` has known design weaknesses:
1. **Lack of Type Safety**: It accepts any `Object`, easily hiding bug patterns where completely unrelated types are compared (e.g., comparing a `UserId` with a `String`), which silently evaluates to `false` without compiler warnings.
2. **Nullable Parameter**: `equals(Object)` must safely accept `null`, which can obscure strict non-null domain invariants.

`Eq<SELF>` provides a compile-time type-checked alternative where equality is only evaluated between compatible types.

---

## Method Specification

### `eq(other)`

```java
boolean eq(final @NonNull SELF other)
```

Determines whether the current instance is equivalent to `other`.

- **Parameters:**
  - `other`: The object to check equality against. Must not be `null`.
- **Returns:** `true` if both objects are equivalent according to domain criteria, `false` otherwise.
- **Throws:** `NullPointerException` if `other` is `null`.

---

## Contract & Equivalence Relation Laws

Implementations of `Eq` must satisfy the mathematical properties of an **equivalence relation**:

1. **Reflexive**: `x.eq(x) == true` for any non-null `x`.
2. **Symmetric**: `x.eq(y) == y.eq(x)` for any non-null `x` and `y`.
3. **Transitive**: If `x.eq(y) == true` and `y.eq(z) == true`, then `x.eq(z) == true`.
4. **Consistency**: Repeated invocations with unchanged state consistently yield the same boolean value.
5. **Non-Nullity**: `x.eq(null)` throws `NullPointerException`.

---

## Type Characteristics

| Feature | Details |
| :--- | :--- |
| **Package** | `org.quurz.foomp.base.types` |
| **Kind** | Functional Interface (`@FunctionalInterface`) |
| **Generics** | `SELF extends Eq<?>` (CRTP pattern) |
| **Null-Safety** | Disallows `null` arguments via `@NonNull` |
