---
title: Base Utility Examples
description: Practical code examples and guides for using the functional utility classes in the Foomp Base module.
---

The `org.quurz.foomp.base.util` package provides powerful tools for functional programming in Java. This guide demonstrates how to use the most common classes in real-world scenarios.

## Maybe (Safe Optionality)

`Maybe<A>` is a lazy alternative to Java's `Optional`. It handles values that might be missing without risking a `NullPointerException`.

```java
import org.quurz.foomp.base.util.Maybe;

// Create a Maybe that might contain a value
Maybe<String> username = Maybe.some(() -> "Alexander");
Maybe<String> empty = Maybe.none();

// Transform values safely
Maybe<Integer> length = username.map(String::length); // Some(9)

// Provide a default value
String result = empty.getOrElse(() -> "Guest"); // "Guest"

// Chain multiple Maybe computations
Maybe<String> bio = username.flatMap(name -> findBioInDatabase(name));
```

## Eval (Controlling Evaluation)

`Eval<A>` allows you to choose when and how a value is computed.

```java
import org.quurz.foomp.base.util.Eval;

// Eager evaluation (computed immediately)
Eval<Integer> immediate = Eval.now(10 + 5);

// Lazy evaluation (computed on every access)
Eval<Double> random = Eval.always(() -> Math.random());

// Lazy and memoized (computed once, then cached)
Eval<String> databaseConfig = Eval.later(() -> loadConfigFromDisk());

// Usage
String config = databaseConfig.get(); // Triggers the computation once
```

## Attempt & Result (Error Handling)

`Attempt<A>` is a lazy wrapper for operations that can throw exceptions. It defers the "try-catch" until you actually need the result.

```java
import org.quurz.foomp.base.util.Attempt;
import org.quurz.foomp.base.util.Result;

// Define a risky operation lazily
Attempt<Integer> division = Attempt.tryIt(() -> 10 / 0);

// The operation is NOT executed yet.
// To execute and get a Result (Success or Failure):
Result<Integer> outcome = division.execute();

if (outcome.isFailure()) {
    System.out.println("Error: " + outcome.getException().getMessage());
}

// Or use functional chaining
int finalValue = division.execute().getOrElse(() -> -1);
```

## Tuples (Data Containers)

Tuples are immutable containers for multiple values of different types.

```java
import org.quurz.foomp.base.util.Tuple2;
import org.quurz.foomp.base.util.Tuple3;

// Create a pair (Name, Age)
Tuple2<String, Integer> person = Tuple2.tuple2("Alice", 30);

// Access values
String name = person.get1();
Integer age = person.get2();

// Transform values
Tuple2<String, String> updated = person.map2(String::valueOf); // ("Alice", "30")

// Zip values into a tuple
Tuple3<String, Integer, Boolean> triple = Tuple3.tuple3("Alice", 30, true);
```

## Trampoline (Safe Recursion)

Use `Trampoline<T>` to avoid `StackOverflowError` in deep recursive algorithms by converting them into iterative loops on the heap.

```java
import org.quurz.foomp.base.util.Trampoline;

public Trampoline<Integer> factorial(int n, int acc) {
    if (n <= 1) {
        return Trampoline.done(acc);
    }
    // Defer the next step
    return Trampoline.more(() -> factorial(n - 1, n * acc));
}

// Execute the recursive computation safely
int result = factorial(10000, 1).get();
```

## Util (Validation & Contracts)

The `Util` class provides static methods to enforce contracts and ensure your data meets specific requirements.

```java
import org.quurz.foomp.base.util.Util;
import java.util.List;

// Enforce non-null and non-empty
java.util.List<String> names = java.util.Arrays.asList("Junie");
Util.requireNonEmpty(names, () -> new RuntimeException("List is empty"));

// Validate collections
java.util.Set<String> tags = new java.util.HashSet<>(java.util.Arrays.asList("java", "functional"));
java.util.Set<String> allowed = new java.util.HashSet<>(java.util.Arrays.asList("java", "functional", "foomp"));
Util.requireSubSet(allowed, tags, (sup, sub) -> new IllegalArgumentException("Invalid tags: " + sub));
```

### Type & File System Validation

You can also validate class types and file system properties.

```java
import java.io.File;
import java.nio.file.Path;

// Validate Class types
Util.requireConcreteType(String.class, () -> new RuntimeException("Not a concrete class"));
Util.requireInterfaceType(Runnable.class, () -> new RuntimeException("Not an interface"));

// Validate File System
Util.requireRegularFile("config.properties", () -> new RuntimeException("Missing config"));
Util.requireDirectory(new File("data"), () -> new RuntimeException("Data directory missing"));
Util.requireReadable("input.txt", () -> new RuntimeException("Cannot read input"));
Util.requireWriteable(Path.of("output.log"), () -> new RuntimeException("Cannot write log"));
```

### Functional Wrappers

```java
// Wrap a function to never return null
org.quurz.foomp.base.functions.Fun<String, String> safeUpper = Util.requireNonNullResult1(String::toUpperCase, (s) -> new RuntimeException("Null result for " + s));
```

## Zipper (List Operations)

The `Zipper` helps you merge or split lists of related data.

```java
import org.quurz.foomp.base.util.Zipper;
import java.util.List;

List<String> keys = List.of("A", "B", "C");
List<Integer> values = List.of(1, 2, 3);

// Combine two lists into pairs
List<Tuple2<String, Integer>> zipped = Zipper.zip(keys, values);

// Split them back
Tuple2<List<String>, List<Integer>> unzipped = Zipper.unzip(zipped);
```

