package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.util.Nothing;

import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         A functional interface similar to {@link java.util.function.Supplier}, but permitted to throw
 *         checked exceptions from {@link #get()}.
 *     </p>
 *     <p>
 *         This is useful when a lambda or method reference needs to participate in APIs that pull values
 *         lazily yet may fail, e.g. during I/O, reflection, or resource acquisition in
 *         try-with-resources-like scenarios.
 *     </p>
 *     <p>
 *         Contract: unless stated otherwise, inputs must not be {@code null}. Implementations may throw
 *         checked exceptions; callers must handle them.
 *     </p>
 *     <pre>{@code
 *     CheckedProvider<String> supplier = () -> {
 *         if (Files.notExists(path)) throw new FileNotFoundException(path.toString());
 *         return Files.readString(path);
 *     };
 *     }</pre>
 * </div>
 *
 * @param <A> the result type produced by {@code get()}
 *
 * @author Alexander Schell
 *
 * @since 1.0.0
 */
@FunctionalInterface
public interface UnsafeProvider<A>
        extends Applicable<Nothing, A> {

    /**
     * <div>
     *     <p>
     *         Performs the computation and returns its result.
     *     </p>
     *     <p>
     *         Contract: the returned value should not be {@code null} unless explicitly documented
     *         by an implementation. Callers must be prepared to handle checked exceptions.
     *     </p>
     * </div>
     *
     * @return the computation result
     * @throws Exception if an error occurs during computation
     *
     * @since 1.0.0
     */
    A get() throws Exception;

    /**
     * <div>
     *     <p>
     *         Applies this supplier to the given {@link Nothing} placeholder by invoking {@link #get()}.
     *         The parameter is validated but otherwise ignored.
     *     </p>
     *     <p>
     *         Contract: {@code nothing} must not be {@code null}. Propagates any exception thrown by {@code get()}.
     *     </p>
     * </div>
     *
     * @param nothing the {@link Nothing} placeholder; must not be {@code null}
     * @return the computation result
     * @throws Exception            if an error occurs during computation
     * @throws NullPointerException if {@code nothing} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    default A apply(final @NonNull Nothing nothing) throws Exception {
        Objects.requireNonNull(nothing, nullValue("nothing"));
        return this.get();
    }

}
