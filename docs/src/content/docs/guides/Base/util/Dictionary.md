---
title: Dictionary Guide
description: Practical developer guide for persistent, immutable, and lazily evaluated Dictionaries in Foomp.
---

`Dictionary<K, V>` is a functional, immutable key-value map. Every modifying operation, such as `put` or `remove`, produces a new instance while keeping previous instances unmodified (persistent data structure).

Organized internally around a balanced [`RedBlackTree`](/reference/base/util/redblacktree/) indexed by hash codes, `Dictionary` provides logarithmic lookup, insertion, and deletion. Mapped values are managed **lazily** through `Supplier` thunks by default.

---

## When to Use `Dictionary<K, V>`

* **Immutable Configurations & Domain State:** Thread-safe sharing of maps without defensive copies.
* **Functional Transformations & Pipelines:** Deferring expensive value computations with `.map(...)` and `.applyTo(...)` until elements are actually consumed.
* **Safe Element Retrieval:** Preferring `getSafe(key)` returning [`Maybe`](/reference/base/util/maybe/) over error-prone `null` returns.

---

## Examples

### 1. Creation and Basic Operations

```java title="DictionaryBasicExample.java"
import org.quurz.foomp.base.types.Maybe;
import org.quurz.foomp.base.util.Dictionary;
import org.quurz.foomp.base.util.Tuple2;

import static org.quurz.foomp.base.util.Dictionary.dictionary;
import static org.quurz.foomp.base.util.Dictionary.dictionaryOf;
import static org.quurz.foomp.base.util.Tuple2.tuple;

public class DictionaryBasicExample {
    public static void main(String[] args) {
        // Create with initial tuples
        Dictionary<String, Integer> scores = dictionaryOf(
            tuple("Alice", 100),
            tuple("Bob", 85)
        );

        // Persistent insertion (scores remains unmodified)
        Dictionary<String, Integer> updatedScores =
            (Dictionary<String, Integer>) scores.put("Charlie", 95);

        // Safe value retrieval with Maybe
        Maybe<Integer> aliceScore = updatedScores.getSafe("Alice");
        System.out.println("Alice: " + aliceScore.getOrElse(() -> 0)); // 100

        Maybe<Integer> unknownScore = updatedScores.getSafe("David");
        System.out.println("David present? " + unknownScore.isPresent()); // false
    }
}
```

---

### 2. Lazy Mapping of Values

Transformations performed via `.map()` are not computed immediately across all entries, but are deferred lazily:

```java title="DictionaryLazyMappingExample.java"
import org.quurz.foomp.base.util.Dictionary;

import static org.quurz.foomp.base.util.Dictionary.dictionaryOf;
import static org.quurz.foomp.base.util.Tuple2.tuple;

public class DictionaryLazyMappingExample {
    public static void main(String[] args) {
        Dictionary<String, String> userNames = dictionaryOf(
            tuple("u1", "alex"),
            tuple("u2", "junie")
        );

        // map() is lazy: toUpperCase() has NOT been executed yet
        Dictionary<String, String> upperNames = userNames.map(name -> {
            System.out.println("Transforming: " + name);
            return name.toUpperCase();
        });

        System.out.println("Dictionary created, now accessing 'u1'...");
        // The transformation for 'u1' is executed here on demand:
        String u1 = upperNames.get("u1");
        System.out.println("Result u1: " + u1); // ALEX
    }
}
```

---

### 3. Merging Two Dictionaries (`merge`)

You can combine two dictionaries with `merge`. In the case of key collisions, entries from the parameter dictionary overwrite existing entries:

```java title="DictionaryMergeExample.java"
import org.quurz.foomp.base.util.Dictionary;

import java.util.HashMap;

import static org.quurz.foomp.base.util.Dictionary.dictionaryOf;
import static org.quurz.foomp.base.util.Tuple2.tuple;

public class DictionaryMergeExample {
    public static void main(String[] args) {
        Dictionary<String, String> defaultSettings = dictionaryOf(
            tuple("theme", "light"),
            tuple("fontSize", "14px"),
            tuple("autoSave", "true")
        );

        Dictionary<String, String> userSettings = dictionaryOf(
            tuple("theme", "dark"),
            tuple("fontSize", "16px")
        );

        // Merge: userSettings overrides defaultSettings
        Dictionary<String, String> effectiveSettings = defaultSettings.merge(userSettings);

        // Export to Java Map (unwinds all lazy thunks)
        var map = effectiveSettings.toMap(HashMap::new);
        System.out.println("Effective settings: " + map);
        // {theme=dark, fontSize=16px, autoSave=true}
    }
}
```

---

### 4. Applicative Transformation with `applyTo`

When you have a dictionary of transformation functions, `applyTo` applies them to matching keys (key-set intersection):

```java title="DictionaryApplyExample.java"
import org.quurz.foomp.base.util.Dictionary;

import java.util.HashMap;
import java.util.function.Function;

import static org.quurz.foomp.base.util.Dictionary.dictionaryOf;
import static org.quurz.foomp.base.util.Tuple2.tuple;

public class DictionaryApplyExample {
    public static void main(String[] args) {
        Dictionary<String, Integer> baseValues = dictionaryOf(
            tuple("a", 10),
            tuple("b", 20),
            tuple("c", 30)
        );

        Dictionary<String, Function<Integer, String>> formatters = dictionaryOf(
            tuple("a", x -> "Value A: " + (x * 2)),
            tuple("b", x -> "Value B: " + (x + 5))
        );

        Dictionary<String, String> formatted = baseValues.applyTo(formatters);

        System.out.println(formatted.toMap(HashMap::new));
        // {a=Value A: 20, b=Value B: 25} (Key "c" is omitted because no corresponding function was provided)
    }
}
```

---

## Best Practices

:::tip[Dictionary Best Practices]
* **Key Stability:** As with all hash-based structures, keys must be immutable and provide consistent `equals` and `hashCode` implementations.
* **True Immutability:** Methods like `put`, `remove`, and `merge` never modify the target instance; they always return a fresh `Dictionary` instance.
* **Lazy Evaluation Semantics:** Remember that exceptions thrown inside `.map()` closures are deferred until elements are unwound via `get()`, `getSafe()`, or `toMap()`.
:::
