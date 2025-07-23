package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

public class PluginRepository {

    public static PluginRepository pluginRepository(final @NonNull Path path) {
        Objects.requireNonNull(path, nullValue("path"));
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("The provided path does not exist: " + path);    // TODO: Localise
        }
        if(!Files.isDirectory(path)) {
            throw new IllegalArgumentException("The provided path is not a directory: " + path);    // TODO: Localise
        }
        if(!Files.isReadable(path)) {
            throw new IllegalArgumentException("The provided directory is not readable: " + path);    // TODO: Localise
        }
        if(!Files.isWritable(path)) {
            throw new IllegalArgumentException("The provided directory is not writable: " + path);    // TODO: Localise
        }
        return new PluginRepository(path);
    }

    private final Path pluginDirectory;

    private PluginRepository(final Path pluginDirectory) {
        this.pluginDirectory
            = pluginDirectory;
    }

}
