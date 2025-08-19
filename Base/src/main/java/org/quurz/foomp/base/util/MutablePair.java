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
public final class MutablePair<A1, A2>
        implements Value2<A1, A2> {

    /**
     * <div>
     *     <p>
     *         Creates a new MutablePair instance with the given values.
     *     </p>
     * </div>
     *
     * @param value1 The first value
     * @param value2 The second value
     * @param <A1>   Type of the first value
     * @param <A2>   Type of the second value
     * @return A new MutablePair instance
     *
     * @since 1.0.0
     */
    public static <A1, A2> MutablePair<A1, A2> mutablePair(final @Nullable A1 value1,
                                                           final @Nullable A2 value2) {
        return new MutablePair<>(value1, value2);
    }

    private A1 value1;
    private A2 value2;

    private MutablePair(final A1 value1,
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
     *         Creates a new MutablePair with the given first value and the current second value.
     *     </p>
     * </div>
     *
     * @param value the new first value
     * @return a new MutablePair instance
     *
     * @since 1.0.0
     */
    public MutablePair<A1, A2> with1(final @Nullable A1 value) {
        return mutablePair(value, this.value2);
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
     *         Creates a new MutablePair with the current first value and the given second value.
     *     </p>
     * </div>
     *
     * @param value the new second value
     * @return a new MutablePair instance
     *
     * @since 1.0.0
     */
    public MutablePair<A1, A2> with2(final @Nullable A2 value) {
        return mutablePair(this.value1, value);
    }

    /**
     * <div>
     *     <p>
     *         Converts this MutablePair to a Tuple2 of Maybe values.
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MutablePair<?, ?> mutablePair)) return false;

        return Objects.equals(value1, mutablePair.value1) && Objects.equals(value2, mutablePair.value2);
    }

    @Override
    public int hashCode() {
        int result
            = Objects.hashCode(value1);
        result = 31 * result + Objects.hashCode(value2);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MutablePair.class.getSimpleName() + "[", "]")
                .add("value1=" + value1)
                .add("value2=" + value2)
                .toString();
    }

}
