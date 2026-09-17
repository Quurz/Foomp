---
title: Eq Guide
description: Practical guide and idioms for type-safe equality using Eq in Foomp.
---

The `Eq<SELF>` interface brings compile-time type safety to object comparison, eliminating common bugs caused by comparing mismatched types.

---

## The Problem with `Object.equals`

In standard Java, `equals` accepts `Object`:

```java
// Danger in Standard Java:
Long orderId = 42L;
String enteredId = "42";

// Compiles cleanly without warnings, but is ALWAYS false!
boolean matches = orderId.equals(enteredId);
```

With `Eq`, comparison between incompatible types fails at compile time:

```java
public record OrderId(long value) implements Eq<OrderId> {
    @Override
    public boolean eq(OrderId other) {
        return this.value == other.value;
    }
}
```

```java
OrderId id1 = new OrderId(42L);
OrderId id2 = new OrderId(42L);
String wrongType = "42";

id1.eq(id2);         // OK -> true
// id1.eq(wrongType); // COMPILATION ERROR! Type mismatch
```

---

## Implementing `Eq`

When implementing `Eq`, follow standard self-referencing generics:

```java
import org.quurz.foomp.base.types.Eq;
import java.util.Objects;

public final class Customer implements Eq<Customer> {
    private final String id;
    private final String email;

    public Customer(String id, String email) {
        this.id = Objects.requireNonNull(id);
        this.email = Objects.requireNonNull(email);
    }

    @Override
    public boolean eq(Customer other) {
        Objects.requireNonNull(other, "other must not be null");
        return this.id.equals(other.id) && this.email.equalsIgnoreCase(other.email);
    }
}
```

---

## Using `Eq` in Functional Operations

Because `Eq` is a single-abstract-method `@FunctionalInterface`, instances can be curried, passed to higher-order functions, or used as predicates:

```java
OrderId target = new OrderId(101L);

// Use as a predicate
Pred<OrderId> isTarget = target::eq;

boolean found = isTarget.test(new OrderId(101L)); // true
```
