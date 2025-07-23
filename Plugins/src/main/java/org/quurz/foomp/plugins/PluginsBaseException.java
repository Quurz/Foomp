package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

public class PluginsBaseException
        extends RuntimeException {

    public PluginsBaseException(final @NonNull String message) {
        super(Objects.requireNonNull(message, nullValue("message")));
    }

    public PluginsBaseException(final @NonNull String message,
                                final @NonNull Throwable cause) {
        super(
            Objects.requireNonNull(message, nullValue("message")),
            Objects.requireNonNull(cause, nullValue("cause"))
        );
    }

    public PluginsBaseException(final @NonNull Throwable cause) {
        super(Objects.requireNonNull(cause, nullValue("cause")));
    }

}
