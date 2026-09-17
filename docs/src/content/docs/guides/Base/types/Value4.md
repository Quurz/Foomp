---
title: Value4 Guide
description: Practical developer guide for working with the Value4 quaternary value carrier interface in Foomp.
---

The `Value4<A1, A2, A3, A4>` interface provides a standardized contract for 4-element product carriers, such as RGBA color models, 4D spacetime vectors $(t, x, y, z)$, or database row projections.

---

## When to Use `Value4<A1, A2, A3, A4>`

* **RGBA / Color Processing:** Carrying red, green, blue, and alpha channel values.
* **4D Physics & Spacetime:** Representing four-dimensional spacetime coordinates.
* **Audit Metadata:** Packaging entity ID, payload, timestamp, and signature together in a single container.

---

## Examples

### 1. RGBA Color Channel Processing

```java title="RgbaProcessingExample.java"
import org.quurz.foomp.base.types.Value4;
import org.quurz.foomp.base.util.Tuple4;

public class RgbaProcessingExample {

    public static String toCssRgbaString(Value4<Integer, Integer, Integer, Double> color) {
        return String.format("rgba(%d, %d, %d, %.2f)",
            color.get1(), // Red
            color.get2(), // Green
            color.get3(), // Blue
            color.get4()  // Alpha
        );
    }

    public static void main(String[] args) {
        Value4<Integer, Integer, Integer, Double> brandColor = Tuple4.tuple4(255, 102, 0, 0.85);
        String css = toCssRgbaString(brandColor);
        System.out.println(css); // rgba(255, 102, 0, 0.85)
    }
}
```

---

### 2. Audit Trail Envelope Destructuring

```java title="AuditEnvelopeExample.java"
import org.quurz.foomp.base.types.Value4;
import org.quurz.foomp.base.util.Record4;
import java.time.Instant;

public class AuditEnvelopeExample {
    public static void main(String[] args) {
        Value4<String, String, Instant, Integer> envelope = Record4.record4(
            "EVT-9021",
            "USER_LOGIN",
            Instant.now(),
            200
        );

        System.out.println("Event ID:   " + envelope.get1());
        System.out.println("Type:       " + envelope.get2());
        System.out.println("Timestamp:  " + envelope.get3());
        System.out.println("Status:     " + envelope.get4());
    }
}
```

---

## Best Practices

:::tip[Working with Value4]
* **Prefer Named Records for Complex Domains:** While `Value4` and `Tuple4` are excellent for ad-hoc grouping and mathematical tuples, consider domain-specific types or named records for large multi-field business entities.
* **Leverage `Mappable4`:** Use `Mappable4` when transforming individual components (`map1` through `map4` or `mapAll`) without manually unpacking and repacking components.
:::
