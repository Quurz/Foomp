---
title: Working with Mappable4
description: Practical recipes and quadrifunctor transformation patterns using Mappable4 in Foomp.
---

import { Aside, Tabs, TabItem } from '@astrojs/starlight/components';

`Mappable4<WT, A1, A2, A3, A4>` enables flexible, type-safe transformations across 4 independent type parameters. It powers 4-element structures such as `Tuple4` and `Record4`.

---

## The Quadrifunctor Model

Each channel can be transformed independently without affecting the other three, or all four channels can be transformed together in one operation:

```
  Mappable4<A1, A2, A3, A4> ─── map1(f1) ───────────────> Mappable4<B1, A2, A3, A4>
  Mappable4<A1, A2, A3, A4> ─── map2(f2) ───────────────> Mappable4<A1, B2, A3, A4>
  Mappable4<A1, A2, A3, A4> ─── map3(f3) ───────────────> Mappable4<A1, A2, B3, A4>
  Mappable4<A1, A2, A3, A4> ─── map4(f4) ───────────────> Mappable4<A1, A2, A3, B4>
  Mappable4<A1, A2, A3, A4> ─── mapAll(f1, f2, f3, f4) ─> Mappable4<B1, B2, B3, B4>
```

---

## Practical Examples

### 1. RGBA Color Channel Processing

Transform color channels in an immutable `Record4<Integer, Integer, Integer, Double>`:

```java title="ColorMappingExample.java"
import org.quurz.foomp.base.util.Record4;

public class ColorMappingExample {

    public static void main(String[] args) {
        // RGBA: Red (0-255), Green (0-255), Blue (0-255), Alpha (0.0-1.0)
        Record4<Integer, Integer, Integer, Double> color = Record4.of(255, 128, 0, 1.0);

        // Adjust only opacity / alpha channel
        Record4<Integer, Integer, Integer, Double> semiTransparent = color.map4(a -> a * 0.5);
        System.out.println(semiTransparent); // Record4(255, 128, 0, 0.5)

        // Convert RGB from [0, 255] to normalized floats [0.0, 1.0] and format alpha as percentage
        Record4<Float, Float, Float, String> normalized = color.mapAll(
            r -> r / 255.0f,
            g -> g / 255.0f,
            b -> b / 255.0f,
            a -> String.format("%.0f%%", a * 100)
        );

        System.out.println(normalized);
        // Record4(1.0, 0.5019608, 0.0, 100%)
    }
}
```

---

### 2. Audit Trail Metadata Transformation (`Tuple4`)

Work with audit records containing `(Timestamp, UserId, Action, IPAddress)`:

```java title="AuditLogExample.java"
import org.quurz.foomp.base.util.Tuple4;
import java.time.Instant;

public class AuditLogExample {

    public static void main(String[] args) {
        Tuple4<Instant, Long, String, String> log = Tuple4.of(
            Instant.now(),
            1042L,
            "login_success",
            "192.168.1.50"
        );

        // Anonymize IP address and uppercase the action string
        Tuple4<Instant, Long, String, String> anonymized = log.mapAll(
            Fun.identity(),
            Fun.identity(),
            String::toUpperCase,
            ip -> "xxx.xxx.xxx." + ip.substring(ip.lastIndexOf('.') + 1)
        );

        System.out.println(anonymized.get3()); // "LOGIN_SUCCESS"
        System.out.println(anonymized.get4()); // "xxx.xxx.xxx.50"
    }
}
```

---

## Summary of Operations

| Method | Target | Other Parameters |
| :--- | :--- | :--- |
| `map1(f1)` | `A1` $\to$ `B1` | `A2`, `A3`, `A4` preserved |
| `map2(f2)` | `A2` $\to$ `B2` | `A1`, `A3`, `A4` preserved |
| `map3(f3)` | `A3` $\to$ `B3` | `A1`, `A2`, `A4` preserved |
| `map4(f4)` | `A4` $\to$ `B4` | `A1`, `A2`, `A3` preserved |
| `mapAll(f1, f2, f3, f4)` | `A1..A4` $\to$ `B1..B4` | All 4 mapped in a single pass |

---

## Best Practices

:::tip[Performance Advantage]
`mapAll` transforms all 4 fields directly without creating temporary 4-tuple instances, keeping allocations to a minimum.
:::
