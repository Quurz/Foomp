package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *   <p>
 *     Exception indicating that saving or persisting a plugin failed.
 *   </p>
 *   <p>
 *     This exception is a specialization of {@link PluginException} and is
 *     thrown when a plugin cannot be saved due to IO errors, serialization
 *     issues, permission problems, or other runtime problems during persistence.
 *   </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public class PluginSaveException
        extends PluginException {

    /**
     * <div>
     *   <p>
     *     Constructs a new plugin save exception with no detail message.
     *   </p>
     * </div>
     *
     * @since 1.0.0
     */
    public PluginSaveException() {
        super();
    }

    /**
     * <div>
     *   <p>
     *     Constructs a new plugin save exception with the specified detail message.
     *   </p>
     * </div>
     *
     * @param message the detail message
     *
     * @throws NullPointerException if {@code message} is {@code null}
     *
     * @since 1.0.0
     */
    public PluginSaveException(final @NonNull String message) {
        super(Objects.requireNonNull(message, nullValue("message")));
    }

    /**
     * <div>
     *   <p>
     *     Constructs a new plugin save exception with the specified detail message and cause.
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
    public PluginSaveException(final @NonNull String message,
                               final @NonNull Throwable cause) {
        super(
            Objects.requireNonNull(message, nullValue("message")),
            Objects.requireNonNull(cause, nullValue("cause"))
        );
    }

    /**
     * <div>
     *   <p>
     *     Constructs a new plugin save exception with the specified cause.
     *   </p>
     * </div>
     *
     * @param cause the cause of this exception
     *
     * @throws NullPointerException if {@code cause} is {@code null}
     *
     * @since 1.0.0
     */
    public PluginSaveException(final @NonNull Throwable cause) {
        super(Objects.requireNonNull(cause, nullValue("cause")));
    }

}
