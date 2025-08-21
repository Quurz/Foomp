package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Function;

/**
 * <div>
 *     <p>
 *         Interface for types that can be transformed (“transmogrified”) into another type via a
 *         user‑provided function. This enables fluent conversion pipelines without coupling to
 *         a specific target type.
 *     </p>
 *     <p>
 *         Sometimes it’s convenient to “transform” an object into a different representation and
 *         keep working fluently with the result. Implementing {@code Transmogrifyable} provides a
 *         single, explicit hook for such conversions.
 *     </p>
 *     <p>
 *         Pop‑culture footnote: The term “Transmogrifier” pays homage to Calvin &amp; Hobbes’ cardboard box
 *         invention (see <a href="https://www.gocomics.com/calvinandhobbes/1987/03/23">Calvin &amp; Hobbes, 1987‑03‑23</a>).
 *         Variants of the idea also pop up across classic sci‑fi (Star Trek, anyone?).
 *     </p>
 * </div>
 *
 * @param <SELF> the implementing type (self type)
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Transmogrifyable<SELF extends Transmogrifyable<?>> {

    /**
     * <div>
     *     <p>
     *         Applies the given transformation to this instance and returns the result, enabling
     *         fluent conversion to an arbitrary target type.
     *     </p>
     *     <p>
     *         Contract: {@code transmogrifier} must not be {@code null} and must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param transmogrifier the transformation function; must not be {@code null}
     * @param <T>            the target type produced by the transformation
     * @return a non‑null value of type {@code T}
     *
     * @since 1.0.0
     */
    @NonNull <T> T transmogrify(final @NonNull Function<? super SELF, ? extends T> transmogrifier);

}
