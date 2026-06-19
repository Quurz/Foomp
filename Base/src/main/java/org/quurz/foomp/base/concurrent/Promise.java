package org.quurz.foomp.base.concurrent;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Monadic;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import static org.quurz.foomp.base.localisation.BaseMessages.unsupportedOperation;

@FunctionalInterface
@SuppressWarnings("NonAsciiCharacters")
public interface Promise<A>
        extends Monadic<Promise.µ, A>,
                Future<A>,
                Higher1<Promise.µ, A> {

    class µ implements WitnessType { protected µ() {} }

    @NonNull A claim()
        throws ConcurrentExecutionException;

    default @NonNull A claim(final long timeOut,
                             final @NonNull TimeUnit timeUnit)
            throws ConcurrentExecutionException {
        throw new UnsupportedOperationException(unsupportedOperation());
    }

    @Override
    default boolean cancel(final boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException(unsupportedOperation());
    }

    @Override
    default boolean isCancelled() {
        throw new UnsupportedOperationException(unsupportedOperation());
    }

    @Override
    default boolean isDone() {
        throw new UnsupportedOperationException(unsupportedOperation());
    }

    @Override
    default A get()
            throws InterruptedException,
                   ExecutionException {
        throw new UnsupportedOperationException(unsupportedOperation());
    }

    @Override
    default A get(final long timeout,
                  final @NonNull TimeUnit unit)
            throws InterruptedException,
                   ExecutionException,
                   TimeoutException {
        throw new UnsupportedOperationException(unsupportedOperation());
    }

    @Override
    @NonNull
    default A resultNow() {
        throw new UnsupportedOperationException(unsupportedOperation());
    }

    @Override
    @NonNull
    default Throwable exceptionNow() {
        throw new UnsupportedOperationException(unsupportedOperation());
    }

    @Override
    @NonNull
    default State state() {
        throw new UnsupportedOperationException(unsupportedOperation());
    }

    @Override
    @NonNull
    default <B> Higher1<? extends µ, B> map(final @NonNull Function<? super A, ? extends B> transformation) {
        throw new UnsupportedOperationException(unsupportedOperation());
    }

    @Override
    @NonNull
    default <B> Higher1<? extends µ, B> applyTo(final @NonNull Higher1<? extends µ, ? extends Function<? super A, ? extends B>> transformation) {
        throw new UnsupportedOperationException(unsupportedOperation());
    }

    @Override
    @NonNull
    default <B> Higher1<? extends µ, B> flatMap(final @NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation) {
        throw new UnsupportedOperationException(unsupportedOperation());
    }

}
