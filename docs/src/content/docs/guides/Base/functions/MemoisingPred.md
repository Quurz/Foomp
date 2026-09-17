---
title: Working with MemoisingPred
description: Practical recipes and guide for caching boolean predicate evaluations using MemoisingPred in Foomp.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

`MemoisingPred` provides thread-safe in-memory caching for boolean predicates. In data processing pipelines, validations, and stream filters, identical items or repetitive attributes often recur. Memoizing the evaluation ensures each distinct object is checked at most once.

---

## 1. Accelerating Stream Filtering

In large datasets with duplicate values (or duplicate keys), filtering using an expensive predicate can degrade performance significantly:

```java title="StreamFilteringExample.java"
import org.quurz.foomp.base.functions.MemoisingPred;
import org.quurz.foomp.base.functions.Pred;
import java.util.List;

public class PrimeFilterService {

    // Memoized primality test
    private final MemoisingPred<Long> isPrimeCached = 
        Pred.<Long>pred(PrimeFilterService::calculateIsPrime)
            .memoise();

    public List<Long> filterPrimes(List<Long> numbers) {
        // Repeated numbers across the list are resolved instantly from cache
        return numbers.stream()
                      .filter(isPrimeCached)
                      .toList();
    }

    private static boolean calculateIsPrime(long n) {
        if (n <= 1) return false;
        for (long i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
}
```

---

## 2. Heavy Regex & Schema Validation

Validating strings with complex regular expressions or schema parsers can consume heavy CPU resources. Memoizing the validation predicate caches matching results:

```java title="EmailValidator.java"
import org.quurz.foomp.base.functions.Pred;
import org.quurz.foomp.base.functions.MemoisingPred;
import java.util.regex.Pattern;

public class UserValidator {

    private static final Pattern STRICT_EMAIL = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    private final MemoisingPred<String> validEmail =
        Pred.<String>pred(email -> STRICT_EMAIL.matcher(email).matches())
            .memoise();

    public boolean isValidEmail(String email) {
        return validEmail.test(email);
    }
}
```

---

## 3. Composing with Algebraic Combinators

`MemoisingPred` inherits all of `Pred`'s algebraic combinators (`and`, `or`, `xor`, `nand`, `nor`, `negate`). You can combine multiple memoized predicates into expressive validation trees:

```java title="CompositeRules.java"
import org.quurz.foomp.base.functions.Pred;
import org.quurz.foomp.base.functions.MemoisingPred;

MemoisingPred<Transaction> isDomestic = 
    Pred.<Transaction>pred(Transaction::isDomestic).memoise();

MemoisingPred<Transaction> isHighValue = 
    Pred.<Transaction>pred(tx -> tx.getAmount() > 10_000).memoise();

MemoisingPred<Transaction> isSuspiciousOrigin = 
    Pred.<Transaction>pred(Transaction::hasSuspiciousOrigin).memoise();

// Short-circuiting composite rule
Pred<Transaction> flaggedTransaction = 
    isSuspiciousOrigin.or(isHighValue.and(isDomestic.negate()));

boolean alert = flaggedTransaction.test(currentTransaction);
```

---

## 4. Cache Invalidation and Dynamic Resets

When underlying validation rules or reference data change, clear the cache using `.clear()`:

```java title="CacheInvalidation.java"
MemoisingPred<String> validator = ...;

// Invalidate all cached evaluation outcomes
validator.clear();

// Method chaining allows clearing and evaluating in one expression
boolean freshOutcome = validator.clear().test("sample-input");
```

---

## 5. Parallel Stream Concurrency

Because `MemoisingPred` is backed by `ConcurrentHashMap`, parallel stream operations can share the same memoized predicate safely without concurrency locks or duplicate work:

```java title="ParallelStream.java"
MemoisingPred<String> expensiveCheck = 
    Pred.<String>pred(HeavyChecker::check).memoise();

List<String> processed = rawData.parallelStream()
                                .filter(expensiveCheck)
                                .toList();
```

---

## Best Practices

:::note[Contract Checklist]
1. **Purity:** Predicate evaluations must be referentially transparent. Given equal inputs, `test(a)` must always yield the same boolean outcome.
2. **Immutable Arguments:** Ensure input objects have stable `equals` and `hashCode` implementations.
3. **Non-Null Inputs:** Arguments passed to `test` must never be `null`.
:::
