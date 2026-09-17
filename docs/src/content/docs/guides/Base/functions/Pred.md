---
title: Working with Pred
description: Practical recipes and guide for boolean logic, algebraic gate combinations, lazy suppliers, and stream filtering using Pred in Foomp.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

`Pred<A>` is Foomp's foundational functional predicate interface. Extending Java's standard `java.util.function.Predicate<A>`, it equips developers with complete propositional logic (AND, OR, XOR, NAND, NOR, NOT), multi-condition varargs aggregations, lazy short-circuiting with dynamic boolean suppliers, and built-in caching via `.memoise()`.

---

## 1. Creating and Wrapping Predicates

You can construct `Pred<A>` instances directly from lambdas, method references, constant factories, or by adapting existing standard `Predicate` instances.

```java title="PredCreation.java"
import org.quurz.foomp.base.functions.Pred;
import java.util.function.Predicate;

// Lambda definition
Pred<String> isNonBlank = s -> !s.trim().isEmpty();
Pred<Integer> isPositive = n -> n > 0;

// Constant predicates (with strict null defense)
Pred<User> alwaysAllow = Pred.alwaysTrue();
Pred<User> alwaysDeny = Pred.alwaysFalse();

// Wrapping existing standard Java Predicate
Predicate<String> legacyPred = s -> s.startsWith("AUTH_");
Pred<String> safePred = Pred.pred(legacyPred);
```

---

## 2. Complete Propositional Logic (XOR, NAND, NOR)

Standard Java only offers `.and()`, `.or()`, and `.negate()`. `Pred<A>` brings full algebraic completeness directly to your domain logic:

```java title="LogicalGates.java"
import org.quurz.foomp.base.functions.Pred;

Pred<User> hasAdminRole = User::isAdmin;
Pred<User> isEmergencyOverride = User::hasEmergencyToken;
Pred<User> isAccountLocked = User::isLocked;

// XOR: Exactly one condition must be true
Pred<User> exclusiveAccess = hasAdminRole.xor(isEmergencyOverride);

// NAND: Not both conditions allowed at the same time
Pred<User> safeSession = hasAdminRole.nand(isAccountLocked);

// NOR: Neither condition must be true
Pred<User> cleanAccount = isAccountLocked.nor(User::isFlagged);
```

<Tabs>
  <TabItem label="Exclusive OR (XOR)">
    ```java
    // (A OR B) AND NOT (A AND B)
    Pred<Integer> exactlyOneCondition = isEven.xor(isGreaterThan100);
    ```
  </TabItem>
  <TabItem label="NAND (Not Both)">
    ```java
    // NOT (A AND B)
    Pred<Order> validDiscount = hasVoucher.nand(isEmployeeDiscount);
    ```
  </TabItem>
  <TabItem label="NOR (Neither)">
    ```java
    // NOT (A OR B)
    Pred<Document> isPublic = isConfidential.nor(isInternalOnly);
    ```
  </TabItem>
</Tabs>

---

## 3. Varargs Conjunction and Disjunction

When combining many rules into composite business policies, varargs combinators make complex compound conditions readable and maintainable:

```java title="RuleComposition.java"
import org.quurz.foomp.base.functions.Pred;

record Order(double amount, boolean isVip, boolean inStock, boolean fraudFlag) {}

Pred<Order> isStockAvailable = Order::inStock;
Pred<Order> isReasonableAmount = o -> o.amount() <= 10_000.0;
Pred<Order> isCleanRiskProfile = o -> !o.fraudFlag();

// Varargs AND: All rules must match
Pred<Order> isAutoApprovable = Pred.and(
    isStockAvailable,
    isReasonableAmount,
    isCleanRiskProfile
);

// Varargs OR: Any rule qualifies
Pred<Order> qualifiesForPriority = Pred.or(
    Order::isVip,
    o -> o.amount() > 500.0,
    o -> o.inStock()
);
```

---

## 4. Short-Circuiting with Dynamic Suppliers

You can compose a predicate with dynamic runtime flags using `BooleanSupplier` or `Supplier<Boolean>`. The supplier is evaluated lazily, guaranteeing minimal runtime overhead:

```java title="LazySupplierEvaluation.java"
import org.quurz.foomp.base.functions.Pred;

Pred<Request> isValidOrigin = req -> req.ip().startsWith("10.0.");

// BooleanSupplier evaluated ONLY if isValidOrigin returns true
Pred<Request> isAllowed = isValidOrigin.and(() -> featureFlagService.isFeatureEnabled("BETA_API"));

// BooleanSupplier evaluated ONLY if first predicate is false
Pred<Request> isFallbackAllowed = isValidOrigin.or(() -> securityPolicy.allowGuestAccess());
```

---

## 5. Integration with Memoization and Streams

`Pred<A>` seamlessly interoperates with JDK collections and streams, and can be converted into a thread-safe caching predicate via `.memoise()`:

```java title="StreamFiltering.java"
import org.quurz.foomp.base.functions.Pred;
import org.quurz.foomp.base.functions.MemoisingPred;
import java.util.List;

// Heavy / expensive predicate
Pred<String> isGrammaticallyValid = text -> ComplexNlpEngine.validate(text);

// Wrap with thread-safe ConcurrentHashMap cache
MemoisingPred<String> cachedValidator = isGrammaticallyValid.memoise();

List<String> validComments = rawComments.stream()
    .filter(cachedValidator)
    .toList();
```

---

## Summary Comparison

| Feature | Standard `java.util.function.Predicate<A>` | Foomp `Pred<A>` |
| :--- | :--- | :--- |
| **Logic Operators** | `and`, `or`, `negate` | `and`, `or`, `xor`, `nand`, `nor`, `negate` |
| **Varargs Aggregations** | None | `Pred.and(...)`, `Pred.or(...)`, `Pred.xor(...)` |
| **Lazy Dynamic Suppliers** | None | `and(BooleanSupplier)`, `or(Supplier<Boolean>)`, etc. |
| **Memoization Support** | None | Direct `.memoise()` returning `MemoisingPred<A>` |
| **Constant Factories** | None (only `Predicate.not` in Java 11+) | `alwaysTrue()`, `alwaysFalse()`, `not(...)`, `pred(...)` |
