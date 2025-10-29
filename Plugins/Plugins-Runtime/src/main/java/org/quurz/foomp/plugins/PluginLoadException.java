package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *   <p>
 *     Exception indicating that loading a plugin failed.
 *   </p>
 *   <p>
 *     This exception is a specialization of {@link PluginException} and is
 *     thrown when a plugin cannot be loaded due to IO errors, classloading
 *     issues, or other runtime problems during initialization.
 *   </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public class PluginLoadException
        extends PluginException {

    /**
     * <div>
     *   <p>
     *     Constructs a new plugin load exception with no detail message.
     *   </p>
     * </div>
     *
     * @since 1.0.0
     */
    public PluginLoadException() {
        super();
    }

    /**
     * <div>
     *   <p>
     *     Constructs a new plugin load exception with the specified detail message.
     *   </p>
     * </div>
     *
     * @param message the detail message
     *
     * @throws NullPointerException if {@code message} is {@code null}
     *
     * @since 1.0.0
     */
    public PluginLoadException(final @NonNull String message) {
        super(Objects.requireNonNull(message, nullValue("message")));
    }

    /**
     * <div>
     *   <p>
     *     Constructs a new plugin load exception with the specified detail message and cause.
     *   </p>
     * </div>
     *
     * @param message the detail message
     * @param cause   the cause of this exception
     *
     * @since 1.0.0
     */
    public PluginLoadException(final @NonNull String message,
                               final @NonNull Throwable cause) {
        super(
            Objects.requireNonNull(message, nullValue("message")),
            Objects.requireNonNull(cause, nullValue("cause"))
        );
    }

    /**
     * <div>
     *   <p>
     *     Constructs a new plugin load exception with the specified cause.
     *   </p>
     * </div>
     *
     * @param cause the cause of this exception
     *
     * @throws NullPointerException if {@code cause} is {@code null}
     *
     * @since 1.0.0
     */
    public PluginLoadException(final @NonNull Throwable cause) {
        super(Objects.requireNonNull(cause, nullValue("cause")));
    }

}
