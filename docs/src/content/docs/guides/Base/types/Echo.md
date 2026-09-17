---
title: Echo Guide
description: Practical guide and idioms for using and implementing the Echo interface in Foomp.
---

The `Echo` interface provides a dedicated, functional approach to object serialization, formatting, and tree/diagram rendering without polluting or overloading `Object.toString()`.

---

## Why Use `Echo` Instead of `toString()`?

In standard Java development, `toString()` is frequently used for two conflicting purposes:
1. **Developer diagnostics**: Inspecting object states in debuggers or error log outputs.
2. **Domain/Visual representation**: Formatting data for CLI output, reports, SemVer strings, or ASCII visualization.

By implementing `Echo`, your domain models and data structures provide an explicit formatting contract:

```java
public record Money(long cents, String currency) implements Echo {
    @Override
    public String echo() {
        return String.format("%s %.2f", currency, cents / 100.0);
    }
}
```

```java
Money price = new Money(4990, "EUR");

System.out.println(price.echo()); // EUR 49.90
```

---

## Common Use Cases in Foomp

### 1. Canonical Representation with `SemVer`

Foomp's `SemVer` implements `Echo` to format semantic versions:

```java
import org.quurz.foomp.base.misc.SemVer;

SemVer version = SemVer.parseSemVer("2.1.0-rc.1+sha.9b84a").get();

// Outputs standard SemVer compliant string:
System.out.println(version.echo()); // "2.1.0-rc.1+sha.9b84a"
```

### 2. ASCII Tree Visualization with `AVLTree` & `RedBlackTree`

Tree structures implement `Echo` to render visual branch diagrams:

```java
import org.quurz.foomp.base.util.AVLTree;

AVLTree<Integer> tree = AVLTree.of(10, 5, 15, 3, 7, 12, 18);

// Print tree diagram directly
System.out.println(tree.echo());
```

Output:
```text
10
├── 5
│   ├── 3
│   └── 7
└── 15
    ├── 12
    └── 18
```

---

## Functional Usage & Method References

Because `Echo` is a single-abstract-method (SAM) `@FunctionalInterface`, you can use it in functional pipelines, logging helpers, and stream mappings:

```java
import org.quurz.foomp.base.types.Echo;
import java.util.List;

List<Echo> items = List.of(
    SemVer.of(1, 0, 0),
    new Money(1500, "USD")
);

// Map and print all representations cleanly
items.stream()
    .map(Echo::echo)
    .forEach(System.out::println);
```
