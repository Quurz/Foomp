---
title: Managing Key-Value Mappings with Dict
description: Practical developer guide, safe lookups, immutable mutations, converting to standard Java Maps, and dictionary patterns with Dict in Foomp.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

`Dict<K, V>` is Foomp's functional interface for key–value mappings and associative stores. It offers fluent manipulation, safe lookups via `getSafe()` that prevent `NoSuchElementException`, and seamless conversion to standard `java.util.Map` types.

---

## 1. Creating and Updating Dictionaries

Concrete dictionaries like [`Dictionary<K, V>`](/reference/base/util/dictionary/) are persistent and immutable. Calling `put()` or `remove()` returns a new dictionary without modifying the original instance.

```java title="DictionaryBasics.java"
import org.quurz.foomp.base.types.Dict;
import org.quurz.foomp.base.util.Dictionary;
import static org.quurz.foomp.base.util.Dictionary.dictionary;

public class DictionaryBasics {
    public static void main(String[] args) {
        // Create an empty dictionary and chain insertions fluently
        Dict<String, Integer> scores = dictionary();
        Dict<String, Integer> updated = scores
            .put("Alice", 100)
            .put("Bob", 85)
            .put("Charlie", 92);

        System.out.println("Contains Bob? " + updated.contains("Bob")); // true
        System.out.println("Original empty size remains empty: " + scores.contains("Alice")); // false
    }
}
```

---

## 2. Direct vs. Safe Lookups

<Tabs>
  <TabItem label="Safe Lookup with getSafe() (Recommended)">
    ```java title="SafeLookup.java"
    import org.quurz.foomp.base.types.Dict;
    import org.quurz.foomp.base.types.Value;
    import org.quurz.foomp.base.util.Dictionary;
    import static org.quurz.foomp.base.util.Dictionary.dictionary;

    public class SafeLookup {
        public static void main(String[] args) {
            Dict<String, String> env = dictionary()
                .put("HOST", "localhost")
                .put("PORT", "8080");

            // Returns Value<String>
            Value<String> host = env.getSafe("HOST");
            Value<String> timeout = env.getSafe("TIMEOUT");

            System.out.println(host.isPresent() ? host.get() : "127.0.0.1"); // localhost
            System.out.println(timeout.isPresent() ? timeout.get() : "30s");     // 30s
        }
    }
    ```
  </TabItem>
  <TabItem label="Direct Lookup with get()">
    ```java title="DirectLookup.java"
    import org.quurz.foomp.base.types.Dict;
    import org.quurz.foomp.base.util.Dictionary;
    import static org.quurz.foomp.base.util.Dictionary.dictionary;
    import java.util.NoSuchElementException;

    public class DirectLookup {
        public static void main(String[] args) {
            Dict<String, String> env = dictionary().put("HOST", "localhost");

            String host = env.get("HOST"); // "localhost"

            try {
                env.get("UNKNOWN");
            } catch (NoSuchElementException e) {
                System.out.println("Key was missing!");
            }
        }
    }
    ```
  </TabItem>
</Tabs>

---

## 3. Exporting to Standard Java Maps

You can convert any `Dict` into a standard mutable Java `Map` (such as `HashMap`, `LinkedHashMap`, or `TreeMap`) using `toMap()` with a constructor reference:

```java title="ExportToMap.java"
import org.quurz.foomp.base.types.Dict;
import org.quurz.foomp.base.util.Dictionary;
import static org.quurz.foomp.base.util.Dictionary.dictionary;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ExportToMap {
    public static void main(String[] args) {
        Dict<String, Integer> dict = dictionary()
            .put("c", 3)
            .put("a", 1)
            .put("b", 2);

        // Convert to a standard HashMap
        Map<String, Integer> hashMap = dict.toMap(HashMap::new);
        System.out.println("HashMap: " + hashMap);

        // Convert to a sorted TreeMap
        Map<String, Integer> treeMap = dict.toMap(TreeMap::new);
        System.out.println("TreeMap: " + treeMap); // Sorted by key: {a=1, b=2, c=3}
    }
}
```

---

## See Also

* [`Dict` Reference](/reference/base/types/dict/) – Complete interface specification.
* [`Dictionary` Reference](/reference/base/util/dictionary/) – Concrete persistent dictionary implementation.
* [`Value` Reference](/reference/base/types/value/) – Optional value container for safe unwrapping.
