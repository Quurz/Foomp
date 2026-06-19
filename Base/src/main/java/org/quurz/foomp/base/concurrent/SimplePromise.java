package org.quurz.foomp.base.concurrent;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.higher.Higher1;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Function;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

@SuppressWarnings("NonAsciiCharacters")
public class SimplePromise<A>
        implements Promise<A> {

    public static class µ extends Promise.µ { protected µ() { super(); } }

    public static <X, Y> Fun<X, Promise<Y>> promise(final @NonNull Fun<X, Y> function) {
        Objects.requireNonNull(nullValue("function"));

        return x -> {
            Objects.requireNonNull(nullValue("x"));
            final boolean shutDown;
            final ExecutorService executorService;
            if (Thread.currentThread() instanceof ForkJoinWorkerThread forkJoinWorkerThread) {
                shutDown
                    = false;
                executorService
                    = forkJoinWorkerThread.getPool();
            } else {
                shutDown
                    = true;
                executorService
                    = Executors.newVirtualThreadPerTaskExecutor();
            }

            return new SimplePromise<>(
                executorService.submit(
                    () -> Objects.requireNonNull(function.apply(x), nullResultFrom("function"))
                ),
                shutDown,
                executorService
            );
        };
    }

    protected final Future<A> future;
    protected final boolean shutDown;
    protected final ExecutorService executorService;
    protected boolean alreadyShuttingDown;

    protected SimplePromise(final Future<A> future,
                            final boolean shutDown,
                            final ExecutorService executorService) {
        this.future
            = future;
        this.shutDown
            = shutDown;
        this.executorService
            = executorService;
        this.alreadyShuttingDown
            = false;
    }

    @Override
    public @NonNull A claim()
            throws ConcurrentExecutionException {
        try {
            return this.future.get();
        } catch (  InterruptedException
                 | ExecutionException exception) {
            throw new ConcurrentExecutionException(exception);
        } finally {
            if (this.shutDown) {
                this.executorService.shutdown();
            }
        }
    }

    @Override
    public @NonNull A claim(final long timeOut,
                            final @NonNull TimeUnit timeUnit)
            throws ConcurrentExecutionException {
        Objects.requireNonNull(timeUnit, nullValue("timeUnit"));
        try {
            return this.future.get(timeOut, timeUnit);
        } catch (  InterruptedException
                 | ExecutionException
                 | TimeoutException exception) {
            throw new ConcurrentExecutionException(exception);
        } finally {
            if (this.shutDown) {
                this.executorService.shutdown();
            }
        }
    }

    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        return this.future.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return this.future.isCancelled();
    }

    @Override
    public boolean isDone() {
        return this.future.isDone();
    }

    @Override
    public @NonNull A get()
            throws InterruptedException,
                   ExecutionException {
        A result;
        try {
            result
                = this.future.get();
        } finally {
            if (this.shutDown) {
                this.executorService.shutdown();
            }
        }
        return result;
    }

    @Override
    public @NonNull A get(final long timeout,
                          final @NonNull TimeUnit timeUnit)
            throws InterruptedException,
                   ExecutionException,
                   TimeoutException {
        A result;
        try {
            result
                = this.future.get(timeout, timeUnit);
        } finally {
            if (this.shutDown) {
                this.executorService.shutdown();
            }
        }
        return result;
    }

    @Override
    public @NonNull A resultNow() {
        return this.future.resultNow();
    }

    @Override
    public @NonNull Throwable exceptionNow() {
        return this.future.exceptionNow();
    }

    @Override
    public @NonNull State state() {
        return this.future.state();
    }

    @Override
    public @NonNull <B> Promise<B> map(final @NonNull Function<? super A, ? extends B> transformation) {
        Objects.requireNonNull(transformation, nullValue("transaformation"));
        return null;    // TODO
    }

    @Override
    public @NonNull <B> Promise<B> applyTo(final @NonNull Higher1<? extends Promise.µ, ? extends Function<? super A, ? extends B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("transaformation"));
        return null;    // TODO
    }

    @Override
    public @NonNull <B> Promise<B> flatMap(final @NonNull Function<? super A, ? extends Higher1<? extends Promise.µ, B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("transaformation"));
        return null;    // TODO
    }

}
