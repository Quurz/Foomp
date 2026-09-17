---
title: Working with Operator
description: Practical recipes and guide for unary transformations and endomorphisms using Operator in Foomp.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

`Operator<A>` represents a unary operation where the input and output types are identical ($A \to A$). It extends both standard Java's `UnaryOperator<A>` and Foomp's `Fun<A, A>`, providing a type-safe and composable way to model mutations, data normalizations, state transitions, and text/numeric transformations.

---

## 1. Creating and Wrapping Operators

You can define operators using lambda expressions, method references, or by wrapping existing standard Java `UnaryOperator` instances.

```java title="OperatorCreation.java"
import org.quurz.foomp.base.functions.Operator;
import java.util.function.UnaryOperator;

// Direct lambda definition
Operator<String> trimmer = String::trim;
Operator<Integer> doubler = x -> x * 2;

// Wrapping an existing UnaryOperator with strict null validation
UnaryOperator<String> legacyOp = s -> s.toLowerCase();
Operator<String> safeOp = Operator.operator(legacyOp);

// Identity operator
Operator<String> noop = Operator.identity();
```

:::note[Null Defense]
Using `Operator.operator(legacyOp)` wraps legacy standard operators with checks that immediately throw `NullPointerException` if the operand or result is `null`.
:::

---

## 2. Operator Chaining (`andThen` & `compose`)

Because domain and codomain are identical ($A \to A$), multiple operators can be composed into pipelines that continuously refine a value of type `A`:

```java title="OperatorComposition.java"
import org.quurz.foomp.base.functions.Operator;

Operator<String> trim = String::trim;
Operator<String> lowercase = String::toLowerCase;
Operator<String> sanitize = s -> s.replaceAll("[^a-z0-9]", "_");

// Forward composition (left-to-right)
Operator<String> normalizer = trim
    .andThen(lowercase)
    .andThen(sanitize);

String cleaned = normalizer.apply("  User-Input#42!  ");
// Result: "user_input_42_"
```

<Tabs>
  <TabItem label="Forward: andThen">
    ```java
    // f.andThen(g) => g(f(x))
    Operator<Integer> add1 = x -> x + 1;
    Operator<Integer> mult2 = x -> x * 2;

    Operator<Integer> pipeline = add1.andThen(mult2);
    int result = pipeline.apply(3); // (3 + 1) * 2 = 8
    ```
  </TabItem>
  <TabItem label="Backward: compose">
    ```java
    // f.compose(g) => f(g(x))
    Operator<Integer> add1 = x -> x + 1;
    Operator<Integer> mult2 = x -> x * 2;

    Operator<Integer> pipeline = add1.compose(mult2);
    int result = pipeline.apply(3); // (3 * 2) + 1 = 7
    ```
  </TabItem>
</Tabs>

---

## 3. Modeling State Transitions

Endomorphic operators are ideal for immutable state machines and record transformations:

```java title="StateTransitions.java"
import org.quurz.foomp.base.functions.Operator;

record GameState(int score, int level, int lives) {
    GameState addScore(int delta) {
        return new GameState(score + delta, level, lives);
    }
    GameState nextLevel() {
        return new GameState(score, level + 1, lives);
    }
    GameState loseLife() {
        return new GameState(score, level, Math.max(0, lives - 1));
    }
}

Operator<GameState> collectCoin = state -> state.addScore(100);
Operator<GameState> levelUp = state -> state.nextLevel();
Operator<GameState> takeDamage = state -> state.loseLife();

// Combine multiple events into a turn sequence
Operator<GameState> turnSequence = collectCoin
    .andThen(collectCoin)
    .andThen(levelUp);

GameState initial = new GameState(0, 1, 3);
GameState updated = turnSequence.apply(initial);
// Result: GameState[score=200, level=2, lives=3]
```

---

## 4. Iterative Application and Fixed Points

You can repeatedly apply an `Operator<A>` to simulate loops or convergence:

```java title="IterativeApplication.java"
import org.quurz.foomp.base.functions.Operator;

Operator<Double> sqrtStep = x -> 0.5 * (x + 2.0 / x); // Newton-Raphson for sqrt(2)

double approx = 1.0;
for (int i = 0; i < 5; i++) {
    approx = sqrtStep.apply(approx);
}
System.out.println("Approximation: " + approx); // ~1.41421356...
```

---

## 5. Interoperability with JDK Collections & Streams

Since `Operator<A>` implements `java.util.function.UnaryOperator<A>`, you can pass it directly to `List.replaceAll(...)` or `Stream.iterate(...)`:

```java title="JdkInterop.java"
import org.quurz.foomp.base.functions.Operator;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

Operator<String> uppercase = String::toUpperCase;

List<String> names = new ArrayList<>(List.of("alice", "bob", "carol"));
names.replaceAll(uppercase);
// names: ["ALICE", "BOB", "CAROL"]

// Generate an infinite stream of powers of 2
Operator<Long> doubleVal = n -> n * 2;
List<Long> powersOfTwo = Stream.iterate(1L, doubleVal)
    .limit(8)
    .toList();
// [1, 2, 4, 8, 16, 32, 64, 128]
```

---

## Summary Comparison

| Concept | Standard Java `UnaryOperator<A>` | Foomp `Operator<A>` |
| :--- | :--- | :--- |
| **Null-Safety** | Unspecified / Allows `null` | Strictly enforced non-null contract |
| **Inheritance** | `Function<A, A>` | `UnaryOperator<A>` + `Fun<A, A>` |
| **Combinators** | `compose`, `andThen` | `compose`, `andThen`, plus `Fun` combinators |
| **Memoization** | Manual | Built-in via `.memoise()` (from `Fun`) |
| **Async Support** | None | Built-in via `.async()` (from `Fun`) |
