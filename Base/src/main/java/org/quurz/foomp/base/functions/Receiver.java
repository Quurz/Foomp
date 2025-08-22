package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.util.Nothing;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSuppliedFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Nothing.nothing;

/**
 * <div>
 *     <p>
 *         A {@code Receiver} is an enhanced {@link Consumer} that processes input values and
 *         exposes a functional interface suitable for combinator-style operations.
 *     </p>
 *     <p>
 *         This interface augments standard consumer behaviour with optional filtering via a
 *         {@link Predicate} before values are accepted. It also implements {@link Fun} for
 *         seamless integration with functional APIs.
 *     </p>
 *     <p>
 *         Contract: unless stated otherwise, inputs must not be {@code null}. Implementations
 *         should reject {@code null} values and must not produce {@code null} results where applicable.
 *     </p>
 * </div>
 *
 * @param <A> the type of accepted input values
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Receiver<A>
        extends Fun<A, Nothing>,
                Consumer<A> {

    /**
     * <div>
     *     <p>
     *         Creates a new {@code Receiver} that delegates to the given {@link Consumer} without filtering.
     *     </p>
     *     <p>
     *         Contract: {@code consumer} must not be {@code null}; accepted values must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param consumer the consumer to delegate to; must not be {@code null}
     * @param <A>      the input value type
     * @return a new {@code Receiver} wrapping the given {@code consumer}
     *
     * @throws NullPointerException if {@code consumer} is {@code null}
     */
    @SuppressWarnings("unused")
    static <A> Receiver<A> receiver(final @NonNull Consumer<A> consumer) {
        Objects.requireNonNull(consumer, nullValue("consumer"));
        return receiver(consumer, value -> true);
    }

    /**
     * <div>
     *     <p>
     *         Creates a new {@code Receiver} that delegates to the given {@link Consumer} and filters
     *         incoming values using the provided {@link Predicate}.
     *     </p>
     *     <p>
     *         Contract: {@code consumer} and {@code filter} must not be {@code null}; accepted values must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param consumer the consumer that processes accepted values; must not be {@code null}
     * @param filter   the acceptance condition; must not be {@code null}
     * @param <A>      the input value type
     * @return a new {@code Receiver} encapsulating the given logic
     *
     * @throws NullPointerException if {@code consumer} or {@code filter} is {@code null}
     */
    static <A> Receiver<A> receiver(final @NonNull Consumer<A> consumer,
                                    final @NonNull Predicate<A> filter) {
        Objects.requireNonNull(consumer, nullValue("consumer"));
        Objects.requireNonNull(filter, nullValue("filter"));
        return value -> {
            Objects.requireNonNull(value, nullValue("value"));
            if (filter.test(value)) {
                consumer.accept(value);
            }
        };
    }

    /**
     * <div>
     *     <p>
     *         Creates a new {@code Receiver} that delegates to the given {@link Consumer}, filters values
     *         with {@link Predicate}, and throws an exception built by {@code exceptionBuilder} for rejected values.
     *     </p>
     *     <p>
     *         Contract: {@code consumer}, {@code filter}, and {@code exceptionBuilder} must not be {@code null}.
     *         The {@code exceptionBuilder} must not return {@code null}. Accepted values must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param consumer         the consumer processing accepted values; must not be {@code null}
     * @param filter           the acceptance condition; must not be {@code null}
     * @param exceptionBuilder builds the exception for rejected values; must not be {@code null} and must not return {@code null}
     * @param <A>              the input value type
     * @return a new {@code Receiver} encapsulating the given logic
     *
     * @throws NullPointerException if any parameter is {@code null} or if {@code exceptionBuilder} returns {@code null}
     */
    static <A> Receiver<A> receiver(final @NonNull Consumer<A> consumer,
                                    final @NonNull Predicate<A> filter,
                                    final @NonNull Function<? super A, ? extends RuntimeException> exceptionBuilder) {
        Objects.requireNonNull(consumer, nullValue("consumer"));
        Objects.requireNonNull(filter, nullValue("filter"));
        Objects.requireNonNull(exceptionBuilder, nullValue("exceptionBuilder"));
        return value -> {
            Objects.requireNonNull(value, nullValue("value"));
            if (filter.test(value)) {
                consumer.accept(value);
            } else {
                throw Objects.requireNonNull(exceptionBuilder.apply(value), nullResultFrom("exceptionBuilder"));
            }
        };
    }

    /**
     * <div>
     *     <p>
     *         Processes a single input value and returns {@link Nothing} to satisfy the {@link Fun} contract.
     *     </p>
     *     <p>
     *         Contract: {@code value} must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param value the input value to process; must not be {@code null}
     * @return the {@link Nothing} singleton
     *
     * @throws NullPointerException if {@code value} is {@code null}
     */
    @Override
    @NonNull
    default Nothing apply(final @NonNull A value) {
        this.accept(Objects.requireNonNull(value, nullValue("value")));
        return nothing;
    }

    /**
     * <div>
     *     <p>
     *         Accepts a value and returns {@code this} to enable fluent chaining.
     *     </p>
     *     <p>
     *         Contract: {@code value} must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param value the value to process; must not be {@code null}
     * @return this {@code Receiver}
     *
     * @throws NullPointerException if {@code value} is {@code null}
     */
    @NonNull
    default Receiver<A> acceptAndContinue(final @NonNull A value) {
        this.accept(Objects.requireNonNull(value, nullValue("value")));
        return this;
    }

    /**
     * <div>
     *     <p>
     *         Processes the first element and any number of additional elements and returns this receiver.
     *     </p>
     *     <p>
     *         Contract: none of the elements may be {@code null}.
     *     </p>
     * </div>
     *
     * @param first  the first element to process; must not be {@code null}
     * @param others additional elements to process; must not be {@code null}
     * @return this {@code Receiver} for further use
     *
     * @throws NullPointerException if any element is {@code null}
     */
    @SuppressWarnings("unchecked")
    default Receiver<A> acceptAllAndContinue(final @NonNull A first,
                                             final @NonNull A... others) {
        Objects.requireNonNull(first, nullValue("first"));
        Objects.requireNonNull(others, nullValue("others"));
        this.accept(first);
        for (final A other : others) {
            this.accept(other);
        }
        return this;
    }

    /**
     * <div>
     *     <p>
     *         Processes all elements from the given {@link Iterable} and returns this receiver.
     *     </p>
     *     <p>
     *         Contract: {@code iterator} and all yielded elements must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param iterator an iterable collection of elements; must not be {@code null}
     * @return this {@code Receiver} for further use
     *
     * @throws NullPointerException if {@code iterator} is {@code null} or yields {@code null}
     */
    default Receiver<A> acceptAllAndContinue(final @NonNull Iterator<A> iterator) {
        Objects.requireNonNull(iterator, nullValue("iterator"));
        while (iterator.hasNext()) {
            final var value
                = Objects.requireNonNull(iterator.next(), nullSuppliedFrom("iterator"));
            this.accept(value);
        }
        return this;
    }

    /**
     * <div>
     *     <p>
     *         Processes all elements from the given {@link Stream} and returns this receiver.
     *     </p>
     *     <p>
     *         Contract: {@code values} and all of its elements must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param values a stream of elements to process; must not be {@code null}
     * @return this {@code Receiver} for further use
     *
     * @throws NullPointerException if {@code values} is {@code null} or contains {@code null}
     */
    default Receiver<A> acceptAllAndContinue(final @NonNull Stream<A> values) {
        Objects.requireNonNull(values, nullValue("values"));
        values.forEach(this);
        return this;
    }

    /**
     * <div>
     *     <p>
     *         Processes all elements from the given {@link Collection} and returns this receiver.
     *     </p>
     *     <p>
     *         Contract: {@code values} and all contained elements must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param values a collection of elements to process; must not be {@code null}
     * @return this {@code Receiver} for further use
     *
     * @throws NullPointerException if {@code values} is {@code null} or contains {@code null}
     */
    default Receiver<A> acceptAllAndContinue(final @NonNull Collection<A> values) {
        Objects.requireNonNull(values, nullValue("values"));
        for (final A value : values) {
            this.accept(value);
        }
        return this;
    }

}
