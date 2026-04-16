package org.quurz.foomp.base.localisation;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * <div>
 *     <p>
 *         Localisation of base-layer messages used across the library.
 *     </p>
 *     <p>
 *         Provides a central access point to a {@link ResourceBundle} with strongly-named
 *         message factory methods for common validation and error texts.
 *     </p>
 *     <p>
 *         Contract: unless stated otherwise, arguments must not be {@code null}.
 *         Returned messages are never {@code null}.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 * @author Alexander Schell
 */
public final class BaseMessages {

    private static final ResourceBundle RESOURCE_BUNDLE
        = ResourceBundle.getBundle("BaseMessages", Locale.getDefault());

    private BaseMessages() {}

    /*
        Messages for Foomp-Base
     */

    /**
     * <div>
     *     <p>
     *         Indicates that no value is present.
     *     </p>
     * </div>
     *
     * @return the corresponding message (never {@code null})
     *
     * @since 1.0.0
     */
    public static String noValuePresent() {
        return RESOURCE_BUNDLE.getString("NO_VALUE_PRESENT");
    }

    /**
     * <div>
     *     <p>
     *         Indicates that no value is present in the given container.
     *     </p>
     * </div>
     *
     * @param containerName the container name; must not be {@code null}
     * @return the formatted message (never {@code null})
     *
     * @throws NullPointerException if {@code containerName} is {@code null}
     *
     * @since 1.0.0
     */
    public static String noValuePresentIn(final String containerName) {
        Objects.requireNonNull(containerName);
        return String.format(RESOURCE_BUNDLE.getString("NO_VALUE_PRESENT_IN"), containerName);
    }

    /**
     * <div>
     *     <p>
     *         Indicates that the requested object could not be found.
     *     </p>
     * </div>
     *
     * @param object the object that was not found; must not be {@code null}
     * @return the formatted message (never {@code null})
     *
     * @throws NullPointerException if {@code object} is {@code null}
     *
     * @since 1.0.0
     */
    public static String notFound(final @NonNull Object object) {
        Objects.requireNonNull(object);
        return String.format(RESOURCE_BUNDLE.getString("NOT_FOUND"), object);
    }

    /**
     * <div>
     *     <p>
     *         Indicates that the requested object could not be found inside a specific container.
     *     </p>
     * </div>
     *
     * @param object        the object that was not found; must not be {@code null}
     * @param containerName the container name; must not be {@code null}
     * @return the formatted message (never {@code null})
     *
     * @throws NullPointerException if any argument is {@code null}
     *
     * @since 1.0.0
     */
    public static String notFoundIn(final @NonNull Object object,
                                    final @NonNull String containerName) {
        Objects.requireNonNull(object);
        Objects.requireNonNull(containerName);
        return String.format(RESOURCE_BUNDLE.getString("NOT_FOUND_IN"), object, containerName);
    }

    /**
     * <div>
     *     <p>
     *         Null arguments are not allowed.
     *     </p>
     * </div>
     *
     * @param argumentName the name of the argument that was {@code null}; must not be {@code null}
     * @return the formatted message (never {@code null})
     *
     * @throws NullPointerException if {@code argumentName} is {@code null}
     *
     * @since 1.0.0
     */
    public static String nullValue(@NonNull final String argumentName) {
        Objects.requireNonNull(argumentName);
        return String.format(RESOURCE_BUNDLE.getString("NULL_ARGUMENT"), argumentName);
    }

    /**
     * <div>
     *     <p>
     *         An empty collection is not useful.
     *     </p>
     * </div>
     *
     * @param collectionName the collection name; must not be {@code null}
     * @return the formatted message (never {@code null})
     *
     * @throws NullPointerException if {@code collectionName} is {@code null}
     *
     * @since 1.0.0
     */
    public static String emptyCollection(final String collectionName) {
        Objects.requireNonNull(collectionName);
        return String.format(RESOURCE_BUNDLE.getString("EMPTY_COLLECTION"), collectionName);
    }

    /**
     * <div>
     *     <p>
     *         The collection contains a {@code null} element at the specified index.
     *     </p>
     * </div>
     *
     * @param containerName the collection name; must not be {@code null}
     * @param index         the index of the {@code null} element
     * @return the formatted message (never {@code null})
     *
     * @throws NullPointerException if {@code containerName} is {@code null}
     *
     * @since 1.0.0
     */
    public static String nullElementInAt(final String containerName,
                                         final int index) {
        Objects.requireNonNull(containerName);
        return String.format(RESOURCE_BUNDLE.getString("NULL_ELEMENT_IN_AT"), containerName, index);
    }

    /**
     * <div>
     *     <p>
     *         The collection contains a {@code null} element.
     *     </p>
     * </div>
     *
     * @param containerName the collection name; must not be {@code null}
     * @return the formatted message (never {@code null})
     *
     * @throws NullPointerException if {@code containerName} is {@code null}
     *
     * @since 1.0.0
     */
    public static String nullElementIn(final String containerName) {
        Objects.requireNonNull(containerName);
        return String.format(RESOURCE_BUNDLE.getString("NULL_ELEMENT_IN"), containerName);
    }

