---
title: Working with Fun3
description: Practical recipes for ternary functions in Foomp, covering currying, multi-stage partial application, and pipeline composition.
---

import { Aside, Tabs, TabItem } from '@astrojs/starlight/components';

Java's standard library provides functional interfaces for unary (`Function`) and binary (`BiFunction`) operations, but lacks built-in support for ternary operations (functions with three arguments).

`Fun3<X1, X2, X3, Y>` bridges this gap, providing a first-class ternary functional interface with:
* **Strict non-null contracts** across all arguments and return values.
* **Currying & Uncurrying** (`.curry()` and `Fun3.uncurry()`).
* **Flexible Partial Application** (`.partial1()`, `.partial2()`, `.partial3()`) returning [`Fun2`](/reference/base/functions/fun2/).
* **Forward Composition** (`.andThen()`).

---

## Defining and Invoking a Ternary Function

A typical ternary use case is constructing compound values or configuring client connections (e.g., host, port, and path):

```java
import org.quurz.foomp.base.functions.Fun3;

// Ternary function: (protocol, host, port) -> base URL
Fun3<String, String, Integer, String> urlBuilder = 
    (protocol, host, port) -> protocol + "://" + host + ":" + port;

String apiEndpoint = urlBuilder.apply("https", "api.example.com", 8443);
System.out.println(apiEndpoint); // https://api.example.com:8443
```

:::note[Non-Null Contract & Responsibilities]
`Fun3` specifies a strict `@NonNull` contract for all arguments and return values. As `apply` is an abstract interface method, runtime null-checks inside your lambda are your responsibility (or verified statically via tools like Checker Framework). Foomp's built-in combinators (`andThen`, `partialX`, `curry`) automatically enforce runtime null-safety at each boundary.
:::

---

## Currying and Step-by-Step Invocation

Currying decomposes a 3-argument function `(X1, X2, X3) -> Y` into a chain of three unary functions: `X1 -> (X2 -> (X3 -> Y))`.

```java
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.functions.Fun3;
import static org.quurz.foomp.base.functions.Fun3.uncurry;

Fun3<String, String, String, String> emailFormatter = 
    (from, to, subject) -> "From: " + from + " | To: " + to + " | Subject: " + subject;

// 1. Curry the ternary function
Fun<String, Fun<String, Fun<String, String>>> curried = emailFormatter.curry();

// 2. Supply arguments step-by-step
Fun<String, Fun<String, String>> fromAdmin = curried.apply("admin@example.com");
Fun<String, String> toUser = fromAdmin.apply("user@example.com");

String email1 = toUser.apply("Welcome to Foomp!");
String email2 = toUser.apply("Security Alert");

System.out.println(email1); 
// From: admin@example.com | To: user@example.com | Subject: Welcome to Foomp!

// 3. Uncurry back to Fun3
Fun3<String, String, String, String> restored = uncurry(curried);
```

---

## Multi-Stage Partial Application with Suppliers

Foomp's `partial1`, `partial2`, and `partial3` methods bind one parameter using a `Supplier`, returning a binary [`Fun2`](/reference/base/functions/fun2/). 

Because arguments are bound via suppliers, you can seamlessly integrate dynamic context:

```java
import org.quurz.foomp.base.functions.Fun2;
import org.quurz.foomp.base.functions.Fun3;
import org.quurz.foomp.base.functions.Fun;

// (environment, serviceName, version) -> config key
Fun3<String, String, String, String> keyGen = 
    (env, service, ver) -> env + "." + service + ".v" + ver;

// Fix environment dynamically from system property (partial1 -> Fun2)
Fun2<String, String, String> prodKeyGen = 
    keyGen.partial1(() -> System.getProperty("app.env", "production"));

// Fix version to constant (partial2 on Fun2 -> Fun)
Fun<String, String> prodV2KeyGen = 
    prodKeyGen.partial2(() -> "2");

// Now we have a simple unary function taking only the service name
System.out.println(prodV2KeyGen.apply("auth-service")); 
// production.auth-service.v2
```

---

## Composing Pipelines (`andThen`)

You can chain transformations onto the result of a `Fun3` without breaking fluency:

```java
import org.quurz.foomp.base.functions.Fun3;
import java.net.URI;

Fun3<String, String, Integer, String> rawUrl = 
    (scheme, host, port) -> scheme + "://" + host + ":" + port;

// Transform String output directly into a URI instance
Fun3<String, String, Integer, URI> uriBuilder = rawUrl.andThen(URI::create);

URI endpoint = uriBuilder.apply("https", "localhost", 8080);
System.out.println(endpoint.getHost()); // localhost
```

---

## Summary

* **`Fun3<X1, X2, X3, Y>`:** First-class ternary function with strict null validation.
* **`curry()` / `uncurry()`:** Convert between 3-argument functions and unary chains (`Fun<X1, Fun<X2, Fun<X3, Y>>>`).
* **`partial1()` / `partial2()` / `partial3()`:** Fix any argument with a `Supplier` to yield a binary [`Fun2`](/reference/base/functions/fun2/).
* **`andThen()`:** Post-process ternary function results into new types.
