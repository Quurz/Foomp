---
title: Maybe Guide
description: Practical developer guide for safe optionality, lazy transformations, and monadic chains with Maybe in Foomp.
---

`Maybe<A>` models the presence (`Some`) or absence (`None`) of a value. It provides a type-safe and expressive alternative to `null` checks and standard Java `Optional`.

---

## When to Use `Maybe<A>`

* **Null Avoidance:** Model fields, return types, or dictionary lookups that might not yield a value.
* **Lazy Computation Chains:** `map` operations in `Maybe` are lazy, deferring value evaluation until accessed.
* **Functional Composition:** Chain operations using `flatMap`, `filter`, and `zip`.

---

## Examples

### 1. Creating and Unwrapping `Maybe`

```java title="MaybeBasicsExample.java"
import org.quurz.foomp.base.util.Maybe;

import static org.quurz.foomp.base.util.Maybe.*;

public class MaybeBasicsExample {
    public static void main(String[] args) {
        Maybe<String> present = some("Hello Foomp");
        Maybe<String> absent = none();

        // Safe extraction
        String greeting = present.getOrElse(() -> "Default Greeting");
        System.out.println(greeting); // Hello Foomp

        String fallback = absent.getOrElse(() -> "Default Greeting");
        System.out.println(fallback); // Default Greeting
    }
}
```

---

### 2. Functional Pipelines with `map`, `filter`, and `flatMap`

```java title="MaybePipelineExample.java"
import org.quurz.foomp.base.util.Maybe;

import static org.quurz.foomp.base.util.Maybe.*;

public class MaybePipelineExample {

    record User(String name, Integer age) {}

    static Maybe<User> findUser(int id) {
        return id == 1 ? some(new User("Alice", 28)) : none();
    }

    public static void main(String[] args) {
        Maybe<String> adultUserName = findUser(1)
            .filter(user -> user.age() >= 18)
            .map(User::name)
            .map(String::toUpperCase);

        adultUserName.ifSome(name -> System.out.println("Found adult user: " + name));
    }
}
```

---

### 3. Combining Maybes with `zip`

```java title="MaybeZipExample.java"
import org.quurz.foomp.base.util.Maybe;
import org.quurz.foomp.base.util.Tuple2;

import static org.quurz.foomp.base.util.Maybe.*;

public class MaybeZipExample {
    public static void main(String[] args) {
        Maybe<String> firstName = some("John");
        Maybe<String> lastName = some("Doe");

        Maybe<Tuple2<String, String>> fullName = firstName.zip(lastName);

        fullName.ifSome(t -> System.out.println("Full Name: " + t.first() + " " + t.second()));
        // Output: Full Name: John Doe
    }
}
```

---

## Best Practices

:::tip[Maybe Best Practices]
* **Prefer `Maybe` over `null`:** Avoid returning `null` from methods; return `Maybe<A>` to communicate optionality via the type system.
* **Use `maybeOfNullable` for Interop:** When working with external libraries returning nullable objects, immediately wrap them with `Maybe.maybeOfNullable(raw)`.
:::
