package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.quurz.foomp.base.util.Maybe;
import org.quurz.foomp.base.util.Tuple2;
import org.quurz.foomp.base.util.Util;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Stream;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Maybe.maybeOfNullable;
import static org.quurz.foomp.base.util.Tuple2.tuple2;
import static org.quurz.foomp.base.util.Util.requireNonNullElements;

/**
 * <div>
 *     <p>
 *         A typed argument wrapper that encapsulates both a value and its explicit runtime type.
 *     </p>
 *     <p>
 *         This class is particularly useful for reflective invocations where {@code null} values need to be
 *         passed while preserving the parameter's type at runtime.
 *     </p>
 *     <p>
 *         Example:
 *         <pre>
 *             {@code Argument<String> arg = Argument.argument(String.class, null);}
 *         </pre>
 *     </p>
 * </div>
 *
 * @param <A> the compile-time type of the argument's value
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */

public final class Argument<A> {

    /**
     * <div>
     *     <p>
     *         Creates a new {@link Argument} instance with an explicit type and without a value.
     *     </p>
     *     <p>
     *         Useful when only the parameter type should be provided for a reflective call and the value
     *         is set later or must be passed as {@code null}.
     *     </p>
     * </div>
     *
     * @param type the explicit, non-nullable type of the argument
     * @param <A>  the generic type of the value
     * @return a new {@link Argument} with the given type and no value
     * @throws NullPointerException if {@code type} is {@code null}
     *
     * @since 1.0.0
     */
    public static <A> Argument<A> argument(final @NonNull Class<? extends A> type) {
        Objects.requireNonNull(type, nullValue("type"));
        return argument(type, null);
    }

    /**
     * <div>
     *     <p>
     *         Creates a new {@link Argument} with an explicit type and an optional value.
     *     </p>
     *     <p>
     *         This factory method is the general-purpose variant to set both type and value.
     *         The type must not be {@code null}; the value may be {@code null}.
     *     </p>
     * </div>
     *
     * @param type  the explicit, non-nullable type of the argument
     * @param value the argument value; may be {@code null}
     * @param <A>   the generic type of the value
     * @return a new {@link Argument} with the given type and value
     * @throws NullPointerException if {@code type} is {@code null}
     *
     * @since 1.0.0
     */
    public static <A> Argument<A> argument(final @NonNull Class<? extends A> type,
                                           final @Nullable A value) {
        Objects.requireNonNull(type, nullValue("type"));
        return new Argument<>(type, value);
    }

    /**
     * <div>
     *     <p>
     *         Extracts types and values from an array of {@link Argument} objects.
     *     </p>
     *     <p>
     *         The result is a {@link Tuple2} whose first element is an array of types and
     *         whose second element is an array of the corresponding values.
     *     </p>
     *     <p>
     *         The method is suitable for preparing reflective constructor calls.
     *         The order of elements is preserved.
     *     </p>
     * </div>
     *
     * @param arguments a non-null array of {@code Argument} objects; must not contain {@code null} elements
     * @return a tuple containing two arrays: one of types and one of values
     * @throws NullPointerException if {@code arguments} is {@code null}
     * @throws IllegalArgumentException if any element of the array is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("rawtypes")
    public static Tuple2<Class<?>[], Object[]> extractTypesAndValues(final @NonNull Argument[] arguments) {
        Objects.requireNonNull(arguments, nullValue("arguments"));
        requireNonNullElements(arguments, "arguments", IllegalArgumentException::new);

        final var types = new Class<?>[arguments.length];
        final var values = new Object[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            final Argument<?> arg = arguments[i];
            types[i] = arg.getType();
            values[i] = arg.getValue();
        }

        return tuple2(types, values);
    }

    /**
     * <div>
     *     <p>
     *         Extracts types and values from a {@link java.util.List} of {@link Argument} objects.
     *     </p>
     *     <p>
     *         The result is a {@link Tuple2} whose first element is an array of types and
     *         whose second element is an array of the corresponding values.
     *     </p>
     *     <p>
     *         The method is suitable for preparing reflective calls (e.g., constructors or methods).
     *         The order of elements is preserved.
     *     </p>
     * </div>
     *
     * @param arguments a non-null list of {@code Argument} objects; must not contain {@code null} elements
     * @return a tuple containing two arrays: one of types and one of values
     * @throws NullPointerException if {@code arguments} is {@code null}
     * @throws IllegalArgumentException if any element of the list is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("rawtypes")
    public static Tuple2<Class<?>[], Object[]> extractTypesAndValues(final @NonNull List<Argument> arguments) {
        Objects.requireNonNull(arguments, nullValue("arguments"));
        Util.requireNonNullElements(arguments,"arguments", IllegalArgumentException::new);

        final var types
            = arguments.stream()
                .map(Argument::getType)
                .toArray(Class<?>[]::new);
        final var values
            = arguments.stream()
                .map(Argument::getValue)
                .toArray(Object[]::new);

        return tuple2(types, values);
    }

    /**
     * <div>
     *     <p>
     *         Extracts types and values from a {@link java.util.stream.Stream} of {@link Argument} objects.
     *     </p>
     *     <p>
     *         The result is a {@link Tuple2} whose first element is an array of types and
     *         whose second element is an array of the corresponding values.
     *     </p>
     *     <p>
     *         The provided stream is consumed fully.
     *         <b>Note:</b> To avoid ordering issues, processing is forced to be sequential
     *         (regardless of whether the stream is parallel). The order corresponds to the stream's encounter order.
     *         For non-ordered sources (e.g., {@code HashSet}), the order is not defined.
     *     </p>
     * </div>
     *
     * @param arguments a non-null stream of {@code Argument} objects; must not contain {@code null} elements
     * @return a tuple containing two arrays: one of types and one of values
     * @throws NullPointerException if {@code arguments} is {@code null}
     * @throws IllegalArgumentException if any element of the stream is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("rawtypes")
    public static Tuple2<Class<?>[], Object[]> extractTypesAndValues(final @NonNull Stream<Argument> arguments) {
        Objects.requireNonNull(arguments, nullValue("arguments"));
        final var argumentsList
            = arguments.sequential().toList();
        Util.requireNonNullElements(argumentsList, "arguments", IllegalArgumentException::new);
        return extractTypesAndValues(argumentsList);
    }

    private final Class<? extends A> type;
    private final A value;

    private Argument(final Class<? extends A> type,
                     final A value) {
        this.type
            = type;
        this.value
            = value;
    }

    /**
     * <div>
     *     <p>
     *         Returns the explicitly provided type of this argument.
     *     </p>
     * </div>
     *
     * @return the non-nullable type of the argument
     *
     * @since 1.0.0
     */
    @NonNull
    public Class<? extends A> getType() {
        return this.type;
    }

