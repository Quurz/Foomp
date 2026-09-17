---
title: Mergeable<SELF>
description: Formal API reference and contract for algebraic merging and state combination in Foomp.
---

import { Aside, Tabs, TabItem } from '@astrojs/starlight/components';

`Mergeable<SELF extends Mergeable<?>>` defines an algebraic contract for types that can be combined or concatenated with another instance of the same type.

Merging typically produces a new instance combining the contents or state of both source objects according to domain-specific resolution rules (such as concatenation, union, or balanced tree merging).

```java
package org.quurz.foomp.base.types;

public interface Mergeable<SELF extends Mergeable<?>> {
    @NonNull SELF merge(final @NonNull SELF other);
}
```

---

## Type Parameter

| Parameter | Bound | Description |
| :--- | :--- | :--- |
| `<SELF>` | `extends Mergeable<?>` | The concrete self-type enabling strongly typed fluent merging without unchecked casting. |

---

## Method Specification

### `merge(final @NonNull SELF other)`

Merges this object with the specified `other` object.

```java
@NonNull SELF merge(final @NonNull SELF other)
```

* **Parameters:**
  * `other` (`@NonNull SELF`): The secondary instance to merge into this instance. Must not be `null`.
* **Returns:**
  * `@NonNull SELF`: A new instance representing the combined state.
* **Throws:**
  * `NullPointerException` if `other` is `null`.

#### Contract & Semantics

1. **Immutability & Purity:**
   * In persistent and immutable structures, `merge` must **never** mutate either of the source instances. It must produce and return a new instance.
2. **Associativity (Semigroup Property):**
   * While not strictly enforced by the Java type system, implementations generally adhere to associativity where applicable:
     $$\forall a, b, c: (a.\text{merge}(b)).\text{merge}(c) \equiv a.\text{merge}(b.\text{merge}(c))$$
3. **Null-Safety:**
   * Passing `null` as the `other` operand throws a `NullPointerException` immediately. The method never returns `null`.

---

## Key Implementations in Foomp

| Type | Merging Behavior |
| :--- | :--- |
| [`BinaryTree<A>`](/reference/base/types/binarytree/) | Combines two binary search trees (`AVLTree`, `RedBlackTree`), rebalancing all nodes into a unified tree. |
| [`Dictionary<K, V>`](/reference/base/util/dictionary/) | Combines two key-value mappings. If key collisions occur, entries from `other` overwrite entries in `this`. |
| [`Seq<A>`](/reference/base/types/seq/) | Concatenates two ordered sequences or persistent collections into a single sequence. |

---

## Related Types

* [`BinaryTree<A>`](/reference/base/types/binarytree/) – Binary search tree contract extending `Mergeable<BinaryTree<A>>`.
* [`Seq<A>`](/reference/base/types/seq/) – Ordered sequence contract extending `Mergeable<Seq<A>>`.
* [`FoldableLeft<A>`](/reference/base/types/foldableleft/) – Left-associative fold that can combine collections of `Mergeable` items.
