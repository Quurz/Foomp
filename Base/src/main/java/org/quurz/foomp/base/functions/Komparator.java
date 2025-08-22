package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Comparator;
import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         A functional interface defining comparison logic between two values of the same type.
 *         It returns a well-defined {@link Komparison} enum instead of raw integers, restricting
 *         outcomes to {@code LESS}, {@code EQUAL}, or {@code GREATER} and making the result
 *         suitable for switch-expressions.
 *     </p>
 *     <p>
 *         Contract: unless stated otherwise, inputs must not be {@code null} and results must not be {@code null}.
 *     </p>
 * </div>
 *
 * @param <A> the type of values to compare
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Komparator<A>
        extends Fun2<A, A, Komparator.Komparison> {

    /**
     * <div>
     *     <p>
     *         Enumeration of possible comparison outcomes.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    enum Komparison {
        /**
         * <div>
         *     <p>
         *         The first value is less than the second value.
         *     </p>
         * </div>
         */
        LESS,

        /**
         * <div>
         *     <p>
         *         The first value is equal to the second value.
         *     </p>
         * </div>
         */
        EQUAL,

        /**
         * <div>
         *     <p>
         *         The first value is greater than the second value.
         *     </p>
         * </div>
         */
        GREATER
    }

    /**
     * <div>
     *     <p>
     *         Creates a {@code Komparator} from a standard {@link java.util.Comparator}.
     *         Maps {@code compare} results to {@link Komparison} via sign semantics, enabling
     *         ergonomic use in switch-expressions.
     *     </p>
     *     <p>
     *         Contract: {@code comparator} must not be {@code null}; inputs provided to the returned
     *         comparator must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param <A>        the value type
     * @param comparator a standard {@link java.util.Comparator}; must not be {@code null}
     *
     * @return a {@code Komparator} delegating to the given {@code comparator}
     *
     * @throws NullPointerException if {@code comparator} is {@code null}
     *
     * @since 1.0.0
     */
    static <A> Komparator<A> komparator(final @NonNull Comparator<A> comparator) {
        Objects.requireNonNull(comparator, nullValue("comparator"));
        return (first, second) -> {
            Objects.requireNonNull(first, nullValue("first"));
            Objects.requireNonNull(second, nullValue("second"));
            return switch (Integer.signum(comparator.compare(first, second))) {
                case -1 -> Komparison.LESS;
                case 1 -> Komparison.GREATER;
                default -> Komparison.EQUAL;
            };
        };
    }

    /**
     * <div>
     *     <p>
     *         Compares two values and returns a {@link Komparison} describing their order.
     *     </p>
     *     <p>
     *         Contract: {@code first} and {@code second} must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param first  the first value to compare; must not be {@code null}
     * @param second the second value to compare; must not be {@code null}
     * @return the comparison outcome as {@link Komparison}
     *
     * @throws NullPointerException if any argument is {@code null}
     *
     * @since 1.0.0
     */
    @NonNull
    Komparison kompare(final @NonNull A first,
                       final @NonNull A second);

    /**
     * <div>
     *     <p>
     *         Applies this comparator to the given values and returns a {@link Komparison}.
     *         Ensures both inputs are non-null before delegating to {@link #kompare(Object, Object)}.
     *     </p>
     * </div>
     *
     * @see #kompare(Object, Object)
     * @see Fun2#apply(Object, Object)
     *
     * @param first  the first value; must not be {@code null}
     * @param second the second value; must not be {@code null}
     *
     * @return the comparison outcome as {@link Komparison}
     *
     * @throws NullPointerException if any argument is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    default Komparator.@NonNull Komparison apply(final @NonNull A first,
                                                 final @NonNull A second) {
        Objects.requireNonNull(first, nullValue("first"));
        Objects.requireNonNull(second, nullValue("second"));
        return kompare(first, second);
    }

}
