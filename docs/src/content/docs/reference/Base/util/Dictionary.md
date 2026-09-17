---
title: Dictionary<K, V>
description: API reference for Dictionary, a persistent, immutable, and lazily evaluated key-value map in Foomp.
---

`Dictionary<K, V>` is an immutable, persistent, and lazily evaluated associative key-value map in Foomp.

Internally, it is backed by a balanced [`RedBlackTree`](/reference/base/util/redblacktree/) indexed by key hash codes (`Objects.hashCode(key)`). Hash collisions are resolved deterministically using immutable collision chains (`Entry`) within the tree nodes.

Values are managed through `Supplier` thunks (spools), ensuring that transformations (such as `map` or `applyTo`) are deferred lazily until explicitly unwound via `get`, `getSafe`, or `toMap`.

---

## Type Signature

```java
package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Appliable;
import org.quurz.foomp.base.types.Dict;
import org.quurz.foomp.base.types.Mappable;
import org.quurz.foomp.base.types.Mergeable;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.Higher2;
import org.quurz.foomp.higher.WitnessType;

public class Dictionary<K, V>
        implements Mergeable<Dictionary<K, V>>,
                   Mappable<Dictionary.µ, V>,
                   Appliable<Dictionary.µ, V>,
                   Dict<K, V>,
                   Higher2<Dictionary.µ, K, V>,
                   Higher1<Dictionary.µ, V>
```

### Type Parameters
* `K`: The type of keys.
* `V`: The type of mapped values.

---

## Higher-Kinded Witness Type & Narrowing

```java
public static final class µ implements WitnessType { private µ() {} }
```

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <K, V> Dictionary<K, V>` | `narrow(@NonNull Higher1<? extends µ, V> wide)` | Safely downcasts a `Higher1` container to `Dictionary<K, V>`. |
| `static <K, V> Dictionary<K, V>` | `narrow(@NonNull Higher2<? extends µ, K, V> wide)` | Safely downcasts a `Higher2` container to `Dictionary<K, V>`. |

---

## Static Factory Methods

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <K, V> Dictionary<K, V>` | `dictionary()` | Returns an empty singleton instance of `Dictionary`. |
| `static <K, V> Dictionary<K, V>` | `dictionaryOf(@NonNull Tuple2<K, V>... entries)` | Creates a `Dictionary` populated with the given key-value tuples. |
| `static <K, V> Dictionary<K, V>` | `dictionaryFrom(@NonNull Map<K, V> source)` | Creates a `Dictionary` from an existing Java `java.util.Map`. |

---

## Method Summary

### Element Access & Modification

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull Dict<K, V>` | `put(@NonNull K key, @NonNull V value)` | Adds or updates a key-value mapping. Returns a new persistent `Dictionary`. |
| `boolean` | `contains(@NonNull K key)` | Checks whether the specified key is present in the dictionary. |
| `@NonNull V` | `get(@NonNull K key)` | Retrieves the value associated with `key`. Throws `NoSuchElementException` if absent. Evaluates lazy thunks (`@UnwindingOperation`). |
| `@NonNull Maybe<V>` | `getSafe(@NonNull K key)` | Returns `Maybe.some(value)` or `Maybe.none()`. Evaluates lazy thunks (`@UnwindingOperation`). |
| `@NonNull Dict<K, V>` | `remove(@NonNull K key)` | Removes the specified key. Throws `NoSuchElementException` if the key does not exist. Returns a new persistent `Dictionary`. |

### Functional Transformations & Combination

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `@NonNull <W> Dictionary<K, W>` | `map(@NonNull Function<? super V, ? extends W> transformation)` | Lazily transforms all values. Evaluation occurs on demand when elements are accessed. |
| `@NonNull <W> Dictionary<K, W>` | `applyTo(@NonNull Dictionary<K, ? extends Function<? super V, ? extends W>> transformation)` | Applies functions from another dictionary to values with matching keys (key-set intersection). |
| `@NonNull Dictionary<K, V>` | `merge(@NonNull Dictionary<K, V> other)` | Merges this dictionary with another. In case of key conflicts, values from `other` take precedence. |
| `@NonNull <M extends Map<K, V>> M` | `toMap(@NonNull Supplier<M> init)` | Collects all entries into a new Java `Map` (e.g. `HashMap::new`), unwinding all lazy value thunks (`@UnwindingOperation`). |

---

## See Also

* [`RedBlackTree<A>`](/reference/base/util/redblacktree/) – Underlying balanced tree structure.
* [`Tuple2<A, B>`](/reference/base/util/tuple2/) – Key-value pairs used by `dictionaryOf`.
* [`Maybe<A>`](/reference/base/util/maybe/) – Safe return type for optional element lookup.
