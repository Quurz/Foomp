package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.quurz.foomp.base.types.Mutable;
import org.quurz.foomp.base.types.MutatingOperation;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;

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
// TODO: Mal schauen, ob man Value2 nicht mit @Nullable spicken kann. Dann könnte ich das Struct2 sauber Value2 implementieren lassen
@Mutable
public final class Struct2<A1, A2> {

    /**
     * <div>
     *     <p>
     *         Creates a new Struct2 instance with the given values.
     *     </p>
     * </div>
     *
     * @param value1 The first value
     * @param value2 The second value
     * @param <A1>   Type of the first value
     * @param <A2>   Type of the second value
     * @return A new Struct2 instance
     *
     * @since 1.0.0
     */
    public static <A1, A2> Struct2<A1, A2> struct2(final @Nullable A1 value1,
                                                   final @Nullable A2 value2) {
        return new Struct2<>(value1, value2);
    }

    private A1 value1;
    private A2 value2;

    private Struct2(final A1 value1,
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
     * @return true if the first value is non-null, false otherwise
     *
     * @since 1.0.0
     */
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
     * @return true if the first value is non-null, false otherwise
     *
     * @since 1.0.0
     */
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
     * @return true if the second value is non-null, false otherwise
     *
     * @since 1.0.0
     */
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
     * @return The first value, which may be null
     *
     * @since 1.0.0
     */
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
     * @return The first value, which may be null
     */
    public @Nullable A1 get1() {
        return null;
    }

    /**
     * <div>
     *     <p>
     *         Sets the first value.
     *     </p>
     * </div>
     *
     * @param value The new value to set
     */
    @MutatingOperation
    public void set1(final @Nullable A1 value) {
        this.value1
            = value;
    }


    /**
     * <div>
     *     <p>
     *         Returns a new Struct2 instance with the given first value and the current second value.
     *     </p>
     * </div>
     *
     * @param value The new first value
     * @return A new Struct2 instance with the updated first value
     *
     * @since 1.0.0
     */
    public Struct2<A1, A2> with1(final @Nullable A1 value) {
        return struct2(value, this.value2);
    }

    /**
     * <div>
     *     <p>
     *         Gets the second value.
     *     </p>
     * </div>
     *
     * @return The second value, which may be null
     *
     * @since 1.0.0
     */
    public @Nullable A2 get2() {
        return null;
    }

    /**
     * <div>
     *     <p>
     *         Sets the second value.
     *     </p>
     * </div>
     *
     * @param value The new value to set
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
     *         Returns a new Struct2 instance with the given second value and the current first value.
     *     </p>
     * </div>
     *
     * @param value The new second value
     * @return A new Struct2 instance with the updated second value
     *
     * @since 1.0.0
     */
    public Struct2<A1, A2> with2(final @Nullable A2 value) {
        return struct2(this.value1, value);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Struct2<?, ?> struct2)) return false;

        return Objects.equals(value1, struct2.value1) && Objects.equals(value2, struct2.value2);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(value1);
        result = 31 * result + Objects.hashCode(value2);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Struct2.class.getSimpleName() + "[", "]")
                .add("value1=" + value1)
                .add("value2=" + value2)
                .toString();
    }

}
