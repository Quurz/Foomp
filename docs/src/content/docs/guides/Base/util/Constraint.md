---
title: Constraint Guide
description: Practical developer guide for composable domain validation and error reporting using Constraint in Foomp.
---

`Constraint<A, FAILURE>` pairs a validation predicate with failure mapping, producing a [`Maybe<FAILURE>`](/reference/base/util/maybe/) that signals whether a value is valid (`Maybe.none()`) or violates a business rule (`Maybe.some(failure)`).

---

## When to Use `Constraint<A, FAILURE>`

* **Domain Model & Input Validation:** Validating request payloads, user input, entity invariants, or configuration values.
* **Declarative Rules:** Defining reusable validation rules as standalone first-class objects or functions.
* **Composable Error Reporting:** Collecting rich failure objects (such as enums, error codes, localized messages, or validation reports) instead of relying on thrown runtime exceptions.

---

## Examples

### 1. Basic Validation with Constant Failures

```java title="ConstraintBasicExample.java"
import org.quurz.foomp.base.types.Maybe;
import org.quurz.foomp.base.util.Constraint;

import static org.quurz.foomp.base.util.Constraint.constraint;

public class ConstraintBasicExample {

    enum ValidationError {
        USERNAME_TOO_SHORT,
        USERNAME_CONTAINS_SPACES
    }

    public static void main(String[] args) {
        Constraint<String, ValidationError> minLength =
            constraint(name -> name != null && name.length() >= 3, ValidationError.USERNAME_TOO_SHORT);

        Constraint<String, ValidationError> noSpaces =
            constraint(name -> name != null && !name.contains(" "), ValidationError.USERNAME_CONTAINS_SPACES);

        // Valid username
        System.out.println("Valid user: " + minLength.isValid("alex")); // true

        // Invalid usernames
        Maybe<ValidationError> error1 = minLength.checkViolation("al");
        System.out.println("Error 1: " + error1.get()); // USERNAME_TOO_SHORT

        Maybe<ValidationError> error2 = noSpaces.apply("alex schell");
        System.out.println("Error 2: " + error2.get()); // USERNAME_CONTAINS_SPACES
    }
}
```

---

### 2. Dynamic Failure Production

You can generate detailed failure messages containing the offending value:

```java title="ConstraintDynamicExample.java"
import org.quurz.foomp.base.types.Maybe;
import org.quurz.foomp.base.util.Constraint;

import static org.quurz.foomp.base.util.Constraint.constraint;

public class ConstraintDynamicExample {
    public static void main(String[] args) {
        Constraint<Integer, String> validPort = constraint(
            port -> port != null && port >= 1024 && port <= 65535,
            invalidPort -> "Port number " + invalidPort + " is outside the allowed non-privileged range (1024-65535)."
        );

        Maybe<String> failure = validPort.checkViolation(80);
        if (failure.isPresent()) {
            System.err.println("Validation failed: " + failure.get());
        }
    }
}
```

---

### 3. Validating Collections and Pipelines

Because `Constraint` implements `Function<A, Maybe<FAILURE>>`, it integrates cleanly with Java Streams:

```java title="ConstraintPipelineExample.java"
import org.quurz.foomp.base.types.Maybe;
import org.quurz.foomp.base.util.Constraint;

import java.util.List;

import static org.quurz.foomp.base.util.Constraint.constraint;

public class ConstraintPipelineExample {
    public static void main(String[] args) {
        Constraint<String, String> nonBlank =
            constraint(s -> s != null && !s.isBlank(), "Value must not be empty or blank");

        List<String> inputs = List.of("hello", "", "world", "   ");

        List<String> errors = inputs.stream()
            .map(nonBlank)
            .filter(Maybe::isPresent)
            .map(Maybe::get)
            .toList();

        System.out.println("Collected Errors: " + errors);
        // Output: Collected Errors: [Value must not be empty or blank, Value must not be empty or blank]
    }
}
```

---

## Best Practices

:::tip[Validation Patterns]
* **Null Safety:** Check for null in your predicates if the validated type may receive nullable inputs (`isValid(@Nullable A value)` allows null).
* **Return Typed Errors:** Prefer structured failure types (e.g. `enum`s or record classes) over raw strings for `FAILURE` when building multi-layer or API validations.
:::
