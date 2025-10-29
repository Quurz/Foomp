
package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.InputStream;

/**
 * <div>
 *   <p>
 *     Repository for persisting and retrieving plugin artifacts.
 *   </p>
 *   <p>
 *     This interface defines the contract for a plugin storage backend that manages
 *     plugin JAR files. Implementations are responsible for organizing plugins by
 *     coordinate (name and version) and ensuring data integrity.
 *   </p>
 *   <p>
 *     Operations:
 *     <ul>
 *       <li><b>save</b>: persists a plugin JAR to the repository</li>
 *       <li><b>load</b>: retrieves a plugin JAR as an input stream</li>
 *       <li><b>remove</b>: deletes a plugin JAR from the repository</li>
 *     </ul>
 *   </p>
 *   <p>
 *     Contract: all methods enforce non-null parameters and throw specific exceptions
 *     for different failure scenarios. Implementations should be thread-safe where possible.
 *   </p>
 *   <p>
 *     Example usage:
 *   </p>
 *   <pre>{@code
 *   PluginRepository repo = DefaultPluginRepository.defaultPluginRepository(
 *       Paths.get("/var/plugins")
 *   );
 *
 *   // Save a plugin
 *   var coordinate = PluginCoordinate.pluginCoordinate("my-plugin", SemVer.semVer(1, 0, 0));
 *   try (InputStream in = Files.newInputStream(Paths.get("plugin.jar"))) {
 *       repo.save(coordinate, in);
 *   }
 *
 *   // Load it back
 *   try (InputStream in = repo.load(coordinate)) {
 *       // process plugin
 *   }
 *
 *   // Remove it
 *   repo.remove(coordinate);
 *   }</pre>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 *
 * @see DefaultPluginRepository
 * @see PluginCoordinate
 */
public interface PluginRepository {

    /**
     * <div>
     *   <p>
     *     Persists a plugin JAR file to the repository.
     *   </p>
     *   <p>
     *     This method reads the plugin data from the provided input stream and stores
     *     it under the specified coordinate. If a plugin with the same coordinate already
     *     exists and has identical content (verified by hash), the operation is idempotent
     *     and completes successfully. If the content differs, an exception is thrown.
     *   </p>
     *   <p>
     *     The input stream is fully consumed during this operation but is not closed
     *     by this method; the caller remains responsible for closing it.
     *   </p>
     * </div>
     *
     * @param coordinate  the plugin coordinate (name and version); must not be {@code null}
     * @param inputStream the input stream providing the plugin JAR data; must not be {@code null}
     *
     * @throws NullPointerException         if {@code coordinate} or {@code inputStream} is {@code null}
     * @throws PluginSaveException          if an IO error or other failure occurs during save
     * @throws PluginAlreadyExistsException if a plugin with the same coordinate exists but
     *                                      has different content
     *
     * @since 1.0.0
     */
    void save(final @NonNull PluginCoordinate coordinate,
              final @NonNull InputStream inputStream)
            throws PluginSaveException,
                   PluginAlreadyExistsException;

    /**
     * <div>
     *   <p>
     *     Retrieves a plugin JAR file from the repository as an input stream.
     *   </p>
     *   <p>
     *     The returned input stream must be closed by the caller after use. The stream
     *     provides read access to the plugin JAR data as it was stored during the save operation.
     *   </p>
     * </div>
     *
     * @param coordinate the plugin coordinate (name and version); must not be {@code null}
     * @return an input stream providing the plugin JAR data; never {@code null}
     *
     * @throws NullPointerException     if {@code coordinate} is {@code null}
     * @throws PluginNotFoundException  if no plugin with the specified coordinate exists
     * @throws PluginLoadException      if an IO error or other failure occurs during load
     *
     * @since 1.0.0
     */
    InputStream load(final @NonNull PluginCoordinate coordinate)
            throws PluginNotFoundException,
                   PluginLoadException;

    /**
     * <div>
     *   <p>
     *     Removes a plugin JAR file from the repository.
     *   </p>
     *   <p>
     *     This operation permanently deletes the plugin data. If the plugin does not exist,
     *     an exception is thrown.
     *   </p>
     * </div>
     *
     * @param coordinate the plugin coordinate (name and version); must not be {@code null}
     *
     * @throws NullPointerException     if {@code coordinate} is {@code null}
     * @throws PluginNotFoundException  if no plugin with the specified coordinate exists
     * @throws PluginRemoveException    if an IO error or other failure occurs during removal
     *
     * @since 1.0.0
     */
    void remove(final @NonNull PluginCoordinate coordinate)
            throws PluginNotFoundException,
                   PluginRemoveException;

}