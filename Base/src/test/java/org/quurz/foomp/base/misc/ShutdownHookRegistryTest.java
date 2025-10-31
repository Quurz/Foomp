package org.quurz.foomp.base.misc;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.*;
import static org.quurz.foomp.base.misc.ShutdownHookRegistry.shutdownHook;
import static org.quurz.foomp.base.misc.ShutdownHookRegistry.shutdownHookRegistry;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("ShutdownHookRegistry")
class ShutdownHookRegistryTest {

    private static final Logger LOGGER = getLogger(ShutdownHookRegistryTest.class);

    @Nested
    @DisplayName("ShutdownHook factory method tests")
    class ShutdownHookFactoryTests {

        @Test
        @DisplayName("shutdownHook() creates valid hook")
        void shutdownHookCreatesValidHook() {
            final var executed = new AtomicBoolean(false);
            final var hook = shutdownHook(
                    "TestHook",
                    10,
                    () -> executed.set(true),
                    Duration.ofSeconds(5)
            );

            assertThat(hook.getName()).isEqualTo("TestHook");
            assertThat(hook.getPriority()).isEqualTo(10);
            assertThat(hook.getTimeout()).isEqualTo(Duration.ofSeconds(5));

            // Verify action works
            hook.getAction().run();
            assertThat(executed.get()).isTrue();
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("shutdownHook() throws on null name")
        void shutdownHookThrowsOnNullName() {
            assertThatThrownBy(() -> shutdownHook(null, 0, () -> {}, Duration.ofSeconds(1)))
                    .isInstanceOf(NullPointerException.class);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("shutdownHook() throws on null action")
        void shutdownHookThrowsOnNullAction() {
            assertThatThrownBy(() -> shutdownHook("Test", 0, null, Duration.ofSeconds(1)))
                    .isInstanceOf(NullPointerException.class);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("shutdownHook() throws on null timeout")
        void shutdownHookThrowsOnNullTimeout() {
            assertThatThrownBy(() -> shutdownHook("Test", 0, () -> {}, null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("shutdownHook() throws on negative priority")
        void shutdownHookThrowsOnNegativePriority() {
            assertThatThrownBy(() -> shutdownHook("Test", -1, () -> {}, Duration.ofSeconds(1)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("non-negative");
        }

        @Test
        @DisplayName("shutdownHook() throws on zero timeout")
        void shutdownHookThrowsOnZeroTimeout() {
            assertThatThrownBy(() -> shutdownHook("Test", 0, () -> {}, Duration.ZERO))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("positive");
        }

        @Test
        @DisplayName("shutdownHook() throws on negative timeout")
        void shutdownHookThrowsOnNegativeTimeout() {
            assertThatThrownBy(() -> shutdownHook("Test", 0, () -> {}, Duration.ofSeconds(-1)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("positive");
        }
    }

    @Nested
    @DisplayName("ShutdownHook comparison and equality tests")
    class ShutdownHookComparisonTests {

        @Test
        @DisplayName("compareTo() orders by priority descending")
        void compareToOrdersByPriorityDescending() {
            final var hook1 = shutdownHook("A", 10, () -> {}, Duration.ofSeconds(1));
            final var hook2 = shutdownHook("B", 20, () -> {}, Duration.ofSeconds(1));
            final var hook3 = shutdownHook("C", 5, () -> {}, Duration.ofSeconds(1));

            assertThat(hook2.compareTo(hook1)).isLessThan(0); // 20 before 10
            assertThat(hook1.compareTo(hook3)).isLessThan(0); // 10 before 5
            assertThat(hook3.compareTo(hook2)).isGreaterThan(0); // 5 after 20
        }

        @Test
        @DisplayName("compareTo() orders by name when priority is equal")
        void compareToOrdersByNameWhenPriorityEqual() {
            final var hookA = shutdownHook("Alpha", 10, () -> {}, Duration.ofSeconds(1));
            final var hookB = shutdownHook("Beta", 10, () -> {}, Duration.ofSeconds(1));
            final var hookC = shutdownHook("Gamma", 10, () -> {}, Duration.ofSeconds(1));

            assertThat(hookA.compareTo(hookB)).isLessThan(0);
            assertThat(hookB.compareTo(hookC)).isLessThan(0);
            assertThat(hookC.compareTo(hookA)).isGreaterThan(0);
        }

        @Test
        @DisplayName("equals() and hashCode() based on name and priority")
        void equalsAndHashCodeBasedOnNameAndPriority() {
            final var hook1 = shutdownHook("Test", 10, () -> {}, Duration.ofSeconds(1));
            final var hook2 = shutdownHook("Test", 10, () -> {}, Duration.ofSeconds(999));
            final var hook3 = shutdownHook("Test", 20, () -> {}, Duration.ofSeconds(1));
            final var hook4 = shutdownHook("Other", 10, () -> {}, Duration.ofSeconds(1));

            // Same name and priority = equal (timeout doesn't matter)
            assertThat(hook1).isEqualTo(hook2);
            assertThat(hook1.hashCode()).isEqualTo(hook2.hashCode());

            // Different priority
            assertThat(hook1).isNotEqualTo(hook3);

            // Different name
            assertThat(hook1).isNotEqualTo(hook4);
        }

        @Test
        @DisplayName("toString() contains name, priority, and timeout")
        void toStringContainsRelevantInfo() {
            final var hook = shutdownHook("TestHook", 42, () -> {}, Duration.ofSeconds(30));
            final var str = hook.toString();

            assertThat(str).contains("TestHook");
            assertThat(str).contains("42");
            assertThat(str).contains("30");
        }
    }

    @Nested
    @DisplayName("Builder tests")
    class BuilderTests {

        @Test
        @DisplayName("builder creates empty registry by default")
        void builderCreatesEmptyRegistryByDefault() {
            final var registry = shutdownHookRegistry().build();

            assertThat(registry.size()).isZero();
            assertThat(registry.isInstalled()).isFalse();
        }

        @Test
        @DisplayName("register() adds hook")
        void registerAddsHook() {
            final var hook = shutdownHook("Test", 0, () -> {}, Duration.ofSeconds(1));
            final var registry = shutdownHookRegistry()
                    .register(hook)
                    .build();

            assertThat(registry.size()).isEqualTo(1);
        }

        @Test
        @DisplayName("register() with parameters creates and adds hook")
        void registerWithParametersCreatesAndAddsHook() {
            final var registry = shutdownHookRegistry()
                    .register("Test", 10, () -> {}, Duration.ofSeconds(5))
                    .build();

            assertThat(registry.size()).isEqualTo(1);
        }

        @Test
        @DisplayName("register() multiple hooks")
        void registerMultipleHooks() {
            final var registry = shutdownHookRegistry()
                    .register("Hook1", 10, () -> {}, Duration.ofSeconds(1))
                    .register("Hook2", 20, () -> {}, Duration.ofSeconds(2))
                    .register("Hook3", 5, () -> {}, Duration.ofSeconds(3))
                    .build();

            assertThat(registry.size()).isEqualTo(3);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("register() throws on null hook")
        void registerThrowsOnNullHook() {
            assertThatThrownBy(() -> shutdownHookRegistry().register(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @SuppressWarnings("unused")
        @Test
        @DisplayName("withLogAdapter() sets custom logger")
        void withLogAdapterSetsCustomLogger() {
            final List<String> logMessages = new ArrayList<>();
            final var logAdapter = LogAdapter.delegatingLogAdapter(
                    (msg, _$) -> logMessages.add("DEBUG: " + msg),
                    (msg, _$) -> logMessages.add("INFO: " + msg),
                    (msg, _$) -> logMessages.add("WARN: " + msg),
                    (msg, _$) -> logMessages.add("ERROR: " + msg)
            );

            final var registry = shutdownHookRegistry()
                    .withLogAdapter(logAdapter)
                    .register("Test", 0, () -> {}, Duration.ofSeconds(1))
                    .build();

            // Install triggers info log
            registry.install();

            assertThat(logMessages).anyMatch(msg -> msg.contains("INFO") && msg.contains("installed"));
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("withLogAdapter() throws on null")
        void withLogAdapterThrowsOnNull() {
            assertThatThrownBy(() -> shutdownHookRegistry().withLogAdapter(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Registry installation tests")
    class RegistryInstallationTests {

        @Test
        @DisplayName("install() marks registry as installed")
        void installMarksRegistryAsInstalled() {
            final var registry = shutdownHookRegistry()
                    .register("Test", 0, () -> {}, Duration.ofSeconds(1))
                    .build();

            assertThat(registry.isInstalled()).isFalse();
            registry.install();
            assertThat(registry.isInstalled()).isTrue();
        }

        @Test
        @DisplayName("install() throws on second call")
        void installThrowsOnSecondCall() {
            final var registry = shutdownHookRegistry()
                    .register("Test", 0, () -> {}, Duration.ofSeconds(1))
                    .build();

            registry.install();

            assertThatThrownBy(registry::install)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("already installed");
        }

        @SuppressWarnings("unused")
        @Test
        @DisplayName("install() logs installation")
        void installLogsInstallation() {
            final List<String> logMessages = new ArrayList<>();
            final var logAdapter = LogAdapter.delegatingLogAdapter(
                    (msg, _$) -> {},
                    (msg, _$) -> logMessages.add(msg),
                    (msg, _$) -> {},
                    (msg, _$) -> {}
            );

            final var registry = shutdownHookRegistry()
                    .withLogAdapter(logAdapter)
                    .register("Test", 0, () -> {}, Duration.ofSeconds(1))
                    .build();

            registry.install();

            assertThat(logMessages)
                    .anyMatch(msg -> msg.contains("installed") && msg.contains("1 hook"));
        }
    }

    @Nested
    @DisplayName("Hook execution simulation tests")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class HookExecutionSimulationTests {

        // Note: We can't easily test actual shutdown hook execution,
        // but we can test the execution logic by exposing it or using reflection

        @Test
        @DisplayName("hooks execute in priority order (simulated)")
        void hooksExecuteInPriorityOrder() throws Exception {
            final List<String> executionOrder = new ArrayList<>();

            final var registry = shutdownHookRegistry()
                    .register("Low", 1, () -> executionOrder.add("Low"), Duration.ofSeconds(1))
                    .register("High", 100, () -> executionOrder.add("High"), Duration.ofSeconds(1))
                    .register("Medium", 50, () -> executionOrder.add("Medium"), Duration.ofSeconds(1))
                    .build();

            // Use reflection to call private executeAllHooks method
            final var method = ShutdownHookRegistry.class.getDeclaredMethod("executeAllHooks");
            method.setAccessible(true);
            method.invoke(registry);

            // Wait a bit for execution
            Thread.sleep(100);

            assertThat(executionOrder).containsExactly("High", "Medium", "Low");
        }

        @Test
        @DisplayName("hook timeout is enforced (simulated)")
        void hookTimeoutIsEnforced() throws Exception {
            final var latch = new CountDownLatch(1);
            final var timedOut = new AtomicBoolean(false);

            final var registry = shutdownHookRegistry()
                    .register("SlowHook", 0, () -> {
                        try {
                            Thread.sleep(5000); // Sleep longer than timeout
                        } catch (InterruptedException e) {
                            timedOut.set(true);
                        } finally {
                            latch.countDown();
                        }
                    }, Duration.ofMillis(100)) // Short timeout
                    .build();

            final var method = ShutdownHookRegistry.class.getDeclaredMethod("executeAllHooks");
            method.setAccessible(true);

            new Thread(() -> {
                try {
                    method.invoke(registry);
                } catch (Exception e) {
                    LOGGER.error("Error invoking executeAllHooks", e);
                }
            }).start();

            // Wait for hook to complete or timeout
            assertThat(latch.await(2, TimeUnit.SECONDS)).isTrue();
            assertThat(timedOut.get()).isTrue();
        }

        @Test
        @DisplayName("failing hook doesn't prevent others from executing (simulated)")
        void failingHookDoesntPreventOthers() throws Exception {
            final var counter = new AtomicInteger(0);

            final var registry = shutdownHookRegistry()
                    .register("FailingHook", 100, () -> {
                        counter.incrementAndGet();
                        throw new RuntimeException("Test failure");
                    }, Duration.ofSeconds(1))
                    .register("SuccessfulHook", 50, counter::incrementAndGet, Duration.ofSeconds(1))
                    .build();

            final var method = ShutdownHookRegistry.class.getDeclaredMethod("executeAllHooks");
            method.setAccessible(true);
            method.invoke(registry);

            Thread.sleep(100);

            // Both hooks should have executed
            assertThat(counter.get()).isEqualTo(2);
        }
    }
}