---
title: Working with Fun
description: A practical guide to composing pipelines, memoizing calculations, and executing asynchronous operations with Fun.
---

import { Aside, Tabs, TabItem } from '@astrojs/starlight/components';

In modern Java development, functions are the core building blocks of data transformation. While standard Java provides `java.util.function.Function<T, R>`, it lacks strict guarantees against `null` values and misses built-in combinators for common operations like memoization or asynchronous execution.

`Fun<X, Y>` enhances Java's standard function model by:
* **Enforcing strict null hygiene** at every step (no `null` inputs, no `null` returns).
* **Providing seamless async execution** via `.async()`.
* **Enabling thread-safe caching** via `.memoise()`.
* **Supporting ergonomic function composition** with `.andThen()` and `.compose()`.

---

## Strict Null Hygiene

In standard Java, functions silently accept `null` arguments or produce `null` outputs, often leading to unpredictable `NullPointerException` errors deep down the call stack.

<Tabs>
  <TabItem label="Standard Function (Null Leakage)">
    ```java
    Function<String, String> trim = s -> s == null ? null : s.trim();
    Function<String, Integer> length = String::length;

    // Passing null through composed function throws NPE unpredictably
    Function<String, Integer> pipeline = trim.andThen(length);
    pipeline.apply(null); // ❌ NullPointerException thrown inside length, not trim!
    ```
  </TabItem>
  <TabItem label="Foomp Fun (Strict Contract)">
    ```java
    import org.quurz.foomp.base.functions.Fun;

    Fun<String, String> trim = String::trim;
    Fun<String, Integer> length = String::length;

    Fun<String, Integer> pipeline = trim.andThen(length);

    // ✅ Throws immediately with a clear contract error when null is passed
    pipeline.apply(null); // Throws NullPointerException: "x must not be null"
    ```
  </TabItem>
</Tabs>

:::tip[Wrapping Legacy Functions]
If you receive a standard `java.util.function.Function` from an external library, use `Fun.fun(externalFunction)` or `fun.nullSafe()` to enforce non-null boundaries immediately.
:::

---

## Function Composition Pipelines

You can build clean, declarative data processing pipelines using `.andThen()` (forward composition) and `.compose()` (backward composition):

```java
import org.quurz.foomp.base.functions.Fun;

// Individual transformation steps
Fun<String, String> sanitize = s -> s.trim().toLowerCase();
Fun<String, String[]> tokenize = s -> s.split("\\s+");
Fun<String[], Integer> countWords = tokens -> tokens.length;

// 1. Forward composition with andThen: sanitize -> tokenize -> countWords
Fun<String, Integer> wordCounter = sanitize
        .andThen(tokenize)
        .andThen(countWords);

int words = wordCounter.apply("  Functional Programming in Modern Java  ");
System.out.println("Word count: " + words); // 4

// 2. Backward composition with compose: (f ∘ g)(x) = f(g(x))
Fun<Integer, String> formatCount = count -> "Total words: " + count;
Fun<String, String> analyzeText = formatCount.compose(wordCounter);

System.out.println(analyzeText.apply("Hello World")); // Total words: 2
```

---

## Caching Expensive Computations with `memoise()`

When working with pure, computationally heavy operations (such as mathematical algorithms, parsing schemas, or compiling regexes), you can turn any `Fun` into a high-performance cached function with `.memoise()`:

```java
import org.quurz.foomp.base.functions.Fun;
import java.math.BigInteger;

// 1. Define a heavy pure function
Fun<Integer, BigInteger> factorial = n -> {
    System.out.println("Calculating factorial for " + n + "...");
    BigInteger result = BigInteger.ONE;
    for (int i = 2; i <= n; i++) {
        result = result.multiply(BigInteger.valueOf(i));
    }
    return result;
};

// 2. Wrap with thread-safe caching
var cachedFactorial = factorial.memoise();

// First call: executes calculation
BigInteger f1 = cachedFactorial.apply(20); // Prints: Calculating factorial for 20...

// Second call: returns cached result immediately without calculation
BigInteger f2 = cachedFactorial.apply(20); // Instant return!
```

:::caution[Purity Requirement]
Memoization assumes **referential transparency**. Do not memoize functions that depend on mutable state, current system time, or external I/O where results may change.
:::

---

## Turning Synchronous Code Asynchronous with `async()`

Foomp allows you to convert any synchronous transformation into an asynchronous operation returning a `CompletableFuture<Y>` with zero boilerplate:

```java
import org.quurz.foomp.base.functions.Fun;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

Fun<String, Integer> parseAndCompute = text -> {
    // Heavy synchronous task
    return text.hashCode() * 31;
};

// 1. Asynchronous execution on the default ForkJoinPool
Fun<String, CompletableFuture<Integer>> asyncTask = parseAndCompute.async();

CompletableFuture<Integer> futureResult = asyncTask.apply("Sample payload");
futureResult.thenAccept(result -> System.out.println("Result: " + result));

// 2. Asynchronous execution on a custom Executor
var customPool = Executors.newFixedThreadPool(4);
Fun<String, CompletableFuture<Integer>> pooledTask = parseAndCompute.async(customPool);

pooledTask.apply("Another payload")
          .thenAccept(result -> System.out.println("Async custom pool result: " + result));
```

---

## Interoperability with Java Streams & APIs

Because `Fun<X, Y>` directly implements `java.util.function.Function<X, Y>`, it is 100% compatible with Java Streams, `Optional`, and existing libraries:

```java
import org.quurz.foomp.base.functions.Fun;
import java.util.List;

Fun<String, String> clean = s -> s.replaceAll("[^a-zA-Z]", "");
Fun<String, Integer> len = String::length;

List<String> rawInputs = List.of("apple!", "banana#", "cherry?");

// Seamlessly pass Fun to Java Stream API
List<Integer> lengths = rawInputs.stream()
        .map(clean.andThen(len))
        .toList();

System.out.println(lengths); // [5, 6, 6]
```

---

## Summary

* **Strict Null Safety:** `Fun<X, Y>` rejects `null` inputs and returns, avoiding unhandled errors.
* **Declarative Pipelines:** Compose functions effortlessly with `.andThen()` and `.compose()`.
* **Instant Caching:** Accelerate pure operations with `.memoise()`.
* **Zero-Boilerplate Async:** Convert synchronous functions into non-blocking `CompletableFuture` workflows via `.async()`.
* **JDK Compatibility:** Works natively as a drop-in replacement for standard `java.util.function.Function`.
