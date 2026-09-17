---
title: SemVer
description: Formal API reference, parsing rules, precedence comparisons, immutability, and builder patterns of SemVer in Foomp.
---

`org.quurz.foomp.base.misc.SemVer`

`SemVer` is an immutable, type-safe representation of a Semantic Version conforming strictly to the [SemVer 2.0.0 specification](https://semver.org/).

```java
public class SemVer implements Serializable, Comparable<SemVer>, Echo
```

A semantic version consists of three mandatory non-negative numeric components (`MAJOR.MINOR.PATCH`) and two optional string identifiers wrapped in [`Maybe<String>`](/reference/base/util/maybe/): pre-release tags and build metadata.

---

## Anatomy of a SemVer

$$\underbrace{\text{MAJOR}}_{\ge 0} \,.\, \underbrace{\text{MINOR}}_{\ge 0} \,.\, \underbrace{\text{PATCH}}_{\ge 0} \,-\, \underbrace{\text{Pre-Release}}_{\text{optional}} \,+\, \underbrace{\text{Build Metadata}}_{\text{optional}}$$

* **MAJOR:** Incompatible API / breaking changes.
* **MINOR:** Backwards-compatible new features.
* **PATCH:** Backwards-compatible bug fixes.
* **Pre-Release:** Optional pre-release identifier (e.g. `alpha`, `beta.1`, `rc.2`).
* **Build Metadata:** Optional build diagnostic info (e.g. `build.20241029`, `sha.1a2b3c`).

---

## Factory & Parsing Methods

### 1. `semVer(major, minor, patch)`
```java
public static SemVer semVer(final int major, final int minor, final int patch)
```
Creates a release version with only mandatory numeric components. Pre-release and build metadata are absent (`Maybe.none()`).
* **Throws:** `IllegalArgumentException` if `major`, `minor`, or `patch` is negative ($< 0$).

### 2. `parseSemVer(version)`
```java
public static SemVer parseSemVer(@NonNull final String version)
```
Parses a SemVer 2.0.0 formatted string (e.g. `"1.2.3-beta.1+20241029"`).
* **Throws:** `NullPointerException` if `version` is `null`.
* **Throws:** `IllegalArgumentException` if the string does not strictly match the SemVer 2.0.0 specification or contains invalid numbers.

### 3. `semVerBuilder()`
```java
public static SemVerBuilder semVerBuilder()
```
Returns a fluent builder initialized with defaults (`0.0.0`, no pre-release, no build metadata).

---

## Constructors & Accessors

### Constructors
```java
public SemVer(int major, int minor, int patch, String preRelease, String buildMetadata)
public SemVer(int major, int minor, int patch, Maybe<String> preRelease, Maybe<String> buildMetadata)
```

### Component Getters
* `int getMajor()` – Returns the major version number.
* `int getMinor()` – Returns the minor version number.
* `int getPatch()` – Returns the patch version number.
* `Maybe<String> getPreRelease()` – Returns `Maybe.some(tag)` or `Maybe.none()`.
* `Maybe<String> getBuildMetadata()` – Returns `Maybe.some(build)` or `Maybe.none()`.

---

## Immutability & Version Evolution

Because `SemVer` instances are immutable, all modification methods return newly allocated instances:

| Method | Description | Example ($1.2.3\text{-beta}$) |
| :--- | :--- | :--- |
| `incrementMajor()` | Increments `major` by 1; resets `minor` and `patch` to 0; preserves tags. | $2.0.0\text{-beta}$ |
| `incrementMinor()` | Increments `minor` by 1; resets `patch` to 0; preserves tags. | $1.3.0\text{-beta}$ |
| `incrementPatch()` | Increments `patch` by 1; preserves tags. | $1.2.4\text{-beta}$ |
| `withPreRelease(tag)` | Sets or updates pre-release tag. | $1.2.3\text{-rc.1}$ |
| `withoutPreRelease()` | Removes pre-release tag. | $1.2.3$ |
| `withBuildMetadata(meta)` | Sets or updates build metadata. | $1.2.3\text{-beta+build.99}$ |
| `withoutBuildMetadata()` | Removes build metadata. | $1.2.3\text{-beta}$ |

---

## Precedence & Equality Rules

### Precedence Comparison (`Comparable<SemVer>`)

Precedence determines the ordering of versions in sorting and version constraint resolutions:

1. **Numeric Hierarchy:** Compare `MAJOR`, then `MINOR`, then `PATCH` numerically from left to right.
2. **Pre-Release vs Normal:** A normal release has **higher precedence** than a pre-release version with the same triple ($1.0.0 > 1.0.0\text{-alpha}$).
3. **Pre-Release Ordering:** When both versions have pre-release tags, they are compared lexicographically by dot-separated identifiers.
4. **Build Metadata Ignored:** As mandated by SemVer 2.0.0 §10, build metadata is completely **ignored** during precedence comparisons:
   $$\text{1.0.0+build.1} \equiv \text{1.0.0+build.2} \quad (\text{compareTo} == 0)$$

### Object Equality (`equals` & `hashCode`)

Two `SemVer` objects are equal via `.equals()` if and only if **all five components** (`major`, `minor`, `patch`, `preRelease`, and `buildMetadata`) match exactly.

### String Conversion (`echo()` vs `toString()`)

* `echo()` (from [`Echo`](/reference/base/types/echo/)): Produces a standard, spec-compliant SemVer string (e.g. `"1.2.3-alpha+20241029"`).
* `toString()`: Returns a detailed diagnostic debugging representation (e.g. `SemVer[major=1, minor=2, patch=3, preRelease=Some(alpha), buildMetadata=Some(20241029)]`).

---

## See Also

* [Guide: Managing Versions with SemVer](/guides/base/misc/semver/) – Practical recipes for version bumping, sorting, and parsing.
* [`Maybe<A>` Reference](/reference/base/util/maybe/) – Container used for optional pre-release and build metadata.
* [Full JavaDoc: `SemVer`](/api/base/foomp.base/org/quurz/foomp/base/misc/SemVer.html)
