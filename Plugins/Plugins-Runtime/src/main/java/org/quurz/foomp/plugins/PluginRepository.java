package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.io.InputStream;

public interface PluginRepository {

    void save(final @NonNull PluginCoordinate coordinate,
              final @NonNull InputStream inputStream)
        throws IOException;

    InputStream load(final @NonNull PluginCoordinate coordinate)
        throws IOException;

    void remove(final @NonNull PluginCoordinate coordinate)
        throws IOException;

}
