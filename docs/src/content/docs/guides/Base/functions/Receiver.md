---
title: Working with Receiver
description: Practical recipes, fluent event ingestion, stream integration, and conditional validation pipelines using Receiver in Foomp.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

`Receiver<A>` is Foomp's functional consumer interface. It wraps side-effecting operations while bridging Java's `java.util.function.Consumer<A>` with the functional `Fun<A, Nothing>` abstraction. With built-in filtering, exception mapping, and fluent bulk ingestion (`acceptAllAndContinue`), `Receiver` provides a clean mechanism for sinks, event dispatchers, loggers, and data collectors.

---

## 1. Creating Receivers & Basic Usage

You can create `Receiver` instances using lambdas, method references, or the factory methods in `Receiver`:

```java title="ReceiverCreation.java"
import org.quurz.foomp.base.functions.Receiver;
import java.util.ArrayList;
import java.util.List;

List<String> eventLog = new ArrayList<>();

// Direct lambda or method reference
Receiver<String> logger = eventLog::add;

// Basic invocation via accept
logger.accept("UserLoggedIn: alex");

// Invocation via apply (returning Nothing)
logger.apply("OrderPlaced: #4092");
```

---

## 2. Conditional Ingestion & Pre-Filtering

Instead of polluting your consumer logic with `if (...)` guards, `Receiver.receiver(consumer, filter)` allows you to build pre-filtered consumers declaratively:

```java title="FilteredReceiver.java"
import org.quurz.foomp.base.functions.Receiver;
import java.util.ArrayList;
import java.util.List;

List<Integer> evenNumbers = new ArrayList<>();

// Only even numbers will reach the list
Receiver<Integer> evenReceiver = Receiver.receiver(
    evenNumbers::add,
    n -> n % 2 == 0
);

evenReceiver.accept(1); // Ignored
evenReceiver.accept(2); // Added
evenReceiver.accept(3); // Ignored
evenReceiver.accept(4); // Added

// evenNumbers contains: [2, 4]
```

---

## 3. Validation with Custom Exceptions

When receiving unexpected or invalid data is a fatal error, you can provide an `exceptionBuilder`:

```java title="ValidatingReceiver.java"
import org.quurz.foomp.base.functions.Receiver;

Receiver<String> emailSink = Receiver.receiver(
    emailService::sendNotification,
    email -> email.contains("@") && email.contains("."),
    invalidEmail -> new IllegalArgumentException("Invalid email address: " + invalidEmail)
);

emailSink.accept("user@example.com"); // OK
emailSink.accept("invalid-address");  // Throws IllegalArgumentException
```

---

## 4. Fluent Chaining & Bulk Processing

`Receiver` provides fluent methods returning `this`, allowing you to chain intake calls or ingest data from streams, collections, and iterators in a single expression:

<Tabs>
  <TabItem label="Fluent Chaining">
    ```java
    Receiver<String> sink = Receiver.receiver(auditLog::record);

    sink.acceptAndContinue("Step 1 initialized")
        .acceptAndContinue("Step 2 processing")
        .acceptAndContinue("Step 3 finished");
    ```
  </TabItem>
  <TabItem label="Varargs Ingestion">
    ```java
    Receiver<String> sink = Receiver.receiver(auditLog::record);

    sink.acceptAllAndContinue("Init", "Processing", "Commit", "Done");
    ```
  </TabItem>
  <TabItem label="Collections & Streams">
    ```java
    List<String> pendingTasks = List.of("Task A", "Task B", "Task C");

    sink.acceptAllAndContinue(pendingTasks)
        .acceptAllAndContinue(Stream.of("Task D", "Task E"));
    ```
  </TabItem>
</Tabs>

---

## 5. Using Receiver with Functional Combinators (`Fun<A, Nothing>`)

Because `Receiver<A>` implements `Fun<A, Nothing>`, it can be passed anywhere a functional mapping from $A \to \text{Nothing}$ is expected:

```java title="FunctionalIntegration.java"
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.functions.Receiver;
import org.quurz.foomp.base.util.Nothing;

Receiver<Order> orderNotifier = Receiver.receiver(emailService::notifyOrder);

// As Fun<Order, Nothing>
Fun<Order, Nothing> func = orderNotifier;

// Adapt and compose
Fun<String, Order> orderParser = Order::parse;
Fun<String, Nothing> parseAndNotify = orderParser.andThen(orderNotifier);

parseAndNotify.apply("ORDER_ID=9812");
```

---

## Summary Comparison

| Feature | Standard `java.util.function.Consumer<A>` | Foomp `Receiver<A>` |
| :--- | :--- | :--- |
| **Functional Interface** | `Consumer<A>` | `Consumer<A>` + `Fun<A, Nothing>` |
| **Fluent Ingestion** | `andThen(Consumer)` only | `acceptAndContinue`, `acceptAllAndContinue` |
| **Filtering Sinks** | Manual `if` statement | Factory `Receiver.receiver(consumer, filter)` |
| **Validation with Exceptions** | Manual checks | Factory `Receiver.receiver(consumer, filter, exBuilder)` |
| **Batch Sources** | Manual `forEach` loop | Native `Collection`, `Iterator`, `Stream`, varargs intake |
