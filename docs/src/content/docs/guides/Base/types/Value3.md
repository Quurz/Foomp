---
title: Value3 Guide
description: Practical developer guide for working with the Value3 ternary value carrier interface in Foomp.
---

The `Value3<A1, A2, A3>` interface models containers holding exactly three components. It is commonly used for 3D coordinates, RGB color specifications, date triples (year, month, day), or database records.

---

## When to Use `Value3<A1, A2, A3>`

* **3D Geometry & Spatial Processing:** Representing points $(x, y, z)$ or vectors in 3D space.
* **RGB Color Channels:** Carrying red, green, and blue components.
* **Generic 3-Tuple Access:** Accessing components positionally via `.get1()`, `.get2()`, and `.get3()`.

---

## Examples

### 1. 3D Coordinate Point Processing

```java title="SpatialPointExample.java"
import org.quurz.foomp.base.types.Value3;
import org.quurz.foomp.base.util.Tuple3;

public class SpatialPointExample {

    public static double computeDistanceFromOrigin(Value3<Double, Double, Double> point) {
        double x = point.get1();
        double y = point.get2();
        double z = point.get3();
        return Math.sqrt(x * x + y * y + z * z);
    }

    public static void main(String[] args) {
        Value3<Double, Double, Double> point = Tuple3.tuple3(3.0, 4.0, 12.0);
        double distance = computeDistanceFromOrigin(point);
        System.out.println("Distance: " + distance); // 13.0
    }
}
```

---

### 2. Destructuring a 3-Component Record

```java title="Record3DestructuringExample.java"
import org.quurz.foomp.base.types.Value3;
import org.quurz.foomp.base.util.Record3;

public class Record3DestructuringExample {
    public static void main(String[] args) {
        Value3<String, String, Integer> userEntry = Record3.record3("schell", "Alexander", 2026);

        if (userEntry.isPresent1() && userEntry.isPresent2() && userEntry.isPresent3()) {
            System.out.printf("User: %s, %s (Year: %d)%n",
                userEntry.get1(),
                userEntry.get2(),
                userEntry.get3()
            );
        }
    }
}
```

---

## Best Practices

:::tip[Working with Value3]
* **Use Positional Accessors Clearly:** Use `.get1()`, `.get2()`, and `.get3()` when generic positional access is desired.
* **Combine with `Mappable3`:** When mapping each component independently, combine `Value3` containers with `Mappable3` for type-safe transformations (`map1`, `map2`, `map3`, `mapAll`).
:::
