package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.dataflow.qual.Pure;

import java.util.Comparator;
import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         A functional interface defining comparison logic between two values of the same type.
 *         It returns a well-defined {@link Relation} enum instead of raw integers, restricting
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
public interface Comparer<A>
        extends Fun2<A, A, Comparer.Relation> {

    /**
     * <div>
     *     <p>
     *         Enumeration of possible comparison outcomes.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    enum Relation {

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
     *         Creates a {@code Comparer} from a standard {@link java.util.Comparator}.
     *         Maps {@code compare} results to {@link Relation} via sign semantics, enabling
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
     * @return a {@code Comparer} delegating to the given {@code comparator}
     *
     * @throws NullPointerException if {@code comparator} is {@code null}
     *
     * @since 1.0.0
     */
    static <A> Comparer<A> comparer(final @NonNull Comparator<A> comparator) {
        Objects.requireNonNull(comparator, nullValue("comparator"));
        return (first, second) -> {
            Objects.requireNonNull(first, nullValue("first"));
            Objects.requireNonNull(second, nullValue("second"));
            return switch (Integer.signum(comparator.compare(first, second))) {
                case -1 -> Relation.LESS;
                case 1 -> Relation.GREATER;
                default -> Relation.EQUAL;
            };
        };
    }

    /**
     * <div>
     *     <p>
     *         Compares two values and returns a {@link Relation} describing their order.
     *     </p>
     *     <p>
     *         Contract: {@code first} and {@code second} must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param first  the first value to compare; must not be {@code null}
     * @param second the second value to compare; must not be {@code null}
     * @return the comparison outcome as {@link Relation}
     *
     * @throws NullPointerException if any argument is {@code null}
     *
     * @since 1.0.0
     */
    @Pure
    @NonNull
    Relation compare(final @NonNull A first,
                     final @NonNull A second);

    /**
     * <div>
     *     <p>
     *         Applies this comparator to the given values and returns a {@link Relation}.
     *         Ensures both inputs are non-null before delegating to {@link #compare(Object, Object)}.
     *     </p>
     * </div>
     *
     * @see #compare(Object, Object)
     * @see Fun2#apply(Object, Object)
     *
     * @param first  the first value; must not be {@code null}
     * @param second the second value; must not be {@code null}
     *
     * @return the comparison outcome as {@link Relation}
     *
     * @throws NullPointerException if any argument is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @Pure
    default @NonNull Relation apply(final @NonNull A first,
                                    final @NonNull A second) {
        Objects.requireNonNull(first, nullValue("first"));
        Objects.requireNonNull(second, nullValue("second"));
        return compare(first, second);
    }

}