## Either & Result (States and Success)

`Either<L, R>` represents a value of one of two possible types (a disjoint union). By convention, `Left` is used for failure and `Right` for success. `Result<A>` is a specialized `Either` where the left side is always an `Exception`.

```java
import org.quurz.foomp.base.util.Either;
import org.quurz.foomp.base.util.Result;

// Either: Choice between two types
Either<String, Integer> value = Either.right(42);
String display = value.mapEither(
    err -> "Error: " + err,
    res -> "Success: " + res
).getRightOrElse(() -> "Unknown");

// Result: Specialized Either for Exception handling
Result<Double> sqrt = Result.success(16.0)
    .transmogrify(res -> {
        if (res.getRight() < 0) return Result.failure(new ArithmeticException("Negative"));
        return Result.success(Math.sqrt(res.getRight()));
    });

if (sqrt.isSuccess()) {
    System.out.println("Result: " + sqrt.getValue());
}
```

## Continuation (Control Flow)

`Continuation<A, R>` (also known as the Cont Monad) represents computations in continuation-passing style. It can be used to manage complex control flows.

```java
import org.quurz.foomp.base.util.Continuation;

// Define a computation that will eventually yield a String, producing an Integer result
Continuation<String, Integer> hello = Continuation.pureContinuation("Hello");

// Chain operations
Continuation<Integer, Integer> length = hello.map(String::length);

// Execute by providing a final "callback"
int result = length.apply(len -> len * 2); // 10
```

## DecisionTree (Rule Engines & Branching Logic)

`DecisionTree<F, R>` allows building a tree of rules that are evaluated against a "fact" object. It supports simple transformations as well as combined side-effects.

### Basic Branching
```java
import org.quurz.foomp.base.util.DecisionTree;

DecisionTree<Integer, String> ageCheck = DecisionTree.decisionTree(
    age -> age >= 18,
    DecisionTree.decisionLeaf(fact -> "Adult"),
    DecisionTree.decisionLeaf(fact -> "Minor")
);

String status = ageCheck.examine(20); // "Adult"
```

### Side-Effects in Leaves
You can combine transformations with side-effects (Consumers). This is useful for logging or updating external state during tree traversal.

#### Consumer then Transformer
The side-effect is executed before the transformation.
```java
DecisionTree<String, Integer> leaf = DecisionTree.decisionLeaf(
    fact -> System.out.println("Processing: " + fact), // Side-effect
    fact -> fact.length()                             // Transformation
);
```

#### Transformer then Consumer
The side-effect is executed after the transformation and receives the transformed result.
```java
DecisionTree<String, Integer> leaf = DecisionTree.decisionLeaf(
    fact -> fact.length(),                            // Transformation
    result -> System.out.println("Result: " + result) // Side-effect on result
);
```

## Constraint (Object Validation)

`Constraint<A, FAILURE>` represents a single validation rule.

```java
import org.quurz.foomp.base.util.Constraint;
import org.quurz.foomp.base.util.Maybe;

Constraint<String, String> lengthCheck = Constraint.constraint(
    s -> s.length() >= 5,
    "String is too short"
);

Maybe<String> violation = lengthCheck.checkViolation("Hi"); // Some("String is too short")
boolean isValid = lengthCheck.isValid("Hello World"); // true
```

## Bucket (Batching Elements)

`Bucket<A>` collects elements and "flushes" them when a size limit is reached.

```java
import org.quurz.foomp.base.util.Bucket;
import java.util.ArrayList;
import java.util.List;

List<List<String>> batches = new ArrayList<>();
Bucket<String> logBucket = Bucket.bucket(3, batch -> batches.add(new ArrayList<>(batch)));

logBucket.add("Log 1");
logBucket.add("Log 2");
logBucket.add("Log 3"); // Triggers flush: batches contains [["Log 1", "Log 2", "Log 3"]]
logBucket.add("Log 4");
logBucket.flush();      // Manual flush: batches contains [..., ["Log 4"]]
```

## MutablePair (Changeable Containers)

Unlike Tuples, `MutablePair` allows updating its values.

```java
import org.quurz.foomp.base.util.MutablePair;

MutablePair<String, Integer> state = MutablePair.mutablePair("Initial", 0);

state.set1("Updated");
state.set2(42);

System.out.println(state.get1()); // "Updated"
```

## Stateful (State Monad)

`Stateful<A, S>` represents a computation that carries an internal state `S` and produces a value `A`.

```java
import org.quurz.foomp.base.util.Stateful;
import org.quurz.foomp.base.util.Tuple2;

// A counter state: increment state and return the old value
Stateful<Integer, Integer> increment = Stateful.stateful(s -> Tuple2.tuple2(s, s + 1));

// Chain stateful operations
Stateful<Integer, Integer> doubleIncrement = increment.flatMap(v1 -> increment);

Tuple2<Integer, Integer> result = doubleIncrement.runState(10);
// result.get1() -> 11 (the value from the second increment)
// result.get2() -> 12 (the final state)
```

## Records (Alternative to Tuples)

Records are similar to Tuples but often used when you want to group values that represent a single entity (like a database row).

```java
import org.quurz.foomp.base.util.Record2;

Record2<String, String> user = Record2.record2("Alexander", "Admin");

String name = user.get1();
String role = user.get2();
```
