package org.quurz.foomp.base.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.quurz.foomp.higher.Higher1;
import org.slf4j.Logger;

import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.util.Task.narrow;
import static org.quurz.foomp.base.util.Task.task;
import static org.slf4j.LoggerFactory.getLogger;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TaskTest {

    private static final Logger LOGGER
        = getLogger(TaskTest.class);

    @Nested
    @DisplayName("Factory and Narrowing")
    class Factory_And_Narrowing {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void task_with_null_throws_NullPointerException() {
            LOGGER.info("Task.task(null) should throw NullPointerException");
            assertThatThrownBy(() -> task(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void task_with_value_creates_task() throws ExecutionException, InterruptedException {
            LOGGER.info("Task.task(value) should create a task with given value");
            final var t = task("test");
            assertThat(t).isNotNull();
            final var result = t.runAsync().get();
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.getValue()).isEqualTo("test");
        }

        @Test
        void task_without_arguments_creates_task_with_nothing() throws ExecutionException, InterruptedException {
            LOGGER.info("Task.task() should create a task with Nothing");
            final var t = task();
            assertThat(t).isNotNull();
            final var result = t.runAsync().get();
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.getValue()).isEqualTo(Nothing.nothing);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void narrow_with_null_throws_NullPointerException() {
            LOGGER.info("Task.narrow(null) should throw NullPointerException");
            assertThatThrownBy(() -> narrow(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void narrow_with_valid_higher_returns_same_task() {
            LOGGER.info("Task.narrow(higher) should return same Task instance");
            final var t = task("foobar");
            final Higher1<? extends Task.µ, String> higher = t;
            final var narrowed = narrow(higher);
            assertThat(narrowed).isSameAs(t);
        }

        @Test
        void narrow_with_foreign_higher_throws_IllegalArgumentException() {
            LOGGER.info("Task.narrow(foreignHigher) should throw IllegalArgumentException");
            final Higher1<Task.µ, String> fakeHigher = new FakeHigher<>();
            assertThatThrownBy(() -> narrow(fakeHigher))
                .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Nested
    @DisplayName("Behaviour")
    class Behaviour {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void map_with_null_throws_NullPointerException() {
            LOGGER.info("Task.map(null) should throw NullPointerException");
            assertThatThrownBy(() -> task(21).map(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void map_transforms_value() throws ExecutionException, InterruptedException {
            LOGGER.info("Task.map should transform value");
            final var t = task(21).map(i -> i * 2);
            final var result = t.runAsync().get();
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.getValue()).isEqualTo(42);
        }

        @Test
        void map_captures_exception_during_transformation() throws ExecutionException, InterruptedException {
            LOGGER.info("Task.map should capture exceptions into failed Result");
            final var t = task(21).map(_ -> {
                throw new IllegalStateException("Boom");
            });
            final var result = t.runAsync().get();
            assertThat(result.isSuccess()).isFalse();
            assertThat(result.getException()).isInstanceOf(IllegalStateException.class);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void applyTo_with_null_throws_NullPointerException() {
            LOGGER.info("Task.applyTo(null) should throw NullPointerException");
            assertThatThrownBy(() -> task(21).applyTo(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void applyTo_applies_function_from_task() throws ExecutionException, InterruptedException {
            LOGGER.info("Task.applyTo should apply function from another Task");
            final var tVal = task(21);
            final var tFn = task((java.util.function.Function<Integer, Integer>) (i -> i * 2));
            final var resultTask = tVal.applyTo(tFn);
            final var result = resultTask.runAsync().get();
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.getValue()).isEqualTo(42);
        }

        @Test
        void applyTo_captures_exception_during_application() throws ExecutionException, InterruptedException {
            LOGGER.info("Task.applyTo should capture exception during function application");
            final var tVal = task(21);
            final var tFn = task((java.util.function.Function<Integer, Integer>) (_ -> {
                throw new ArithmeticException("Division error");
            }));
            final var result = tVal.applyTo(tFn).runAsync().get();
            assertThat(result.isSuccess()).isFalse();
            assertThat(result.getException()).isInstanceOf(ArithmeticException.class);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void flatMap_with_null_throws_NullPointerException() {
            LOGGER.info("Task.flatMap(null) should throw NullPointerException");
            assertThatThrownBy(() -> task(21).flatMap(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void flatMap_binds_and_flattens_task() throws ExecutionException, InterruptedException {
            LOGGER.info("Task.flatMap should bind and flatten task");
            final var t = task(21).flatMap(i -> task(i * 2));
            final var result = t.runAsync().get();
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.getValue()).isEqualTo(42);
        }

        @Test
        void flatMap_captures_exception_during_transformation() throws ExecutionException, InterruptedException {
            LOGGER.info("Task.flatMap should capture exception during transformation");
            final var t = task(21).flatMap(_ -> {
                throw new IllegalArgumentException("Invalid");
            });
            final var result = t.runAsync().get();
            assertThat(result.isSuccess()).isFalse();
            assertThat(result.getException()).isInstanceOf(IllegalArgumentException.class);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void zip_with_null_other_throws_NullPointerException() {
            LOGGER.info("Task.zip(null, combiner) should throw NullPointerException");
            assertThatThrownBy(() -> task(21).zip(null, Integer::sum))
                .isInstanceOf(NullPointerException.class);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void zip_with_null_combiner_throws_NullPointerException() {
            LOGGER.info("Task.zip(other, null) should throw NullPointerException");
            assertThatThrownBy(() -> task(21).zip(task(21), null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void zip_combines_values_from_both_tasks() throws ExecutionException, InterruptedException {
            LOGGER.info("Task.zip should combine values from both tasks");
            final var t1 = task(20);
            final var t2 = task(22);
            final var zipped = t1.zip(t2, Integer::sum);
            final var result = zipped.runAsync().get();
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.getValue()).isEqualTo(42);
        }

        @Test
        void zip_captures_exception_from_combiner() throws ExecutionException, InterruptedException {
            LOGGER.info("Task.zip should capture exception thrown by combiner");
            final var t1 = task(20);
            final var t2 = task(22);
            final var zipped = t1.zip(t2, (_, _) -> {
                throw new ArithmeticException("Zip computation failed");
            });
            final var result = zipped.runAsync().get();
            assertThat(result.isSuccess()).isFalse();
            assertThat(result.getException()).isInstanceOf(ArithmeticException.class);
        }

        @Test
        void zip_propagates_failure_from_first_task() throws ExecutionException, InterruptedException {
            LOGGER.info("Task.zip should propagate failure from first task");
            final var t1 = task(20).<Integer>map(_ -> {
                throw new IllegalStateException("First failed");
            });
            final var t2 = task(22);
            final var zipped = t1.zip(t2, Integer::sum);
            final var result = zipped.runAsync().get();
            assertThat(result.isSuccess()).isFalse();
            assertThat(result.getException()).isInstanceOf(IllegalStateException.class);
        }

        @Test
        void zip_propagates_failure_from_second_task() throws ExecutionException, InterruptedException {
            LOGGER.info("Task.zip should propagate failure from second task");
            final var t1 = task(20);
            final var t2 = task(22).<Integer>map(_ -> {
                throw new IllegalStateException("Second failed");
            });
            final var zipped = t1.zip(t2, Integer::sum);
            final var result = zipped.runAsync().get();
            assertThat(result.isSuccess()).isFalse();
            assertThat(result.getException()).isInstanceOf(IllegalStateException.class);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void runAsync_with_null_executor_throws_NullPointerException() {
            LOGGER.info("Task.runAsync(null) should throw NullPointerException");
            assertThatThrownBy(() -> task(42).runAsync(null))
                .isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> task(42).map(i -> i * 2).runAsync(null))
                .isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> task(42).applyTo(task(i -> i * 2)).runAsync(null))
                .isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> task(42).flatMap(i -> task(i * 2)).runAsync(null))
                .isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> task(42).zip(task(1), Integer::sum).runAsync(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void runAsync_with_custom_executor_executes_successfully() throws ExecutionException, InterruptedException {
            LOGGER.info("Task.runAsync(executor) should execute with custom executor");
            final var executor = java.util.concurrent.Executors.newSingleThreadExecutor();
            try {
                final var result = task("asyncResult").runAsync(executor).get();
                assertThat(result.isSuccess()).isTrue();
                assertThat(result.getValue()).isEqualTo("asyncResult");
            } finally {
                executor.shutdown();
            }
        }

    }

    private static final class FakeHigher<A> implements Higher1<Task.µ, A> {
    }

}
