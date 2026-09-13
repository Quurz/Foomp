package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Result.failure;
import static org.quurz.foomp.base.util.Result.success;

@SuppressWarnings("NonAsciiCharacters")
public class Task<A> {

    public static <A> Task<A> promise(final @NonNull A value) {
        Objects.requireNonNull(value, nullValue("value"));
        return new Task<>(new State.Ready<>(value));
    }

    private sealed interface State<A>
            permits State.Ready,
                    State.Running,
                    State.Exceptional {

        final class Ready<A>
                implements State<A> {

            private final A value;

            private Ready(final A value) {
                this.value
                    = value;
            }

            private A getValue() {
                return this.value;
            }

        }

        final class Running<A>
                implements State<A> {

            private final Supplier<A> supplier;

            private Running(final Supplier<A> supplier) {
                this.supplier
                    = supplier;
            }

            private Supplier<A> getSupplier() {
                return this.supplier;
            }

        }

        final class Exceptional<A>
                implements State<A> {

            private final Exception exception;

            private Exceptional(final Exception exception) {
                this.exception
                    = exception;
            }

            private Exception getException() {
                return this.exception;
            }

        }

    }

    private static <A> State<A> ready(final A value) {
        return new State.Ready<>(value);
    }

    private static <A> State<A> running(final Supplier<A> supplier) {
        return new State.Running<>(supplier);
    }

    private static <A> State<A> exceptional(final Exception exception) {
        return new State.Exceptional<>(exception);
    }

    private final Queue<Thread> parkedThreads;

    private volatile State<A> state;

    private Task(final State<A> currentState) {
        this.parkedThreads
            = new ConcurrentLinkedQueue<>();

        this.state
            = currentState;
    }

    public Result<A> get()
            throws NoSuchElementException {

        if (!(this.state instanceof State.Ready<?>)) {
            LockSupport.park();
            this.parkedThreads.add(Thread.currentThread());
        }

        return switch (this.state) {
            case State.Ready<A> ready -> success(ready.getValue());
            case State.Exceptional<A> exceptional -> failure(exceptional.getException());
            default -> throw new IllegalStateException("Unexpected state: " + this.state);
        };

    }


}
