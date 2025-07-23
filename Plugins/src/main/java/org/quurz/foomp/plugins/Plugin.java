package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.plugins.DummyReadWriteLock.dummyReadWriteLock;

public class Plugin<A>
        extends AbstractDelegatingProxy<A> {

    public static <A> Plugin<A> plugin(final @NonNull A $__delegate) {
        Objects.requireNonNull($__delegate, nullValue(DELEGATE_FIELD_NAME));
        return plugin($__delegate, true);
    }

    public static <A> Plugin<A> plugin(final @NonNull A $__delegate,
                                       final boolean lockDelegate) {
        Objects.requireNonNull($__delegate, nullValue(DELEGATE_FIELD_NAME));
        return new Plugin<>($__delegate, lockDelegate);
    }

    private Plugin(final A $__delegate,
                   final boolean lockDelegate) {
        super(
            $__delegate,
            lockDelegate
                ? new ReentrantReadWriteLock(true)
                : dummyReadWriteLock()
        );
    }

}
