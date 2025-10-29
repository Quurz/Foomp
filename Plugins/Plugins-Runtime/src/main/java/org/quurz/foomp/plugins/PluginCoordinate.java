package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.StringJoiner;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *   <p>
 *     Immutable identifier for a plugin consisting of a name and a semantic version.
 *   </p>
 *   <p>
 *     A plugin coordinate uniquely identifies a specific version of a plugin within
 *     the plugin system. It serves as a lightweight key for plugin lookups, comparisons,
 *     and dependency resolution.
 *   </p>
 *   <p>
 *     Semantics:
 *     <ul>
 *       <li><b>Name</b>: unique identifier for the plugin (e.g., "database-connector")</li>
 *       <li><b>Version</b>: semantic version following SemVer 2.0.0 specification</li>
 *     </ul>
 *   </p>
 *   <p>
 *     Contract: both name and version must not be {@code null}. The class is immutable;
 *     instances are thread-safe and can be safely used as keys in collections.
 *   </p>
 *   <p>
 *     Examples:
 *   </p>
 *   <pre>{@code
 *   var coord1 = PluginCoordinate.pluginCoordinate(
 *       "my-plugin",
 *       SemVer.semVer(1, 2, 3)
 *   );
 *   
 *   var coord2 = PluginCoordinate.pluginCoordinate(
 *       "database-connector",
 *       SemVer.semVerBuilder()
 *           .major(2)
 *           .minor(0)
 *           .patch(0)
 *           .preRelease("beta.1")
 *           .build()
 *   );
 *   
 *   // Use as map key
 *   Map<PluginCoordinate, Plugin<?>> registry = new HashMap<>();
 *   registry.put(coord1, plugin);
 *   }</pre>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 *
 * @see SemVer
 * @see PluginDescriptor
 */
public class PluginCoordinate {

    /**
     * <div>
     *   <p>
     *     Creates a plugin coordinate with the specified name and version.
     *   </p>
     * </div>
     *
     * @param name    the plugin name; must not be {@code null}
     * @param version the semantic version; must not be {@code null}
     * @return a new {@link PluginCoordinate}; never {@code null}
     * @throws NullPointerException if {@code name} or {@code version} is {@code null}
     *
     * @since 1.0.0
     */
    public static PluginCoordinate pluginCoordinate(final @NonNull String name,
                                                    final @NonNull SemVer version) {
        Objects.requireNonNull(name, nullValue("name"));
        Objects.requireNonNull(version, nullValue("version"));
        return new PluginCoordinate(name, version);
    }

    private final String name;
    private final SemVer version;

    private PluginCoordinate(final String name,
                             final SemVer version) {
        this.name = name;
        this.version = version;
    }

    /**
     * <div>
     *   <p>
     *     Returns the plugin name.
     *   </p>
     * </div>
     *
     * @return the plugin name; never {@code null}
     *
     * @since 1.0.0
     */
    public String getName() {
        return this.name;
    }

    /**
     * <div>
     *   <p>
     *     Returns the semantic version.
     *   </p>
     * </div>
     *
     * @return the plugin version; never {@code null}
     *
     * @since 1.0.0
     */
    public SemVer getVersion() {
        return this.version;
    }

    /**
     * <div>
     *   <p>
     *     Compares this plugin coordinate for equality with another object.
     *   </p>
     *   <p>
     *     Two plugin coordinates are equal if both name and version are equal.
     *   </p>
     * </div>
     *
     * @param o the object to compare with
     * @return {@code true} if equal; {@code false} otherwise
     *
     * @since 1.0.0
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PluginCoordinate that)) return false;
        return Objects.equals(name, that.name) && Objects.equals(version, that.version);
    }

    /**
     * <div>
     *   <p>
     *     Computes the hash code for this plugin coordinate based on name and version.
     *   </p>
     * </div>
     *
     * @return the hash code
     *
     * @since 1.0.0
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, version);
    }

    /**
     * <div>
     *   <p>
     *     Returns a debug-friendly string representation of this plugin coordinate.
     *   </p>
     * </div>
     *
     * @return a string representation showing name and version
     *
     * @since 1.0.0
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", PluginCoordinate.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("version=" + version)
                .toString();
    }

}
