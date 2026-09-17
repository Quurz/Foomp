---
title: Working with Provider
description: Practical recipes, lazy evaluation, monadic composition, HKT integration, and value injection using Provider in Foomp.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

`Provider<A>` is Foomp's functional value supplier and monadic container ($() \to A$). By extending `java.util.function.Supplier<A>` while implementing full functor (`map`), monadic (`flatMap`), applicative (`applyTo`), and HKT (`Higher1<Provider.µ, A>`) abstractions, `Provider` allows developers to defer computation, structure dependency injection, and transform values cleanly without intermediate boilerplate.

---

## 1. Creating Providers

You can create `Provider` instances directly from raw values, standard `Supplier` instances, or custom lambdas:

```java title="ProviderCreation.java"
import org.quurz.foomp.base.functions.Provider;
import java.time.Instant;

// Constant provider (eagerly captures value)
Provider<String> appName = Provider.provider("FoompCore");

// Dynamic supplier (evaluated lazily on each get())
Provider<Instant> currentTime = Provider.providerFrom(Instant::now);

// Direct lambda expression
Provider<Integer> randomDice = () -> (int) (Math.random() * 6) + 1;
```

---

## 2. Functor & Monadic Transformations (`map`, `flatMap`)

Unlike standard Java `Supplier`, `Provider` acts as a full-fledged functional monad:

<Tabs>
  <TabItem label="Functor (map)">
    ```java
    Provider<String> hostProvider = Provider.provider("api.example.com");

    // Transform lazy values without unwrapping
    Provider<URI> endpointProvider = hostProvider.map(host -> URI.create("https://" + host + "/v1"));
    ```
  </TabItem>
  <TabItem label="Monad (flatMap)">
    ```java
    Provider<Config> configProvider = AppConfig::load;

    // Sequence dependent providers
    Provider<DatabasePool> poolProvider = configProvider.flatMap(
        config -> Provider.providerFrom(() -> DatabasePool.connect(config.dbUrl()))
    );
    ```
  </TabItem>
</Tabs>

---

## 3. Unwinding & Transmogrification

When working with dynamic or expensive providers, you can use `.unwind()` to freeze the current value into a constant provider, or `.transmogrify()` to pipe the provider into external builders:

```java title="UnwindAndTransmogrify.java"
import org.quurz.foomp.base.functions.Provider;

Provider<Long> dynamicTimestamp = System::currentTimeMillis;

// dynamicTimestamp.get() yields a new timestamp every call
// Calling .unwind() evaluates once and freezes the result:
Provider<Long> frozenSnapshot = dynamicTimestamp.unwind();

// Transmogrify into a custom service
MyClient client = Provider.provider("https://api.domain.com")
    .transmogrify(urlProvider -> new MyClient(urlProvider.get()));
```

---

## 4. Higher-Kinded Types (HKT) Integration

`Provider` is fully integrated into Foomp's category theory and higher-kinded types framework via its witness `Provider.µ`:

```java title="HigherKindedProvider.java"
import org.quurz.foomp.base.functions.Provider;
import org.quurz.foomp.higher.Higher1;

// General higher-kinded token
Higher1<Provider.µ, String> higherVal = Provider.provider("Hello HKT");

// Narrowing back safely
Provider<String> concreteProvider = Provider.narrow(higherVal);
System.out.println(concreteProvider.get()); // "Hello HKT"
```

---

## 5. Bridging with `Fun<Nothing, A>`

Because zero-argument functions in category theory correspond to $() \to A$ (or $\text{Nothing} \to A$), `Provider<A>` implements `Fun<Nothing, A>`:

```java title="ZeroArityBridge.java"
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.functions.Provider;
import org.quurz.foomp.base.util.Nothing;

Provider<String> tokenProvider = () -> AuthToken.generate();

// Usable directly as Fun<Nothing, String>
Fun<Nothing, String> tokenFun = tokenProvider;
String token = tokenFun.apply(Nothing.nothing);
```

---

## Summary Comparison

| Feature | Standard `java.util.function.Supplier<A>` | Foomp `Provider<A>` |
| :--- | :--- | :--- |
| **Monadic Operations** | None | `.map(...)`, `.flatMap(...)`, `.applyTo(...)` |
| **HKT Support** | None | `Higher1<Provider.µ, A>`, `Provider.narrow(...)` |
| **Functional Bridge** | Standalone interface | Extends `Fun<Nothing, A>`, `Value<A>`, `Unwindable` |
| **Evaluation Freezing** | Manual caching/memoization | Native `.unwind()` operation |
| **Null Defenses** | None | Guarantees non-null values at construction and evaluation |
