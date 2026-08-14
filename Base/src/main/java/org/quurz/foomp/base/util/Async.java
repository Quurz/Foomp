package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Monadic;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;
import sun.misc.Unsafe;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

@SuppressWarnings("NonAsciiCharacters")
public class Async<A>
        extends CompletableFuture<A>
        implements Monadic<Async.µ, A>,
                   Higher1<Async.µ, A> {

    public static final class µ implements WitnessType { private µ() {} }

    private static sealed class State<A>
            permits Async.State.Ready,
                    Async.State.Pending,
                    Async.State.Exceptional {

        static final class Ready<A>
                extends State<A> {

            final A value;

            Ready(final A value) {
                this.value
                    = value;
            }

        }

        static final class Pending<A>
                extends State<A> {

        }

        static final class Exceptional<A>
                extends State<A> {

            final Throwable exception;

            Exceptional(final Throwable exception) {
                this.exception
                    = exception;
            }

        }

    }

    public static <A> Async<A> narrow(final @NonNull Higher1<Async.µ, A> wide) {
        return (Async<A>) Objects.requireNonNull(wide, nullValue("wide"));
    }

    private volatile Supplier<State<A>> spool;

    private Async(final Supplier<State<A>> spool) {
        this.spool
            = spool;
    }

    @Override
    public @NonNull <B> Async<B> map(final @NonNull Function<? super A, ? extends B> transformation) {
        Objects.requireNonNull(transformation, "transformation");
        return null;    // TODO
    }

    @Override
    public @NonNull <B> Async<B> applyTo(final @NonNull Higher1<? extends µ, ? extends Function<? super A, ? extends B>> transformation) {
        Objects.requireNonNull(transformation, "transformation");
        return null;    // TODO
    }

    @Override
    public @NonNull <B> Async<B> flatMap(final @NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation) {
        Objects.requireNonNull(transformation, "transformation");
        return null;    // TODO
    }

}
