---
title: Base Localisation Overview
description: A guide to the message localisation system in the Foomp Base module.
---

The `org.quurz.foomp.base.localisation` package provides a structured and type-safe way to handle user-facing and internal error messages. It ensures consistency across the library and supports internationalisation (i18n).

## BaseMessages

The `BaseMessages` class is the central entry point for obtaining localised strings. It wraps a `ResourceBundle` and provides static factory methods for common message types used in validations and exception handling.

### Key Features

- **Type-Safe Access**: Instead of using string keys directly, developers use named methods like `nullValue(String)` or `notFound(Object)`.
- **Parameterised Messages**: Many methods support parameters (e.g., argument names, container names, or indices) to provide more context in error messages.
- **Resource Bundle Integration**: It automatically loads messages from `BaseMessages.properties` based on the system's default `Locale`.
- **Null Safety**: All methods perform `null` checks on their arguments to ensure the integrity of the message generation process.

### Common Message Types

| Method | Description | Example Output |
| :--- | :--- | :--- |
| `nullValue(String)` | For illegal `null` arguments. | "Argument 'name' must not be null" |
| `noValuePresent()` | For empty containers/optionals. | "No value present" |
| `notFound(Object)` | For missing elements. | "Element '42' not found" |
| `emptyCollection(String)` | For mandatory non-empty collections. | "Collection 'items' must not be empty" |
| `negativeValue(String)` | For numeric range validation. | "Argument 'size' must not be negative" |
| `illegalIntervalBounds(...)` | For invalid ranges. | "Lower bound '10' must be less than or equal to upper bound '5'" |

## How it Works

The system relies on a standard Java `ResourceBundle` mechanism.

1.  **Properties File**: A file named `BaseMessages.properties` (and its locale-specific variants like `BaseMessages_de.properties`) contains the actual translations.
2.  **Factory Methods**: Each method in `BaseMessages` corresponds to a key in the properties file and uses `String.format` to inject arguments if necessary.

### Example Usage

```java
public void setAge(int age) {
    if (age < 0) {
        throw new IllegalArgumentException(BaseMessages.negativeValue("age"));
    }
    this.age = age;
}
```

## Internal Surprise Handling

The method `notSupposedToHappen()` provides a standardized message for code paths that are theoretically unreachable but included for safety.

> "This came as an absolute surprise =O"

## Summary Table

| Component | Responsibility |
| :--- | :--- |
| `BaseMessages` | Static API for retrieving and formatting localised strings. |
| `BaseMessages.properties` | The default English message definitions. |
| `ResourceBundle` | The underlying Java mechanism for locale-sensitive data. |

## Why use BaseMessages?

Using a centralised localisation class instead of hardcoded strings offers several benefits:
1.  **Consistency**: Identical errors across different parts of the library will always use the same wording.
2.  **Maintainability**: Fixing a typo or rephrasing a message only needs to be done in one place.
3.  **I18n Readiness**: Support for new languages can be added by simply providing a new `.properties` file, without changing a single line of code.
