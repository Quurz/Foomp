package org.quurz.foomp.base.localisation;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.file.Path;
import java.time.Duration;
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

    /**
     * <div>
     *     <p>
     *         Private constructor to prevent instantiation of this utility class.
     *     </p>
     * </div>
     */
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
     *         Indicates that the specified path does not represent a directory.
     *     </p>
     * </div>
     *
     * @param path the path that is not a directory; must not be {@code null}
     * @return a formatted message (never {@code null})
     *
     * @throws NullPointerException if {@code path} is {@code null}
     *
     * @since 1.0.0
     */
    public static String notADirectory(final @NonNull String path) {
        Objects.requireNonNull(path);
        return String.format(RESOURCE_BUNDLE.getString("NOT_A_DIRECTORY"), path);
    }

    /**
     * <div>
     *     <p>
     *         Indicates that the specified path does not represent a directory.
     *     </p>
     * </div>
     *
     * @param path the path that is not a directory; must not be {@code null}
     * @return a formatted message (never {@code null})
     *
     * @throws NullPointerException if {@code path} is {@code null}
     *
     * @since 1.0.0
     */
    public static String notADirectory(final @NonNull Path path) {
        Objects.requireNonNull(path);
        return notADirectory(path.toString());
    }

    /**
     * <div>
     *     <p>
     *         Indicates that the specified path is not readable.
     *     </p>
     * </div>
     *
     * @param path the path that is not readable; must not be {@code null}
     * @return a formatted message (never {@code null})
     *
     * @throws NullPointerException if {@code path} is {@code null}
     *
     * @since 1.0.0
     */
    public static String notReadable(final @NonNull String path) {
        Objects.requireNonNull(path);
        return String.format(RESOURCE_BUNDLE.getString("NOT_READABLE"), path);
    }

    /**
     * <div>
     *     <p>
     *         Indicates that the specified path is not readable.
     *     </p>
     * </div>
     *
     * @param path the path that is not readable; must not be {@code null}
     * @return a formatted message (never {@code null})
     *
     * @throws NullPointerException if {@code path} is {@code null}
     *
     * @since 1.0.0
     */
    public static String notReadable(final @NonNull Path path) {
        Objects.requireNonNull(path);
        return notReadable(path.toString());
    }

    /**
     * <div>
     *     <p>
     *         Indicates that the specified path is not writable.
     *     </p>
     * </div>
     *
     * @param path the path that is not writable; must not be {@code null}
     * @return a formatted message (never {@code null})
     *
     * @throws NullPointerException if {@code path} is {@code null}
     *
     * @since 1.0.0
     */
    public static String notWritable(final @NonNull String path) {
        Objects.requireNonNull(path);
        return String.format(RESOURCE_BUNDLE.getString("NOT_WRITABLE"), path);
    }

    /**
     * <div>
     *     <p>
     *         Indicates that the specified path is not writable.
     *     </p>
     * </div>
     *
     * @param path the path that is not writable; must not be {@code null}
     * @return a formatted message (never {@code null})
     *
     * @throws NullPointerException if {@code path} is {@code null}
     *
     * @since 1.0.0
     */
    public static String notWritable(final @NonNull Path path) {
        Objects.requireNonNull(path);
        return notWritable(path.toString());
    }

    /**
     * <div>
     *     <p>
     *         Indicates that the specified path does not represent a regular file.
     *     </p>
     * </div>
     *
     * @param path the path that is not a regular file; must not be {@code null}
     * @return a formatted message (never {@code null})
     *
     * @throws NullPointerException if {@code path} is {@code null}
     *
     * @since 1.0.0
     */
    public static String notARegularFile(final @NonNull String path) {
        Objects.requireNonNull(path);
        return String.format(RESOURCE_BUNDLE.getString("NOT_A_REGULAR_FILE"), path);
    }

    /**
     * <div>
     *     <p>
     *         Indicates that the specified path does not represent a regular file.
     *     </p>
     * </div>
     *
     * @param path the path that is not a regular file; must not be {@code null}
     * @return a formatted message (never {@code null})
     *
     * @throws NullPointerException if {@code path} is {@code null}
     *
     * @since 1.0.0
     */
    public static String notARegularFile(final @NonNull Path path) {
        Objects.requireNonNull(path);
        return notARegularFile(path.toString());
    }

    /**
     * <div>
     *     <p>
     *         Indicates that the specified class is not an interface.
     *     </p>
     * </div>
     *
     * @param clazz the class that is not an interface; must not be {@code null}
     * @return a formatted message (never {@code null})
     *
     * @throws NullPointerException if {@code clazz} is {@code null}
     *
     * @since 1.0.0
     */
    public static String notAnInterface(final @NonNull Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return String.format(RESOURCE_BUNDLE.getString("NOT_AN_INTERFACE"), clazz.getCanonicalName());
    }

    /**
     * <div>
     *     <p>
     *         Indicates that the specified class is not a concrete class.
     *     </p>
     * </div>
     *
     * @param clazz the class that is not a concrete class; must not be {@code null}
     * @return a formatted message (never {@code null})
     *
     * @throws NullPointerException if {@code clazz} is {@code null}
     *
     * @since 1.0.0
     */
    public static String notAConcreteClass(final @NonNull Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return String.format(RESOURCE_BUNDLE.getString("NOT_A_CONCRETE_CLASS"), clazz.getCanonicalName());
    }

    /**
     * <div>
     *     <p>
     *         Indicates an illegal argument.
     *     </p>
     * </div>
     *
     * @param argumentName the argument name; must not be {@code null}
     * @return the formatted message (never {@code null})
     *
     * @throws NullPointerException if {@code argumentName} is {@code null}
     *
     * @since 1.0.0
     */
    public static String illegalArgument(final @NonNull String argumentName) {
        Objects.requireNonNull(argumentName);
        return String.format(RESOURCE_BUNDLE.getString("ILLEGAL_ARGUMENT"), argumentName);
    }

    /**
     * <div>
     *     <p>
     *         Indicates that an argument cannot be cast to the specified class.
     *     </p>
     * </div>
     *
     * @param argumentName the argument name; must not be {@code null}
     * @param clazz        the target class; must not be {@code null}
     * @return the formatted message (never {@code null})
     *
     * @throws NullPointerException if any argument is {@code null}
     *
     * @since 1.0.0
     */
    public static String cantCast(final @NonNull String argumentName,
                                  final @NonNull Class<?> clazz) {
        Objects.requireNonNull(argumentName);
        Objects.requireNonNull(clazz);
        return String.format(RESOURCE_BUNDLE.getString("CANT_CAST"), argumentName, clazz.getCanonicalName());
    }

    /**
     * <div>
     *     <p>
     *         Indicates that the major version must be non-negative.
     *     </p>
     * </div>
     *
     * @return the formatted message (never {@code null})
     *
     * @since 1.0.0
     */
    public static @NonNull String majorVersionNegative() {
        return RESOURCE_BUNDLE.getString("MAJOR_VERSION_NEGATIVE");
    }

    /**
     * <div>
     *     <p>
     *         Indicates that the minor version must be non-negative.
     *     </p>
     * </div>
     *
     * @return the formatted message (never {@code null})
     *
     * @since 1.0.0
     */
    public static @NonNull String minorVersionNegative() {
        return RESOURCE_BUNDLE.getString("MINOR_VERSION_NEGATIVE");
    }

    /**
     * <div>
     *     <p>
     *         Indicates that the patch version must be non-negative.
     *     </p>
     * </div>
     *
     * @return the formatted message (never {@code null})
     *
     * @since 1.0.0
     */
    public static @NonNull String patchVersionNegative() {
        return RESOURCE_BUNDLE.getString("PATCH_VERSION_NEGATIVE");
    }

    /**
     * <div>
     *     <p>
     *         Indicates that the given version string does not conform to the SemVer format.
     *     </p>
     * </div>
     *
     * @param format the invalid version string; must not be {@code null}
     * @return the formatted message (never {@code null})
     *
     * @throws NullPointerException if {@code format} is {@code null}
     *
     * @since 1.0.0
     */
    public static @NonNull String invalidSemVerFormat(final @NonNull String format) {
        Objects.requireNonNull(format);
        return String.format(RESOURCE_BUNDLE.getString("INVALID_SEMVER_FORMAT"), format);
    }

    /**
     * <div>
     *     <p>
     *         Indicates that a numeric component within a version string is invalid.
     *     </p>
     * </div>
     *
     * @param version the invalid numeric component or version substring; must not be {@code null}
     * @return the formatted message (never {@code null})
     *
     * @throws NullPointerException if {@code version} is {@code null}
     *
     * @since 1.0.0
     */
    public static @NonNull String invalidNumericComponentInVersion(final @NonNull String version) {
        Objects.requireNonNull(version);
        return String.format(RESOURCE_BUNDLE.getString("INVALID_NUMERIC_COMPONENT_IN_VERSION"), version);
    }

    /**
     * <div>
     *     <p>
     *         Indicates that the shutdown hook registry has already been installed.
     *     </p>
     * </div>
     *
     * @return the formatted message (never {@code null})
     *
     * @since 1.0.0
     */
    public static @NonNull String shutdownHookRegistryAlreadyInstalled() {
        return RESOURCE_BUNDLE.getString("SHUTDOWN_HOOK_REGISTRY_ALREADY_INSTALLED");
    }

    /**
     * <div>
     *     <p>
     *         Indicates that the shutdown hook registry was installed with a given number of hooks.
     *     </p>
     * </div>
     *
     * @param numberOfHooks the number of installed shutdown hooks
     * @return the formatted message (never {@code null})
     *
     * @since 1.0.0
     */
    public static @NonNull String shutdownHookRegistryInstalled(final int numberOfHooks) {
        return String.format(RESOURCE_BUNDLE.getString("SHUTDOWN_HOOK_REGISTRY_INSTALLED"), numberOfHooks);
    }

    /**
     * <div>
     *     <p>
     *         Indicates that the execution of registered shutdown hooks is starting.
     *     </p>
     * </div>
     *
     * @param numberOfHooks the number of shutdown hooks to execute
     * @return the formatted message (never {@code null})
     *
     * @since 1.0.0
     */
    public static @NonNull String executingShutdownHooks(final int numberOfHooks) {
        return String.format(RESOURCE_BUNDLE.getString("EXECUTING_SHUTDOWN_HOOKS"), numberOfHooks);
    }

    /**
     * <div>
     *     <p>
     *         Indicates that all shutdown hooks have completed execution.
     *     </p>
     * </div>
     *
     * @return the formatted message (never {@code null})
     *
     * @since 1.0.0
     */
    public static @NonNull String allShutdownHooksCompleted() {
        return RESOURCE_BUNDLE.getString("ALL_SHUTDOWN_HOOKS_COMPLETED");
    }

    /**
     * <div>
     *     <p>
     *         Indicates that a specific shutdown hook is being executed with its priority and timeout.
     *     </p>
     * </div>
     *
     * @param name     the name of the shutdown hook; must not be {@code null}
     * @param priority the priority of the shutdown hook
     * @param timeout  the timeout configured for the shutdown hook; must not be {@code null}
     * @return the formatted message (never {@code null})
     *
     * @throws NullPointerException if any argument is {@code null}
     *
     * @since 1.0.0
     */
    public static @NonNull String executingShutdownHook(final @NonNull String name,
                                                        final int priority,
                                                        final @NonNull Duration timeout) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(timeout);
        return String.format(RESOURCE_BUNDLE.getString("EXECUTING_SHUTDOWN_HOOK"), name, priority, timeout);
    }

    /**
     * <div>
     *     <p>
     *         Indicates that a specific shutdown hook completed successfully.
     *     </p>
     * </div>
     *
     * @param name the name of the completed shutdown hook; must not be {@code null}
     * @return the formatted message (never {@code null})
     *
     * @throws NullPointerException if {@code name} is {@code null}
     *
     * @since 1.0.0
     */
    public static @NonNull String shutdownHookCompleted(final @NonNull String name) {
        Objects.requireNonNull(name);
        return String.format(RESOURCE_BUNDLE.getString("SHUTDOWN_HOOK_COMPLETED"), name);
    }

    /**
     * <div>
     *     <p>
     *         Indicates that a specific shutdown hook timed out.
     *     </p>
     * </div>
     *
     * @param name the name of the timed-out shutdown hook; must not be {@code null}
     * @return the formatted message (never {@code null})
     *
     * @throws NullPointerException if {@code name} is {@code null}
     *
     * @since 1.0.0
     */
    public static @NonNull String shutdownHookTimedOut(final @NonNull String name) {
        Objects.requireNonNull(name);
        return String.format(RESOURCE_BUNDLE.getString("SHUTDOWN_HOOK_TIMED_OUT"), name);
    }

    /**
     * <div>
     *     <p>
     *         Indicates that a specific shutdown hook failed during execution.
     *     </p>
     * </div>
     *
     * @param name         the name of the failed shutdown hook; must not be {@code null}
     * @param errorMessage the error message describing the failure; must not be {@code null}
     * @return the formatted message (never {@code null})
     *
     * @throws NullPointerException if any argument is {@code null}
     *
     * @since 1.0.0
     */
    public static @NonNull String shutdownHookFailed(final @NonNull String name,
                                                     final @NonNull String errorMessage) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(errorMessage);
        return String.format(RESOURCE_BUNDLE.getString("SHUTDOWN_HOOK_FAILED"), name, errorMessage);
    }

    /**
     * <div>
     *     <p>
     *         Indicates that a shutdown hook execution was interrupted.
     *     </p>
     * </div>
     *
     * @param name the name of the interrupted shutdown hook; must not be {@code null}
     * @return the formatted message (never {@code null})
     *
     * @throws NullPointerException if {@code name} is {@code null}
     *
     * @since 1.0.0
     */
    public static @NonNull String shutdownHookInterrupted(final @NonNull String name) {
        Objects.requireNonNull(name);
        return String.format(RESOURCE_BUNDLE.getString("SHUTDOWN_HOOK_INTERRUPTED"), name);
    }

    /**
     * <div>
     *     <p>
     *         Indicates that an unexpected exception occurred while executing a shutdown hook.
     *     </p>
     * </div>
     *
     * @param name         the name of the shutdown hook; must not be {@code null}
     * @param errorMessage the error message of the unexpected exception; must not be {@code null}
     * @return the formatted message (never {@code null})
     *
     * @throws NullPointerException if any argument is {@code null}
     *
     * @since 1.0.0
     */
    public static @NonNull String unexpectedExceptionInShutdownHook(final @NonNull String name,
                                                                    final @NonNull String errorMessage) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(errorMessage);
        return String.format(RESOURCE_BUNDLE.getString("UNEXPECTED_EXCEPTION_IN_SHUTDOWN_HOOK"), name, errorMessage);
    }

    /**
     * <div>
     *     <p>
     *         Indicates that the shutdown hook executor did not terminate in time during shutdown.
     *     </p>
     * </div>
     *
     * @return the formatted message (never {@code null})
     *
     * @since 1.0.0
     */
    public static @NonNull String shutdownHookExecutorNotTerminatedInTime() {
        return RESOURCE_BUNDLE.getString("SHUTDOWN_HOOK_EXECUTOR_NOT_TERMINATED_IN_TIME");
    }

    /**
     * <div>
     *     <p>
     *         Indicates that the requested operation is not supported.
     *     </p>
     * </div>
     *
     * @return the message (never {@code null})
     *
     * @since 1.0.0
     */
    public static String unsupportedOperation() {
        return RESOURCE_BUNDLE.getString("UNSUPPORTED_OPERATION");
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