    /**
     * <div>
     *     <p>
     *         The result must not be {@code null}.
     *     </p>
     * </div>
     *
     * @return the message (never {@code null})
     *
     * @since 1.0.0
     */
    public static String nullResult() {
        return RESOURCE_BUNDLE.getString("NULL_RESULT");
    }

    /**
     * <div>
     *     <p>
     *         The result produced by the given method must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param methodName the method name; must not be {@code null}
     * @return the formatted message (never {@code null})
     *
     * @throws NullPointerException if {@code methodName} is {@code null}
     *
     * @since 1.0.0
     */
    public static String nullResultFrom(final @NonNull String methodName) {
        Objects.requireNonNull(methodName);
        return String.format(RESOURCE_BUNDLE.getString("NULL_RESULT_FROM"), methodName);
    }

    /**
     * <div>
     *     <p>
     *         A supplier must never yield {@code null}.
     *     </p>
     * </div>
     *
     * @return the message (never {@code null})
     *
     * @since 1.0.0
     */
    public static String nullSupplied() {
        return RESOURCE_BUNDLE.getString("NULL_SUPPLIED");
    }

    /**
     * <div>
     *     <p>
     *         A supplier must never yield {@code null} (including the supplier’s name for context).
     *     </p>
     * </div>
     *
     * @param supplierName the name of the supplier; must not be {@code null}
     * @return the formatted message (never {@code null})
     *
     * @throws NullPointerException if {@code supplierName} is {@code null}
     *
     * @since 1.0.0
     */
    public static String nullSuppliedFrom(final @NonNull String supplierName) {
        Objects.requireNonNull(supplierName);
        return String.format(RESOURCE_BUNDLE.getString("NULL_SUPPLIED_FROM"), supplierName);
    }

    /**
     * <div>
     *     <p>
     *         The value must not be negative.
     *     </p>
     * </div>
     *
     * @param parameterName the parameter name; must not be {@code null}
     * @return the formatted message (never {@code null})
     *
     * @throws NullPointerException if {@code parameterName} is {@code null}
     *
     * @since 1.0.0
     */
    public static String negativeValue(final @NonNull String parameterName) {
        Objects.requireNonNull(parameterName);
        return String.format(RESOURCE_BUNDLE.getString("NEGATIVE"), parameterName);
    }

    /**
     * <div>
     *     <p>
     *         The value must be positive.
     *     </p>
     * </div>
     *
     * @param parameterName the parameter name; must not be {@code null}
     * @return the formatted message (never {@code null})
     *
     * @throws NullPointerException if {@code parameterName} is {@code null}
     *
     * @since 1.0.0
     */
    public static String nonPositiveValue(final @NonNull String parameterName) {
        Objects.requireNonNull(parameterName);
        return String.format(RESOURCE_BUNDLE.getString("NOT_POSITIVE"), parameterName);
    }

    /**
     * <div>
     *     <p>
     *         The value must not be zero.
     *     </p>
     * </div>
     *
     * @param parameterName the parameter name; must not be {@code null}
     * @return the formatted message (never {@code null})
     *
     * @throws NullPointerException if {@code parameterName} is {@code null}
     *
     * @since 1.0.0
     */
    public static String zeroValue(final @NonNull String parameterName) {
        Objects.requireNonNull(parameterName);
        return String.format(RESOURCE_BUNDLE.getString("ZERO"), parameterName);
    }

    /**
     * <div>
     *     <p>
     *         Generates an error message for inserting a duplicate element into a collection
     *         that disallows duplicates.
     *     </p>
     * </div>
     *
     * @param element the element that already exists; must not be {@code null}
     * @return a formatted message indicating the element already exists (never {@code null})
     *
     * @throws NullPointerException if {@code element} is {@code null}
     *
     * @since 1.0.0
     */
    public static String duplicateElement(final @NonNull Object element) {
        Objects.requireNonNull(element);
        return String.format(RESOURCE_BUNDLE.getString("DUPLICATE_ELEMENT"), element);
    }

    /**
     * <div>
     *     <p>
     *         Indicates illegal interval bounds (lower bound greater than upper bound).
     *     </p>
     * </div>
     *
     * @param lowerBound the lower bound; must not be {@code null}
     * @param upperBound the upper bound; must not be {@code null}
     * @return the formatted message (never {@code null})
     *
     * @throws NullPointerException if any argument is {@code null}
     *
     * @since 1.0.0
     */
    public static String illegalIntervalBounds(final @NonNull Object lowerBound,
                                               final @NonNull Object upperBound) {
        Objects.requireNonNull(lowerBound);
        Objects.requireNonNull(upperBound);
        return String.format(RESOURCE_BUNDLE.getString("ILLEGAL_INTERVAL_BOUNDS"), lowerBound, upperBound);
    }

    /**
     * <div>
     *     <p>
     *         Indicates that something unexpectedly went wrong.
     *     </p>
     * </div>
     *
     * @return the message (never {@code null})
     *
     * @since 1.0.0
     */
    public static String notSupposedToHappen() {
        return RESOURCE_BUNDLE.getString("NOT_SUPPOSED_TO_HAPPEN");
    }

}
