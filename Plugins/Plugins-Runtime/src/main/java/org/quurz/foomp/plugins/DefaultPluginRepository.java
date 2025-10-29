package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *   <p>
 *     File system-based implementation of {@link PluginRepository}.
 *   </p>
 *   <p>
 *     This repository stores plugin JAR files in a Maven-style directory structure:
 *     {@code baseDirectory/plugin-name/version/plugin.jar}. Plugins are uniquely
 *     identified by their {@link PluginCoordinate} (name + version).
 *   </p>
 *   <p>
 *     Features:
 *     <ul>
 *       <li><b>SHA-1 verification</b>: saves are idempotent if content is identical</li>
 *       <li><b>Atomic operations</b>: uses temporary files and atomic moves</li>
 *       <li><b>Clean structure</b>: organizes plugins hierarchically by name and version</li>
 *       <li><b>Proper cleanup</b>: removes temporary files on failure</li>
 *     </ul>
 *   </p>
 *   <p>
 *     Directory structure example:
 *   </p>
 *   <pre>
 *   /var/plugins/
 *     my-plugin/
 *       1.0.0/
 *         plugin.jar
 *       1.2.3/
 *         plugin.jar
 *     database-connector/
 *       2.0.0-beta/
 *         plugin.jar
 *   </pre>
 *   <p>
 *     Thread-safety: operations on the same plugin coordinate are safe due to atomic
 *     file operations, but concurrent saves to the same coordinate from different processes
 *     may result in race conditions (last writer wins for identical content, exception otherwise).
 *   </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 *
 * @see PluginRepository
 * @see PluginCoordinate
 */
