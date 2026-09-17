---
title: Dict<K, V>
description: Formal API reference, dictionary mapping contract, safe lookup methods, key removal, and Map conversion for Dict in Foomp.
---

`org.quurz.foomp.base.types.Dict<K, V>`

`Dict` is the fundamental dictionary interface in Foomp representing a key–value mapping. It provides insertion, membership testing, throwing and safe lookups, key removal, and conversion to standard Java collections.

```java
public interface Dict<K, V>
```

`Dict` serves as the primary abstraction for map data structures in Foomp, notably implemented by [`Dictionary<K, V>`](/reference/base/util/dictionary/).

---

## Key Characteristics & Design

* **Non-Null Keys & Values:** Keys and values are strictly required to be non-null.
* **Dual-Access Model:** Lookups can be performed either directly via [`get(key)`](#get) (which throws `NoSuchElementException` on missing keys) or safely via [`getSafe(key)`](#getsafe) (which returns a [`Value<V>`](/reference/base/types/value/)).
* **Fluent Mutation & Evolution:** Modification methods ([`put`](#put), [`remove`](#remove)) return `Dict<K, V>` for chaining. In persistent implementations (like `Dictionary`), these methods return new evolved dictionary instances preserving immutability.
* **Interop with JDK Collections:** Method [`toMap(Supplier<M>)`](#tomap) allows exporting key–value pairs into arbitrary mutable standard Java `Map` implementations.

---

## Method Specification

### `put`

```java
@NonNull Dict<K, V> put(
    final @NonNull K key,
    final @NonNull V value
);
```

Inserts or replaces the key–value pair in this dictionary.

#### Parameters
* **`key`**: The key to associate; must not be `null`.
* **`value`**: The value to associate; must not be `null`.

#### Return Value
* Returns the `@NonNull Dict<K, V>` with the added mapping (a new instance for immutable implementations).

---

### `contains`

```java
boolean contains(final @NonNull K key);
```

Tests whether this dictionary contains an entry with the specified key.

#### Parameters
* **`key`**: The key to test; must not be `null`.

#### Return Value
* Returns `true` if the key exists; `false` otherwise.

---

### `get`

```java
@NonNull V get(final @NonNull K key)
    throws NoSuchElementException;
```

Retrieves the value associated with the specified key.

#### Parameters
* **`key`**: The key whose value is to be fetched; must not be `null`.

#### Return Value
* Returns the `@NonNull V` value associated with the key.

#### Exceptions
* `NoSuchElementException` if the key does not exist in this dictionary.

---

### `getSafe`

```java
@NonNull Value<V> getSafe(final @NonNull K key);
```

Safely retrieves the value associated with the specified key without throwing exceptions.

#### Parameters
* **`key`**: The key to look up; must not be `null`.

#### Return Value
* Returns a `@NonNull Value<V>` containing the value if present, or an empty `Value` if absent.

---

### `remove`

```java
@NonNull Dict<K, V> remove(final @NonNull K key)
    throws NoSuchElementException;
```

Removes the mapping for the given key from this dictionary.

#### Parameters
* **`key`**: The key to remove; must not be `null`.

#### Return Value
* Returns the `@NonNull Dict<K, V>` without the mapping.

#### Exceptions
* `NoSuchElementException` if the key does not exist in this dictionary.

---

### `toMap`

```java
@NonNull <M extends Map<K, V>> M toMap(final @NonNull Supplier<M> init);
```

Collects all entries of this dictionary into a supplied mutable Java `Map`.

#### Parameters
* **`init`**: A `@NonNull Supplier<M>` creating the target map (e.g. `HashMap::new`, `TreeMap::new`).

#### Return Value
* Returns the populated map instance `M`.

---

## Concrete Implementations

| Implementation | Characteristics |
| :--- | :--- |
| [`Dictionary<K, V>`](/reference/base/util/dictionary/) | Persistent, immutable dictionary backed by an internal balanced tree structure. Supports functional mapping, applicative operations, and higher-kinded types (`Higher2`, `Higher1`). |

---

## See Also

* [Guide: Managing Key-Value Mappings with Dict](/guides/base/types/dict/) – Practical patterns, safe queries, immutable updates, and map exports.
* [`Dictionary` Reference](/reference/base/util/dictionary/) – Concrete immutable dictionary documentation.
* [`Value` Reference](/reference/base/types/value/) – Safe optional value container.
