---
title: Working with Fun2
description: Practical recipes for binary functions in Foomp, covering currying, supplier-based partial application, and argument flipping.
---

import { Aside, Tabs, TabItem } from '@astrojs/starlight/components';

Binary operations (functions accepting two inputs and producing an output) are ubiquitous in programming: arithmetic calculations, string joins, data mergers, coordinate calculations, and comparators.

While Java provides `java.util.function.BiFunction<T, U, R>`, it lacks algebraic flexibility: you cannot easily curry it, flip its argument order, or partially apply arguments without verbose lambda boilerplate.

`Fun2<X1, X2, Y>` extends `BiFunction` to provide:
* **Strict non-null contracts** on inputs and outputs.
* **First-class Currying & Uncurrying** via `.curry()` and `Fun2.uncurry()`.
* **Dynamic Partial Application** via `.partial1()` and `.partial2()`.
* **Argument Flipping** via `.flip()`.
* **Output Composition** via `.andThen()`.

---

## Strict Null Hygiene

`Fun2` guarantees that neither input argument nor the final return value can be `null`.

<Tabs>
  <TabItem label="Standard BiFunction (Silent Nulls)">
    ```java
    BiFunction<String, String, String> join = (a, b) -> a + ": " + b;

    // Accepts nulls silently, producing corrupted string outputs
    String result = join.apply(null, "value"); // "null: value"
    ```
  </TabItem>
  <TabItem label="Foomp Fun2 (Strict Contract)">
    ```java
    import org.quurz.foomp.base.functions.Fun2;
    import static org.quurz.foomp.base.functions.Fun2.fun2;

    Fun2<String, String, String> join = (a, b) -> a + ": " + b;

    // ✅ Rejects null arguments immediately at the boundary
    join.apply(null, "value"); // Throws NullPointerException: "x1 must not be null"
    ```
  </TabItem>
</Tabs>

---

## Currying and Uncurrying

**Currying** translates a function taking two arguments `(X1, X2) -> Y` into a chain of two single-argument functions `X1 -> (X2 -> Y)`.

```java
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.functions.Fun2;
import static org.quurz.foomp.base.functions.Fun2.uncurry;

// Binary function: (prefix, text) -> formatted string
Fun2<String, String, String> tagger = (tag, text) -> "<" + tag + ">" + text + "</" + tag + ">";

// 1. Curry the binary function
Fun<String, Fun<String, String>> curriedTagger = tagger.curry();

// 2. Create specialized unary functions by applying the first argument
Fun<String, String> htmlParagraph = curriedTagger.apply("p");
Fun<String, String> htmlHeading = curriedTagger.apply("h1");

System.out.println(htmlParagraph.apply("Hello World")); // <p>Hello World</p>
System.out.println(htmlHeading.apply("Welcome"));       // <h1>Welcome</h1>

// 3. Uncurry back to binary form
Fun2<String, String, String> restoredTagger = uncurry(curriedTagger);
System.out.println(restoredTagger.apply("em", "Italic")); // <em>Italic</em>
```

---

## Partial Application with Dynamic Suppliers

Foomp's `partial1` and `partial2` methods fix one argument using a `java.util.function.Supplier`. 

Because a `Supplier` is passed instead of a static value, the argument is re-evaluated on **each invocation**. This enables powerful patterns with dynamic context, configuration values, or timestamps.

```java
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.functions.Fun2;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

Fun2<String, String, String> logger = (time, message) -> "[" + time + "] " + message;

DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

// Fix the first argument dynamically to current system time
Fun<String, String> currentLog = logger.partial1(() -> LocalTime.now().format(formatter));

System.out.println(currentLog.apply("System started"));  // [14:30:15] System started
// ... 2 seconds later ...
System.out.println(currentLog.apply("User logged in"));  // [14:30:17] User logged in
```

### Fixing Constant Arguments
To partially apply a static constant, simply pass a lambda supplier `() -> constant`:

```java
Fun2<Double, Double, Double> power = Math::pow;

// partial1: Fix base to 2.0 (calculates 2^x)
Fun<Double, Double> powerOfTwo = power.partial1(() -> 2.0);
System.out.println(powerOfTwo.apply(8.0)); // 256.0

// partial2: Fix exponent to 2.0 (calculates x^2)
Fun<Double, Double> square = power.partial2(() -> 2.0);
System.out.println(square.apply(9.0)); // 81.0
```

---

## Flipping Argument Order (`flip`)

When working with higher-order functions or APIs where parameter orders do not match your pipeline, `.flip()` swaps `(x1, x2)` to `(x2, x1)`:

```java
import org.quurz.foomp.base.functions.Fun2;

// Original: (haystack, needle) -> contains
Fun2<String, String, Boolean> contains = (haystack, needle) -> haystack.contains(needle);

// Flipped: (needle, haystack) -> contains
Fun2<String, String, Boolean> isContainedIn = contains.flip();

String text = "Functional programming in Java";

System.out.println(contains.apply(text, "Java"));     // true (haystack first)
System.out.println(isContainedIn.apply("Java", text)); // true (needle first)

// Easily bind the needle dynamically
var containsJava = isContainedIn.partial1(() -> "Java");
System.out.println(containsJava.apply(text)); // true
```

---

## Composing Binary Pipelines (`andThen`)

You can attach a post-processing step to the binary function using `.andThen()`:

```java
import org.quurz.foomp.base.functions.Fun2;

Fun2<Integer, Integer, Integer> multiply = (a, b) -> a * b;

// Multiply two numbers, then convert the result to a currency string
Fun2<Integer, Integer, String> calculateCost = multiply.andThen(cents -> "$" + (cents / 100.0));

String total = calculateCost.apply(5, 250);
System.out.println("Total price: " + total); // Total price: $12.5
```

---

## Summary

* **`Fun2<X1, X2, Y>`:** Binary functional interface with strict non-null contracts.
* **`curry()` / `uncurry()`:** Convert between binary functions and unary function chains.
* **`partial1()` / `partial2()`:** Partially apply arguments using dynamic suppliers.
* **`flip()`:** Swap argument order effortlessly without rewriting lambdas.
* **`andThen()`:** Pipe the binary output directly into subsequent transformations.
