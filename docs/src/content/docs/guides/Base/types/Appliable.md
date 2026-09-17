---
title: Applicative Functors with Appliable
description: Practical developer guide, applicative programming patterns, multi-argument combinations, and real-world examples with Appliable in Foomp.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

`Appliable` is the foundation of **Applicative Functors** in Foomp. While standard functors (`Mappable.map`) apply a regular function `A -> B` to a container `F<A>`, applicatives allow the function itself to be wrapped inside the container `F<Function<A, B>>`.

This unlocks powerful patterns: combining multiple independent contexts, applying partially applied curried functions, and running operations without nested callbacks.

---

## 1. Functor vs. Applicative vs. Monad

| Pattern | Interface | Method Signature | Use Case |
| :--- | :--- | :--- | :--- |
| **Functor** | [`Mappable`](/reference/base/types/mappable/) | `map(A -> B) -> F<B>` | Transform value with a pure function. |
| **Applicative** | [`Appliable`](/reference/base/types/appliable/) | `applyTo(F<A -> B>) -> F<B>` | Combine independent effectful values or apply wrapped functions. |
| **Monad** | [`Bindable`](/reference/base/types/bindable/) | `flatMap(A -> F<B>) -> F<B>` | Sequential steps where step 2 depends on step 1's result. |

:::tip[Why Applicative?]
Applicative operations do not depend on intermediate values, allowing engines (like `Task` or validation pipelines) to execute branches independently or in parallel, unlike sequential `flatMap`.
:::

---

## 2. Practical Examples

<Tabs>
  <TabItem label="Maybe (Optional Combination)">
    ```java title="MaybeApplicativeExample.java"
    import org.quurz.foomp.base.functions.Fun;
    import org.quurz.foomp.base.functions.Fun2;
    import org.quurz.foomp.base.util.Maybe;
    import static org.quurz.foomp.base.util.Maybe.just;
    import static org.quurz.foomp.base.util.Maybe.nothing;

    public class MaybeExample {
        public static void main(String[] args) {
            Maybe<Integer> maybeA = just(10);
            Maybe<Integer> maybeB = just(20);

            // Binary addition curried into a Maybe context
            Fun2<Integer, Integer, Integer> add = (a, b) -> a + b;
            
            // Step 1: Map the first argument -> Maybe<Fun<Integer, Integer>>
            Maybe<Fun<Integer, Integer>> curried = maybeA.map(add::curry);

            // Step 2: Apply the wrapped function to maybeB
            Maybe<Integer> sum = maybeB.applyTo(curried);

            System.out.println(sum); // Just(30)

            // If either value is Nothing, the result safely evaluates to Nothing
            Maybe<Integer> failed = nothing().applyTo(curried);
            System.out.println(failed); // Nothing
        }
    }
    ```
  </TabItem>
  <TabItem label="Sequence (Cartesian Products)">
    ```java title="SequenceApplicativeExample.java"
    import org.quurz.foomp.base.functions.Fun;
    import org.quurz.foomp.base.util.Sequence;
    import static org.quurz.foomp.base.util.Sequence.sequence;

    public class SequenceExample {
        public static void main(String[] args) {
            Sequence<Fun<Integer, Integer>> operations = sequence(
                x -> x + 1,
                x -> x * 10
            );

            Sequence<Integer> values = sequence(1, 2, 3);

            // Applies every operation to every value (Cartesian product)
            Sequence<Integer> results = values.applyTo(operations);

            // Produces: [2, 3, 4, 10, 20, 30]
            System.out.println(results.toList());
        }
    }
    ```
  </TabItem>
  <TabItem label="Attempt (Safe Validation)">
    ```java title="AttemptApplicativeExample.java"
    import org.quurz.foomp.base.functions.Fun;
    import org.quurz.foomp.base.util.Attempt;
    import static org.quurz.foomp.base.util.Attempt.success;

    public class AttemptExample {
        record User(String name, int age) {}

        public static void main(String[] args) {
            Attempt<String> name = success("Alice");
            Attempt<Integer> age = success(30);

            // Curried constructor: name -> age -> User
            Attempt<Fun<Integer, User>> curried = name.map(
                n -> (Integer a) -> new User(n, a)
            );

            Attempt<User> user = age.applyTo(curried);
            System.out.println(user); // Success(User[name=Alice, age=30])
        }
    }
    ```
  </TabItem>
</Tabs>

---

## 3. Applicative vs. Monadic Pipelines

Notice how applicatives combine values without nesting `flatMap` blocks:

```java title="ApplicativeVsMonadic.java"
// Monadic (Sequential & nested):
Maybe<String> user = maybeFirst.flatMap(first ->
    maybeLast.map(last -> first + " " + last)
);

// Applicative (Lifting & applying independent containers):
Maybe<String> userApp = maybeLast.applyTo(
    maybeFirst.map(first -> last -> first + " " + last)
);
```

---

## See Also

* [`Appliable` Reference](/reference/base/types/appliable/) – Formal method signatures and applicative laws.
* [Guide: Rank-2 Applicatives with Appliable2](/guides/base/types/appliable2/) – Working with multi-value pairs and bifunctors.
* [`Monadic` Reference](/reference/base/types/monadic/) – Composite interface combining `Mappable`, `Appliable`, and `Bindable`.
