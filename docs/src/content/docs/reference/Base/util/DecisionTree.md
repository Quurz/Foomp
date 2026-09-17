---
title: DecisionTree<F, R>
description: API reference for DecisionTree, a functional decision tree for rule-based evaluation in Foomp.
---

`DecisionTree<F, R>` is a functional decision tree designed to evaluate facts (type `F`) and produce outcomes (type `R`).

The tree is structured into branching decision nodes (`Node`) that evaluate a `Predicate<? super F>` to choose a branch, and leaf nodes (`TransformingLeaf`, `TransformingAndConsumingLeaf`, `ConsumingAndTransformingLeaf`) that compute the final outcome and optionally execute side effects.

---

## Type Signature

```java
package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public sealed interface DecisionTree<F, R>
        permits DecisionTree.Node,
                DecisionTree.TransformingLeaf,
                DecisionTree.TransformingAndConsumingLeaf,
                DecisionTree.ConsumingAndTransformingLeaf
```

### Type Parameters
* `F`: The type of input facts evaluated by the decision tree.
* `R`: The type of results produced by the leaves.

---

## Static Factory Methods

### Branching Nodes

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <F, R> DecisionTree<F, R>` | `decisionTree(@NonNull Predicate<? super F> predicate, @NonNull DecisionTree<F, ? extends R> yesTree, @NonNull DecisionTree<F, ? extends R> noTree)` | Creates a branching decision node. If the predicate holds for the fact, recurses into `yesTree`, otherwise into `noTree`. |

### Leaf Nodes

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <F, R> DecisionTree<F, R>` | `decisionLeaf(@NonNull Function<? super F, ? extends R> transformer)` | Creates a pure transforming leaf that calculates the result via `transformer.apply(fact)`. |
| `static <F, R> DecisionTree<F, R>` | `decisionLeaf(@NonNull Function<? super F, ? extends R> transformer, @NonNull Consumer<? super R> consumer)` | Creates a leaf that first computes the result and then executes a side-effect consumer on the result (`transformer -> consumer -> return result`). |
| `static <F, R> DecisionTree<F, R>` | `decisionLeaf(@NonNull Consumer<? super F> consumer, @NonNull Function<? super F, ? extends R> transformer)` | Creates a leaf that first executes a side-effect consumer on the input fact and then computes the result (`consumer -> transformer -> return result`). |

---

## Method Summary

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `R` | `examine(@NonNull F fact)` | Evaluates the decision tree for the given `fact`. Traverses decision nodes according to predicates and computes the outcome at the target leaf. Throws `NullPointerException` if `fact` or the computed result is `null`. |

---

## See Also

* [`Predicate`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/Predicate.html) – Condition tester for branching nodes.
* [`Function`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/Function.html) – Transformation of facts to results at leaf nodes.
* [`Consumer`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/Consumer.html) – Side-effect execution at leaf nodes.
