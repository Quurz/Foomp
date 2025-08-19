package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

public sealed interface Validator<A, FAILURE>
        permits Validator.Node,
                Validator.Leaf {

    default void fooBar(final Function<A, Maybe<FAILURE>> foo,   // foo: Constraint implementiert Function<A, Maybe<FAILURE>>
                        final BiPredicate<A, FAILURE> bar) {     // Prüfung auf FATAL

    }

    // Versuch, mal alles zusammen zu schrauben. Naja. Jedenfalls ein Versuch des ersten Schritts.
    default void fooBarBaz(final BiFunction<Function<A, Maybe<FAILURE>>, BiPredicate<A, FAILURE>, Tuple2<Maybe<FAILURE>, Boolean>> baz) {

    }

    default void validate(final @Nullable A value) {

    }

    default void validateParallel(final @Nullable A value) {

    }

    default void validateParallel(final @Nullable A value,
                                  final @NonNull Executor executor) {

    }

    final class Node<A, FAILURE>
            implements Validator<A, FAILURE> {

    }

    final class Leaf<A, FAILURE>
            implements Validator<A, FAILURE> {

    }

}
