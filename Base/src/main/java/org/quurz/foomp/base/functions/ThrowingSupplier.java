package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.util.Nothing;

import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         A functional interface similar to {@link java.util.function.Supplier} but
 *         allowed to throw checked exceptions.
 *     </p>
 *     <p>
 *         This interface is particularly useful in situations where a lambda expression
 *         or a method may throw an exception, e.g., for lazy initialization or within
 *         try-with-resources-like constructs.
 *     </p>
 *     <pre>{@code
 *         ThrowingSupplier<String> supplier = () -> {
 *             if (Math.random() < 0.5) {
 *                 throw new IOException("Unlucky!");
 *             }
 *             return "OK";
 *         };
 *     }</pre>
 * </div>
 *
 * @param <A> The type of the result returned by {@code get()}.
 *
 * @author Alexander Schell
 *
 * @since 1.0.0
 */
@FunctionalInterface
public interface ThrowingSupplier<A>
        extends Applicable<Nothing, A> {

    /**
     * <div>
     *     <p>
     *         Performs the computation and returns the result.
     *     </p>
     * </div>
     *
     * @return The result of the computation.
     * @throws Exception If an error occurs during computation.
     *
     * @since 1.0.0
     */
    A get()
        throws Exception;

    /**
     * <div>
     *     <p>
     *         Applies this supplier to the given {@code Nothing} value by invoking
     *         the {@code get()} method. The parameter is validated but otherwise ignored.
     *     </p>
     * </div>
     *
     * @param nothing The {@code Nothing} value to apply this supplier to.
     * @return The result of the computation.
     * @throws Exception            If an error occurs during computation.
     * @throws NullPointerException If {@code nothing} is {@code null}.
     *
     * @since 1.0.0
     */
    @Override
    default A apply(final @NonNull Nothing nothing)
            throws Exception {
        Objects.requireNonNull(nothing, nullValue("nothing"));
        return this.get();
    }

}
