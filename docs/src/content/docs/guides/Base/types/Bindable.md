---
title: Monadic Pipelines with Bindable
description: Practical developer guide, chaining dependent operations, railway-oriented programming, and avoiding nested callbacks with Bindable in Foomp.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

`Bindable<WT, A>` provides the `flatMap` method, the cornerstone of **Monadic Programming** in Java. It allows you to chain a series of computations where each step depends on the value produced by the previous step, automatically handling edge cases like missing values (`Maybe`), thrown exceptions (`Attempt`), or asynchronous delays (`Task`).

---

## 1. Why `flatMap`? (Eliminating Pyramid of Doom)

Without monadic bind, operations that can fail or return optional containers lead to deeply nested `if-present` or `try-catch` blocks:

<Tabs>
  <TabItem label="Standard Nested Checks (Imperative)">
    ```java
    User user = findUser(userId);
    if (user != null) {
        Address address = user.getAddress();
        if (address != null) {
            String zip = address.getZipCode();
            if (zip != null) {
                return validateZip(zip);
            }
        }
    }
    return null;
    ```
  </TabItem>
  <TabItem label="Monadic Pipeline with Bindable (Foomp)">
    ```java
    import org.quurz.foomp.base.util.Maybe;

    Maybe<ValidZip> validZip = findUser(userId)
        .flatMap(User::getAddressSafe)
        .flatMap(Address::getZipCodeSafe)
        .flatMap(this::validateZip);
    ```
  </TabItem>
</Tabs>

---

## 2. Practical Examples

<Tabs>
  <TabItem label="Maybe (Optional Chaining)">
    ```java title="MaybeBindExample.java"
    import org.quurz.foomp.base.util.Maybe;
    import static org.quurz.foomp.base.util.Maybe.just;
    import static org.quurz.foomp.base.util.Maybe.nothing;

    public class MaybeBindExample {
        record Order(String id, double amount) {}

        static Maybe<Order> findOrder(String id) {
            return id.equals("ORD-1") ? just(new Order("ORD-1", 99.50)) : nothing();
        }

        static Maybe<Double> applyDiscount(Order order) {
            return order.amount() > 50.0 ? just(order.amount() * 0.9) : nothing();
        }

        public static void main(String[] args) {
            Maybe<Double> finalPrice = findOrder("ORD-1")
                .flatMap(MaybeBindExample::applyDiscount);

            System.out.println(finalPrice); // Just(89.55)

            Maybe<Double> missing = findOrder("ORD-999")
                .flatMap(MaybeBindExample::applyDiscount);

            System.out.println(missing); // Nothing
        }
    }
    ```
  </TabItem>
  <TabItem label="Attempt (Railway Oriented Programming)">
    ```java title="AttemptBindExample.java"
    import org.quurz.foomp.base.util.Attempt;
    import static org.quurz.foomp.base.util.Attempt.attempt;

    public class AttemptBindExample {
        static Attempt<String> readConfig(String path) {
            return attempt(() -> "port=8080");
        }

        static Attempt<Integer> parsePort(String config) {
            return attempt(() -> Integer.parseInt(config.split("=")[1]));
        }

        public static void main(String[] args) {
            Attempt<Integer> port = readConfig("/etc/app.conf")
                .flatMap(AttemptBindExample::parsePort);

            // Execute the lazy computation via tryIt() yielding Result<Integer>
            Result<Integer> result = port.tryIt();
            if (result.isSuccess()) {
                System.out.println("Port: " + result.getValue()); // Port: 8080
            }
        }
    }
    ```
  </TabItem>
  <TabItem label="Sequence (List Monad / Combinatorics)">
    ```java title="SequenceBindExample.java"
    import org.quurz.foomp.base.util.Sequence;
    import static org.quurz.foomp.base.util.Sequence.sequence;

    public class SequenceBindExample {
        public static void main(String[] args) {
            Sequence<String> suits = sequence("♠", "♥");
            Sequence<String> ranks = sequence("A", "K");

            // Monadic flatMap computes the Cartesian product lazily
            Sequence<String> deck = suits.flatMap(suit ->
                ranks.map(rank -> rank + suit)
            );

            System.out.println(deck.toList()); // [A♠, K♠, A♥, K♥]
        }
    }
    ```
  </TabItem>
</Tabs>

---

## 3. Best Practices

:::tip[Keep Transformations Pure]
The function passed into `flatMap` should ideally be pure (side-effect free) unless using an effect container specifically designed for side-effects such as `Task` or `Eval`.
:::

:::note[Null Safety]
`flatMap` strictly expects non-null return values from the function. If your function might yield nothing, return `Maybe.nothing()` or `Attempt.failure(...)` rather than `null`.
:::

---

## See Also

* [`Bindable` Reference](/reference/base/types/bindable/) – Formal API contract and monad laws.
* [`Mappable` Reference](/reference/base/types/mappable/) – Functor mapping (`map`).
* [`Appliable` Reference](/reference/base/types/appliable/) – Applicative functor application (`applyTo`).
* [`Monadic` Reference](/reference/base/types/monadic/) – Unified interface for monads in Foomp.
