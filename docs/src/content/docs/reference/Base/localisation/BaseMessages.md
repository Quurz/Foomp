---
title: BaseMessages
description: Formal API reference, message catalog, formatting specifications, and localization architecture of BaseMessages in Foomp.
---

`org.quurz.foomp.base.localisation.BaseMessages`

`BaseMessages` is the centralized localization and diagnostic message provider for the `Foomp-Base` module. It acts as a strongly-typed façade over a Java [`ResourceBundle`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/ResourceBundle.html) (`BaseMessages.properties`), ensuring consistent, localized, and well-formatted exception and validation messages across all functional containers, combinators, and data structures in Foomp.

```java
public final class BaseMessages
```

All methods in `BaseMessages` are `public static`, thread-safe, and enforce strict non-null argument contracts.

---

## Architectural Overview

`BaseMessages` loads its bundle via:

```java
ResourceBundle.getBundle("BaseMessages", Locale.getDefault())
```

When building custom domain logic or validating arguments, referencing `BaseMessages` guarantees that exception messages adhere to the standardized formatting patterns used throughout the Foomp ecosystem.

---

## Method Catalog by Category

### 1. Null-Safety & Contract Validation

These methods provide standard messages for boundary checks, precondition enforcement, and `Objects.requireNonNull(...)` calls.

#### `nullValue`
```java
public static String nullValue(@NonNull final String argumentName)
```
Generates a message indicating that a required argument is `null`.
* **Pattern:** `Argument '%s' must not be null`
* **Throws:** `NullPointerException` if `argumentName` is `null`.

#### `nullResult`
```java
public static String nullResult()
```
Generates a generic message indicating that an operation returned `null` unexpectedly.
* **Pattern:** `Result must not be null`

#### `nullResultFrom`
```java
public static String nullResultFrom(@NonNull final String methodName)
```
Generates a message identifying the specific method or transformation that returned `null`.
* **Pattern:** `Result from '%s' must not be null`
* **Throws:** `NullPointerException` if `methodName` is `null`.

#### `nullSupplied`
```java
public static String nullSupplied()
```
Generates a message indicating that a lazy supplier evaluated to `null`.
* **Pattern:** `Supplied value must not be null`

#### `nullSuppliedFrom`
```java
public static String nullSuppliedFrom(@NonNull final String supplierName)
```
Generates a message identifying the specific supplier that produced `null`.
* **Pattern:** `Supplied value from '%s' must not be null`
* **Throws:** `NullPointerException` if `supplierName` is `null`.

#### `nullElementIn`
```java
public static String nullElementIn(final String containerName)
```
Generates a message indicating that a collection contains a forbidden `null` element.
* **Pattern:** `Null-Element in '%s'`
* **Throws:** `NullPointerException` if `containerName` is `null`.

#### `nullElementInAt`
```java
public static String nullElementInAt(final String containerName, final int index)
```
Generates a message indicating that a collection contains a `null` element at a specific index.
* **Pattern:** `Null-Element in '%s' at index %d`
* **Throws:** `NullPointerException` if `containerName` is `null`.

---

### 2. Container State & Element Lookup

These methods describe empty states, missing elements, and lookup failures in containers (`Maybe`, `Sequence`, trees, maps).

#### `noValuePresent`
```java
public static String noValuePresent()
```
Indicates that an unwinding operation failed because the container is empty (e.g. `Maybe.nothing()`).
* **Pattern:** `No value present`

#### `noValuePresentIn`
```java
public static String noValuePresentIn(final String containerName)
```
Indicates that a named container has no value present.
* **Pattern:** `No value present in %s`
* **Throws:** `NullPointerException` if `containerName` is `null`.

#### `notFound`
```java
public static String notFound(@NonNull final Object object)
```
Indicates that a requested element could not be found.
* **Pattern:** `Element '%s' not found`
* **Throws:** `NullPointerException` if `object` is `null`.

#### `notFoundIn`
```java
public static String notFoundIn(@NonNull final Object object, @NonNull final String containerName)
```
Indicates that an element could not be found within a specific named container.
* **Pattern:** `Element '%s' not found in %s`
* **Throws:** `NullPointerException` if any argument is `null`.

