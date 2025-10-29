package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.nio.file.Files;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

public class DefaultPluginRepository
        implements PluginRepository {

    public static PluginRepository defaultPluginRepository(final @NonNull Path baseDirectory) {
        Objects.requireNonNull(baseDirectory, nullValue("baseDirectory"));
        if (!baseDirectory.toFile().isDirectory()) {  // TODO: Check in Util-Klasse und Lokalisierung
            throw new IllegalArgumentException("Not a directory: " + baseDirectory);
        }
        // TODO: Zugrissrechte auf Verzeichnis prüfen
        return new DefaultPluginRepository(baseDirectory);
    }

    private final Path baseDirectory;

    private DefaultPluginRepository(final Path baseDirectory) {
        this.baseDirectory
            = baseDirectory;
    }

    @Override
    public void save(final @NonNull PluginCoordinate coordinate,
                     final @NonNull InputStream inputStream)
            throws IOException {
        Objects.requireNonNull(coordinate, nullValue("coordinate"));
        Objects.requireNonNull(inputStream, nullValue("inputStream"));

        final var jarFilePath = this.buildJarFilePath(coordinate);

        // Create parent directories
        Files.createDirectories(jarFilePath.getParent());

        // Copy InputStream to file (overwrites if exists)
        Files.copy(inputStream, jarFilePath, StandardCopyOption.REPLACE_EXISTING);
    }

@Override
public InputStream load(final @NonNull PluginCoordinate coordinate) 
        throws IOException {
    Objects.requireNonNull(coordinate, nullValue("coordinate"));
    final var jarFilePath = this.buildJarFilePath(coordinate);
    return Files.newInputStream(jarFilePath);
}

@Override
public void remove(final @NonNull PluginCoordinate coordinate) 
        throws IOException {
    Objects.requireNonNull(coordinate, nullValue("coordinate"));
    final var jarFilePath = this.buildJarFilePath(coordinate);
    Files.deleteIfExists(jarFilePath);
}

    private Path buildJarFilePath(final @NonNull PluginCoordinate coordinate) {
        final var pluginSubDirectory
            = this.baseDirectory.resolve(coordinate.getName().replace(' ', '_'));
        return pluginSubDirectory.resolve(coordinate.getVersion().toString().replace(' ', '_') + ".jar");
    }

}
