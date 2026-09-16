package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Maybe.maybeOfNullable;

class DoubleLinked<A> {

    private final @NonNull A value;

    private DoubleLinked<A> prev;
    private DoubleLinked<A> next;

    DoubleLinked(final @NonNull A value) {
        Objects.requireNonNull(value, nullValue("value"));
        this.value
            = value;
    }

    @NonNull A getValue() {
        return this.value;
    }

    Maybe<DoubleLinked<A>> getNext() {
        return maybeOfNullable(this.next);
    }

    Maybe<DoubleLinked<A>> getPrev() {
        return maybeOfNullable(this.prev);
    }

    DoubleLinked<A> appendNoCopy(final A element) {
        final var newNext
            = new DoubleLinked<>(element);
        this.next
            = newNext;
        newNext.prev
            = this;
        return newNext;
    }

}
