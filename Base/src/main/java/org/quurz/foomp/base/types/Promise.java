package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

// TODO
@FunctionalInterface
@SuppressWarnings("NonAsciiCharacters")
public interface Promise<A>
        extends Future<A>,
                Monadic<Promise.µ, A>,
                Value<A>,
                Higher1<Promise.µ, A> {

    final class µ implements WitnessType { private µ() {} }

    A claim();

    @Override
    default boolean cancel(final boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    default boolean isCancelled() {
        return false;
    }

    @Override
    default boolean isDone() {
        return false;
    }

    @Override
    default @NonNull A get() {
        return null;
    }

    @Override
    default @NonNull A get(final long timeout,
                           final @NonNull TimeUnit unit)
            throws InterruptedException,
                   ExecutionException,
                   TimeoutException {
        Objects.requireNonNull(unit, nullValue("unit"));
        return null;
    }

    @Override
    @NonNull
    default <B> Promise<B> applyTo(final @NonNull Higher1<? extends µ, ? extends Function<? super A, ? extends B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return null;
    }

    @Override
    @NonNull
    default <B> Promise<B> flatMap(final @NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return null;
    }

    @Override
    @NonNull
    default <B> Promise<B> map(final @NonNull Function<? super A, ? extends B> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return null;
    }

    @Override
    default boolean isPresent() {
        return false;
    }

}
