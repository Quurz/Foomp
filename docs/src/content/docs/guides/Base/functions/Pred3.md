---
title: Working with Pred3
description: Practical recipes and guide for ternary conditions, progressive arity reduction, multi-attribute policy validation, and boolean combinators using Pred3 in Foomp.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

`Pred3<A1, A2, A3>` represents a ternary boolean condition ($A1 \times A2 \times A3 \to \{\text{true}, \text{false}\}$). Standard Java has no built-in `TriPredicate`, making `Pred3` an essential functional building block for multi-attribute authorization, spatial/temporal range checks, geometric checks, and progressive partial reduction.

---

## 1. Creating Ternary Predicates

You can declare ternary predicates using 3-parameter lambda expressions:

```java title="Pred3Creation.java"
import org.quurz.foomp.base.functions.Pred3;

// Range containment: min <= value <= max
Pred3<Integer, Integer, Integer> isBetween = (min, val, max) -> val >= min && val <= max;

// Multi-attribute authorization
Pred3<User, Resource, Permission> canPerform = (user, resource, perm) ->
    user.isActive() && user.getRole().hasPermission(resource.getType(), perm);

// Point-in-bounding-box check
Pred3<Double, Double, Double> within3DVolume = (x, y, z) ->
    Math.abs(x) <= 10.0 && Math.abs(y) <= 10.0 && Math.abs(z) <= 10.0;
```

---

## 2. Progressive Partial Application

`Pred3` provides `partial1`, `partial2`, and `partial3`, which accept a `Supplier` and reduce the ternary predicate to a [`Pred2<A, B>`](/reference/base/functions/pred2/). Calling partial application again on `Pred2` reduces it directly to a unary [`Pred<A>`](/reference/base/functions/pred/):

```java title="ProgressiveReduction.java"
import org.quurz.foomp.base.functions.Pred3;
import org.quurz.foomp.base.functions.Pred2;
import org.quurz.foomp.base.functions.Pred;

Pred3<String, String, Integer> rule = (tenant, role, minLevel) ->
    tenant.equals("PROD") && role.equals("ADMIN") && minLevel >= 3;

// Step 1: Fix tenant via partial1 -> returns Pred2<String, Integer>
Pred2<String, Integer> prodRoleRule = rule.partial1(() -> "PROD");

// Step 2: Fix role via partial1 on Pred2 -> returns Pred<Integer>
Pred<Integer> prodAdminLevelRule = prodRoleRule.partial1(() -> "ADMIN");

// Final unary test
boolean passes = prodAdminLevelRule.test(5); // true
```

<Tabs>
  <TabItem label="Fix 1st Arg (partial1)">
    ```java
    // Pred3<A1, A2, A3> + Supplier<A1> => Pred2<A2, A3>
    Pred2<Integer, Integer> inDefaultRange = isBetween.partial1(() -> 0);
    boolean valid = inDefaultRange.test(5, 10); // 0 <= 5 <= 10 -> true
    ```
  </TabItem>
  <TabItem label="Fix 2nd Arg (partial2)">
    ```java
    // Pred3<A1, A2, A3> + Supplier<A2> => Pred2<A1, A3>
    Pred2<Integer, Integer> containsZero = isBetween.partial2(() -> 0);
    boolean valid = containsZero.test(-5, 5); // -5 <= 0 <= 5 -> true
    ```
  </TabItem>
  <TabItem label="Fix 3rd Arg (partial3)">
    ```java
    // Pred3<A1, A2, A3> + Supplier<A3> => Pred2<A1, A2>
    Pred2<Integer, Integer> cappedAt100 = isBetween.partial3(() -> 100);
    boolean valid = cappedAt100.test(10, 50); // 10 <= 50 <= 100 -> true
    ```
  </TabItem>
</Tabs>

---

## 3. Logical Combinators on Ternary Rules

Just like `Pred` and `Pred2`, `Pred3` supports `and`, `or`, `xor`, `nand`, and `nor` with other ternary predicates or dynamic boolean suppliers:

```java title="TernaryLogicGates.java"
import org.quurz.foomp.base.functions.Pred3;

record Subject(String id, boolean vip) {}
record Target(String classification) {}
record Action(String verb) {}

Pred3<Subject, Target, Action> isDirectOwner = (sub, tgt, act) ->
    sub.id().equals("owner");

Pred3<Subject, Target, Action> isVipReadOnly = (sub, tgt, act) ->
    sub.vip() && "READ".equals(act.verb());

// Composite access rule: owner OR (vip AND read)
Pred3<Subject, Target, Action> accessPolicy = isDirectOwner.or(isVipReadOnly);

// Disallow classified access unless override active
Pred3<Subject, Target, Action> restrictedPolicy = accessPolicy.and(
    () -> !SecurityFlags.isLockdownActive()
);
```

---

## Summary Comparison

| Feature | `Pred<A>` | `Pred2<A1, A2>` | `Pred3<A1, A2, A3>` |
| :--- | :--- | :--- | :--- |
| **Arity** | 1 ($A \to \mathbb{B}$) | 2 ($A_1 \times A_2 \to \mathbb{B}$) | 3 ($A_1 \times A_2 \times A_3 \to \mathbb{B}$) |
| **JDK Base** | `Predicate<A>` | `BiPredicate<A1, A2>` | None (Pure Foomp Interface) |
| **Logic Gates** | `and`, `or`, `xor`, `nand`, `nor`, `negate` | `and`, `or`, `xor`, `nand`, `nor`, `negate` | `and`, `or`, `xor`, `nand`, `nor`, `negate` |
| **Partial Reduction** | N/A | `partial1`, `partial2` $\to$ `Pred` | `partial1`, `partial2`, `partial3` $\to$ `Pred2` |
| **Memoization** | Direct `.memoise()` | Via currying/partial evaluation | Via progressive partial evaluation |
