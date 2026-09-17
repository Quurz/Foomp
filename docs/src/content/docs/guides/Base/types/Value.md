---
title: Value Guide
description: Practical developer guide for working with the Value single-value carrier interface in Foomp.
---

The `Value<A>` interface is the unified foundation in Foomp for containers holding a single value. It combines Java’s `Supplier<A>` contract with explicit presence checking (`isPresent()`).

---

## When to Use `Value<A>`

* **Abstracting Containers:** Writing generic APIs that can accept any single-value carrier (such as `Box<A>`, `Maybe<A>`, `Eval<A>`, or `Provider<A>`) without coupling to a specific container implementation.
* **Java Interoperability:** Passing Foomp containers directly into standard JDK methods expecting `Supplier<A>`.
* **Safe Extraction:** Guarding against missing values when dealing with potentially empty containers.

---

## Examples

### 1. Unified Value Extraction

```java title="ValueExtractionExample.java"
import org.quurz.foomp.base.types.Value;
import org.quurz.foomp.base.util.Box;
import org.quurz.foomp.base.util.Maybe;

public class ValueExtractionExample {

    public static <T> void printValueSummary(Value<T> valueCarrier) {
        if (valueCarrier.isPresent()) {
            System.out.println("Present value: " + valueCarrier.get());
        } else {
            System.out.println("Value is absent.");
        }
    }

    public static void main(String[] args) {
        Value<String> box = Box.box(() -> "Hello Foomp");
        Value<String> presentMaybe = Maybe.just("Found");
        Value<String> absentMaybe = Maybe.nothing();

        printValueSummary(box);          // Present value: Hello Foomp
        printValueSummary(presentMaybe); // Present value: Found
        printValueSummary(absentMaybe);  // Value is absent.
    }
}
```

---

### 2. JDK Interoperability as `Supplier<A>`

Because `Value<A>` extends `Supplier<A>`, it works natively with `CompletableFuture`, `Optional`, and logging APIs:

```java title="SupplierInteroperabilityExample.java"
import org.quurz.foomp.base.types.Value;
import org.quurz.foomp.base.util.Box;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class SupplierInteroperabilityExample {
    public static void main(String[] args) {
        Value<String> lazyGreeting = Box.box(() -> "Computed in background");

        // Pass directly to CompletableFuture.supplyAsync
        CompletableFuture<String> future = CompletableFuture.supplyAsync(lazyGreeting);
        future.thenAccept(System.out::println);

        // Fallback in Optional.orElseGet
        Optional<String> emptyOptional = Optional.empty();
        String result = emptyOptional.orElseGet(lazyGreeting);
        System.out.println("Fallback result: " + result);
    }
}
```

---

## Best Practices

:::tip[Best Practices for Value]
* **Check `isPresent()` When Working with Generic Values:** Unless the concrete type is known to be total (e.g. `Box`), check `isPresent()` before invoking `get()` to prevent `NoSuchElementException`.
* **Prefer Functional Combinators on Concrete Monads:** When working directly with `Maybe` or `Either`, prefer functional methods (`map`, `flatMap`, `match`) over manual `if (value.isPresent())` checks.
:::
