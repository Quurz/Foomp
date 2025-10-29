
package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Echo;
import org.quurz.foomp.base.util.Maybe;

import java.util.Objects;
import java.util.StringJoiner;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Maybe.*;

/**
 * <div>
 *   <p>
 *     Immutable representation of a Semantic Version (SemVer) according to the
 *     <a href="https://semver.org/">SemVer 2.0.0 specification</a>.
 *   </p>
 *   <p>
 *     A semantic version consists of three required numeric components (MAJOR.MINOR.PATCH)
 *     and two optional string components (pre-release and build metadata).
 *   </p>
 *   <p>
 *     Semantics:
 *     <ul>
 *       <li><b>MAJOR</b>: incremented for incompatible API changes</li>
 *       <li><b>MINOR</b>: incremented for backwards-compatible new functionality</li>
 *       <li><b>PATCH</b>: incremented for backwards-compatible bug fixes</li>
 *       <li><b>Pre-Release</b>: optional identifier for pre-release versions (e.g., "alpha", "beta.1")</li>
 *       <li><b>Build Metadata</b>: optional build information (e.g., "build.123", "20130313144700")</li>
 *     </ul>
 *   </p>
 *   <p>
 *     Precedence Rules:
 *     <ul>
 *       <li>Versions are compared by MAJOR, MINOR, and PATCH numerically</li>
 *       <li>Pre-release versions have LOWER precedence than normal versions (1.0.0-alpha &lt; 1.0.0)</li>
 *       <li>Build metadata is IGNORED during precedence comparison</li>
 *     </ul>
 *   </p>
 *   <p>
 *     Contract: all numeric components must be non-negative. String components must not be {@code null}
 *     when provided. The class is immutable; all modification methods return new instances.
 *   </p>
 *   <p>
 *     Examples:
 *   </p>
 *   <pre>{@code
 *   var v1 = SemVer.semVer(1, 2, 3);                    // "1.2.3"
 *   var v2 = v1.incrementMinor();                        // "1.3.0"
 *   var v3 = v1.withPreRelease("beta");                  // "1.2.3-beta"
 *   var v4 = v3.withBuildMetadata("build.456");          // "1.2.3-beta+build.456"
 *
 *   v1.compareTo(v3);  // 1 (release > pre-release)
 *
 *   var v5 = SemVer.semVerBuilder()
 *       .major(2)
 *       .minor(0)
 *       .patch(0)
 *       .preRelease("rc.1")
 *       .build();                                         // "2.0.0-rc.1"
 *   }</pre>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 *
 * @see <a href="https://semver.org/">Semantic Versioning 2.0.0</a>
 */
