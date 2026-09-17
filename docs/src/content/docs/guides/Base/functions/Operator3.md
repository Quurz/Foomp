---
title: Working with Operator3
description: Practical recipes and guide for ternary operators, multi-stage partial application, and clamping operations in Foomp.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

`Operator3<A>` represents a ternary operator where all three inputs and the result share the same type ($A \times A \times A \to A$). It is ideal for modeling 3-operand operations such as numeric clamping (`clamp(min, max, value)`), color/vector interpolation, conditional multiplexing, and string replacements.

---

## 1. Creating Ternary Operators

Define ternary operators directly via lambda expressions:

```java title="Operator3Creation.java"
import org.quurz.foomp.base.functions.Operator3;

// Clamping a value between min and max bounds
Operator3<Integer> clamp = (min, max, val) -> Math.max(min, Math.min(max, val));

int clamped = clamp.apply(0, 100, 150); // 100
int inRange = clamp.apply(0, 100, 42);  // 42

// Ternary string replacement
Operator3<String> replace = (target, pattern, replacement) ->
    target.replace(pattern, replacement);

String result = replace.apply("Hello world!", "world", "Foomp");
// Result: "Hello Foomp!"
```

---

## 2. Progressive Partial Application

`Operator3<A>` allows fixing any one of its 3 operands using dynamic `Supplier<A>` instances. The return type is automatically typed as binary [`Operator2<A>`](/reference/base/functions/operator2/):

```java title="PartialApplication.java"
import org.quurz.foomp.base.functions.Operator3;
import org.quurz.foomp.base.functions.Operator2;
import org.quurz.foomp.base.functions.Operator;

Operator3<Double> weightedSum = (w1, w2, w3) -> w1 + w2 + w3;

// Fix w1 = 10.0 -> returns Operator2<Double> over (w2, w3)
Operator2<Double> fixFirst = weightedSum.partial1(() -> 10.0);

// Fix w2 = 20.0 -> returns Operator<Double> over (w3)
Operator<Double> fixSecond = fixFirst.partial1(() -> 20.0);

// Final evaluation
double total = fixSecond.apply(5.0); // 10.0 + 20.0 + 5.0 = 35.0
```

<Tabs>
  <TabItem label="Fix First Operand (partial1)">
    ```java
    Operator3<Integer> clamp = (min, max, val) -> Math.max(min, Math.min(max, val));

    // Fix min = 0 -> returns Operator2<Integer> for (max, val)
    Operator2<Integer> nonNegativeClamp = clamp.partial1(() -> 0);

    int res = nonNegativeClamp.apply(100, 50); // 50
    ```
  </TabItem>
  <TabItem label="Fix Third Operand (partial3)">
    ```java
    Operator3<String> replace = (target, pattern, replacement) ->
        target.replace(pattern, replacement);

    // Fix replacement = "[REDACTED]" -> returns Operator2<String> for (target, pattern)
    Operator2<String> redactor = replace.partial3(() -> "[REDACTED]");

    String safe = redactor.apply("Secret password is 123", "password");
    // "Secret [REDACTED] is 123"
    ```
  </TabItem>
</Tabs>

---

## 3. Post-Processing with `andThen`

Chain an endomorphic `UnaryOperator<A>` to format or transform the output of the ternary computation:

```java title="PostProcessing.java"
import org.quurz.foomp.base.functions.Operator3;

Operator3<String> joinThree = (a, b, c) -> a + " / " + b + " / " + c;

// Chain to upper-case transformation
Operator3<String> uppercaseJoin = joinThree.andThen(String::toUpperCase);

String path = uppercaseJoin.apply("api", "v1", "users");
// Result: "API / V1 / USERS"
```

---

## 4. Currying 3-Stage Workflows

Because `Operator3<A>` extends `Fun3<A, A, A, A>`, you can curry the ternary operator into nested single-argument functions:

```java title="CurryingExample.java"
import org.quurz.foomp.base.functions.Operator3;
import org.quurz.foomp.base.functions.Fun;

Operator3<Integer> volume = (length, width, height) -> length * width * height;

// Curry into Fun<Integer, Fun<Integer, Fun<Integer, Integer>>>
var curriedVolume = volume.curry();

var withLength = curriedVolume.apply(5);
var withLengthAndWidth = withLength.apply(4);
int finalVolume = withLengthAndWidth.apply(3); // 5 * 4 * 3 = 60
```

---

## Summary Comparison

| Step / Level | Type | Resulting Arity |
| :--- | :--- | :--- |
| **Original Operator** | `Operator3<A>` | Ternary ($A \times A \times A \to A$) |
| **First Partial Application** | `op3.partial1(...)` $\to$ `Operator2<A>` | Binary ($A \times A \to A$) |
| **Second Partial Application** | `op2.partial1(...)` $\to$ `Operator<A>` | Unary ($A \to A$) |
| **Final Invocation** | `op.apply(a)` $\to$ `A` | Value of type `A` |
