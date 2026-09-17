package org.quurz.foomp.base;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.functions.Fun2;
import org.quurz.foomp.base.types.Value;
import org.quurz.foomp.base.types.XorValue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.mock;

public class TestHelper {

    /*
            Constants
     */

    /*
        A generic placeholder string value used for testing.
     */
    protected final String SOME_STRING_VALUE
        = "<Some_String_Value>";

    /*
        A secondary placeholder string value used for distinct equality testing.
     */
    protected final String SOME_OTHER_STRING_VALUE
        = "<Some_Other_String_Value>";

    /*
        A sample error message placeholder for negative test cases.
     */
    protected final String OUCH
        = "<Ouch>";


    /*
            Checks
     */

    /*
        Checks that the given value is non-null, present, and accessible without throwing an exception.
     */
    protected final void checkIsSome(final Value<?> value) {
        assertThat(value)
            .isNotNull();
        assertThat(value.isPresent())
            .isTrue();
        assertThatNoException()
            .isThrownBy(value::get);
        assertThat(value.get())
            .isNotNull();
    }

    /*
        Checks that the given value is present and contains the expected content.
     */
    protected final <T> void checkIsSomeWithValue(final Value<T> value,
                                                  final T expected) {
        this.checkIsSome(value);
        assertThat(value.get())
            .isEqualTo(expected);
    }

    /*
        Checks that the given value is present and matches the given predicate.
     */
    protected final <T> void checkIsSomeWithPredicate(final Value<T> value,
                                                      final Predicate<T> predicate) {
        this.checkIsSome(value);
        assertThat(predicate.test(value.get()))
            .isTrue();
    }

    /*
        Checks that the given value is non-null, empty (not present), and throws NoSuchElementException when accessing its content.
     */
    protected final void checkIsNone(final Value<?> value) {
        assertThat(value)
            .isNotNull();
        assertThat(value.isPresent())
            .isFalse();
        assertThatThrownBy(value::get)
            .isInstanceOf(NoSuchElementException.class);
    }

    /*
        Checks that the given xor-value is a valid Right instance, present, accessible via get/getRight, and throws NoSuchElementException when attempting to access Left.
     */
    protected final void checkIsRight(final XorValue<?, ?> xorValue) {
        assertThat(xorValue)
            .isNotNull();
        assertThat(xorValue.isPresent())
            .isTrue();
        assertThat(xorValue.isRight())
            .isTrue();
        assertThat(xorValue.isLeft())
            .isFalse();
        assertThatNoException()
            .isThrownBy(xorValue::get);
        assertThat(xorValue.get())
            .isEqualTo(xorValue.getRight());
        assertThatThrownBy(xorValue::getLeft)
            .isInstanceOf(NoSuchElementException.class);
    }

    /*
        Checks that the given xor-value is Right and holds the expected value.
     */
    protected final <R> void checkIsRightWithValue(final XorValue<?, R> xorValue,
                                                   final R expected) {
        this.checkIsRight(xorValue);
        assertThat(xorValue.get())
                .isEqualTo(expected);
    }

    /*
        Checks that the given xor-value is Right and matches the given predicate.
     */
    protected final <R> void checkIsRightWithPredicate(final XorValue<?, R> xorValue,
                                                       final Predicate<? super R> predicate) {
        this.checkIsRight(xorValue);
        assertThat(predicate.test(xorValue.getRight()))
            .isTrue();
    }

    /*
        Checks that the given xor-value is a valid Left instance, not present, accessible via getLeft, and throws NoSuchElementException when attempting to access Right.
     */
    protected final void checkIsLeft(final XorValue<?, ?> xorValue) {
        assertThat(xorValue)
            .isNotNull();
        assertThat(xorValue.isPresent())
            .isFalse();
        assertThat(xorValue.isRight())
            .isFalse();
        assertThat(xorValue.isLeft())
            .isTrue();
        assertThatNoException()
            .isThrownBy(xorValue::getLeft);
        assertThatThrownBy(xorValue::getRight)
            .isInstanceOf(NoSuchElementException.class);
    }

    /*
        Checks that the given xor-value is Left and holds the expected value.
     */
    protected final <L> void checkIsLeftWithValue(final XorValue<L, ?> xorValue,
                                                  final L expected) {
        this.checkIsLeft(xorValue);
        assertThat(xorValue.getLeft())
            .isEqualTo(expected);
    }

