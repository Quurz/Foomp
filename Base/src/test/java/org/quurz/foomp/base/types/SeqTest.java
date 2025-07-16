package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Test;
import org.quurz.foomp.higher.Higher1;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

class SeqTest {

    private static final Logger LOGGER
        = getLogger(SeqTest.class);

    @SuppressWarnings({"DataFlowIssue", "NullableProblems"})
    private static final class TestSeq
            implements Seq<Integer> {

        private final boolean empty;

        public TestSeq(boolean empty) {
            this.empty
                = empty;
        }

        @Override
        public boolean isNotEmpty() {
            return !this.empty;
        }

        @Override
        public @NonNull Integer head() throws NoSuchElementException {
            return 0;
        }

        @Override
        public @NonNull Seq<Integer> tail() {
            return null;
        }

        @Override
        public @NonNull Seq<Integer> cons(@NonNull Integer element) {
            return null;
        }

        @Override
        public @NonNull Seq<Integer> consAll(@NonNull Higher1<? extends µ, Integer> other) {
            return null;
        }

        @Override
        public @NonNull Value2<Integer, ? extends Seq<Integer>> decons() throws NoSuchElementException {
            return null;
        }

        @Override
        public @NonNull Value2<? extends Seq<Integer>, ? extends Seq<Integer>> split(@NonNull Predicate<? super Integer> predicate) {
            return null;
        }

        @Override
        public @NonNull Value2<? extends Seq<Integer>, ? extends Seq<Integer>> split() throws IllegalStateException {
            return null;
        }

        @Override
        public @NonNull Seq<Integer> filter(@NonNull Predicate<? super Integer> pred) {
            return null;
        }

        @Override
        public @NonNull Collection<Integer> toCollection(@NonNull Supplier<Collection<Integer>> init) {
            return List.of();
        }

        @Override
        public boolean contains(@NonNull Integer element) {
            return false;
        }

        @Override
        public Integer search(@NonNull Integer element) throws NoSuchElementException {
            return 0;
        }

        @Override
        public @NonNull Stream<Integer> stream() {
            return Stream.empty();
        }

        @Override
        public boolean isPresent() {
            return false;
        }

        @Override
        public @NonNull Integer get() throws NoSuchElementException {
            return 0;
        }

        @Override
        public Iterator<Integer> iterator() {
            return null;
        }
    }

    final TestSeq emptySeq
        = new TestSeq(true);
    final TestSeq nonEmptySeq
        = new TestSeq(false);

    @Test
    void testIsEmpty() {
        LOGGER.info("Test seq.isEmpty and seq.isNotEmpty");

        assertThat(emptySeq.isEmpty())
            .isTrue();
        assertThat(emptySeq.isNotEmpty())
            .isFalse();

        assertThat(nonEmptySeq.isNotEmpty())
            .isTrue();
        assertThat(nonEmptySeq.isEmpty())
            .isFalse();
    }

}
