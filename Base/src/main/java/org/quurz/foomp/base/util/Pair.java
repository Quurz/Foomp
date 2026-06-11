package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.quurz.foomp.base.types.Mutable;
import org.quurz.foomp.base.types.MutatingOperation;
import org.quurz.foomp.base.types.Value2;

import java.util.Objects;
import java.util.StringJoiner;

import static org.quurz.foomp.base.util.Maybe.maybeOfNullable;
import static org.quurz.foomp.base.util.Tuple2.tuple2;

/**
 * <div>
 *     <p>
 *         A mutable structure holding two nullable values of different types.
 *     </p>
 *     <p>
 *         I despise null values, but unfortunately I can't slap everyone's fingers when they want to use them.
 *         That's why this abomination exists.
 *     </p>
 * </div>
 *
 * @param <A1> Type of the first value
 * @param <A2> Type of the second value
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@Mutable
public final class Pair<A1, A2>
        implements Value2<A1, A2> {

    /**
     * <div>
     *     <p>
     *         Creates a new Pair instance with the given values.
     *     </p>
     * </div>
     *
     * @param value1 The first value
     * @param value2 The second value
     * @param <A1>   Type of the first value
     * @param <A2>   Type of the second value
     * @return A new Pair instance
     *
     * @since 1.0.0
     */
    public static <A1, A2> Pair<A1, A2> pair(final @Nullable A1 value1,
                                             final @Nullable A2 value2) {
        return new Pair<>(value1, value2);
    }

    private A1 value1;
    private A2 value2;

    /**
     * <div>
     *     <p>
     *         Internal constructor to initialize a Pair.
     *     </p>
     * </div>
     *
     * @param value1 the first value
     * @param value2 the second value
     */
    private Pair(final A1 value1,
                 final A2 value2) {
        this.value1
            = value1;
        this.value2
            = value2;
    }

    /**
     * <div>
     *     <p>
     *         Checks if the first value is present (non-null).
     *     </p>
     * </div>
     *
     * @return true if the first value is not null, false otherwise
     *
     * @since 1.0.0
     */
    @Override
    public boolean isPresent() {
        return this.is1();
    }

    /**
     * <div>
     *     <p>
     *         Checks if the first value is present (non-null).
     *     </p>
     * </div>
     *
     * @return true if the first value is not null, false otherwise
     *
     * @since 1.0.0
     */
    @Override
    public boolean is1() {
        return this.value1 != null;
    }

    /**
     * <div>
     *     <p>
     *         Checks if the second value is present (non-null).
     *     </p>
     * </div>
     *
     * @return true if the second value is not null, false otherwise
     *
     * @since 1.0.0
     */
    @Override
    public boolean is2() {
        return this.value2 != null;
    }

    /**
     * <div>
     *     <p>
     *         Gets the first value.
     *     </p>
     * </div>
     *
     * @return the first value, may be null
     *
     * @since 1.0.0
     */
    @Override
    public @Nullable A1 get(){
        return this.get1();
    }

    /**
     * <div>
     *     <p>
     *         Gets the first value.
     *     </p>
     * </div>
     *
     * @return the first value, may be null
     *
     * @since 1.0.0
     */
    @Override
    public @Nullable A1 get1() {
        return this.value1;
    }

    /**
     * <div>
     *     <p>
     *         Sets the first value.
     *     </p>
     * </div>
     *
     * @param value the new value to set
     *
     * @since 1.0.0
     */
    @MutatingOperation
    public void set1(final @Nullable A1 value) {
        this.value1
            = value;
    }

    /**
     * <div>
     *     <p>
     *         Creates a new Pair with the given first value and the current second value.
     *     </p>
     * </div>
     *
     * @param value the new first value
     * @return a new Pair instance
     *
     * @since 1.0.0
     */
    public Pair<A1, A2> with1(final @Nullable A1 value) {
        return pair(value, this.value2);
    }

    /**
     * <div>
     *     <p>
     *         Gets the second value.
     *     </p>
     * </div>
     *
     * @return the second value, may be null
     *
     * @since 1.0.0
     */
    public @Nullable A2 get2() {
        return this.value2;
    }

    /**
     * <div>
     *     <p>
     *         Sets the second value.
     *     </p>
     * </div>
     *
     * @param value the new value to set
     *
     * @since 1.0.0
     */
    @MutatingOperation
    public void set2(final @Nullable A2 value) {
        this.value2
            = value;
    }

    /**
     * <div>
     *     <p>
     *         Creates a new Pair with the current first value and the given second value.
     *     </p>
     * </div>
     *
     * @param value the new second value
     * @return a new Pair instance
     *
     * @since 1.0.0
     */
    public Pair<A1, A2> with2(final @Nullable A2 value) {
        return pair(this.value1, value);
    }

    /**
     * <div>
     *     <p>
     *         Converts this Pair to a Tuple2 of Maybe values.
     *     </p>
     * </div>
     *
     * @return a new Tuple2 containing Maybe wrapped values
     *
     * @since 1.0.0
     */
    public @NonNull Tuple2<Maybe<A1>, Maybe<A2>> toTuple() {
        return tuple2(maybeOfNullable(this.value1), maybeOfNullable(this.value2));
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.0
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Pair<?, ?> pair)) return false;

        return Objects.equals(value1, pair.value1) && Objects.equals(value2, pair.value2);
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.0
     */
    @Override
    public int hashCode() {
        int result
            = Objects.hashCode(value1);
        result = 31 * result + Objects.hashCode(value2);
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.0
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", Pair.class.getSimpleName() + "[", "]")
                .add("value1=" + value1)
                .add("value2=" + value2)
                .toString();
    }

}
