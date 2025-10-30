package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Mutable;

import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Represents a plugin lifecycle command that can be executed or cancelled.
 *     </p>
 *     <p>
 *         This sealed class hierarchy defines commands for plugin loading and unloading operations.
 *         Commands are mutable and can be cancelled before execution. Once cancelled, a command
 *         remains in that state permanently.
 *     </p>
 *     <p>
 *         Instances should be created using the static factory methods {@link #loadPlugin(PluginCoordinate)}
 *         and {@link #unloadPlugin(PluginCoordinate)}.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@Mutable
public abstract sealed class Command
        permits Command.LoadPlugin,
                Command.UnloadPlugin {

    /**
     * <div>
     *     <p>
     *         Creates a command to load a plugin.
     *     </p>
     * </div>
     *
     * @param pluginCoordinate the coordinate identifying the plugin to load; must not be {@code null}
     * @return a new load plugin command
     * @throws NullPointerException if {@code pluginCoordinate} is {@code null}
     *
     * @since 1.0.0
     */
    public static Command loadPlugin(final @NonNull PluginCoordinate pluginCoordinate) {
        Objects.requireNonNull(pluginCoordinate, nullValue("pluginCoordinate"));
        return new LoadPlugin(pluginCoordinate);
    }

    /**
     * <div>
     *     <p>
     *         Creates a command to unload a plugin.
     *     </p>
     * </div>
     *
     * @param pluginCoordinate the coordinate identifying the plugin to unload; must not be {@code null}
     * @return a new unload plugin command
     * @throws NullPointerException if {@code pluginCoordinate} is {@code null}
     *
     * @since 1.0.0
     */
    public static Command unloadPlugin(final @NonNull PluginCoordinate pluginCoordinate) {
        Objects.requireNonNull(pluginCoordinate, nullValue("pluginCoordinate"));
        return new UnloadPlugin(pluginCoordinate);
    }

    protected final PluginCoordinate pluginCoordinate;
    private boolean cancelled;

    protected Command(PluginCoordinate pluginCoordinate) {
        this.pluginCoordinate
            = pluginCoordinate;
        this.cancelled
            = false;
    }

    /**
     * <div>
     *     <p>
     *         Cancels this command.
     *     </p>
     *     <p>
     *         Once cancelled, a command cannot be un-cancelled. Cancelled commands should
     *         be skipped during processing.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    public void cancel() {
        this.cancelled
            = true;
    }

    /**
     * <div>
     *     <p>
     *         Checks whether this command has been cancelled.
     *     </p>
     * </div>
     *
     * @return {@code true} if this command has been cancelled, {@code false} otherwise
     *
     * @since 1.0.0
     */
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * <div>
     *     <p>
     *         Returns the plugin coordinate associated with this command.
     *     </p>
     * </div>
     *
     * @return the plugin coordinate; never {@code null}
     *
     * @since 1.0.0
     */
    public PluginCoordinate getPluginCoordinate() {
        return this.pluginCoordinate;
    }

    /**
     * <div>
     *     <p>
     *         Command to load a plugin.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    static final class LoadPlugin
            extends Command {

        private LoadPlugin(PluginCoordinate pluginCoordinate) {
            super(pluginCoordinate);
        }

    }

    /**
     * <div>
     *     <p>
     *         Command to unload a plugin.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    static final class UnloadPlugin
            extends Command {

        private UnloadPlugin(PluginCoordinate pluginCoordinate) {
            super(pluginCoordinate);
        }

    }

}
