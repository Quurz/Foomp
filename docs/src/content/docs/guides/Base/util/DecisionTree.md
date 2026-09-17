---
title: DecisionTree Guide
description: Practical developer guide for rule-based flow control and decision trees using DecisionTree in Foomp.
---

`DecisionTree<F, R>` models deterministic, hierarchical decision trees as functional data structures. An incoming fact of type `F` is evaluated through conditional branching predicates until it reaches a leaf node that computes an outcome of type `R` and optionally performs side effects.

---

## When to Use `DecisionTree<F, R>`

* **Rule-Based Business Logic:** Evaluating credit checks, discount calculations, pricing tiers, or routing decisions.
* **Classification & Categorization:** Assigning users, incoming requests, or domain events to specific categories.
* **Decoupling Evaluation from Action:** Clean, maintainable execution paths without deeply nested `if-else` cascades.

---

## Examples

### 1. Simple Number Classification

```java title="SimpleDecisionTreeExample.java"
import org.quurz.foomp.base.util.DecisionTree;

import static org.quurz.foomp.base.util.DecisionTree.decisionLeaf;
import static org.quurz.foomp.base.util.DecisionTree.decisionTree;

public class SimpleDecisionTreeExample {
    public static void main(String[] args) {
        DecisionTree<Integer, String> numberClassifier =
            decisionTree(
                n -> n < 0,
                decisionLeaf(n -> "Negative: " + n),
                decisionTree(
                    n -> n == 0,
                    decisionLeaf(n -> "Zero"),
                    decisionLeaf(n -> "Positive: " + n)
                )
            );

        System.out.println(numberClassifier.examine(-42)); // Negative: -42
        System.out.println(numberClassifier.examine(0));   // Zero
        System.out.println(numberClassifier.examine(99));  // Positive: 99
    }
}
```

---

### 2. Complex Business Rules (Customer Discount & Eligibility)

Consider an order discounting scenario with a domain record `CustomerOrder`:

```java title="DiscountRulesExample.java"
import org.quurz.foomp.base.util.DecisionTree;

import static org.quurz.foomp.base.util.DecisionTree.decisionLeaf;
import static org.quurz.foomp.base.util.DecisionTree.decisionTree;

public class DiscountRulesExample {

    public record CustomerOrder(boolean isPremiumMember, double orderTotal, int pastOrdersCount) {}

    public static void main(String[] args) {
        DecisionTree<CustomerOrder, Double> discountCalculator =
            decisionTree(
                CustomerOrder::isPremiumMember,
                // Premium members
                decisionTree(
                    order -> order.orderTotal() >= 100.0,
                    decisionLeaf(order -> 0.20), // 20% discount for orders >= 100 EUR
                    decisionLeaf(order -> 0.10)  // 10% base discount for premium
                ),
                // Standard customers
                decisionTree(
                    order -> order.pastOrdersCount() >= 10,
                    decisionLeaf(order -> 0.05), // 5% loyalty discount
                    decisionLeaf(order -> 0.0)   // No discount
                )
            );

        CustomerOrder vip = new CustomerOrder(true, 150.0, 5);
        CustomerOrder loyal = new CustomerOrder(false, 80.0, 12);
        CustomerOrder newbie = new CustomerOrder(false, 30.0, 1);

        System.out.println("VIP Discount: " + (discountCalculator.examine(vip) * 100) + "%");       // 20.0%
        System.out.println("Loyalty Discount: " + (discountCalculator.examine(loyal) * 100) + "%");   // 5.0%
        System.out.println("Newbie Discount: " + (discountCalculator.examine(newbie) * 100) + "%"); // 0.0%
    }
}
```

---

### 3. Leaf Nodes with Logging and Side Effects

`DecisionTree` provides two specialized leaf factories with integrated side effects (`Consumer`):
* `decisionLeaf(transformer, consumer)`: Computes the outcome first and passes the **result** to the consumer (e.g., logging computed results).
* `decisionLeaf(consumer, transformer)`: Executes the consumer on the input **fact** first, then computes the result (e.g., audit logging of inputs).

```java title="SideEffectsLeafExample.java"
import org.quurz.foomp.base.util.DecisionTree;

import static org.quurz.foomp.base.util.DecisionTree.decisionLeaf;
import static org.quurz.foomp.base.util.DecisionTree.decisionTree;

public class SideEffectsLeafExample {
    public static void main(String[] args) {
        DecisionTree<String, Integer> tree =
            decisionTree(
                s -> s.startsWith("VIP_"),
                // Logging before computation (consumer receives the input String fact)
                decisionLeaf(
                    fact -> System.out.println("[AUDIT] Processing VIP fact: " + fact),
                    String::length
                ),
                // Logging after computation (consumer receives the computed Integer result)
                decisionLeaf(
                    String::length,
                    result -> System.out.println("[LOG] Calculated standard length: " + result)
                )
            );

        int vipLen = tree.examine("VIP_ALEX");
        int regularLen = tree.examine("HELLO");
    }
}
```

---

## Best Practices

:::tip[Structuring Decision Trees]
* **Modularity & Reusability:** Subtrees (`DecisionTree<F, R>`) can be defined as independent constants or factory methods and composed into larger decision trees.
* **Null Safety:** Ensure all predicates and transformers are robust against unexpected null values if your fact data allows them. Calling `examine(null)` throws a `NullPointerException`.
:::
