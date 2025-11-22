package org.quurz.foomp.base.util.async;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun2;
import org.quurz.foomp.base.types.Combinable;
import org.quurz.foomp.base.types.Combiner;
import org.quurz.foomp.base.types.Monadic;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.quurz.foomp.base.functions.Fun2.fun2;
import static org.quurz.foomp.base.localisation.BaseMessages.nonPositiveValue;
import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

// TODO: Komplett neu implementieren -> ExecutionContext -> In eigenes Package 'async' verschieben

/**
 * <div>
 *     <p>
 *         Repräsentiert einen asynchron kombinierbaren Wert vom Typ `A`.
 *         Ermöglicht das Verketten von asynchronen Operationen und das Kombinieren der Ergebnisse
 *         unter Verwendung funktionaler Programmiertechniken.
 *     </p>
 *     <p>
 *         Ein `AsyncCombinable`-Objekt kapselt einen Wert, der asynchron berechnet werden kann.
 *         Es bietet Methoden zum Anwenden von Funktionen auf diesen Wert, zum Kombinieren mehrerer
 *         `AsyncCombinable`-Objekte und zur Ausführung der asynchronen Berechnung.
 *     </p>
 * </div>
 *
 * @param <A> Der Datentyp des in diesem Objekt gespeicherten Wertes.
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("NonAsciiCharacters")
public final class AsyncCombinable<A>
        implements Monadic<AsyncCombinable.µ, A>,
        Combinable<AsyncCombinable.µ, A>,
        Higher1<AsyncCombinable.µ, A> {

    public static final class µ implements WitnessType { private µ() {} }

    @SuppressWarnings("unchecked")
    public static <A> AsyncCombinable<A> fix(final @NonNull Higher1<? extends µ, A> unfixed) {
        return (AsyncCombinable<A>) Objects.requireNonNull(unfixed, nullValue("unfixed"));
    }

    @SuppressWarnings("unused")
    public static <A> AsyncCombinable<A> asyncCombinable(final @NonNull A data) {
        Objects.requireNonNull(data, nullValue("data"));
        return new AsyncCombinable<>((executorService, timeout) -> () -> data);
    }

    private final Fun2<ExecutorService, Duration, Callable<A>> spool;
    private final Lock executionLock;

    private AsyncCombinable(final Fun2<ExecutorService, Duration, Callable<A>> spool) {
        this.spool
                = spool;
        this.executionLock
                = new ReentrantLock();
    }

    @Override
    @NonNull
    public <B> AsyncCombinable<B> map(final @NonNull Function<? super A, ? extends B> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return new AsyncCombinable<>((executorService, timeout) -> () -> {
            try {
                return Objects.requireNonNull(
                        transformation.apply(executorService.submit(this.spool.apply(executorService, timeout)).get()),
                        nullResult()
                );
            } catch (final Exception exception) {
                if (exception instanceof AsyncExecutionException asyncExecutionException) {
                    throw asyncExecutionException;
                } else {
                    throw new AsyncExecutionException(exception);
                }
            }
        });
    }

    @Override
    @NonNull
    public <B> AsyncCombinable<B> lift(final @NonNull Higher1<? extends µ, ? extends Function<? super A, ? extends B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("liftA"));
        return new AsyncCombinable<>((executorService, timeout)
                -> ()
                -> this.map(fix(transformation).execute(executorService, timeout)).execute(executorService, timeout));
    }

    @Override
    @NonNull
    public <B> AsyncCombinable<B> bind(final @NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("bindM"));
        return new AsyncCombinable<>((executorService, timeout)
                -> ()
                -> fix(this.map(transformation).execute(executorService, timeout)).execute(executorService, timeout));
    }

    @Override
    @NonNull
    public AsyncCombiner<A> combine() {
        return new AsyncCombinerImpl<>(this);
    }

    public A execute(final @NonNull ExecutorService executorService,
                     final @NonNull Duration timeout)
            throws AsyncExecutionException {
        Objects.requireNonNull(executorService, nullValue("executorService"));
        Objects.requireNonNull(timeout, nullValue("timeout"));

        if (timeout.isPositive()) {    // TODO: In Checks auslagern
            this.executionLock.lock();
            try {
                return executorService.submit(this.spool.apply(executorService, timeout))
                        .get(timeout.toMillis(), TimeUnit.MILLISECONDS);
            } catch (final Exception exception) {
                Throwable current
                        = exception;
                while (current.getCause() != null) {
                    current
                            = current.getCause();
                }
                if (current instanceof AsyncExecutionException asyncExecutionException) {
                    throw asyncExecutionException;
                } else {
                    throw new AsyncExecutionException(current);
                }
            } finally {
                this.executionLock.unlock();
            }
        } else {
            throw new IllegalArgumentException(nonPositiveValue("timeout"));
        }
    }

    public sealed interface AsyncCombiner<A>
            extends Combiner<µ, A>
            permits AsyncCombinerImpl {

        @Override
        <B, R> AsyncCombiner<R> with(final @NonNull Higher1<µ, B> other, final @NonNull BiFunction<? extends A, ? extends B, ? super R> combiner);

        @Override
        AsyncCombinable<A> finish();

    }

    private static final class AsyncCombinerImpl<A>
            implements AsyncCombiner<A> {

        private final List<AsyncCombinable<Object>> asyncCombinables;
        private final List<Fun2<Object, Object, Object>> combiners;
        private final ReadWriteLock readWriteLock;

        @SuppressWarnings({"unchecked", "unused"})
        private AsyncCombinerImpl(final AsyncCombinable<A> head) {
            this.asyncCombinables
                    = new ArrayList<>();
            this.asyncCombinables.add((AsyncCombinable<Object>) head);
            this.combiners
                    = new CopyOnWriteArrayList<>();
            this.readWriteLock
                    = new ReentrantReadWriteLock(true);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <B, R> AsyncCombiner<R> with(final @NonNull Higher1<µ, B> async,
                                            final @NonNull BiFunction<? extends A, ? extends B, ? super R> combiner) {
            Objects.requireNonNull(async, nullValue("async"));
            Objects.requireNonNull(combiner, "combiner");
            this.readWriteLock.writeLock().lock();
            try {
                this.asyncCombinables.add((AsyncCombinable<Object>) async);
                this.combiners.add((Fun2<Object, Object, Object>) fun2(combiner));
                return (AsyncCombiner<R>) this;
            } finally { this.readWriteLock.writeLock().unlock(); }
        }

        @SuppressWarnings("unchecked")
        @Override
        public AsyncCombinable<A> finish() {
            this.readWriteLock.readLock().lock();
            try {
                if (asyncCombinables.size() > 1) {
                    return new AsyncCombinable<>((executorService, timeout) -> () -> {
                        final var futures
                                = this.asyncCombinables.stream()
                                .map(asyncCombinable -> executorService.submit(() -> asyncCombinable.execute(executorService, timeout)))
                                .toList();
                        try {
                            final var results
                                    = new LinkedList<>();
                            for (final var future : futures) {
                                results.add(future.get());
                            }
                            var tempResult
                                    = results.removeFirst();
                            for (final var combiner : combiners) {
                                tempResult
                                        = combiner.apply(tempResult, results.removeFirst());
                            }
                            return (A) tempResult;
                        } catch (final Exception exception) {
                            throw new AsyncExecutionException(exception);
                        } finally {
                            futures.stream()
                                    .filter(future -> !future.isDone())
                                    .forEach(future -> future.cancel(false));
                        }
                    });
                } else {
                    return (AsyncCombinable<A>) this.asyncCombinables.removeFirst();
                }
            } finally { this.readWriteLock.readLock().unlock(); }
        }

    }

}
