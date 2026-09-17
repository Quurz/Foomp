---
title: Managing Semantic Versions with SemVer
description: Practical guide, parsing recipes, version bumping workflows, and sorting strategies using SemVer in Foomp.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

`SemVer` provides an immutable, spec-compliant model for Semantic Versioning 2.0.0. It is suitable for release tooling, dependency management, artifact versioning, and feature toggles.

---

## 1. Creating and Parsing SemVer Instances

<Tabs>
  <TabItem label="Parsing Strings">
    ```java title="ParseExample.java"
    import org.quurz.foomp.base.misc.SemVer;

    // Standard release
    SemVer v1 = SemVer.parseSemVer("1.2.3");

    // Pre-release version
    SemVer v2 = SemVer.parseSemVer("2.0.0-rc.1");

    // With build metadata
    SemVer v3 = SemVer.parseSemVer("1.0.0-beta.2+build.450");
    ```
  </TabItem>
  <TabItem label="Direct Factory">
    ```java title="FactoryExample.java"
    import static org.quurz.foomp.base.misc.SemVer.semVer;

    // Creates "3.1.4"
    SemVer version = semVer(3, 1, 4);
    ```
  </TabItem>
  <TabItem label="Fluent Builder">
    ```java title="BuilderExample.java"
    import static org.quurz.foomp.base.misc.SemVer.semVerBuilder;

    SemVer version = semVerBuilder()
        .major(2)
        .minor(4)
        .patch(0)
        .preRelease("alpha.1")
        .buildMetadata("sha.8f2a1b")
        .build(); // "2.4.0-alpha.1+sha.8f2a1b"
    ```
  </TabItem>
</Tabs>

---

## 2. Release Bumping Workflows

`SemVer` instances are immutable. Version bumping operations return new instances and enforce SemVer 2.0.0 rules (e.g., bumping major resets minor and patch to 0):

```java title="VersionLifecycle.java"
import org.quurz.foomp.base.misc.SemVer;
import static org.quurz.foomp.base.misc.SemVer.semVer;

public class ReleaseWorkflow {
    public static void main(String[] args) {
        SemVer current = semVer(1, 4, 2);

        // Bugfix release: 1.4.3
        SemVer patch = current.incrementPatch();

        // Feature release: 1.5.0
        SemVer minor = current.incrementMinor();

        // Breaking change: 2.0.0
        SemVer major = current.incrementMajor();

        // Prepare release candidate: 2.0.0-rc.1
        SemVer rc = major.withPreRelease("rc.1");

        // Promote to final release: 2.0.0
        SemVer finalRelease = rc.withoutPreRelease();
        
        System.out.println("Final release: " + finalRelease.echo()); // Prints "2.0.0"
    }
}
```

---

## 3. Sorting and Version Precedence

Because `SemVer` implements `Comparable<SemVer>`, lists and streams sort according to standard SemVer precedence:

```java title="VersionSorting.java"
import org.quurz.foomp.base.misc.SemVer;
import java.util.List;
import java.util.stream.Collectors;

public class VersionCatalog {
    public static void main(String[] args) {
        List<SemVer> versions = List.of(
            SemVer.parseSemVer("1.0.0"),
            SemVer.parseSemVer("1.0.0-alpha"),
            SemVer.parseSemVer("2.0.0"),
            SemVer.parseSemVer("1.0.0-beta"),
            SemVer.parseSemVer("1.0.0-alpha.1"),
            SemVer.parseSemVer("1.0.0+build.10") // Equal precedence to 1.0.0
        );

        List<String> sorted = versions.stream()
            .sorted()
            .map(SemVer::echo)
            .collect(Collectors.toList());

        // Correct SemVer precedence order:
        // [1.0.0-alpha, 1.0.0-alpha.1, 1.0.0-beta, 1.0.0, 1.0.0+build.10, 2.0.0]
        System.out.println(sorted);
    }
}
```

:::tip[Precedence vs Equality]
* Use `.compareTo(other) > 0` to check if a version is newer than another (ignoring build metadata).
* Use `.equals(other)` when comparing artifact builds where metadata matters.
:::
