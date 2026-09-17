---
title: Either Guide
description: Practical developer guide for modeling two-track outcomes and error handling with Either in Foomp.
---

`Either<L, R>` represents a value that is either of type `L` (Left) or type `R` (Right). It serves as the functional equivalent of a disjoint union (sum type) and is standard for type-safe error handling without throwing exceptions.

---

## Right-Biased Semantics

In Foomp, `Either` is **right-biased** by default:
* **`Right`**: Typically represents the **success track**. Operations such as `map` and `flatMap` transform the inner value of a `Right`.
* **`Left`**: Typically represents the **failure track**. When a `Left` is present, `map` and `flatMap` short-circuit execution and pass the `Left` through untouched.

---

## Examples

### 1. Typed Validation and Error Handling

```java title="EitherBasicExample.java"
import org.quurz.foomp.base.util.Either;

import static org.quurz.foomp.base.util.Either.left;
import static org.quurz.foomp.base.util.Either.right;

public class EitherBasicExample {

    public enum ErrorCode {
        NEGATIVE_AMOUNT,
        INSUFFICIENT_FUNDS
    }

    public static Either<ErrorCode, Double> withdraw(double balance, double amount) {
        if (amount <= 0) {
            return left(ErrorCode.NEGATIVE_AMOUNT);
        }
        if (amount > balance) {
            return left(ErrorCode.INSUFFICIENT_FUNDS);
        }
        return right(balance - amount);
    }

    public static void main(String[] args) {
        // Successful withdrawal
        Either<ErrorCode, Double> success = withdraw(100.0, 40.0);
        System.out.println("Remaining Balance: " + success.getRightOrElse(() -> 0.0)); // 60.0

        // Failed withdrawal
        Either<ErrorCode, Double> failure = withdraw(100.0, 150.0);
        if (failure.isLeft()) {
            System.err.println("Error encountered: " + failure.getLeft()); // INSUFFICIENT_FUNDS
        }
    }
}
```

---

### 2. Monadic Chaining with `flatMap` (Railway-Oriented Programming)

With `flatMap`, multiple fallible operations can be chained sequentially. If any step returns `Left`, the pipeline short-circuits automatically:

```java title="EitherPipelineExample.java"
import org.quurz.foomp.base.util.Either;

import static org.quurz.foomp.base.util.Either.left;
import static org.quurz.foomp.base.util.Either.right;

public class EitherPipelineExample {

    record User(String id, String email) {}

    static Either<String, String> sanitizeEmail(String rawEmail) {
        return (rawEmail != null && rawEmail.contains("@"))
            ? right(rawEmail.trim().toLowerCase())
            : left("Invalid email address: " + rawEmail);
    }

    static Either<String, User> createUser(String email) {
        return right(new User("usr-123", email));
    }

    public static void main(String[] args) {
        Either<String, User> pipeline =
            sanitizeEmail("  Alex@Example.Com ")
                .flatMap(EitherPipelineExample::createUser);

        pipeline.ifEither(
            err  -> System.err.println("Aborted: " + err),
            user -> System.out.println("User created successfully: " + user)
        );
        // Output: User created successfully: User[id=usr-123, email=alex@example.com]
    }
}
```

---

### 3. Bimap (`mapEither`) and Swapping (`swap`)

When both sides need to be transformed or their roles swapped:

```java title="EitherTransformExample.java"
import org.quurz.foomp.base.util.Either;

import static org.quurz.foomp.base.util.Either.left;
import static org.quurz.foomp.base.util.Either.right;

public class EitherTransformExample {
    public static void main(String[] args) {
        Either<Integer, String> initial = left(404);

        // Bimap: Transform both sides simultaneously
        Either<String, String> mapped = initial.mapEither(
            code -> "HTTP Error: " + code,
            body -> "Response: " + body
        );
        System.out.println(mapped.getLeft()); // HTTP Error: 404

        // swap: Left becomes Right and vice versa
        Either<String, String> swapped = mapped.swap();
        System.out.println("After swap, is it Right? " + swapped.isRight()); // true
        System.out.println("Value: " + swapped.getRight()); // HTTP Error: 404
    }
}
```

---

## Best Practices

:::tip[Choosing Between `Either`, `Result`, and `Maybe`]
* **Use [`Maybe`](/reference/base/util/maybe/):** When a value is present or absent and no detailed error reason is needed (`Some`/`None`).
* **Use [`Result`](/reference/base/util/result/):** When operations may throw exceptions and you want standard `Success(value)` vs `Failure(throwable)` representation.
* **Use [`Either`](/reference/base/util/either/):** When modeling custom, strongly-typed error domains (such as `enum`s or domain records) or generic two-track branching.
:::
