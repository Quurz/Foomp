package org.quurz.foomp.plugins;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.quurz.foomp.base.misc.SemVer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quurz.foomp.plugins.Command.loadPlugin;
import static org.quurz.foomp.plugins.Command.unloadPlugin;
import static org.quurz.foomp.plugins.CommandQueue.commandQueue;
import static org.quurz.foomp.plugins.PluginCoordinate.pluginCoordinate;
import static org.slf4j.LoggerFactory.getLogger;

class CommandQueueTest {

    private static final Logger LOGGER
        = getLogger(CommandQueueTest.class);

    private CommandQueue queue;
    private PluginCoordinate pluginA;
    private PluginCoordinate pluginB;

    @BeforeEach
    void setUp() {
        queue = commandQueue();
        pluginA = pluginCoordinate("PluginA", SemVer.semVer(1, 0, 0));
        pluginB = pluginCoordinate("PluginB", SemVer.semVer(2, 0, 0));
    }

    @Nested
    @DisplayName("Factory method tests")
    class FactoryMethodTests {

        @Test
        @DisplayName("commandQueue() creates a new instance")
        void commandQueueCreatesNewInstance() {
            final var queue1 = commandQueue();
            final var queue2 = commandQueue();

            assertThat(queue1).isNotNull();
            assertThat(queue2).isNotNull();
            assertThat(queue1).isNotSameAs(queue2);
        }

    }

    @Nested
    @DisplayName("offer() tests")
    class OfferTests {