    /*
        Checks that the given xor-value is Left and matches the given predicate.
     */
    protected final <L> void checkIsLeftWithPredicate(final XorValue<L, ?> xorValue,
                                                      final Predicate<? super L> predicate) {
        this.checkIsLeft(xorValue);
        assertThat(predicate.test(xorValue.getLeft()))
            .isTrue();
    }


    /*
            Functions
     */

    /*
        Wraps a unary function with an artificial delay to simulate long-running or asynchronous operations.
     */
    protected <X, Y> Fun<X, Y> waitingFun(final Fun<X, Y> fun,
                                          final Duration wait) {
        return x -> {
            try {
                Thread.sleep(wait.toMillis());
                return fun.apply(x);
            } catch (final Exception exception) {
                throw new RuntimeException(exception);
            }
        };
    }

    /*
        Wraps a binary function with an artificial delay to simulate long-running or asynchronous operations.
     */
    protected <X1, X2, Y> Fun2<X1, X2, Y> waitingFun(final Fun2<X1, X2, Y> fun,
                                                     final Duration wait) {
        return (x1, x2) -> {
            try {
                Thread.sleep(wait.toMillis());
                return fun.apply(x1, x2);
            } catch (final Exception exception) {
                throw new RuntimeException(exception);
            }
        };
    }

    /*
        A helper function returning the length of a given string.
     */
    protected final Fun<String, Integer> funStringLength
        = String::length;

    /*
        A function wrapper interface that tracks the number of times it has been executed.
     */
    protected interface InvocationCountingFun<X, Y> extends Fun<X, Y> {

        int getInvocationCount();

        void resetInvocationCount();

    }

    /*
        Creates a thread-safe invocation-counting function wrapper around the given function.
     */
    protected final <X, Y> InvocationCountingFun<X ,Y> invocationCountingFun(final Function<X, Y> function) {
        return new InvocationCountingFun<>() {
            private final AtomicInteger invocationCount
                = new AtomicInteger(0);

            @Override
            public @NonNull Y apply(@NonNull X x) {
                invocationCount.incrementAndGet();
                return function.apply(x);
            }

            @Override
            public int getInvocationCount() {
                return this.invocationCount.get();
            }

            @Override
            public void resetInvocationCount() {
                this.invocationCount.set(0);
            }
        };
    }

    /*
        A supplier wrapper interface that tracks the number of times it has been called.
     */
    protected interface InvocationCountingSupplier<A>
            extends Supplier<A> {

        int getInvocationCount();

        void resetInvocationCount();

    }

    /*
        Creates a thread-safe invocation-counting supplier wrapper around the given supplier.
     */
    protected final <A> InvocationCountingSupplier<A> invocationCountingSupplier(final Supplier<A> supplier) {
        return new InvocationCountingSupplier<>() {
            private final AtomicInteger invocationCount
                = new AtomicInteger(0);

            @Override
            public A get() {
                invocationCount.incrementAndGet();
                return supplier.get();
            }

            @Override
            public int getInvocationCount() {
                return this.invocationCount.get();
            }

            @Override
            public void resetInvocationCount() {
                this.invocationCount.set(0);
            }
        };
    }

    /*
        A consumer wrapper interface that tracks the number of times it has been called.
     */
    protected interface InvocationCountingConsumer<A>
            extends Consumer<A> {

        int getInvocationCount();

        void resetInvocationCount();

    }

    /*
        Creates a thread-safe invocation-counting consumer wrapper around the given consumer.
     */
    protected <A> InvocationCountingConsumer<A> invocationCountingConsumer(final Consumer<A> consumer) {
        return new InvocationCountingConsumer<A>() {
            private final AtomicInteger invocationCount
                = new AtomicInteger(0);

            @Override
            public void accept(final A a) {
                invocationCount.incrementAndGet();
                consumer.accept(a);
            }

            @Override
            public int getInvocationCount() {
                return this.invocationCount.get();
            }

            @Override
            public void resetInvocationCount() {
                this.invocationCount.set(0);
            }
        };
    }


    /*
            Misc
     */

    /*
        Returns a new list containing the elements of the specified list in randomized order without mutating the input list.
     */
    protected final <A> List<A> shuffle(final List<A> list) {
        Objects.requireNonNull(list);

        final var random
            = new Random();
        final var copy
            = new ArrayList<A>(list);
        final var shuffledList
            = new ArrayList<A>(list.size());

        while (!copy.isEmpty()) {
            var randomIndex
                = random.nextInt(copy.size());
            if (copy.size() == 1) {
                randomIndex = 0;
            }
            shuffledList.add(copy.remove(randomIndex));
        }

        return shuffledList;
    }


}
