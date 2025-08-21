package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.stream.Stream;

/**
 * <div>
 *     <p>
 *         Functional interface for objects that can be represented as a {@link Stream}.
 *         This enables functional processing of the contained elements.
 *     </p>
 *     <p>
 *         Unless stated otherwise by an implementation, the returned stream should be created lazily,
 *         reflect the encounter order of the underlying structure, and must not be {@code null}.
 *         Callers are responsible for closing the stream if the implementation returns a closeable stream.
 *     </p>
 * </div>
 *
 * @param <A> the element type of the stream
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Streamable<A> {

    /**
     * <div>
     *     <p>
     *         Returns a {@link Stream} that represents the elements of this structure.
     *         Implementations should create the stream lazily where possible.
     *     </p>
     * </div>
     *
     * @return a non-null {@link Stream} over the elements of this structure
     *
     * @since 1.0.0
     */
    @NonNull Stream<A> stream();

}
