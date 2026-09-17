# Foomp

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](license.txt)
[![Java](https://img.shields.io/badge/Java-25-orange.svg)](https://openjdk.org/)
[![Gradle](https://img.shields.io/badge/Gradle-Multi--Module-02303A.svg)](settings.gradle.kts)

> **F**unctional **O**bject-**O**riented **M**onadic **P**rogramming for modern Java.

Foomp is a mathematically grounded, type-safe functional programming library designed for modern Java. It bridges the gap between object-oriented architecture and pure functional paradigms by introducing higher-kinded types (HKT), category theory abstractions, rich monadic containers, and lazy/persistent data structures.

---

## ✨ Features

- **🧩 Rich Monadic Containers:** Full-featured implementations of `Maybe`, `Either`, `Result`, `Attempt`, `Task`, `Eval`, `Stateful`, `Continuation`, `Trampoline`, and `DecisionTree`.
- **📐 Higher-Kinded Types (HKT):** Lightweight, type-safe encoding for higher-kinded types (`Higher1`–`Higher4`) in standard Java using witness types.
- **⚡ Advanced Functions & Composition:** First-class functional interfaces (`Fun`, `Pred`, `Provider`, `Receiver`, arities 1–4) with built-in memoization, currying, partial application, async execution, and fluent logic gates.
- **🚀 Asynchronous Task Management:** Composable, lazy asynchronous computations via `Task` with automatic error capture and monadic chaining.
- **🌲 Persistent & Lazy Data Structures:** Efficient, immutable and lazy data structures including segmented sequences (`Sequence`), eager array sequences (`SeqList`), `Dictionary`, `AVLTree`, and `RedBlackTree`.
- **🛡️ Null Safety & Hygiene:** Comprehensive `@NonNull` contract enforcement, strict immutability, and defensive design.

---

## 📦 Project Modules

| Module | Description |
| :--- | :--- |
| **`foomp.base`** (`:base`) | Core functional building blocks: containers (`Maybe`, `Either`, `Task`, etc.), function abstractions (`Fun`, `Pred`), persistent collections, and utility types. |
| **`foomp.higher`** (`:higher`) | Higher-Kinded Types foundation (`Higher1`–`Higher4`, `WitnessType`, `Hkt`) and algebraic typeclasses (`Functor`, `Applicative`, `Monad`, `Foldable`). |
| **`docs`** | Interactive documentation and API guides built with Astro & Starlight. |

---

## 🚀 Quick Start

### 1. Monadic Error Handling (`Result` / `Maybe` / `Either`)

```java
import org.quurz.foomp.base.util.Maybe;
import org.quurz.foomp.base.util.Result;

// Safe division returning a Maybe
Maybe<Double> divide(double a, double b) {
    return b == 0 ? Maybe.none() : Maybe.of(a / b);
}

// Fluent monadic transformation
Maybe<String> formatted = divide(10.0, 2.0)
    .map(res -> String.format("Result: %.2f", res))
    .or("Division by zero");
```

### 2. Lazy & Segmented Sequences (`Sequence`)

```java
import org.quurz.foomp.base.util.Sequence;

Sequence<Integer> evens = Sequence.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    .filter(n -> n % 2 == 0)
    .map(n -> n * 10);

// Sequences are lazy and composable
List<Integer> resultList = evens.toList();
```

### 3. Asynchronous Tasks (`Task`)

```java
import org.quurz.foomp.base.util.Task;
import org.quurz.foomp.base.util.Result;

Task<String> fetchUser = Task.of(() -> api.getUserById("42"));

// Composing asynchronous tasks monadically
Task<UserProfile> profileTask = fetchUser
    .flatMap(user -> Task.of(() -> api.getProfile(user)));

// Execute asynchronously and receive a Result
profileTask.runAsync(result -> {
    result.ifSuccess(profile -> System.out.println("Profile: " + profile))
          .ifFailure(error -> System.err.println("Failed: " + error.getMessage()));
});
```

---

## 🛠️ Building & Testing

### Prerequisites
* **JDK 25** or newer
* Gradle (or use the included `./gradlew` wrapper)

### Build Commands

```bash
# Compile and run all unit tests
./gradlew test

# Full build including Javadoc and JAR generation
./gradlew build

# Generate test coverage reports (JaCoCo)
./gradlew jacocoTestReport

# Assemble Javadocs for the documentation site
./gradlew assembleDocsForStarlight
```

---

## 📚 Documentation

Detailed guides, architecture deep-dives, and full JavaDocs are available in the `docs` directory.

To run the documentation site locally:

```bash
cd docs
npm install
npm run dev
```

Visit `http://localhost:4321` to explore the docs.

---

## 📄 License

This project is licensed under the [MIT License](license.txt) - see the `license.txt` file for details.
