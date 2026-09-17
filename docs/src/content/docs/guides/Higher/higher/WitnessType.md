---
title: WitnessType Guide
description: In-depth guide on Witness Types, lightweight higher-kinded polymorphism, and type-level markers in Foomp.
---

import { Aside } from '@astrojs/starlight/components';

In category theory and functional programming, type constructors ($F<_>$) operate at a higher abstraction level than concrete types. Because Java does not have first-class type constructors, Foomp uses [`WitnessType`](/reference/higher/higher/witnesstype/) as a compile-time marker to uniquely identify type constructors.

---

## Why Witness Types?

Consider the desire to write a generic swap operation that works on any binary type constructor $F<A, B> \to F<B, A>$:

```java
// What we would like to write in ideal Java:
<F<_>, A, B> F<B, A> swap(F<A, B> container);
```

Since the syntax above is invalid in Java, we need a way to pass the *identity* of `F` to the compiler as a type parameter:

```java
// How Foomp encodes this using WitnessType:
<WT extends WitnessType, A, B> Higher2<WT, B, A> swap(Higher2<WT, A, B> container);
```

Here, `WT` acts as the **witness** proving to the compiler which concrete container family `Higher2` belongs to.

---

## Defining a Custom Witness Type

To equip your own generic class with HKT support in Foomp, follow these three steps:

### 1. Define the Inner Marker Class
Declare a `public static final class` (typically named `µ`) implementing `WitnessType`. Make the constructor private to ensure it cannot be instantiated:

```java title="CustomBox.java"
package com.example;

import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;
import org.quurz.foomp.base.annotations.NonNull;

import java.util.Objects;
import java.util.function.Function;

public final class CustomBox<A> implements Higher1<CustomBox.µ, A> {

    // 1. Declare the witness marker
    public static final class µ implements WitnessType {
        private µ() {}
    }

    private final A value;

    public CustomBox(@NonNull A value) {
        this.value = Objects.requireNonNull(value, "value must not be null");
    }

    public A getValue() {
        return value;
    }

    public <B> CustomBox<B> map(Function<? super A, ? extends B> mapper) {
        return new CustomBox<>(mapper.apply(this.value));
    }

    // 2. Provide the safe narrowing function
    @SuppressWarnings("unchecked")
    public static <A> CustomBox<A> narrow(@NonNull Higher1<? extends CustomBox.µ, A> wide) {
        return (CustomBox<A>) Objects.requireNonNull(wide, "wide must not be null");
    }
}
```

---

## Using Witness Types in Generic Libraries

Witness types allow you to build extensible generic libraries that can operate over arbitrary containers:

```java title="PolymorphicLogger.java"
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;
import org.quurz.foomp.base.types.Appliable;
import org.quurz.foomp.base.util.Maybe;
import org.quurz.foomp.base.util.Attempt;

public class PolymorphicLogger {

    // Generic function operating on any Rank-1 Appliable container
    public static <WT extends WitnessType, A> Higher1<WT, A> peekAndLog(
            Appliable<WT, A> container,
            String prefix) {
        return container.map(val -> {
            System.out.println(prefix + ": " + val);
            return val;
        });
    }

    public static void main(String[] args) {
        Maybe<String> maybeUser = Maybe.just("Alice");
        Attempt<String> attemptUser = Attempt.success("Bob");

        // Peek with identical polymorphic logic
        Maybe<String> loggedMaybe = Maybe.narrow(peekAndLog(maybeUser, "[Maybe]"));
        Attempt<String> loggedAttempt = Attempt.narrow(peekAndLog(attemptUser, "[Attempt]"));
    }
}
```

---

## Witness Type vs Concrete Class

| Property | Concrete Class (e.g. `Maybe<A>`) | Witness Type (e.g. `Maybe.µ`) |
| :--- | :--- | :--- |
| **Kind** | Proper Type ($*$) | Marker for Type Constructor ($* \to *$) |
| **Instantiation** | Yes (`new Maybe<>(...)`, `Maybe.just(...)`) | No (`private µ() {}`) |
| **Runtime Overhead** | Object instance on heap | Zero (phantom type marker) |
| **Role in HKT** | Carries runtime data and methods | Type-level token in `HigherN<WT, ...>` |

---

## Best Practices

:::tip[Witness Type Design Rules]
* **Never create instances of `WitnessType`**: Witnesses are strictly compile-time phantom types.
* **Keep naming consistent**: Use `µ` across all classes for uniform readability across the codebase.
* **Unique Witness per Constructor**: Never reuse the same `WitnessType` marker across two different data structures.
:::
