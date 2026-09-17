---
title: Transforming Objects with Transmogrifyable
description: Practical recipes and use cases for fluent conversions using the Transmogrifyable interface in Foomp.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

The `Transmogrifyable` interface allows custom domain types or containers to integrate seamlessly into fluent transformation pipelines.

## Implementing `Transmogrifyable`

Implementing `Transmogrifyable` is simple: define your class with the self-referential generic parameter and implement the single `transmogrify` method by passing `this` to the function.

```java title="UserProfile.java"
import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Transmogrifyable;
import java.util.Objects;
import java.util.function.Function;

public record UserProfile(String username, String email, int loyaltyPoints)
        implements Transmogrifyable<UserProfile> {

    @Override
    public <T> @NonNull T transmogrify(final @NonNull Function<? super UserProfile, ? extends T> transmogrifier) {
        Objects.requireNonNull(transmogrifier, "transmogrifier must not be null");
        return Objects.requireNonNull(transmogrifier.apply(this), "transmogrifier result must not be null");
    }
}
```

---

## Practical Examples

<Tabs>
  <TabItem label="DTO Mapping">
    ```java title="MappingExample.java"
    public class MappingExample {
        public static void main(String[] args) {
            UserProfile profile = new UserProfile("ada_lovelace", "ada@example.org", 1500);

            // Fluent conversion to an external DTO
            UserSummaryDto dto = profile.transmogrify(p ->
                new UserSummaryDto(p.username(), p.loyaltyPoints() > 1000 ? "VIP" : "Standard")
            );

            System.out.println(dto.status()); // VIP
        }
    }

    record UserSummaryDto(String username, String status) {}
    ```
  </TabItem>
  <TabItem label="JSON / XML Serialization">
    ```java title="SerializationExample.java"
    public class SerializationExample {
        public static void main(String[] args) {
            UserProfile profile = new UserProfile("ada", "ada@domain.tld", 200);

            // Transform directly into an HTTP response string or JSON
            String jsonPayload = profile.transmogrify(p -> String.format(
                "{\"user\":\"%s\",\"email\":\"%s\"}", p.username(), p.email()
            ));

            System.out.println(jsonPayload);
        }
    }
    ```
  </TabItem>
  <TabItem label="Function Reference Transformation">
    ```java title="PipelineExample.java"
    public class PipelineExample {
        public static void main(String[] args) {
            UserProfile profile = new UserProfile("grace_hopper", "grace@navy.mil", 9000);

            // Using static method reference for clean, readable pipelines
            String formattedAudit = profile.transmogrify(AuditFormatter::formatForAuditLog);
            System.out.println(formattedAudit);
        }
    }

    class AuditFormatter {
        public static String formatForAuditLog(UserProfile u) {
            return "[AUDIT] User: " + u.username() + " (Points: " + u.loyaltyPoints() + ")";
        }
    }
    ```
  </TabItem>
</Tabs>

---

## Summary of Best Practices

:::tip[Guidelines]
* **Enforce Null-Safety:** Always check both that the `transmogrifier` is non-null and that the resulting `T` is non-null.
* **Keep Models Focused:** Use `transmogrify` with external mappers or converters instead of bloating core domain entities with format-specific methods.
:::
