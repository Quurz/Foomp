package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *   <p>
 *     Exception indicating that removing or deleting a plugin failed.
 *   </p>
 *   <p>
 *     This exception is a specialization of {@link PluginException} and is
 *     thrown when a plugin cannot be removed due to IO errors, permission
 *     problems, file locks, or other runtime problems during deletion.
 *   </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public class PluginRemoveException
        extends PluginException {

    /**
     * <div>
     *   <p>
     *     Constructs a new plugin remove exception with no detail message.
     *   </p>
     * </div>
     *
     * @since 1.0.0
     */
    public PluginRemoveException() {
        super();
    }

    /**
     * <div>
     *   <p>
     *     Constructs a new plugin remove exception with the specified detail message.
     *   </p>
     * </div>
     *
     * @param message the detail message
     *
     * @throws NullPointerException if {@code message} is {@code null}
     *
     * @since 1.0.0
     */
    public PluginRemoveException(final @NonNull String message) {
        super(Objects.requireNonNull(message, nullValue("message")));
    }

    /**
     * <div>
     *   <p>
     *     Constructs a new plugin remove exception with the specified detail message and cause.
     *   </p>
     * </div>
     *
     * @param message the detail message
     * @param cause   the cause of this exception
     *
     * @throws NullPointerException if {@code message} or {@code cause} is {@code null}
     *
     * @since 1.0.0
     */
    public PluginRemoveException(final @NonNull String message,
                                 final @NonNull Throwable cause) {
        super(
            Objects.requireNonNull(message, nullValue("message")),
            Objects.requireNonNull(cause, nullValue("cause"))
        );
    }

    /**
     * <div>
     *   <p>
     *     Constructs a new plugin remove exception with the specified cause.
     *   </p>
     * </div>
     *
     * @param cause the cause of this exception
     *
     * @throws NullPointerException if {@code cause} is {@code null}
     *
     * @since 1.0.0
     */
    public PluginRemoveException(final @NonNull Throwable cause) {
        super(Objects.requireNonNull(cause, nullValue("cause")));
    }

}
