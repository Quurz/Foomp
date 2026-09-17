---
title: Copyable<SELF>
description: Formal API reference, type-safe copying contract, self-type bounds, and comparison with Object.clone() in Foomp.
---

`org.quurz.foomp.base.types.Copyable<SELF extends Copyable<?>>`

`Copyable` is a type-safe functional interface that defines value-level copying for objects, serving as a modern, type-safe replacement for `java.lang.Cloneable` and `Object.clone()`.

```java
@FunctionalInterface
public interface Copyable<SELF extends Copyable<?>>
```

---

## Why `Copyable` instead of `Cloneable`?

Java's built-in `Cloneable` interface has well-known architectural issues:
* It lacks a public `clone()` method (it is a marker interface, and `Object.clone()` is `protected`).
* It throws `CloneNotSupportedException` at runtime.
* It returns raw `Object`, forcing callers to use unchecked casts.
* It bypasses standard constructors, which can break object initialization invariants.

`Copyable<SELF>` solves all of these problems through **Curiously Recurring Template Pattern (CRTP)** self-type generics:

```java
public class MyData implements Copyable<MyData> {
    @Override
    public @NonNull MyData copy() {
        return new MyData(...);
    }
}
```

---

## Method Specification

### `copy`

```java
@NonNull SELF copy();
```

Creates and returns a copy of this instance.

#### Return Value
* Returns a `@NonNull SELF` representing the copied instance with exact return-type specialization.

#### Contract & Semantics
1. **Deep vs. Shallow Copy:** Implementations should explicitly document whether nested structures are cloned recursively (deep copy) or referenced by pointer (shallow copy). In Foomp containers (e.g. `Pair`, `Box`), if inner elements implement `Copyable`, they are copied deeply; otherwise, reference identity is maintained.
2. **Immutability Exemption:** For strictly immutable types, returning `this` is valid if value semantics cannot be violated.
3. **Null-Safety:** The method must never return `null`.

---

## Implementing Types in Foomp

`Copyable` is implemented by various core utility structures:

| Type | Copy Behavior |
| :--- | :--- |
| [`Pair<A1, A2>`](/reference/base/util/pair/) | Produces a new `Pair`. Recursively invokes `.copy()` on components if they implement `Copyable`. |
| [`Box<A>`](/reference/base/util/box/) | Creates a new `Box` wrapping the element (or its copy if `A` is `Copyable`). |

---

## See Also

* [Guide: Type-Safe Copying with Copyable](/guides/base/types/copyable/) – Practical examples, recursive deep copying, and container replication.
* [`Transmogrifyable` Reference](/reference/base/types/transmogrifyable/) – Structure reshaping and conversion contract.