    /**
     * <div>
     *     <p>
     *         Returns whether a value is present.
     *     </p>
     * </div>
     *
     * @return {@code true} if a value is present; otherwise {@code false}
     *
     * @since 1.0.0
     */
    public boolean hasValue() {
        return this.value != null;
    }

    /**
     * <div>
     *     <p>
     *         Returns whether no value is present.
     *     </p>
     * </div>
     *
     * @return {@code true} if no value is present; otherwise {@code false}
     *
     * @since 1.0.0
     */
    public boolean hasNoValue() {
        return this.value == null;
    }

    /**
     * <div>
     *     <p>
     *         Returns the argument's value. This may be {@code null}.
     *     </p>
     * </div>
     *
     * @return the stored value (may be {@code null})
     *
     * @since 1.0.0
     */
    @Nullable
    public A getValue() {
        return this.value;
    }

    /**
     * <div>
     *     <p>
     *         Returns the argument's value as a {@link Maybe}.
     *     </p>
     *     <p>
     *         If no value is set, an empty {@code Maybe} is returned. This method is null-safe and explicitly
     *         models the absence of a value so that manual {@code null} checks can be avoided.
     *     </p>
     * </div>
     *
     * @return a {@code Maybe} containing the value, or empty if no value is present
     *
     * @since 1.0.0
     */
    public Maybe<A> getValueSafe() {
        return maybeOfNullable(this.value);
    }

    /**
     * <div>
     *     <p>
     *         Compares this {@code Argument} with another object for equality.
     *     </p>
     *     <p>
     *         Two {@code Argument} instances are equal if both their types and their values are equal.
     *     </p>
     * </div>
     *
     * @param o the object to compare with
     * @return {@code true} if the given object is an equal {@code Argument}; otherwise {@code false}
     *
     * @since 1.0.0
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Argument<?> argument)) return false;

        return Objects.equals(value, argument.value) && type.equals(argument.type);
    }

    /**
     * <div>
     *     <p>
     *         Returns a hash code for this {@code Argument}.
     *     </p>
     *     <p>
     *         The hash code is based on the argument's type and value.
     *     </p>
     * </div>
     *
     * @return the computed hash code
     *
     * @since 1.0.0
     */
    @Override
    public int hashCode() {
        int result = Objects.hashCode(value);
        result = 31 * result + type.hashCode();
        return result;
    }

    /**
     * <div>
     *     <p>
     *         Returns a string representation of this {@code Argument}.
     *     </p>
     *     <p>
     *         The format is {@code Argument[type=..., value=...]}.
     *     </p>
     * </div>
     *
     * @return a human-readable description of this {@code Argument} instance
     *
     * @since 1.0.0
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", Argument.class.getSimpleName() + "[", "]")
                .add("type=" + type)
                .add("value=" + value)
                .toString();
    }


}
