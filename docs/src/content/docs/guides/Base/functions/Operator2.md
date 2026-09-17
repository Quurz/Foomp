---
title: Working with Operator2
description: Practical recipes and guide for binary operators, reductions, currying, and partial application in Foomp.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

`Operator2<A>` represents a homogeneous binary operation where both operands and the result share the exact same type ($A \times A \to A$). By bridging `BinaryOperator<A>` and `Fun2<A, A, A>`, `Operator2<A>` serves as the foundational interface for arithmetic, string aggregations, vector/matrix combinations, and partial application returning unary [`Operator<A>`](/reference/base/functions/operator/).

---

## 1. Creating and Wrapping Binary Operators

You can create `Operator2<A>` instances using standard lambda syntax or wrap existing JDK `BinaryOperator` instances:

```java title="Operator2Creation.java"
import org.quurz.foomp.base.functions.Operator2;
import java.util.function.BinaryOperator;

// Direct lambda arithmetic
Operator2<Integer> add = (x, y) -> x + y;
Operator2<Integer> multiply = (x, y) -> x * y;
Operator2<String> joinWithComma = (s1, s2) -> s1 + ", " + s2;

// Wrapping standard Java BinaryOperator with non-null defenses
BinaryOperator<Integer> legacyMax = Math::max;
Operator2<Integer> safeMax = Operator2.operator2(legacyMax);

int maxVal = safeMax.apply(10, 20); // 20
```

---

## 2. Partial Application with Dynamic Suppliers

One of the standout features of `Operator2<A>` is its specialized `partial1` and `partial2` methods. Unlike generic `Fun2` (which returns `Fun<A, A>`), `Operator2` guarantees the result is a unary [`Operator<A>`](/reference/base/functions/operator/):

<Tabs>
  <TabItem label="Fix First Operand (partial1)">
    ```java
    Operator2<Integer> subtract = (a, b) -> a - b;

    // Fix a = 100 -> returns Operator<Integer> computing (100 - b)
    var subtractFrom100 = subtract.partial1(() -> 100);

    int res1 = subtractFrom100.apply(30); // 70
    int res2 = subtractFrom100.apply(15); // 85
    ```
  </TabItem>
  <TabItem label="Fix Second Operand (partial2)">
    ```java
    Operator2<Integer> subtract = (a, b) -> a - b;

    // Fix b = 10 -> returns Operator<Integer> computing (a - 10)
    var minus10 = subtract.partial2(() -> 10);

    int res1 = minus10.apply(50); // 40
    int res2 = minus10.apply(25); // 15
    ```
  </TabItem>
</Tabs>

:::tip[Dynamic Suppliers]
Because the partially applied argument is resolved via a `Supplier<A>`, you can bind it to dynamic configurations, atomic counters, or thread-local contexts.
:::

---

## 3. Post-Processing with `andThen`

You can attach a post-processing `UnaryOperator<A>` to transform the computed result:

```java title="PostProcessing.java"
import org.quurz.foomp.base.functions.Operator2;

Operator2<String> concat = (s1, s2) -> s1 + s2;

// After concatenating, trim whitespace
Operator2<String> cleanConcat = concat.andThen(String::trim);

String result = cleanConcat.apply("  hello  ", "  world  ");
// Result: "hello    world" (outer whitespace trimmed)
```

---

## 4. Reversing Operands with `flip`

For non-commutative operations (like division or subtraction), `flip()` cleanly swaps the parameter order:

```java title="FlippingOperands.java"
import org.quurz.foomp.base.functions.Operator2;

Operator2<String> prefixWith = (prefix, text) -> prefix + text;
Operator2<String> appendTo = prefixWith.flip();

String s1 = prefixWith.apply("[INFO] ", "Ready"); // "[INFO] Ready"
String s2 = appendTo.apply("[INFO] ", "Ready");   // "Ready[INFO] "
```

---

## 5. Aggregations & Reductions with Streams

Because `Operator2<A>` implements `java.util.function.BinaryOperator<A>`, it is directly pluggable into Java Stream reductions:

```java title="StreamReductions.java"
import org.quurz.foomp.base.functions.Operator2;
import java.util.List;

Operator2<Integer> sum = (x, y) -> x + y;
Operator2<Integer> product = (x, y) -> x * y;

List<Integer> numbers = List.of(1, 2, 3, 4, 5);

int totalSum = numbers.stream().reduce(0, sum);        // 15
int totalProduct = numbers.stream().reduce(1, product); // 120
```

---

## Summary Comparison

| Capability | Standard `BinaryOperator<A>` | Foomp `Operator2<A>` |
| :--- | :--- | :--- |
| **Null-Safety** | Unspecified / Permits nulls | Runtime enforced non-null contracts |
| **Partial Application** | Not available | `.partial1(...)`, `.partial2(...)` $\to$ `Operator<A>` |
| **Currying** | Not available | Inherited `.curry()` $\to$ `Fun<A, Fun<A, A>>` |
| **Argument Reversal** | Manual wrapper lambda | `.flip()` |
| **Post-Processing** | `andThen(Function)` $\to$ `BiFunction` | `andThen(UnaryOperator)` $\to$ `Operator2<A>` |
