package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;

import static org.quurz.foomp.base.localisation.BaseMessages.noValuePresent;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Disjoint two‑way value container: holds exactly one value, either on the <em>left</em>
 *         of type {@code L} or on the <em>right</em> of type {@code R}. If there is no left value,
 *         there must be a right value, and vice versa.
 *     </p>
 *     <p>
 *         This interface extends {@link Value}{@code <R>} and treats the presence of the <em>right</em>
 *         value as the notion of “being present”. Consequently, {@link #isPresent()} mirrors
 *         {@link #isRight()}, and {@link #get()} returns the right value (or throws if absent).
 *     </p>
 * </div>
 *
 * @param <L> the type of the left value
 * @param <R> the type of the right value
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface XorValue<L, R>
        extends Value<R> {

    /**
     * <div>
     *     <p>
     *         Creates a new {@code XorValue} containing a left value.
     *     </p>
     * </div>
     *
     * @param <L>  the type of the left value
     * @param <R>  the type of the right value
     * @param left the left value; must not be {@code null}
     * @return a {@code XorValue} holding the left value
     * @throws NullPointerException if {@code left} is {@code null}
     *
     * @since 1.0.0
     */
    static <L, R> XorValue<L, R> left(final @NonNull L left) {
        Objects.requireNonNull(left, nullValue("left"));
        return new XorValue<>() {
            @Override
            public boolean isRight() {
                return false;
            }

            @Override
            public @NonNull L getLeft() {
                return left;
            }

            @Override
            public @NonNull R getRight() throws NoSuchElementException {
                throw new NoSuchElementException(noValuePresent());
            }

            @Override
            public boolean equals(final Object o) {
                if (this == o) return true;
                if (!(o instanceof XorValue<?, ?> that)) return false;
                return that.isLeft() && Objects.equals(left, that.getLeft());
            }

            @Override
            public int hashCode() {
                return Objects.hash(false, left);
            }

            @Override
            public String toString() {
                return new StringJoiner(", ", "Left[", "]")
                        .add("value=" + left)
                        .toString();
            }
        };
    }

    /**
     * <div>
     *     <p>
     *         Creates a new {@code XorValue} containing a right value.
     *     </p>
     * </div>
     *
     * @param <L>   the type of the left value
     * @param <R>   the type of the right value
     * @param right the right value; must not be {@code null}
     * @return a {@code XorValue} holding the right value
     * @throws NullPointerException if {@code right} is {@code null}
     *
     * @since 1.0.0
     */
    static <L, R> XorValue<L, R> right(final @NonNull R right) {
        Objects.requireNonNull(right, nullValue("right"));
        return new XorValue<>() {
            @Override
            public boolean isRight() {
                return true;
            }

            @Override
            public @NonNull L getLeft() throws NoSuchElementException {
                throw new NoSuchElementException(noValuePresent());
            }

            @Override
            public @NonNull R getRight() {
                return right;
            }

            @Override
            public boolean equals(final Object o) {
                if (this == o) return true;
                if (!(o instanceof XorValue<?, ?> that)) return false;
                return that.isRight() && Objects.equals(right, that.getRight());
            }

            @Override
            public int hashCode() {
                return Objects.hash(true, right);
            }

            @Override
            public String toString() {
                return new StringJoiner(", ", "Right[", "]")
                        .add("value=" + right)
                        .toString();
            }
        };
    }

    /**
     * <div>
     *     <p>
     *         Returns whether a left value is present.
     *     </p>
     * </div>
     *
     * @return {@code true} if a left value is present; {@code false} otherwise
     *
     * @since 1.0.0
     */
    default boolean isLeft() {
        return !this.isRight();
    }

    /**
     * <div>
     *     <p>
     *         Returns whether a right value is present. This mirrors {@link #isRight()} and, as an
     *         override of {@link Value#isPresent()}, defines the presence notion for this type.
     *     </p>
     * </div>
     *
     * @return {@code true} if a right value is present; {@code false} otherwise
     *
     * @since 1.0.0
     */
    default boolean isPresent() {
        return this.isRight();
    }

    /**
     * <div>
     *     <p>
     *         Returns whether a right value is present.
     *     </p>
     * </div>
     *
     * @return {@code true} if a right value is present; {@code false} otherwise
     *
     * @since 1.0.0
     */
    boolean isRight();

    /**
     * <div>
     *     <p>
     *         Returns the left value of this {@code XorValue}.
     *     </p>
     * </div>
     *
     * @return the left value
     * @throws NoSuchElementException if no left value is present
     *
     * @since 1.0.0
     */
    @NonNull
    L getLeft()
            throws NoSuchElementException;

    /**
     * <div>
     *     <p>
     *         Returns the right value of this {@code XorValue}. This is the same as {@link #getRight()}
     *         and overrides {@link Value#get()} to align with the presence semantics.
     *     </p>
     * </div>
     *
     * @return the right value
     * @throws NoSuchElementException if no right value is present
     *
     * @since 1.0.0
     */
    @NonNull
    default R get()
            throws NoSuchElementException {
        return this.getRight();
    }

    /**
     * <div>
     *     <p>
     *         Returns the right value of this {@code XorValue}.
     *     </p>
     * </div>
     *
     * @return the right value
     * @throws NoSuchElementException if no right value is present
     *
     * @since 1.0.0
     */
    @NonNull
    R getRight()
            throws NoSuchElementException;

}
