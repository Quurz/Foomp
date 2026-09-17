---
title: Pair Guide
description: Practical developer guide for working with the mutable 2-tuple Pair in Foomp.
---

`Pair<A1, A2>` is a mutable container designed for situations where two related values need to be held together, updated in place, or interact with legacy nullable APIs.

---

## When to Use `Pair<A1, A2>`

* **In-Place Mutation:** Accumulating or updating state across multiple steps without reallocating containers.
* **Nullable State:** Working with APIs or subsystems where components may legitimately be `null`.
* **Deep Copying:** Duplicating state across boundaries while preserving or cloning underlying `Copyable` elements.
* **Safe Conversion to Functional Tuples:** Safely converting nullable pairs to typed `Tuple2<Maybe<A1>, Maybe<A2>>` structures.

---

## Examples

### 1. Basic Creation and In-Place Mutation

```java title="PairMutationExample.java"
import org.quurz.foomp.base.util.Pair;

import static org.quurz.foomp.base.util.Pair.pair;

public class PairMutationExample {
    public static void main(String[] args) {
        // Create a mutable pair with initial values
        Pair<String, Integer> counter = pair("requests", 0);

        // Mutate values in place
        counter.set2(counter.get2() + 1);
        System.out.println(counter); // Pair[value1=requests, value2=1]

        // Components can be set to null
        counter.set1(null);
        System.out.println("isPresent1: " + counter.isPresent1()); // false
    }
}
```

---

### 2. Functional Updates with `with1` and `with2`

If you want to keep the original pair unchanged while producing a new instance with an updated component, use wither methods:

```java title="PairWitherExample.java"
import org.quurz.foomp.base.util.Pair;

import static org.quurz.foomp.base.util.Pair.pair;

public class PairWitherExample {
    public static void main(String[] args) {
        Pair<String, Double> original = pair("USD", 1.0);

        // Create new pairs with replaced components
        Pair<String, Double> updatedCurrency = original.with1("EUR");
        Pair<String, Double> updatedRate = original.with2(0.92);

        System.out.println("Original: " + original);               // Pair[value1=USD, value2=1.0]
        System.out.println("New Currency: " + updatedCurrency);   // Pair[value1=EUR, value2=1.0]
        System.out.println("New Rate: " + updatedRate);           // Pair[value1=USD, value2=0.92]
    }
}
```

---

### 3. Converting to `Tuple2` of `Maybe` Values

To bridge mutable/nullable pairs into Foomp's functional pipeline:

```java title="PairToTupleExample.java"
import org.quurz.foomp.base.util.Maybe;
import org.quurz.foomp.base.util.Pair;
import org.quurz.foomp.base.util.Tuple2;

import static org.quurz.foomp.base.util.Pair.pair;

public class PairToTupleExample {
    public static void main(String[] args) {
        Pair<String, Integer> pair = pair("Active", null);

        // Converts nullable components to Maybe containers
        Tuple2<Maybe<String>, Maybe<Integer>> tuple = pair.toTuple();

        System.out.println("Status: " + tuple.get1().getOrElse(() -> "Unknown")); // Active
        System.out.println("Count: " + tuple.get2().getOrElse(() -> 0));          // 0
    }
}
```

---

## Best Practices

:::tip[Pair Guidelines]
* **Prefer Immutability When Possible:** If components are non-null and do not require in-place mutation, prefer [`Record2`](/reference/base/util/record2/) or [`Tuple2`](/reference/base/util/tuple2/).
* **Deep Copying:** When sharing a `Pair` across thread boundaries or caching layers, invoke `.copy()` to avoid unintended shared mutations.
* **Bridge with `toTuple()`:** When feeding data into monadic or stream pipelines, convert nullable pairs with `.toTuple()` to leverage type-safe `Maybe` operations.
:::