---

### 3. Collections, Sets & Interval Bounds

#### `emptyCollection`
```java
public static String emptyCollection(final String collectionName)
```
Indicates that an operation requires a non-empty collection.
* **Pattern:** `Collection '%s' must not be empty`
* **Throws:** `NullPointerException` if `collectionName` is `null`.

#### `duplicateElement`
```java
public static String duplicateElement(@NonNull final Object element)
```
Indicates that an insertion would create an illegal duplicate in a unique collection (e.g., set or tree).
* **Pattern:** `Element '%s' already exists`
* **Throws:** `NullPointerException` if `element` is `null`.

#### `illegalIntervalBounds`
```java
public static String illegalIntervalBounds(@NonNull final Object lowerBound, @NonNull final Object upperBound)
```
Indicates that a lower bound exceeds its upper bound.
* **Pattern:** `Lower bound '%s' must be less than or equal to upper bound '%s'`
* **Throws:** `NullPointerException` if any argument is `null`.

---

### 4. Numeric & Range Preconditions

#### `negativeValue`
```java
public static String negativeValue(@NonNull final String parameterName)
```
Indicates that a parameter was negative ($< 0$) when non-negative ($ \ge 0$) was required.
* **Pattern:** `Argument '%s' must not be negative`
* **Throws:** `NullPointerException` if `parameterName` is `null`.

#### `nonPositiveValue`
```java
public static String nonPositiveValue(@NonNull final String parameterName)
```
Indicates that a parameter was non-positive ($\le 0$) when strictly positive ($> 0$) was required.
* **Pattern:** `Argument '%s' must be positive`
* **Throws:** `NullPointerException` if `parameterName` is `null`.

#### `zeroValue`
```java
public static String zeroValue(@NonNull final String parameterName)
```
Indicates that a parameter was zero when non-zero was required.
* **Pattern:** `Argument '%s' must not be zero`
* **Throws:** `NullPointerException` if `parameterName` is `null`.

---

### 5. File System & Path Validation

These methods accept either `String` or `java.nio.file.Path` parameters.

| Method Signature | Formatted Message Pattern |
| :--- | :--- |
| `notADirectory(String / Path)` | `'%s' is not a directory` |
| `notReadable(String / Path)` | `'%s' is not readable` |
| `notWritable(String / Path)` | `'%s' is not writable` |
| `notARegularFile(String / Path)` | `'%s' is not a regular file` |

* **Throws:** `NullPointerException` if the given path is `null`.

---

### 6. Types, Reflection & Casting

#### `notAnInterface`
```java
public static String notAnInterface(@NonNull final Class<?> clazz)
```
Indicates that a given type was expected to be an interface.
* **Pattern:** `'%s' is not an interface` (uses `clazz.getCanonicalName()`)
* **Throws:** `NullPointerException` if `clazz` is `null`.

#### `notAConcreteClass`
```java
public static String notAConcreteClass(@NonNull final Class<?> clazz)
```
Indicates that a class is abstract or an interface when a concrete class was required.
* **Pattern:** `'%s' is not a concrete class` (uses `clazz.getCanonicalName()`)
* **Throws:** `NullPointerException` if `clazz` is `null`.

#### `cantCast`
```java
public static String cantCast(@NonNull final String argumentName, @NonNull final Class<?> clazz)
```
Indicates that an object could not be safely cast to the target type.
* **Pattern:** `Unable to cast '%s' to '%s'`
* **Throws:** `NullPointerException` if any argument is `null`.

---

### 7. Semantic Versioning Validation

These methods provide standardized error messages when parsing or validating semantic version numbers (`SemVer`).

#### `majorVersionNegative`, `minorVersionNegative`, `patchVersionNegative`
```java
public static @NonNull String majorVersionNegative()
public static @NonNull String minorVersionNegative()
public static @NonNull String patchVersionNegative()
```
Indicates that a major, minor, or patch component was negative when a non-negative integer was required.
* **Patterns:**
  * `Major version must be non-negative`
  * `Minor version must be non-negative`
  * `Patch version must be non-negative`