public class DefaultPluginRepository
        implements PluginRepository {

    /**
     * <div>
     *   <p>
     *     Creates a new plugin repository using the specified base directory.
     *   </p>
     *   <p>
     *     The base directory must exist and must be a directory. All plugin subdirectories
     *     will be created as needed during save operations.
     *   </p>
     * </div>
     *
     * @param baseDirectory the root directory for plugin storage; must not be {@code null}
     *                      and must be an existing directory
     * @return a new {@link PluginRepository} instance; never {@code null}
     *
     * @throws NullPointerException     if {@code baseDirectory} is {@code null}
     * @throws IllegalArgumentException if {@code baseDirectory} does not exist or is not a directory
     *
     * @since 1.0.0
     */
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

    /**
     * <div>
     *   <p>
     *     Persists a plugin JAR to the file system with idempotent behavior.
     *   </p>
     *   <p>
     *     Implementation details:
     *     <ol>
     *       <li>Creates version subdirectories if needed</li>
     *       <li>Writes plugin data to a temporary file</li>
     *       <li>Computes SHA-1 hash of the new data</li>
     *       <li>If plugin already exists:
     *         <ul>
     *           <li>Computes hash of existing plugin</li>
     *           <li>If hashes match: cleans up temp file and returns (idempotent)</li>
     *           <li>If hashes differ: throws {@link PluginAlreadyExistsException}</li>
     *         </ul>
     *       </li>
     *       <li>If plugin doesn't exist: atomically moves temp file to target location</li>
     *     </ol>
     *   </p>
     *   <p>
     *     This ensures that duplicate saves with identical content succeed, while
     *     attempts to overwrite with different content are rejected.
     *   </p>
     * </div>
     *
     * @param coordinate  the plugin coordinate; must not be {@code null}
     * @param inputStream the plugin JAR data; must not be {@code null}
     *
     * @throws NullPointerException         if any parameter is {@code null}
     * @throws PluginSaveException          if IO errors occur during save
     * @throws PluginAlreadyExistsException if plugin exists with different content
     *
     * @since 1.0.0
     */
    @Override
    public void save(final @NonNull PluginCoordinate coordinate,
                     final @NonNull InputStream inputStream)
            throws PluginSaveException,
                   PluginAlreadyExistsException {
        Objects.requireNonNull(coordinate, nullValue("coordinate"));
        Objects.requireNonNull(inputStream, nullValue("inputStream"));

        final Path targetPath
            = buildJarFilePath(coordinate);
        final Path tempPath
            = targetPath.resolveSibling("plugin.jar.tmp");

        try {
            // 1. Verzeichnis erstellen
            Files.createDirectories(targetPath.getParent());

            // 2. Temp-File schreiben
            Files.copy(inputStream, tempPath, StandardCopyOption.REPLACE_EXISTING);

            // 3. Hash des neuen Files
            String newHash
                = this.calculateSha1(tempPath);

            // 4. Existiert schon ein plugin.jar?
            if (Files.exists(targetPath)) {
                String existingHash
                    = this.calculateSha1(targetPath);

                if (existingHash.equals(newHash)) {
                    // Identisch → einfach temp-file löschen, fertig
                    Files.delete(tempPath);
                    return; // Idempotent!
                } else {
                    // Unterschiedlich → Fehler!
                    Files.delete(tempPath);
                    throw new PluginAlreadyExistsException(
                        "Plugin " + coordinate + " already exists with different content"    // TODO: Lokalisierte Meldung
                    );
                }
            }

            // 5. Noch nicht vorhanden → atomar umbenennen
            Files.move(tempPath, targetPath, StandardCopyOption.ATOMIC_MOVE);
        } catch (  IOException
                 | NoSuchAlgorithmException exception) {
            // Cleanup
            try {
                Files.deleteIfExists(tempPath);
            } catch (IOException ignored) {}
            throw new PluginSaveException("Failed to save plugin " + coordinate, exception);
        }
    }

    /**
     * <div>
     *   <p>
     *     Retrieves a plugin JAR as an input stream from the file system.
     *   </p>
     *   <p>
     *     The caller is responsible for closing the returned stream. The stream provides
     *     direct read access to the plugin JAR file on disk.
     *   </p>
     * </div>
     *
     * @param coordinate the plugin coordinate; must not be {@code null}
     * @return an input stream for reading the plugin JAR; never {@code null}
     *
     * @throws NullPointerException    if {@code coordinate} is {@code null}
     * @throws PluginNotFoundException if no plugin with the specified coordinate exists
     * @throws PluginLoadException     if IO errors occur while opening the file
     *
     * @since 1.0.0
     */
    @Override
    public InputStream load(final @NonNull PluginCoordinate coordinate)
            throws PluginNotFoundException,
                   PluginLoadException {
        Objects.requireNonNull(coordinate, nullValue("coordinate"));
        final var jarFilePath = this.buildJarFilePath(coordinate);
    
        // Prüfen ob Plugin existiert
        if (!Files.exists(jarFilePath)) {
            throw new PluginNotFoundException("Plugin not found: " + coordinate);  // TODO: Lokalisierung
        }
    
        try {
            return Files.newInputStream(jarFilePath);
        } catch (IOException e) {
            throw new PluginLoadException("Failed to load plugin " + coordinate, e);  // TODO: Lokalisierung
        }
    }

    /**
     * <div>
     *   <p>
     *     Permanently deletes a plugin JAR from the file system.
     *   </p>
     *   <p>
     *     This operation only removes the plugin.jar file itself. Parent directories
     *     (plugin name and version folders) are not automatically removed even if empty.
     *   </p>
     * </div>
     *
     * @param coordinate the plugin coordinate; must not be {@code null}
     *
     * @throws NullPointerException    if {@code coordinate} is {@code null}
     * @throws PluginNotFoundException if no plugin with the specified coordinate exists
     * @throws PluginRemoveException   if IO errors occur during deletion
     *
     * @since 1.0.0
     */
    @Override
    public void remove(final @NonNull PluginCoordinate coordinate) 
            throws PluginNotFoundException,
                   PluginRemoveException {
        Objects.requireNonNull(coordinate, nullValue("coordinate"));
        final var jarFilePath = this.buildJarFilePath(coordinate);
    
        // Prüfen ob Plugin existiert
        if (!Files.exists(jarFilePath)) {
            throw new PluginNotFoundException("Plugin not found: " + coordinate);  // TODO: Lokalisierung
        }
    
        try {
            Files.delete(jarFilePath);
        } catch (IOException e) {
            throw new PluginRemoveException("Failed to remove plugin " + coordinate, e);  // TODO: Lokalisierung
        }
    }

    /**
     * <div>
     *   <p>
     *     Builds the file system path for a plugin JAR based on its coordinate.
     *   </p>
     *   <p>
     *     Structure: {@code baseDirectory/plugin-name/version/plugin.jar}
     *   </p>
     *   <p>
     *     Spaces in plugin names and version strings are replaced with underscores
     *     to ensure file system compatibility.
     *   </p>
     * </div>
     *
     * @param coordinate the plugin coordinate; must not be {@code null}
     * @return the absolute path to the plugin JAR file; never {@code null}
     *
     * @since 1.0.0
     */
    private Path buildJarFilePath(final @NonNull PluginCoordinate coordinate) {
        final var pluginDirectory
            = this.baseDirectory.resolve(coordinate.getName().replace(' ', '_'));
        final var versionDirectory
            = pluginDirectory.resolve(coordinate.getVersion().echo().replace(' ', '_'));
        return versionDirectory.resolve("plugin.jar");
    }

    /**
     * <div>
     *   <p>
     *     Computes the SHA-1 hash of a file for content verification.
     *   </p>
     *   <p>
     *     The hash is computed by reading the file in 8KB chunks and is returned
     *     as a lowercase hexadecimal string.
     *   </p>
     * </div>
     *
     * @param file the file to hash; must not be {@code null}
     * @return the SHA-1 hash as a hex string; never {@code null}
     *
     * @throws NoSuchAlgorithmException if SHA-1 algorithm is not available (should never happen)
     * @throws IOException              if an error occurs reading the file
     *
     * @since 1.0.0
     */
    private String calculateSha1(final Path file)
            throws NoSuchAlgorithmException,
                   IOException {
        final var digest
            = MessageDigest.getInstance("SHA-1");
        try (final InputStream fis = Files.newInputStream(file)) {
            final var buffer
                = new byte[8192];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, read);
            }
        }
        return HexFormat.of().formatHex(digest.digest());
    }

}
