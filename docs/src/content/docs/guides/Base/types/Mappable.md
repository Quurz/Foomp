---
title: Working with Mappable
description: Practical recipes and functor patterns using Mappable in Foomp.
---

import { Aside, Tabs, TabItem } from '@astrojs/starlight/components';

`Mappable<WT, A>` represents the core **Functor** pattern in Foomp. It provides the `.map(...)` operation that allows you to transform values held within a computational context without unwrapping or leaking the context.

---

## The Functor Model

A Functor lifts a pure function `A -> B` into a computational structure `F<A> -> F<B>`:

```
  ┌───────────┐      map(A -> B)      ┌───────────┐
  │ Higher1<A>│ ────────────────────> │ Higher1<B>│
  └───────────┘                       └───────────┘
```

The underlying context (whether it is optionality in `Maybe`, lazy streams in `Sequence`, or deferred evaluation in `Attempt`) is fully preserved.

---

## Practical Examples

### 1. Mapping Optional Values (`Maybe`)

Transform values safely without checking for `null` or explicit conditional branches:

```java title="MaybeMapExample.java"
import org.quurz.foomp.base.util.Maybe;

public class MaybeMapExample {

    public static void main(String[] args) {
        Maybe<String> rawInput = Maybe.just("  foomp functional java  ");

        // Transform whitespace, case, and measure length cleanly
        Maybe<Integer> length = rawInput
            .map(String::trim)
            .map(String::toUpperCase)
            .map(String::length);

        System.out.println(length); // Just(21)

        // Mapping over Nothing is a safe no-op
        Maybe<String> empty = Maybe.nothing();
        Maybe<Integer> emptyLength = empty.map(String::length);
        System.out.println(emptyLength); // Nothing
    }
}
```

---

### 2. Transforming Lazy Streams (`Sequence`)

When mapping over `Sequence<A>`, transformations are applied on-demand as items are iterated:

```java title="SequenceMapExample.java"
import org.quurz.foomp.base.util.Sequence;

public class SequenceMapExample {

    public static void main(String[] args) {
        Sequence<Integer> numbers = Sequence.of(1, 2, 3, 4, 5);

        Sequence<String> formatted = numbers
            .map(n -> n * n)
            .map(sq -> "Square: " + sq);

        formatted.forEach(System.out::println);
        // Square: 1
        // Square: 4
        // Square: 9
        // Square: 16
        // Square: 25
    }
}
```

---

### 3. Pipeline Composition: Chaining vs `andThen`

In Foomp, you can either chain consecutive `.map(...)` calls or pre-compose your functions using `andThen`:

<Tabs>
  <TabItem label="Method Chaining">
    ```java
    Maybe<User> user = findUser();
    Maybe<String> zipCode = user
        .map(User::getAddress)
        .map(Address::getZipCode)
        .map(String::trim);
    ```
  </TabItem>
  <TabItem label="Pre-composed Fun">
    ```java
    import org.quurz.foomp.base.functions.Fun;

    Fun<User, String> extractZip = Fun.of(User::getAddress)
        .andThen(Address::getZipCode)
        .andThen(String::trim);

    Maybe<User> user = findUser();
    Maybe<String> zipCode = user.map(extractZip);
    ```
  </TabItem>
</Tabs>

---

## Best Practices

:::tip[Keep Mapping Pure]
The function supplied to `map` should be pure and free of side effects. For operations that trigger side effects (logging, writing to disks, etc.), consider [`Receiver`](/reference/base/functions/receiver/) or explicit terminal consumers.
:::

:::note[Mappable vs Bindable]
* Use **`Mappable` (`map`)** when your function returns a plain value: `A -> B`.
* Use **[`Bindable`](/reference/base/types/bindable/) (`flatMap`)** when your function returns another container: `A -> Higher1<WT, B>`.
:::
