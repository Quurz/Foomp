package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.function.Function;

import static org.quurz.foomp.base.localisation.BaseMessages.*;

/**
 * <div>
 *     <p>
 *         Functional interface for producing a formatted string representation of an object.
 *         This offers an explicit, type-level alternative to {@link Object#toString()} where
 *         formatting can be controlled independently of the default Java representation.
 *     </p>
 *     <p>
 *         Implementations must be null-safe and return a non-null string.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Echo {

    /**
     * <div>
     *     <p>
     *         Returns a formatted string representation of this object using the implementation's
     *         default formatting policy. This method must never return {@code null}.
     *     </p>
     * </div>
     *
     * @return a non-null formatted string representation of this object
     *
     * @since 1.0.0
     */
    @NonNull
    String echo();

    /**
     * <div>
     *     <p>
     *         Returns a formatted string representation produced by applying the given transformer
     *         function to this instance. This allows callers to supply custom formatting logic.
     *     </p>
     *     <p>
     *         Contract: {@code transformer} must not be {@code null} and must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param transformer a function mapping this {@code Echo} to its string representation; must not be {@code null}
     * @return a non-null formatted string
     *
     * @since 1.0.0
     */
    @NonNull
    default String echo(final @NonNull Function<? super Echo, String> transformer) {
        Objects.requireNonNull(transformer, nullValue("transformer"));
        return Objects.requireNonNull(transformer.apply(this), nullResultFrom("transformer"));
    }

}
