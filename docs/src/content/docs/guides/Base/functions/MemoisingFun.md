---
title: Working with MemoisingFun
description: Practical recipes and guide for caching function evaluation results using MemoisingFun in Foomp.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

`MemoisingFun` provides high-performance, thread-safe in-memory caching for pure deterministic functions. By storing results upon first execution, it transforms $O(2^n)$ recursive computations or expensive transformations into lightning-fast $O(1)$ lookups.

---

## 1. Caching Expensive Computations

A common use case for memoization is computing values with high computational complexity or recursive subproblems:

```java title="FibonacciCalculator.java"
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.functions.MemoisingFun;
import java.math.BigInteger;

public class FibonacciCalculator {

    // Self-referencing memoized recursive function
    private final MemoisingFun<Integer, BigInteger> memoisedFib =
        MemoisingFun.memoisingFun(n -> {
            if (n <= 1) return BigInteger.valueOf(n);
            return fib(n - 1).add(fib(n - 2));
        });

    public BigInteger fib(int n) {
        return memoisedFib.apply(n);
    }
}
```

:::tip[Performance Advantage]
Without memoization, computing `fib(50)` requires over $10^{14}$ recursive invocations. With `MemoisingFun`, `fib(50)` executes in 51 linear steps and subsequent queries for any $n \le 50$ resolve in constant time $O(1)$.
:::

---

## 2. Heavy Data Parsing & Model Mapping

Memoization is equally valuable when mapping static or repeated domain codes to complex objects:

```java title="TaxCodeLookup.java"
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.functions.MemoisingFun;

public class TaxService {

    private final MemoisingFun<String, TaxRule> ruleCache =
        Fun.<String, TaxRule>fun(code -> loadAndCompileTaxRule(code))
           .memoise();

    public BigDecimal calculateTax(String taxCode, BigDecimal amount) {
        TaxRule rule = ruleCache.apply(taxCode);
        return rule.apply(amount);
    }

    private TaxRule loadAndCompileTaxRule(String code) {
        // Expensive parsing, AST construction, or complex validation
        return new TaxRule(code);
    }
}
```

---

## 3. Combining with Pipelines and Combinators

Since `MemoisingFun` extends [`Fun<X, Y>`](/reference/base/functions/fun/), all composition combinators are fully available:

```java title="PipelineComposition.java"
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.functions.MemoisingFun;

// 1. Create expensive parsing step and memoize it
MemoisingFun<String, UserProfile> parseProfile = 
    Fun.<String, UserProfile>fun(UserProfile::fromJson).memoise();

// 2. Compose into a larger processing pipeline
Fun<String, String> displayNamePipeline = 
    parseProfile.andThen(UserProfile::getFullName)
                .andThen(String::toUpperCase);

// First invocation parses JSON and caches UserProfile
String name1 = displayNamePipeline.apply(jsonPayload);

// Second invocation uses the cached UserProfile and runs subsequent pipeline steps
String name2 = displayNamePipeline.apply(jsonPayload);
```

---

## 4. Cache Invalidation and Dynamic Resets

When source configuration or underlying rules change, invalidate the cache via `.clear()`:

```java title="CacheInvalidation.java"
MemoisingFun<String, TaxRule> cache = ...;

// Invalidate all cached entries
cache.clear();

// Method chaining allows clearing before re-evaluating
TaxRule freshRule = cache.clear().apply("VAT_DE");
```

---

## 5. Concurrent Thread-Safety

`MemoisingFun` leverages `ConcurrentHashMap.computeIfAbsent` to guarantee atomic evaluation:

```java title="ConcurrentExecution.java"
MemoisingFun<Integer, String> memoised = 
    Fun.<Integer, String>fun(i -> "Result-" + i).memoise();

// 100 parallel threads querying the same key concurrently
IntStream.range(0, 100).parallel().forEach(i -> {
    String res = memoised.apply(42);
    assert "Result-42".equals(res);
});
// The underlying function was executed exactly once
```

---

## Best Practices

:::note[Contract Checklist]
1. **Purity:** Ensure the function is pure (referentially transparent). Output must depend solely on input arguments.
2. **Immutable Keys:** Use types with reliable, immutable `equals()` and `hashCode()` implementations.
3. **Non-Null Values:** Neither arguments nor return values may be `null`.
:::
