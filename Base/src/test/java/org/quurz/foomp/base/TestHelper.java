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

/**
 * TODO: Nach JUnit Jupiter Best Practices refaktorisieren.
 * - Parametrisierte Tests einsetzen (@ParameterizedTest + @ValueSource/@NullSource/@CsvSource/@MethodSource)
 * - Lesbarkeit: @DisplayName / @DisplayNameGeneration(ReplaceUnderscores)
 * - Wiederverwendbare AssertJ-Assertions für Value/XorValue (z. B. ValueAssert, XorValueAssert)
 * - Zeitprüfungen via Assertions.assertTimeout statt waitingFun(...)
 * - @TempDir für temporäre Dateien/Verzeichnisse
 * - invocationCounting-* ggf. in kleine Test-Utils auslagern
 */
public class TestHelper {

    /*
            Constants
     */

    protected final String SOME_STRING_VALUE
        = "<Some_String_Value>";
    protected final String SOME_OTHER_STRING_VALUE
        = "<Some_Other_String_Value>";
    protected final String OUCH
        = "<Ouch>";


    /*
            Checks
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

    protected final <T> void checkIsSomeWithValue(final Value<T> value,
                                                  final T expected) {
        this.checkIsSome(value);
        assertThat(value.get())
            .isEqualTo(expected);
    }

    protected final <T> void checkIsSomeWithPredicate(final Value<T> value,
                                                      final Predicate<T> predicate) {
        this.checkIsSome(value);
        assertThat(predicate.test(value.get()))
            .isTrue();
    }

    protected final void checkIsNone(final Value<?> value) {
        assertThat(value)
            .isNotNull();
        assertThat(value.isPresent())
            .isFalse();
        assertThatThrownBy(value::get)
            .isInstanceOf(NoSuchElementException.class);
    }

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

    protected final <R> void checkIsRightWithValue(final XorValue<?, R> xorValue,
                                                   final R expected) {
        this.checkIsRight(xorValue);
        assertThat(xorValue.get())
                .isEqualTo(expected);
    }

    protected final <R> void checkIsRightWithPredicate(final XorValue<?, R> xorValue,
                                                       final Predicate<? super R> predicate) {
        this.checkIsRight(xorValue);
        assertThat(predicate.test(xorValue.getRight()))
            .isTrue();
    }

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

    protected final <L> void checkIsLeftWithValue(final XorValue<L, ?> xorValue,
                                                  final L expected) {
        this.checkIsLeft(xorValue);
        assertThat(xorValue.getLeft())
            .isEqualTo(expected);
    }

    protected final <L> void checkIsLeftWithPredicate(final XorValue<L, ?> xorValue,
                                                      final Predicate<? super L> predicate) {
        this.checkIsLeft(xorValue);
        assertThat(predicate.test(xorValue.getLeft()))
            .isTrue();
    }


    /*
            Functions
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

    protected final Fun<String, Integer> funStringLength
        = String::length;

    protected interface InvocationCountingFun<X, Y> extends Fun<X, Y> {

        int getInvocationCount();

        void resetInvocationCount();

    }

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

    protected interface InvocationCountingSupplier<A>
            extends Supplier<A> {

        int getInvocationCount();

        void resetInvocationCount();

    }

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

    protected interface InvocationCountingConsumer<A>
            extends Consumer<A> {

        int getInvocationCount();

        void resetInvocationCount();

    }

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

    /**
     * <div>
     *     <p>
     *         Mischt die Elemente der angegebenen Liste zuf&auml;llig neu und gibt eine neue Liste mit der gemischten Reihenfolge zur&uuml;ck.
     *         Die Eingabeliste bleibt dabei unver&auml;ndert, da intern eine Kopie erstellt wird.
     *     </p>
     *     <p>
     *         Diese Methode kann beispielsweise verwendet werden, um zuf&auml;llige Permutationen von Elementen zu testen,
     *         wie im Fall des Testens von Einf&uuml;geoperationen in einem RedBlackTree.
     *     </p>
     * </div>
     *
     * @param list Die Liste, deren Elemente gemischt werden sollen. Diese Liste bleibt unver&auml;ndert.
     * @param <A> Der Typ der Elemente in der Liste.
     * @return Eine neue Liste, die die Elemente der Eingabeliste in zuf&auml;lliger Reihenfolge enth&auml;lt.
     *
     * @throws NullPointerException Wenn die angegebene Liste <code>null</code> ist.
     *
     * @since 1.0.0
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
