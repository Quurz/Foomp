---
title: Working with Monadic
description: Practical guide to Rank-1 monadic composition, railway pipelines, and applicative evaluation in Foomp.
---

import { Aside, Tabs, TabItem } from '@astrojs/starlight/components';

`Monadic<WT, A>` brings together Functor (`Mappable`), Applicative (`Appliable`), and Monad (`Bindable`) capabilities into a single cohesive interface.

Understanding how and when to use `map`, `applyTo`, and `flatMap` is key to writing elegant, type-safe functional Java code.

---

## Comparison: `map` vs `applyTo` vs `flatMap`

| Operation | Input Transformation | Signature Concept | When to Use |
| :--- | :--- | :--- | :--- |
| **`map`** | Pure function `Fun<A, B>` | $F[A] \times (A \to B) \to F[B]$ | Transforming values inside a container without altering the structure. |
| **`applyTo`** | Contextual function `F[Fun<A, B>]` | $F[A] \times F[A \to B] \to F[B]$ | Applying functions that are themselves encapsulated in the same context. |
| **`flatMap`** | Monadic function `Fun<A, F[B]>` | $F[A] \times (A \to F[B]) \to F[B]$ | Sequencing operations where each step can succeed, fail, or produce new containers. |

---

## Practical Examples

### 1. Monadic Sequencing with `flatMap` (Railway-Oriented Programming)

In sequential business logic, each step depends on the previous result and might fail or yield nothing. `flatMap` automatically short-circuits on failure without manual `if-else` or null checks:

```java title="MonadicPipelineExample.java"
import org.quurz.foomp.base.util.Maybe;

public class MonadicPipelineExample {

    record User(String id, String email) {}
    record Account(String userId, double balance) {}

    static Maybe<User> findUser(String id) {
        return id.equals("usr-123") ? Maybe.just(new User("usr-123", "alice@example.com")) : Maybe.nothing();
    }

    static Maybe<Account> findAccount(User user) {
        return Maybe.just(new Account(user.id(), 250.0));
    }

    public static void main(String[] args) {
        // Monadic chain: findUser -> findAccount -> extract balance
        Maybe<Double> balance = findUser("usr-123")
            .flatMap(MonadicPipelineExample::findAccount)
            .map(Account::balance);

        System.out.println("Balance: " + balance); // Just(250.0)

        // If any step returns Nothing, the whole chain short-circuits to Nothing
        Maybe<Double> missing = findUser("unknown")
            .flatMap(MonadicPipelineExample::findAccount)
            .map(Account::balance);

        System.out.println("Missing user: " + missing); // Nothing
    }
}
```

---

### 2. Applicative Function Application with `applyTo`

When you have multiple independent monadic values and want to combine them using a multi-argument function, you can use `applyTo`:

```java title="ApplicativeExample.java"
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.util.Maybe;

public class ApplicativeExample {

    public static void main(String[] args) {
        Maybe<Integer> x = Maybe.just(10);
        Maybe<Integer> y = Maybe.just(32);

        // Curried addition inside the Maybe context: x -> (y -> x + y)
        Maybe<Fun<Integer, Integer>> curriedAdder = x.map(a -> b -> a + b);

        // Apply y to the function inside curriedAdder
        Maybe<Integer> result = y.applyTo(curriedAdder);

        System.out.println("Sum: " + result); // Just(42)
    }
}
```

---

### 3. Asynchronous Monadic Pipelines with `Task`

`Task<A>` is a monadic wrapper for non-blocking asynchronous execution. Monadic `flatMap` enables clean sequential composition of asynchronous operations without callback hell:

```java title="AsyncTaskExample.java"
import org.quurz.foomp.base.util.Task;

public class AsyncTaskExample {

    static Task<String> fetchAuthToken() {
        return Task.of(() -> "auth-token-xyz");
    }

    static Task<String> fetchUserData(String token) {
        return Task.of(() -> "User Profile for " + token);
    }

    public static void main(String[] args) {
        Task<String> profileWorkflow = fetchAuthToken()
            .flatMap(AsyncTaskExample::fetchUserData)
            .map(String::toUpperCase);

        // Evaluate asynchronous workflow
        String profile = profileWorkflow.join();
        System.out.println(profile); // USER PROFILE FOR AUTH-TOKEN-XYZ
    }
}
```

---

## Best Practices

:::tip[Prefer `map` when functions do not return containers]
If your transformation function returns a plain value ($A \to B$), use `.map(...)`. Only use `.flatMap(...)` when your function returns another container ($A \to \text{Higher1}\langle\text{WT}, B\rangle$).
:::

:::note[Witness Types (`WT`)]
All monadic operations in Foomp preserve the witness type `WT`. This guarantees at compile time that you cannot accidentally flatMap a `Maybe` into a `Sequence` or vice versa.
:::