#### `invalidSemVerFormat`
```java
public static @NonNull String invalidSemVerFormat(final @NonNull String format)
```
Indicates that a version string does not conform to the SemVer specification.
* **Pattern:** `Invalid SemVer format: '%s'`
* **Throws:** `NullPointerException` if `format` is `null`.

#### `invalidNumericComponentInVersion`
```java
public static @NonNull String invalidNumericComponentInVersion(final @NonNull String version)
```
Indicates that a numeric component within a version string is malformed or invalid.
* **Pattern:** `Invalid numeric component in version: '%s'`
* **Throws:** `NullPointerException` if `version` is `null`.

---

### 8. Shutdown Hook Lifecycle & Diagnostics

These methods provide standardized logging and diagnostic messages for the lifecycle of JVM shutdown hooks in `ShutdownHookRegistry`.

| Method Signature | Formatted Message Pattern | Description |
| :--- | :--- | :--- |
| `shutdownHookRegistryAlreadyInstalled()` | `ShutdownHookRegistry already installed` | Registry has already been installed on JVM shutdown. |
| `shutdownHookRegistryInstalled(int numberOfHooks)` | `ShutdownHookRegistry installed with %d hooks` | Registry initialization message with hook count. |
| `executingShutdownHooks(int numberOfHooks)` | `Executing %d shutdown hooks...` | Beginning orderly hook execution. |
| `allShutdownHooksCompleted()` | `All shutdown hooks completed` | All registered shutdown hooks have completed. |
| `executingShutdownHook(String name, int priority, Duration timeout)` | `Executing shutdown hook '%s' (priority: %d, timeout: %s)...` | Trace execution of a single hook with parameters. |
| `shutdownHookCompleted(String name)` | `Shutdown hook '%s' completed` | Successful execution of a single hook. |
| `shutdownHookTimedOut(String name)` | `Shutdown hook '%s' timed out` | Hook exceeded its allotted execution duration. |
| `shutdownHookFailed(String name, String errorMessage)` | `Shutdown hook '%s' failed: %s` | Hook threw an exception or error during execution. |
| `shutdownHookInterrupted(String name)` | `Shutdown hook '%s' was interrupted` | Thread running the hook received an interruption signal. |
| `unexpectedExceptionInShutdownHook(String name, String errorMessage)` | `Unexpected exception in shutdown hook '%s': %s` | Unexpected exception encountered during execution. |
| `shutdownHookExecutorNotTerminatedInTime()` | `Shutdown hook executor did not terminate in time` | Overall thread pool executor shutdown timeout expired. |

---

### 9. General Diagnostic Messages

#### `illegalArgument`
```java
public static String illegalArgument(@NonNull final String argumentName)
```
Indicates a general illegal argument error.
* **Pattern:** `Illegal argument: '%s'`
* **Throws:** `NullPointerException` if `argumentName` is `null`.

#### `unsupportedOperation`
```java
public static String unsupportedOperation()
```
Indicates that the requested method or operation is not supported.
* **Pattern:** `Unsupported operation`

#### `notSupposedToHappen`
```java
public static String notSupposedToHappen()
```
Indicates an invariant violation or unreachable code path.
* **Pattern:** `This came as an absolute surprise =O`

---

## Contract & Guarantees

1. **Non-Null Return:** All methods in `BaseMessages` are guaranteed to return a non-null, fully formatted `String`.
2. **Immediate Fail-Fast:** Parameter validation throws `NullPointerException` immediately if required arguments or template parameters are `null`.
3. **Locale Adaptability:** Reads message definitions dynamically from `BaseMessages.properties` using the JVM's default `Locale`.

---

## See Also

* [Guide: Using BaseMessages in Applications](/guides/base/localisation/basemessages/) – Practical patterns for consistent exception formatting.
* [`Maybe<A>` Reference](/reference/base/util/maybe/) – Functional container using `noValuePresent()`.
* [`Attempt<A>` Reference](/reference/base/util/attempt/) – Safe evaluation wrapper leveraging `BaseMessages`.
* [Full JavaDoc: `BaseMessages`](/api/base/foomp.base/org/quurz/foomp/base/localisation/BaseMessages.html)
