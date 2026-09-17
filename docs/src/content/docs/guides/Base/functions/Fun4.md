---
title: Working with Fun4
description: Practical recipes for quaternary functions in Foomp, covering currying, progressive partial application, and pipeline composition.
---

import { Aside, Tabs, TabItem } from '@astrojs/starlight/components';

When building domain objects or configuring subsystems, functions frequently require four distinct parameters (e.g. RGBA color components, database connections, or HTTP request descriptors).

`Fun4<X1, X2, X3, X4, Y>` provides a quaternary functional interface with:
* **Strict non-null contracts** across all 4 arguments and return values.
* **4-level Currying & Uncurrying** (`.curry()` and `Fun4.uncurry()`).
* **Progressive Partial Application** (`.partial1()` through `.partial4()`) returning [`Fun3`](/reference/base/functions/fun3/).
* **Forward Composition** (`.andThen()`).

---

## Defining and Calling a Quaternary Function

A common quaternary scenario is assembling structured connection endpoints or creating RGBA color values:

```java
import org.quurz.foomp.base.functions.Fun4;

record HttpEndpoint(String protocol, String host, int port, String basePath) {}

// Quaternary factory function
Fun4<String, String, Integer, String, HttpEndpoint> endpointFactory = 
    HttpEndpoint::new;

HttpEndpoint endpoint = endpointFactory.apply("https", "api.github.com", 443, "/v3");
System.out.println(endpoint);
```

:::note[Non-Null Contract & Responsibilities]
`Fun4` specifies a strict `@NonNull` contract for all 4 arguments and the return value. Because `apply` is an abstract interface method, runtime null-checks inside your lambda or class implementation are your responsibility (or verified statically by tools like Checker Framework). Foomp's combinators (`andThen`, `partialX`, `curry`) systematically perform runtime null-checks at boundaries.
:::

---

## 4-Stage Currying

Currying transforms a quaternary function `(X1, X2, X3, X4) -> Y` into four nested unary functions: `X1 -> (X2 -> (X3 -> (X4 -> Y)))`.

```java
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.functions.Fun4;
import static org.quurz.foomp.base.functions.Fun4.uncurry;

Fun4<String, String, String, String, String> auditLogger = 
    (tenant, user, action, resource) -> 
        "[" + tenant + "][" + user + "] executed " + action + " on " + resource;

// 1. Curry the quaternary function
Fun<String, Fun<String, Fun<String, Fun<String, String>>>> curried = auditLogger.curry();

// 2. Pre-bind tenant and user to create a scoped audit action function
var tenantAudit = curried.apply("Tenant-Acme");
var userAudit = tenantAudit.apply("Alice");

// 3. Complete invocation in specific workflows
String log1 = userAudit.apply("READ").apply("UserTable");
String log2 = userAudit.apply("WRITE").apply("PaymentRecord");

System.out.println(log1); // [Tenant-Acme][Alice] executed READ on UserTable
System.out.println(log2); // [Tenant-Acme][Alice] executed WRITE on PaymentRecord

// 4. Uncurry back to Fun4 when needed
Fun4<String, String, String, String, String> restored = uncurry(curried);
```

---

## Progressive Partial Application Pipeline

Foomp's functional hierarchy enables progressive reduction of function arity from 4 down to 1:

```java
import org.quurz.foomp.base.functions.Fun4;
import org.quurz.foomp.base.functions.Fun3;
import org.quurz.foomp.base.functions.Fun2;
import org.quurz.foomp.base.functions.Fun;

// (protocol, host, port, path) -> URL string
Fun4<String, String, Integer, String, String> urlBuilder = 
    (protocol, host, port, path) -> protocol + "://" + host + ":" + port + path;

// Step 1: Bind protocol to "https" -> yields Fun3<String, Integer, String, String>
Fun3<String, Integer, String, String> httpsBuilder = 
    urlBuilder.partial1(() -> "https");

// Step 2: Bind port to 443 -> yields Fun2<String, String, String>
Fun2<String, String, String> defaultPortBuilder = 
    httpsBuilder.partial2(() -> 443);

// Step 3: Bind host dynamically from system configuration -> yields Fun<String, String>
Fun<String, String> apiPathRouter = 
    defaultPortBuilder.partial1(() -> System.getProperty("api.host", "api.example.com"));

// Step 4: Final unary function only requires the path!
System.out.println(apiPathRouter.apply("/users")); // https://api.example.com:443/users
System.out.println(apiPathRouter.apply("/orders")); // https://api.example.com:443/orders
```

---

## Result Composition (`andThen`)

Transform the result of a `Fun4` directly into another domain representation:

```java
import org.quurz.foomp.base.functions.Fun4;
import java.awt.Color;

// Raw rgba constructor
Fun4<Integer, Integer, Integer, Integer, Color> colorFactory = 
    (r, g, b, a) -> new Color(r, g, b, a);

// Transform Color into a CSS rgba(...) string
Fun4<Integer, Integer, Integer, Integer, String> cssRgba = 
    colorFactory.andThen(c -> String.format("rgba(%d, %d, %d, %.2f)", 
        c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha() / 255.0));

System.out.println(cssRgba.apply(255, 128, 0, 204)); // rgba(255, 128, 0, 0.80)
```

---

## Summary

* **`Fun4<X1, X2, X3, X4, Y>`:** First-class quaternary functional interface.
* **`curry()` / `uncurry()`:** Transition seamlessly between 4-argument functions and 4-level unary chains.
* **`partial1()` .. `partial4()`:** Progressively reduce arity: `Fun4` ➔ `Fun3` ➔ `Fun2` ➔ `Fun`.
* **`andThen()`:** Compose the quaternary function with subsequent value transformers.