public class SemVer
        implements Comparable<SemVer>,
                   Echo {

    /**
     * <div>
     *   <p>
     *     Fluent builder for constructing {@link SemVer} instances with optional components.
     *   </p>
     *   <p>
     *     All numeric components default to 0. Optional components (pre-release, build metadata)
     *     default to absent. All setter methods validate their inputs and return {@code this}
     *     for method chaining.
     *   </p>
     *   <p>
     *     Example:
     *   </p>
     *   <pre>{@code
     *   var version = SemVer.semVerBuilder()
     *       .major(2)
     *       .minor(1)
     *       .patch(0)
     *       .preRelease("beta.1")
     *       .buildMetadata("build.20241029")
     *       .build();  // "2.1.0-beta.1+build.20241029"
     *   }</pre>
     * </div>
     *
     * @since 1.0.0
     */
    public static final class SemVerBuilder {

        private int major;
        private int minor;
        private int patch;
        private String preRelease;
        private String buildMetadata;

        private SemVerBuilder() {
            this.major
                = 0;
            this.minor
                = 0;
            this.patch
                = 0;
            this.preRelease
                = null;
            this.buildMetadata
                = null;
        }

        /**
         * <div>
         *   <p>
         *     Sets the MAJOR version component.
         *   </p>
         * </div>
         *
         * @param major the major version number; must be &gt;= 0
         * @return this builder
         * @throws IllegalArgumentException if {@code major} is negative
         *
         * @since 1.0.0
         */
        public SemVerBuilder major(final int major) {
            if (major < 0) {
                throw new IllegalArgumentException("major must be >= 0");
            }
            this.major
                = major;
            return this;
        }

        /**
         * <div>
         *   <p>
         *     Sets the MINOR version component.
         *   </p>
         * </div>
         *
         * @param minor the minor version number; must be &gt;= 0
         * @return this builder
         * @throws IllegalArgumentException if {@code minor} is negative
         *
         * @since 1.0.0
         */
        public SemVerBuilder minor(final int minor) {
            if (minor < 0) {
                throw new IllegalArgumentException("minor must be >= 0");
            }
            this.minor
                = minor;
            return this;
        }

        /**
         * <div>
         *   <p>
         *     Sets the PATCH version component.
         *   </p>
         * </div>
         *
         * @param patch the patch version number; must be &gt;= 0
         * @return this builder
         * @throws IllegalArgumentException if {@code patch} is negative
         *
         * @since 1.0.0
         */
        public SemVerBuilder patch(final int patch) {
            if (patch < 0) {
                throw new IllegalArgumentException("patch must be >= 0");
            }
            this.patch
                = patch;
            return this;
        }

        /**
         * <div>
         *   <p>
         *     Sets the pre-release identifier.
         *   </p>
         * </div>
         *
         * @param preRelease the pre-release string (e.g., "alpha", "beta.1"); must not be {@code null}
         * @return this builder
         * @throws NullPointerException if {@code preRelease} is {@code null}
         *
         * @since 1.0.0
         */
        public SemVerBuilder preRelease(final @NonNull String preRelease) {
            Objects.requireNonNull(preRelease, nullValue("preRelease"));
            this.preRelease
                = preRelease;
            return this;
        }

        /**
         * <div>
         *   <p>
         *     Sets the build metadata.
         *   </p>
         * </div>
         *
         * @param buildMetadata the build metadata string (e.g., "build.123"); must not be {@code null}
         * @return this builder
         * @throws NullPointerException if {@code buildMetadata} is {@code null}
         *
         * @since 1.0.0
         */
        public SemVerBuilder buildMetadata(final @NonNull String buildMetadata) {
            Objects.requireNonNull(buildMetadata, nullValue("buildMetadata"));
            this.buildMetadata = buildMetadata;
            return this;
        }

        /**
         * <div>
         *   <p>
         *     Builds the {@link SemVer} instance from the current builder state.
         *   </p>
         * </div>
         *
         * @return a new {@link SemVer} instance; never {@code null}
         *
         * @since 1.0.0
         */
        public SemVer build() {
            return new SemVer(this.major, this.minor, this.patch, this.preRelease, this.buildMetadata);
        }

    }

    /**
     * <div>
     *   <p>
     *     Creates a new fluent builder for constructing {@link SemVer} instances.
     *   </p>
     * </div>
     *
     * @return a new {@link SemVerBuilder}; never {@code null}
     *
     * @since 1.0.0
     */
    public static SemVerBuilder semVerBuilder() {
        return new SemVerBuilder();
    }

    /**
     * <div>
     *   <p>
     *     Creates a SemVer with the specified MAJOR, MINOR, and PATCH components.
     *     Pre-release and build metadata are absent.
     *   </p>
     * </div>
     *
     * @param major the major version number; must be &gt;= 0
     * @param minor the minor version number; must be &gt;= 0
     * @param patch the patch version number; must be &gt;= 0
     * @return a new {@link SemVer}; never {@code null}
     * @throws IllegalArgumentException if any component is negative
     *
     * @since 1.0.0
     */
    public static SemVer semVer(final int major,
                                final int minor,
                                final int patch) {
        if (major < 0) {
            throw new IllegalArgumentException("major must be >= 0");
        }
        if (minor < 0) {
            throw new IllegalArgumentException("minor must be >= 0");
        }
        if (patch < 0) {
            throw new IllegalArgumentException("patch must be >= 0");
        }
        return new SemVer(major, minor, patch, (String) null, (String) null);
    }

    private final int major;
    private final int minor;
    private final int patch;
    private final Maybe<String> preRelease;
    private final Maybe<String> buildMetadata;

    private SemVer(int major,
                   int minor,
                   int patch,
                   String preRelease,
                   String buildMetadata) {
        this(
            major,
            minor,
            patch,
            maybeOfNullable(preRelease),
            maybeOfNullable(buildMetadata)
        );
    }

    private SemVer(int major,
                   int minor,
                   int patch,
                   Maybe<String> preRelease,
                   Maybe<String> buildMetadata) {
        this.major
            = major;
        this.minor
            = minor;
        this.patch
            = patch;
        this.preRelease
            = preRelease;
        this.buildMetadata
            = buildMetadata;
    }

    /**
     * <div>
     *   <p>
     *     Returns the MAJOR version component.
     *   </p>
     * </div>
     *
     * @return the major version number
     *
     * @since 1.0.0
     */
    public int getMajor() {
        return this.major;
    }

    /**
     * <div>
     *   <p>
     *     Returns the MINOR version component.
     *   </p>
     * </div>
     *
     * @return the minor version number
     *
     * @since 1.0.0
     */
    public int getMinor() {
        return this.minor;
    }

    /**
     * <div>
     *   <p>
     *     Returns the PATCH version component.
     *   </p>
     * </div>
     *
     * @return the patch version number
     *
     * @since 1.0.0
     */
    public int getPatch() {
        return this.patch;
    }

    /**
     * <div>
     *   <p>
     *     Returns the optional pre-release identifier.
     *   </p>
     * </div>
     *
     * @return {@link Maybe#some(Object)} containing the pre-release string if present, otherwise {@link Maybe#none()}
     *
     * @since 1.0.0
     */
    public Maybe<String> getPreRelease() {
        return this.preRelease;
    }

    /**
     * <div>
     *   <p>
     *     Returns the optional build metadata.
     *   </p>
     * </div>
     *
     * @return {@link Maybe#some(Object)} containing the build metadata if present, otherwise {@link Maybe#none()}
     *
     * @since 1.0.0
     */
    public Maybe<String> getBuildMetadata() {
        return this.buildMetadata;
    }

    /**
     * <div>
     *   <p>
     *     Increments the MAJOR version by 1, resets MINOR and PATCH to 0,
     *     and removes pre-release and build metadata.
     *   </p>
     *   <p>
     *     According to SemVer specification §8: when incrementing major,
     *     minor and patch must be reset to zero.
     *   </p>
     * </div>
     *
     * @return a new {@link SemVer} with incremented major version; never {@code null}
     *
     * @since 1.0.0
     */
    public SemVer incrementMajor() {
        return new SemVer(this.major + 1, 0, 0, none(), none());
    }

    /**
     * <div>
     *   <p>
     *     Increments the MINOR version by 1, resets PATCH to 0,
     *     and removes pre-release and build metadata.
     *   </p>
     *   <p>
     *     According to SemVer specification §7: when incrementing minor,
     *     patch must be reset to zero.
     *   </p>
     * </div>
     *
     * @return a new {@link SemVer} with incremented minor version; never {@code null}
     *
     * @since 1.0.0
     */
    public SemVer incrementMinor() {
        return new SemVer(this.major, this.minor + 1, 0, none(), none());
    }

    /**
     * <div>
     *   <p>
     *     Increments the PATCH version by 1 and removes pre-release and build metadata.
     *   </p>
     * </div>
     *
     * @return a new {@link SemVer} with incremented patch version; never {@code null}
     *
     * @since 1.0.0
     */
    public SemVer incrementPatch() {
        return new SemVer(this.major, this.minor, this.patch + 1, none(), none());
    }

    /**
     * <div>
     *   <p>
     *     Returns a new version with the specified pre-release identifier.
     *     Build metadata is removed.
     *   </p>
     * </div>
     *
     * @param preRelease the pre-release identifier (e.g., "alpha", "beta.1"); must not be {@code null}
     * @return a new {@link SemVer} with the pre-release set; never {@code null}
     * @throws NullPointerException if {@code preRelease} is {@code null}
     *
     * @since 1.0.0
     */
    public SemVer withPreRelease(final @NonNull String preRelease) {
        Objects.requireNonNull(preRelease, nullValue("preRelease"));
        return new SemVer(this.major, this.minor, this.patch, some(preRelease), none());
    }

    /**
     * <div>
     *   <p>
     *     Returns a new version with the pre-release identifier removed.
     *     Build metadata is preserved.
     *   </p>
     * </div>
     *
     * @return a new {@link SemVer} without pre-release; never {@code null}
     *
     * @since 1.0.0
     */
    public SemVer withoutPreRelease() {
        return new SemVer(this.major, this.minor, this.patch, Maybe.none(), this.buildMetadata);
    }

    /**
     * <div>
     *   <p>
     *     Returns a new version with the specified build metadata.
     *     Pre-release is preserved.
     *   </p>
     * </div>
     *
     * @param buildMetadata the build metadata (e.g., "build.123"); must not be {@code null}
     * @return a new {@link SemVer} with build metadata set; never {@code null}
     * @throws NullPointerException if {@code buildMetadata} is {@code null}
     *
     * @since 1.0.0
     */
    public SemVer withBuildMetadata(final @NonNull String buildMetadata) {
        Objects.requireNonNull(buildMetadata, nullValue("buildMetadata"));
        return new SemVer(this.major, this.minor, this.patch, this.preRelease, some(buildMetadata));
    }

    /**
     * <div>
     *   <p>
     *     Returns a new version with the build metadata removed.
     *     Pre-release is preserved.
     *   </p>
     * </div>
     *
     * @return a new {@link SemVer} without build metadata; never {@code null}
     *
     * @since 1.0.0
     */
    public SemVer withoutBuildMetadata() {
        return new SemVer(this.major, this.minor, this.patch, this.preRelease, Maybe.none());
    }

    /**
     * <div>
     *   <p>
     *     Compares this version to another according to SemVer precedence rules.
     *   </p>
     *   <p>
     *     Precedence is determined by:
     *     <ol>
     *       <li>MAJOR, MINOR, PATCH (compared numerically)</li>
     *       <li>Pre-release versions have LOWER precedence than normal versions</li>
     *       <li>Pre-release identifiers are compared lexically (simplified)</li>
     *       <li>Build metadata is IGNORED in precedence comparison</li>
     *     </ol>
     *   </p>
     *   <p>
     *     Examples:
     *   </p>
     *   <pre>{@code
     *   1.0.0-alpha < 1.0.0-beta < 1.0.0 < 1.1.0
     *   1.0.0+build.1 == 1.0.0+build.2  // build metadata ignored
     *   }</pre>
     * </div>
     *
     * @param other the version to compare to; must not be {@code null}
     * @return negative if this &lt; other, zero if equal, positive if this &gt; other
     * @throws NullPointerException if {@code other} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public int compareTo(final @NonNull SemVer other) {
        Objects.requireNonNull(other, nullValue("other"));

        int result;

        // 1. Compare MAJOR
        result = Integer.compare(this.major, other.major);

        // 2. Compare MINOR (only if MAJOR equal)
        if (result == 0) {
            result = Integer.compare(this.minor, other.minor);
        }

        // 3. Compare PATCH (only if MINOR equal)
        if (result == 0) {
            result = Integer.compare(this.patch, other.patch);
        }

        // 4. Compare pre-release (only if PATCH equal)
        // When a major, minor, and patch are equal, a pre-release version has LOWER precedence
        // Example: 1.0.0-alpha < 1.0.0
        if (result == 0) {
            final boolean thisHasPre = this.preRelease.isSome();
            final boolean otherHasPre = other.preRelease.isSome();

            if (!thisHasPre && !otherHasPre) {
                result = 0; // Both are releases, equal (build metadata is ignored)
            } else if (!thisHasPre) {
                result = 1;  // this is release, other is pre-release -> this > other
            } else if (!otherHasPre) {
                result = -1; // this is pre-release, other is release -> this < other
            } else {
                // Both have pre-release, compare lexically (simplified)
                // For full SemVer compliance, you'd need to split by '.' and compare identifier-wise
                result = this.preRelease.get().compareTo(other.preRelease.get());
            }
        }

        // Note: Build metadata is explicitly ignored per SemVer spec
        return result;
    }

    /**
     * <div>
     *   <p>
     *     Compares this version for equality with another object.
     *   </p>
     *   <p>
     *     Two versions are equal if all components (including build metadata) are equal.
     *     Note that this differs from {@link #compareTo(SemVer)}, which ignores build metadata.
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
        if (!(o instanceof SemVer semVer)) return false;
        return this.major == semVer.major
            && this.minor == semVer.minor
            && this.patch == semVer.patch
            && Objects.equals(this.preRelease, semVer.preRelease)
            && Objects.equals(this.buildMetadata, semVer.buildMetadata);
    }

    /**
     * <div>
     *   <p>
     *     Computes the hash code for this version based on all components.
     *   </p>
     * </div>
     *
     * @return the hash code
     *
     * @since 1.0.0
     */
    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch, preRelease, buildMetadata);
    }

    /**
     * <div>
     *   <p>
     *     Returns the SemVer-formatted string representation of this version.
     *   </p>
     *   <p>
     *     Format: {@code MAJOR.MINOR.PATCH[-preRelease][+buildMetadata]}
     *   </p>
     *   <p>
     *     Examples:
     *   </p>
     *   <pre>{@code
     *   "1.2.3"
     *   "2.0.0-beta"
     *   "1.5.0+build.456"
     *   "2.1.0-rc.1+20241029"
     *   }</pre>
     * </div>
     *
     * @return the SemVer string; never {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull String echo() {
        final var sb
            = new StringBuilder()
                .append(this.major)
                .append('.')
                .append(this.minor)
                .append('.')
                .append(this.patch);

        this.preRelease.ifSome(pre -> sb.append('-').append(pre));
        this.buildMetadata.ifSome(meta -> sb.append('+').append(meta));

        return sb.toString();
    }

    /**
     * <div>
     *   <p>
     *     Returns a debug-friendly string representation of this version.
     *   </p>
     * </div>
     *
     * @return a string representation showing all internal fields
     *
     * @since 1.0.0
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", SemVer.class.getSimpleName() + "[", "]")
                .add("major=" + major)
                .add("minor=" + minor)
                .add("patch=" + patch)
                .add("preRelease='" + preRelease + "'")
                .add("buildMetadata='" + buildMetadata + "'")
                .toString();
    }

}