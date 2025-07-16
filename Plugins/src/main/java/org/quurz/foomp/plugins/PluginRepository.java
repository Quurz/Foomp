package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

public class PluginRepository {

    public static PluginRepository pluginRepository(final @NonNull Path pluginDirectory) {
        Objects.requireNonNull(pluginDirectory, nullValue("pluginDirectory"));
        if (!Files.exists(pluginDirectory)) {
            throw new IllegalArgumentException("The provided path does not exist: " + pluginDirectory);    // TODO: Localise
        }
        if(!Files.isDirectory(pluginDirectory)) {
            throw new IllegalArgumentException("The provided path is not a directory: " + pluginDirectory);    // TODO: Localise
        }
        if(!Files.isReadable(pluginDirectory)) {
            throw new IllegalArgumentException("The provided directory is not readable: " + pluginDirectory);    // TODO: Localise
        }
        if(!Files.isWritable(pluginDirectory)) {
            throw new IllegalArgumentException("The provided directory is not writable: " + pluginDirectory);    // TODO: Localise
        }
        return new PluginRepository(pluginDirectory);
    }

    private final Path pluginDirectory;

    private PluginRepository(final Path pluginDirectory) {
        this.pluginDirectory
            = pluginDirectory;
    }

}
