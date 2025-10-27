package org.quurz.foomp.plugins;

import java.io.Serial;

/**
 * <div>
 *     <p>
 *         Base runtime exception for the plugin system.
 *     </p>
 *     <p>
 *         This exception serves as the superclass for more specific plugin-related runtime errors,
 *         e.g., loading, initialization, execution, or shutdown issues of plugins.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public class PluginSystemException
        extends RuntimeException {

    @Serial
    private static final long serialVersionUID
        = 23L;

    /**
     * <div>
     *     <p>
     *         Creates a new {@code PluginSystemException} with a detail message.
     *     </p>
     * </div>
     *
     * @param message the detail message; should describe the error precisely
     * @since 1.0.0
     */
    public PluginSystemException(final String message) {
        super(message);
    }

    /**
     * <div>
     *     <p>
     *         Creates a new {@code PluginSystemException} with a detail message and a cause.
     *     </p>
     *     <p>
     *         Use this constructor to chain the original cause (exception chaining) so that the
     *         underlying reason remains traceable.
     *     </p>
     * </div>
     *
     * @param message the detail message
     * @param cause   the cause of the failure (may be {@code null})
     *
     * @since 1.0.0
     */
    public PluginSystemException(final String message,
                                 final Throwable cause) {
        super(message, cause);
    }

    /**
     * <div>
     *     <p>
     *         Creates a new {@code PluginSystemException} with the specified cause.
     *     </p>
     *     <p>
     *         Useful when no additional message is needed and the cause sufficiently
     *         describes the failure condition.
     *     </p>
     * </div>
     *
     * @param cause the cause of the failure (may be {@code null})
     *
     * @since 1.0.0
     */
    public PluginSystemException(final Throwable cause) {
        super(cause);
    }

}
