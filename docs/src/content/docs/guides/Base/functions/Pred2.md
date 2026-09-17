---
title: Working with Pred2
description: Practical recipes and guide for binary conditions, dynamic partial application, cross-field validation, and logical composition using Pred2 in Foomp.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

`Pred2<A1, A2>` represents a binary boolean condition taking two heterogeneous or homogeneous arguments ($A1 \times A2 \to \{\text{true}, \text{false}\}$). By extending `BiPredicate<A1, A2>` and offering complete propositional logic and partial application, `Pred2` simplifies relational comparisons, dual-field validations, and access control rules.

---

## 1. Creating Binary Predicates

You can declare `Pred2<A1, A2>` via lambdas, method references, or wrap existing JDK `BiPredicate` instances:

```java title="Pred2Creation.java"
import org.quurz.foomp.base.functions.Pred2;
import java.util.function.BiPredicate;

// Checking if an item belongs to a category with minimum price
Pred2<String, Double> isPremiumInCat = (category, price) -> "TECH".equals(category) && price > 500.0;

// Relational comparison
Pred2<Integer, Integer> isMultipleOf = (a, b) -> b != 0 && a % b == 0;

// Wrapping JDK BiPredicate
BiPredicate<String, String> legacyEquals = String::equalsIgnoreCase;
Pred2<String, String> safeEquals = Pred2.pred2(legacyEquals);
```

---

## 2. Partial Application (`partial1` & `partial2`)

One of `Pred2`'s most powerful capabilities is converting a binary predicate into a unary [`Pred<A>`](/reference/base/functions/pred/) by supplying one of the arguments dynamically:

```java title="PartialEvaluation.java"
import org.quurz.foomp.base.functions.Pred2;
import org.quurz.foomp.base.functions.Pred;

Pred2<String, Integer> lengthConstraint = (prefix, minLen) -> prefix.length() >= minLen;

// Fix the second argument (minLen = 8) -> yields Pred<String>
Pred<String> isAtLeast8Chars = lengthConstraint.partial2(() -> 8);

boolean valid = isAtLeast8Chars.test("SecretPass"); // true
boolean invalid = isAtLeast8Chars.test("abc");       // false

// Fix the first argument dynamically from a session/user supplier
Pred2<UserRole, Resource> permissionCheck = (role, res) -> role.canAccess(res);
Pred<Resource> currentSessionAccess = permissionCheck.partial1(() -> SecurityContext.getCurrentRole());
```

<Tabs>
  <TabItem label="Fix First: partial1">
    ```java
    // Pred2<A1, A2> + Supplier<A1> => Pred<A2>
    Pred2<String, String> startsWith = (target, prefix) -> target.startsWith(prefix);
    Pred<String> startsWithTest = startsWith.partial1(() -> "test_user");
    ```
  </TabItem>
  <TabItem label="Fix Second: partial2">
    ```java
    // Pred2<A1, A2> + Supplier<A2> => Pred<A1>
    Pred2<Integer, Integer> greaterThan = (x, limit) -> x > limit;
    Pred<Integer> isAdult = greaterThan.partial2(() -> 18);
    ```
  </TabItem>
</Tabs>

---

## 3. Composing Propositional Gates with `Pred2`

`Pred2` supports `and`, `or`, `xor`, `nand`, and `nor`, allowing expressive combining of complex binary rules:

```java title="BinaryGateComposition.java"
import org.quurz.foomp.base.functions.Pred2;

record Account(String id, boolean active, int score) {}
record Transfer(double amount, String targetCountry) {}

Pred2<Account, Transfer> isDomesticLowAmount = (acc, tx) ->
    "DE".equals(tx.targetCountry()) && tx.amount() < 1000.0;

Pred2<Account, Transfer> isHighTrustAccount = (acc, tx) ->
    acc.active() && acc.score() > 750;

// High trust OR domestic low amount must hold
Pred2<Account, Transfer> autoApproveTransfer = isHighTrustAccount.or(isDomesticLowAmount);

// Account cannot be active NAND marked for audit
Pred2<Account, Transfer> fraudCheck = autoApproveTransfer.nand((acc, tx) -> tx.amount() > 50_000.0);
```

---

## 4. Multi-Condition Filtering with Map Entries & Pairs

Combining `Pred2` with streams of key-value pairs or map entries creates concise data processing pipelines:

```java title="MapFiltering.java"
import org.quurz.foomp.base.functions.Pred2;
import java.util.Map;

Pred2<String, Integer> stockThreshold = (product, qty) -> qty < 10;
Pred2<String, Integer> discontinued = (product, qty) -> product.startsWith("OLD_");

// Items that need restock: below threshold AND NOT discontinued
Pred2<String, Integer> needsRestock = stockThreshold.and(discontinued.negate());

Map<String, Integer> inventory = Map.of(
    "LAPTOP", 5,
    "OLD_KEYBOARD", 2,
    "MONITOR", 15
);

var toOrder = inventory.entrySet().stream()
    .filter(e -> needsRestock.test(e.getKey(), e.getValue()))
    .map(Map.Entry::getKey)
    .toList();
// toOrder: ["LAPTOP"]
```

---

## Summary Comparison

| Feature | Standard Java `BiPredicate<T, U>` | Foomp `Pred2<A1, A2>` |
| :--- | :--- | :--- |
| **Logic Combinators** | `and`, `or`, `negate` | `and`, `or`, `xor`, `nand`, `nor`, `negate` |
| **Lazy Dynamic Suppliers** | None | `and(BooleanSupplier)`, `or(Supplier<Boolean>)`, etc. |
| **Partial Application** | Manual nested lambda | `partial1(Supplier<A1>)`, `partial2(Supplier<A2>)` returning `Pred<A>` |
| **Wrapper / Adapters** | None | `Pred2.pred2(...)`, `Pred2.not(...)` |
