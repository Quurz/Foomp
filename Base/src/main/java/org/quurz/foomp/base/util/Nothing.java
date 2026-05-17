package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Transmogrifyable;

import java.util.Objects;
import java.util.function.Function;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *   <p>
 *     Java has no built-in unit type (an "empty tuple"). This class provides one: <code>Nothing</code>.<br/>
 *     The <code>null</code> reference is error-prone; prefer using the <code>Nothing</code> singleton instead.
 *     See also {@link Maybe.None} if you need a typed absence.
 *   </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public final class Nothing
        implements Transmogrifyable<Nothing> {

    /**
     * <div>
     *     <p>
     *         The <code>Nothing</code> singleton.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    public static final Nothing nothing
        = new Nothing();

    /**
     * <div>
     *     <p>
     *         Private constructor to prevent instantiation.
     *     </p>
     * </div>
     */
    private Nothing() {}

    /**
     * <div>
     *   <p>
     *     Applies the given function to this <code>Nothing</code> instance and returns its result.
     *   </p>
     *   <p>
     *     Note: Models a functional-style pattern of producing a value starting from <em>nothing</em>.
     *   </p>
     *   <p>
     *     Aside: A lighthearted, almost metaphysical nod to “ex nihilo”. &#128512;
     *   </p>
     *   <p>
     *       And a nod to Monty Python: <cite>You come from nothing.. You're going back to nothing... What have you lost? Nothing! </cite>
     *   </p>
     * </div>
     *
     * @param transmogrifier the function to apply to this <code>Nothing</code> instance
     * @param <T> the result type
     * @return the result of applying the function
     * @throws NullPointerException if <code>transmogrifier</code> is <code>null</code> or returns <code>null</code>
     *
     * @since 1.0.0
     */
    @Override
    public <T> @NonNull T transmogrify(@NonNull Function<? super Nothing, ? extends T> transmogrifier) {
        Objects.requireNonNull(transmogrifier, nullValue("transmogrifier"));
        return Objects.requireNonNull(transmogrifier.apply(this), nullResultFrom("transmogrifier"));
    }

    /**
     * <div>
     *   <p>
     *     Returns <code>true</code> if the specified object is an instance of <code>Nothing</code>.
     *     Since <code>Nothing</code> is a singleton, all instances are equal by definition.
     *   </p>
     * </div>
     *
     * @param obj the object to compare
     * @return <code>true</code> if the specified object is <code>Nothing</code>, <code>false</code> otherwise
     *
     * @since 1.0.0
     */
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Nothing;
    }

    /**
     * <div>
     *   <p>
     *     Returns a constant hash code for <code>Nothing</code>. This is consistent with {@link #equals(Object)}.
     *   </p>
     * </div>
     *
     * @return the constant hash code of the <code>Nothing</code> object
     *
     * @since 1.0.0
     */
    @Override
    public int hashCode() {
        return 0;
    }

    /**
     * <div>
     *     <p>
     *         Returns a string representation of the {@code Nothing} object.
     *     </p>
     * </div>
     *
     * @return a string representation of the <code>Nothing</code> object
     *
     * @since 1.0.0
     */
    @Override
    public String toString() {
        return "Nothing";
    }

}