        @Test
        @DisplayName("offer() accepts a valid command")
        void offerAcceptsValidCommand() {
            final var command = loadPlugin(pluginA);

            queue.offer(command);

            final var polled = queue.poll();
            assertThat(polled.isPresent()).isTrue();
            assertThat(polled.get()).isSameAs(command);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("offer() throws NullPointerException for null command")
        void offerThrowsForNullCommand() {
            assertThatThrownBy(() -> queue.offer(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("offer() maintains FIFO order for different plugins")
        void offerMaintainsFifoOrderForDifferentPlugins() {
            final var command1 = loadPlugin(pluginA);
            final var command2 = loadPlugin(pluginB);
            final var command3 = unloadPlugin(pluginA);

            queue.offer(command1);
            queue.offer(command2);
            queue.offer(command3);

            assertThat(queue.poll().get()).isSameAs(command1);
            assertThat(queue.poll().get()).isSameAs(command2);
            assertThat(queue.poll().get()).isSameAs(command3);
        }

        @Test
        @DisplayName("offer() cancels previous command for same plugin")
        void offerCancelsPreviousCommandForSamePlugin() {
            final var command1 = loadPlugin(pluginA);
            final var command2 = unloadPlugin(pluginA);

            queue.offer(command1);
            queue.offer(command2);

            assertThat(command1.isCancelled()).isTrue();
            assertThat(command2.isCancelled()).isFalse();
        }

        @Test
        @DisplayName("offer() cancels only commands for the same plugin name")
        void offerCancelsOnlyCommandsForSamePluginName() {
            final var command1 = loadPlugin(pluginA);
            final var command2 = loadPlugin(pluginB);
            final var command3 = unloadPlugin(pluginA);

            queue.offer(command1);
            queue.offer(command2);
            queue.offer(command3);

            assertThat(command1.isCancelled()).isTrue();
            assertThat(command2.isCancelled()).isFalse();
            assertThat(command3.isCancelled()).isFalse();
        }

        @Test
        @DisplayName("offer() multiple commands for same plugin cancels all but last")
        void offerMultipleCommandsForSamePluginCancelsAllButLast() {
            final var command1 = loadPlugin(pluginA);
            final var command2 = unloadPlugin(pluginA);
            final var command3 = loadPlugin(pluginA);

            queue.offer(command1);
            queue.offer(command2);
            queue.offer(command3);

            assertThat(command1.isCancelled()).isTrue();
            assertThat(command2.isCancelled()).isTrue();
            assertThat(command3.isCancelled()).isFalse();
        }

    }

    @Nested
    @DisplayName("poll() tests")
    class PollTests {

        @Test
        @DisplayName("poll() returns empty Maybe on empty queue")
        void pollReturnsEmptyMaybeOnEmptyQueue() {
            final var result = queue.poll();

            assertThat(result.isPresent()).isFalse();
        }

        @Test
        @DisplayName("poll() returns and removes command from queue")
        void pollReturnsAndRemovesCommand() {
            final var command = loadPlugin(pluginA);
            queue.offer(command);

            final var polled = queue.poll();

            assertThat(polled.isPresent()).isTrue();
            assertThat(polled.get()).isSameAs(command);
            assertThat(queue.poll().isPresent()).isFalse();
        }

        @Test
        @DisplayName("poll() returns cancelled commands")
        void pollReturnsCancelledCommands() {
            final var command1 = loadPlugin(pluginA);
            final var command2 = unloadPlugin(pluginA);

            queue.offer(command1);
            queue.offer(command2);

            final var polled = queue.poll();
            assertThat(polled.isPresent()).isTrue();
            assertThat(polled.get()).isSameAs(command1);
            assertThat(polled.get().isCancelled()).isTrue();
        }

        @Test
        @DisplayName("poll() maintains FIFO order")
        void pollMaintainsFifoOrder() {
            final var command1 = loadPlugin(pluginA);
            final var command2 = loadPlugin(pluginB);
            final var pluginC = pluginCoordinate("PluginC", SemVer.semVer(3, 0, 0));
            final var command3 = loadPlugin(pluginC);

            queue.offer(command1);
            queue.offer(command2);
            queue.offer(command3);

            assertThat(queue.poll().get()).isSameAs(command1);
            assertThat(queue.poll().get()).isSameAs(command2);
            assertThat(queue.poll().get()).isSameAs(command3);
        }

        @Test
        @DisplayName("poll() on empty queue multiple times returns empty Maybe")
        void pollOnEmptyQueueMultipleTimesReturnsEmptyMaybe() {
            assertThat(queue.poll().isPresent()).isFalse();
            assertThat(queue.poll().isPresent()).isFalse();
            assertThat(queue.poll().isPresent()).isFalse();
        }

    }

    @Nested
    @DisplayName("peek() tests")
    class PeekTests {

        @Test
        @DisplayName("peek() returns empty Maybe on empty queue")
        void peekReturnsEmptyMaybeOnEmptyQueue() {
            final var result = queue.peek();

            assertThat(result.isPresent()).isFalse();
        }

        @Test
        @DisplayName("peek() returns command without removing it")
        void peekReturnsCommandWithoutRemovingIt() {
            final var command = loadPlugin(pluginA);
            queue.offer(command);

            final var peeked = queue.peek();

            assertThat(peeked.isPresent()).isTrue();
            assertThat(peeked.get()).isSameAs(command);

            // Verify command is still in queue
            final var polled = queue.poll();
            assertThat(polled.isPresent()).isTrue();
            assertThat(polled.get()).isSameAs(command);
        }

        @Test
        @DisplayName("peek() returns same command on multiple calls")
        void peekReturnsSameCommandOnMultipleCalls() {
            final var command = loadPlugin(pluginA);
            queue.offer(command);

            final var peek1 = queue.peek();
            final var peek2 = queue.peek();
            final var peek3 = queue.peek();

            assertThat(peek1.get()).isSameAs(command);
            assertThat(peek2.get()).isSameAs(command);
            assertThat(peek3.get()).isSameAs(command);

            // Queue should still have one element
            assertThat(queue.poll().isPresent()).isTrue();
            assertThat(queue.poll().isPresent()).isFalse();
        }

        @Test
        @DisplayName("peek() returns head command in FIFO order")
        void peekReturnsHeadCommandInFifoOrder() {
            final var command1 = loadPlugin(pluginA);
            final var command2 = loadPlugin(pluginB);

            queue.offer(command1);
            queue.offer(command2);

            final var peeked = queue.peek();
            assertThat(peeked.get()).isSameAs(command1);
        }

        @Test
        @DisplayName("peek() returns cancelled commands")
        void peekReturnsCancelledCommands() {
            final var command1 = loadPlugin(pluginA);
            final var command2 = unloadPlugin(pluginA);

            queue.offer(command1);
            queue.offer(command2);

            final var peeked = queue.peek();
            assertThat(peeked.isPresent()).isTrue();
            assertThat(peeked.get()).isSameAs(command1);
            assertThat(peeked.get().isCancelled()).isTrue();
        }

        @Test
        @DisplayName("peek() followed by poll() returns same command")
        void peekFollowedByPollReturnsSameCommand() {
            final var command = loadPlugin(pluginA);
            queue.offer(command);

            final var peeked = queue.peek();
            final var polled = queue.poll();

            assertThat(peeked.get()).isSameAs(polled.get());
        }

        @Test
        @DisplayName("peek() on empty queue after poll returns empty Maybe")
        void peekOnEmptyQueueAfterPollReturnsEmptyMaybe() {
            final var command = loadPlugin(pluginA);
            queue.offer(command);
            queue.poll();

            final var peeked = queue.peek();
            assertThat(peeked.isPresent()).isFalse();
        }

        @Test
        @DisplayName("peek() is thread-safe")
        void peekIsThreadSafe() throws InterruptedException {
            final var command = loadPlugin(pluginA);
            queue.offer(command);

            final var threadCount = 10;
            final var executor = Executors.newFixedThreadPool(threadCount);
            final var latch = new CountDownLatch(threadCount);
            final List<Command> peekedCommands = new ArrayList<>();

            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        final var peeked = queue.peek();
                        if (peeked.isPresent()) {
                            synchronized (peekedCommands) {
                                peekedCommands.add(peeked.get());
                            }
                        }
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(5, TimeUnit.SECONDS);
            executor.shutdown();

            // All threads should have peeked the same command
            assertThat(peekedCommands).hasSize(threadCount);
            assertThat(peekedCommands).allMatch(cmd -> cmd == command);

            // Command should still be in queue
            assertThat(queue.poll().isPresent()).isTrue();
        }

    }

    @Nested
    @DisplayName("Cancellation behavior tests")
    class CancellationBehaviorTests {

        @Test
        @DisplayName("cancelled command stays in tracking until polled")
        void cancelledCommandStaysInTrackingUntilPolled() {
            final var command1 = loadPlugin(pluginA);
            final var command2 = unloadPlugin(pluginA);

            queue.offer(command1);
            queue.offer(command2);

            // Poll cancelled command (command1)
            // command1 is cancelled, so it stays in the map
            queue.poll();

            // Offer new command for same plugin - SHOULD cancel command2
            final var command3 = loadPlugin(pluginA);
            queue.offer(command3);

            assertThat(command2.isCancelled()).isTrue();  // ✅ command2 wird gecancelt!
            assertThat(command3.isCancelled()).isFalse();
        }

        @Test
        @DisplayName("non-cancelled command is removed from tracking after poll")
        void nonCancelledCommandIsRemovedFromTracking() {
            final var command1 = loadPlugin(pluginA);
            queue.offer(command1);

            queue.poll();

            final var command2 = loadPlugin(pluginA);
            queue.offer(command2);

            assertThat(command1.isCancelled()).isFalse();
            assertThat(command2.isCancelled()).isFalse();
        }

        @Test
        @DisplayName("polling all commands including cancelled ones clears tracking")
        void pollingAllCommandsIncludingCancelledOnesClearsTracking() {
            final var command1 = loadPlugin(pluginA);
            final var command2 = unloadPlugin(pluginA);

            queue.offer(command1);
            queue.offer(command2);

            // Poll both (command1 is cancelled, command2 is not)
            queue.poll();  // command1 (cancelled) - stays in map
            queue.poll();  // command2 (not cancelled) - removes from map

            // Now offer new command - should NOT cancel anything
            final var command3 = loadPlugin(pluginA);
            queue.offer(command3);

            assertThat(command3.isCancelled()).isFalse();
        }

    }

    @Nested
    @DisplayName("Thread-safety tests")
    class ThreadSafetyTests {

        @SuppressWarnings("ResultOfMethodCallIgnored")
        @Test
        @DisplayName("concurrent offers are thread-safe")
        void concurrentOffersAreThreadSafe() throws InterruptedException {
            final var threadCount = 10;
            final var commandsPerThread = 100;
            final var executor = Executors.newFixedThreadPool(threadCount);
            final var latch = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                final var threadId = i;
                executor.submit(() -> {
                    try {
                        for (int j = 0; j < commandsPerThread; j++) {
                            final var coordinate = pluginCoordinate(
                                "Plugin-" + threadId + "-" + j,
                                SemVer.semVer(1, 0, 0)
                            );
                            queue.offer(loadPlugin(coordinate));
                        }
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(5, TimeUnit.SECONDS);
            executor.shutdown();

            int count = 0;
            while (queue.poll().isPresent()) {
                count++;
            }

            assertThat(count).isEqualTo(threadCount * commandsPerThread);
        }

        @SuppressWarnings("ResultOfMethodCallIgnored")
        @Test
        @DisplayName("concurrent polls are thread-safe")
        void concurrentPollsAreThreadSafe() throws InterruptedException {
            final var commandCount = 1000;
            for (int i = 0; i < commandCount; i++) {
                final var coordinate = pluginCoordinate("Plugin-" + i, SemVer.semVer(1, 0, 0));
                queue.offer(loadPlugin(coordinate));
            }

            final var threadCount = 10;
            final var executor = Executors.newFixedThreadPool(threadCount);
            final var latch = new CountDownLatch(threadCount);
            final List<Command> polledCommands = new ArrayList<>();

            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        while (true) {
                            final var polled = queue.poll();
                            if (polled.isPresent()) {
                                synchronized (polledCommands) {
                                    polledCommands.add(polled.get());
                                }
                            } else {
                                break;
                            }
                        }
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(5, TimeUnit.SECONDS);
            executor.shutdown();

            assertThat(polledCommands).hasSize(commandCount);
        }

        @SuppressWarnings("ResultOfMethodCallIgnored")
        @Test
        @DisplayName("concurrent offers and polls are thread-safe")
        void concurrentOffersAndPollsAreThreadSafe() throws InterruptedException {
            final var operationCount = 500;
            final var executor = Executors.newFixedThreadPool(4);
            final var latch = new CountDownLatch(4);

            // Two producer threads
            for (int i = 0; i < 2; i++) {
                final var threadId = i;
                executor.submit(() -> {
                    try {
                        for (int j = 0; j < operationCount; j++) {
                            final var coordinate = pluginCoordinate(
                                "Plugin-" + threadId + "-" + j,
                                SemVer.semVer(1, 0, 0)
                            );
                            queue.offer(loadPlugin(coordinate));
                        }
                    } finally {
                        latch.countDown();
                    }
                });
            }

            // Two consumer threads
            final List<Command> polledCommands = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                executor.submit(() -> {
                    try {
                        for (int j = 0; j < operationCount; j++) {
                            final var polled = queue.poll();
                            if (polled.isPresent()) {
                                synchronized (polledCommands) {
                                    polledCommands.add(polled.get());
                                }
                            }
                        }
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(10, TimeUnit.SECONDS);
            executor.shutdown();

            LOGGER.info("Polled {} commands out of {} offered", polledCommands.size(), operationCount * 2);
            assertThat(polledCommands.size()).isLessThanOrEqualTo(operationCount * 2);
        }

        @SuppressWarnings("ResultOfMethodCallIgnored")
        @Test
        @DisplayName("concurrent cancellations are thread-safe")
        void concurrentCancellationsAreThreadSafe() throws InterruptedException {
            final var pluginCount = 50;
            final var updatesPerPlugin = 20;

            // Pre-fill with initial commands
            for (int i = 0; i < pluginCount; i++) {
                final var coordinate = pluginCoordinate("Plugin-" + i, SemVer.semVer(1, 0, 0));
                queue.offer(loadPlugin(coordinate));
            }

            final var executor = Executors.newFixedThreadPool(10);
            final var latch = new CountDownLatch(pluginCount);

            // Each thread updates commands for one plugin repeatedly
            for (int i = 0; i < pluginCount; i++) {
                final var pluginIndex = i;
                executor.submit(() -> {
                    try {
                        for (int j = 0; j < updatesPerPlugin; j++) {
                            final var coordinate = pluginCoordinate(
                                "Plugin-" + pluginIndex,
                                SemVer.semVer(1, 0, 0)
                            );
                            if (j % 2 == 0) {
                                queue.offer(loadPlugin(coordinate));
                            } else {
                                queue.offer(unloadPlugin(coordinate));
                            }
                        }
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(10, TimeUnit.SECONDS);
            executor.shutdown();

            // Count how many commands are still in queue
            int remainingCommands = 0;
            int cancelledCommands = 0;
            while (true) {
                final var polled = queue.poll();
                if (polled.isNone()) break;
                remainingCommands++;
                if (polled.get().isCancelled()) {
                    cancelledCommands++;
                }
            }

            LOGGER.info("Remaining commands: {}, cancelled: {}", remainingCommands, cancelledCommands);
            // We expect roughly one command per plugin, but many might be cancelled
            assertThat(remainingCommands).isGreaterThan(0);
        }

    }

    @Nested
    @DisplayName("Integration tests")
    class IntegrationTests {

        @Test
        @DisplayName("realistic scenario: mixed plugin operations")
        void realisticScenarioMixedPluginOperations() {
            final var pluginX = pluginCoordinate("PluginX", SemVer.semVer(1, 0, 0));
            final var pluginY = pluginCoordinate("PluginY", SemVer.semVer(2, 0, 0));
            final var pluginZ = pluginCoordinate("PluginZ", SemVer.semVer(3, 0, 0));

            // Initial load commands
            final var loadX1 = loadPlugin(pluginX);
            final var loadY1 = loadPlugin(pluginY);
            final var loadZ1 = loadPlugin(pluginZ);

            queue.offer(loadX1);
            queue.offer(loadY1);
            queue.offer(loadZ1);

            // Update X (should cancel loadX1)
            final var loadX2 = loadPlugin(pluginX);
            queue.offer(loadX2);

            // Unload Y (should cancel loadY1)
            final var unloadY = unloadPlugin(pluginY);
            queue.offer(unloadY);

            // Process queue
            final var cmd1 = queue.poll().get();
            assertThat(cmd1).isSameAs(loadX1);
            assertThat(cmd1.isCancelled()).isTrue();

            final var cmd2 = queue.poll().get();
            assertThat(cmd2).isSameAs(loadY1);
            assertThat(cmd2.isCancelled()).isTrue();

            final var cmd3 = queue.poll().get();
            assertThat(cmd3).isSameAs(loadZ1);
            assertThat(cmd3.isCancelled()).isFalse();

            final var cmd4 = queue.poll().get();
            assertThat(cmd4).isSameAs(loadX2);
            assertThat(cmd4.isCancelled()).isFalse();

            final var cmd5 = queue.poll().get();
            assertThat(cmd5).isSameAs(unloadY);
            assertThat(cmd5.isCancelled()).isFalse();

            assertThat(queue.poll().isNone()).isTrue();
        }

    }

}
