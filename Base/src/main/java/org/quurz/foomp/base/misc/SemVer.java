package org.quurz.foomp.base.misc;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Echo;
import org.quurz.foomp.base.util.Maybe;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Pattern;

import static org.quurz.foomp.base.localisation.BaseMessages.invalidNumericComponentInVersion;
import static org.quurz.foomp.base.localisation.BaseMessages.invalidSemVerFormat;
import static org.quurz.foomp.base.localisation.BaseMessages.majorVersionNegative;
import static org.quurz.foomp.base.localisation.BaseMessages.minorVersionNegative;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.localisation.BaseMessages.patchVersionNegative;
import static org.quurz.foomp.base.util.Maybe.none;
import static org.quurz.foomp.base.util.Util.requireNonNegative;

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
        implements Serializable,
                   Comparable<SemVer>,
                   Echo {

    @Serial
    private static final long serialVersionUID
        = 23L;

    // Regex for SemVer 2.0.0: MAJOR.MINOR.PATCH[-PRERELEASE][+BUILDMETADATA]
    private static final String SEM_VER_PATTERN
        = "^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)" // MAJOR.MINOR.PATCH+
            + "(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)" // Pre-release
            + "(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?"
            + "(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?$";      // Build metadata
    private static final Pattern SEM_VER_PATTERN_REGEX
        = Pattern.compile(SEM_VER_PATTERN);

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
         *     Sets the major version component.
         *   </p>
         * </div>
         *
         * @param major the major version; must be non-negative
         * @return this builder; never {@code null}
         * @throws IllegalArgumentException if {@code major} is negative
         *
         * @since 1.0.0
         */
        public SemVerBuilder major(final int major) {
            this.major
                = requireNonNegative(
                    major,
                    () -> new IllegalArgumentException(majorVersionNegative())
                );
            return this;
        }

        /**
         * <div>
         *   <p>
         *     Sets the minor version component.
         *   </p>
         * </div>
         *
         * @param minor the minor version; must be non-negative
         * @return this builder; never {@code null}
         * @throws IllegalArgumentException if {@code minor} is negative
         *
         * @since 1.0.0
         */
        public SemVerBuilder minor(final int minor) {
            this.minor
                = requireNonNegative(
                    minor,
                    () -> new IllegalArgumentException(minorVersionNegative())
                );
            return this;
        }

        /**
         * <div>
         *   <p>
         *     Sets the patch version component.
         *   </p>
         * </div>
         *
         * @param patch the patch version; must be non-negative
         * @return this builder; never {@code null}
         * @throws IllegalArgumentException if {@code patch} is negative
         *
         * @since 1.0.0
         */
        public SemVerBuilder patch(final int patch) {
            this.patch
                = requireNonNegative(
                    patch,
                    () -> new IllegalArgumentException(patchVersionNegative())
                );
            return this;
        }

        /**
         * <div>
         *   <p>
         *     Sets the pre-release version component.
         *   </p>
         * </div>
         *
         * @param preRelease the pre-release identifier; must not be {@code null}
         * @return this builder; never {@code null}
         * @throws NullPointerException if {@code preRelease} is {@code null}
         *
         * @since 1.0.0
         */
        public SemVerBuilder preRelease(final @NonNull String preRelease) {
            this.preRelease
                = Objects.requireNonNull(preRelease, nullValue("preRelease"));
            return this;
        }

        /**
         * <div>
         *   <p>
         *     Sets the build metadata component.
         *   </p>
         * </div>
         *
         * @param buildMetadata the build metadata string; must not be {@code null}
         * @return this builder; never {@code null}
         * @throws NullPointerException if {@code buildMetadata} is {@code null}
         *
         * @since 1.0.0
         */
        public SemVerBuilder buildMetadata(final @NonNull String buildMetadata) {
            this.buildMetadata
                = Objects.requireNonNull(buildMetadata, nullValue("buildMetadata"));
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
     *     Parses a semantic version string into a {@link SemVer} instance.
     *   </p>
     *   <p>
     *     The parser accepts standard SemVer 2.0.0 format: {@code MAJOR.MINOR.PATCH[-PRERELEASE][+BUILDMETADATA]}
     *   </p>
     *   <p>
     *     Examples of valid inputs:
     *   </p>
     *   <ul>
     *     <li>{@code "1.0.0"}</li>
     *     <li>{@code "1.2.3-alpha"}</li>
     *     <li>{@code "2.0.0-beta.1"}</li>
     *     <li>{@code "1.0.0+build.123"}</li>
     *     <li>{@code "1.2.3-rc.1+build.456"}</li>
     *   </ul>
     * </div>
     *
     * @param version the version string to parse; must not be {@code null}
     * @return a new {@link SemVer} instance; never {@code null}
     * @throws NullPointerException if {@code version} is {@code null}
     * @throws IllegalArgumentException if the version string is not valid SemVer format
     *
     * @since 1.0.0
     */
    public static SemVer parseSemVer(final @NonNull String version) {
        Objects.requireNonNull(version, nullValue("version"));
        final var matcher
            = SEM_VER_PATTERN_REGEX.matcher(version.trim());
        
        if (!matcher.matches()) {
            throw new IllegalArgumentException(invalidSemVerFormat(version));
        }
        
        try {
            final int major
                = Integer.parseInt(matcher.group(1));
            final int minor
                = Integer.parseInt(matcher.group(2));
            final int patch
                = Integer.parseInt(matcher.group(3));
            final String preRelease
                = matcher.group(4); // May be null
            final String buildMetadata
                = matcher.group(5); // May be null
            
            return new SemVer(major, minor, patch, preRelease, buildMetadata);
        } catch (NumberFormatException numberFormatException) {
            throw new IllegalArgumentException(
                invalidNumericComponentInVersion(version), numberFormatException
            );
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
     *     Static factory method to create a {@link SemVer} with mandatory components only.
     *   </p>
     * </div>
     *
     * @param major the major version; must be non-negative
     * @param minor the minor version; must be non-negative
     * @param patch the patch version; must be non-negative
     * @return a new {@link SemVer}; never {@code null}
     * @throws IllegalArgumentException if any numeric component is negative
     *
     * @since 1.0.0
     */
    public static SemVer semVer(final int major,
                                final int minor,
                                final int patch) {
        return new SemVer(major, minor, patch, none(), none());
    }

    private final int major;
    private final int minor;
    private final int patch;
    private final Maybe<String> preRelease;
    private final Maybe<String> buildMetadata;

    /**
     * <div>
     *   <p>
     *     Internal constructor using raw strings for optional components.
     *   </p>
     * </div>
     *
     * @param major the major version
     * @param minor the minor version
     * @param patch the patch version
     * @param preRelease the pre-release string (may be {@code null})
     * @param buildMetadata the build metadata string (may be {@code null})
     */
    public SemVer(final int major,
                  final int minor,
                  final int patch,
                  final String preRelease,
                  final String buildMetadata) {
        this(major, minor, patch, Maybe.maybeOfNullable(preRelease), Maybe.maybeOfNullable(buildMetadata));
    }

    /**
     * <div>
     *   <p>
     *     Canonical constructor for {@link SemVer} using {@link Maybe} for optional components.
     *   </p>
     * </div>
     *
     * @param major the major version; must be non-negative
     * @param minor the minor version; must be non-negative
     * @param patch the patch version; must be non-negative
     * @param preRelease the optional pre-release component; must not be {@code null}
     * @param buildMetadata the optional build metadata component; must not be {@code null}
     * @throws IllegalArgumentException if any numeric component is negative
     * @throws NullPointerException if {@code preRelease} or {@code buildMetadata} is {@code null}
     *
     * @since 1.0.0
     */
    public SemVer(final int major,
                  final int minor,
                  final int patch,
                  final Maybe<String> preRelease,
                  final Maybe<String> buildMetadata) {
        this.major
            = requireNonNegative(
                major,
                () -> new IllegalArgumentException(majorVersionNegative())
        );

        this.minor
            = requireNonNegative(
                minor,
                () -> new IllegalArgumentException(minorVersionNegative())
            );

        this.patch
            =  requireNonNegative(
                patch,
                () -> new IllegalArgumentException(patchVersionNegative())
            );

        this.preRelease
            = Objects.requireNonNull(preRelease, nullValue("preRelease"));
        this.buildMetadata
            = Objects.requireNonNull(buildMetadata, nullValue("buildMetadata"));
    }

    /**
     * @return the major version component
     */
    public int getMajor() {
        return this.major;
    }

    /**
     * @return the minor version component
     */
    public int getMinor() {
        return this.minor;
    }

    /**
     * @return the patch version component
     */
    public int getPatch() {
        return this.patch;
    }

    /**
     * @return the optional pre-release component
     */
    public Maybe<String> getPreRelease() {
        return this.preRelease;
    }

    /**
     * @return the optional build metadata component
     */
    public Maybe<String> getBuildMetadata() {
        return this.buildMetadata;
    }

    /**
     * <div>
     *   <p>
     *     Increments the major version.
     *   </p>
     *   <p>
     *     Following the SemVer specification, this resets the minor and patch versions to 0.
     *     The optional components are preserved.
     *   </p>
     * </div>
     *
     * @return a new {@link SemVer} instance with incremented major version
     *
     * @since 1.0.0
     */
    public SemVer incrementMajor() {
        return new SemVer(this.major + 1, 0, 0, this.preRelease, this.buildMetadata);
    }

    /**
     * <div>
     *   <p>
     *     Increments the minor version.
     *   </p>
     *   <p>
     *     Following the SemVer specification, this resets the patch version to 0.
     *     The optional components are preserved.
     *   </p>
     * </div>
     *
     * @return a new {@link SemVer} instance with incremented minor version
     *
     * @since 1.0.0
     */
    public SemVer incrementMinor() {
        return new SemVer(this.major, this.minor + 1, 0, this.preRelease, this.buildMetadata);
    }

    /**
     * <div>
     *   <p>
     *     Increments the patch version.
     *   </p>
     *   <p>
     *     The optional components are preserved.
     *   </p>
     * </div>
     *
     * @return a new {@link SemVer} instance with incremented patch version
     *
     * @since 1.0.0
     */
    public SemVer incrementPatch() {
        return new SemVer(this.major, this.minor, this.patch + 1, this.preRelease, this.buildMetadata);
    }

    /**
     * <div>
     *   <p>
     *     Returns a new version with the specified pre-release identifier.
     *   </p>
     * </div>
     *
     * @param preRelease the pre-release identifier; must not be {@code null}
     * @return a new {@link SemVer} instance; never {@code null}
     * @throws NullPointerException if {@code preRelease} is {@code null}
     *
     * @since 1.0.0
     */
    public SemVer withPreRelease(final @NonNull String preRelease) {
        return new SemVer(this.major, this.minor, this.patch, Maybe.some(Objects.requireNonNull(preRelease, nullValue("preRelease"))), this.buildMetadata);
    }

    /**
     * <div>
     *   <p>
     *     Returns a new version without any pre-release identifier.
     *   </p>
     * </div>
     *
     * @return a new {@link SemVer} instance; never {@code null}
     *
     * @since 1.0.0
     */
    public SemVer withoutPreRelease() {
        return new SemVer(this.major, this.minor, this.patch, none(), this.buildMetadata);
    }

    /**
     * <div>
     *   <p>
     *     Returns a new version with the specified build metadata.
     *   </p>
     * </div>
     *
     * @param buildMetadata the build metadata string; must not be {@code null}
     * @return a new {@link SemVer} instance; never {@code null}
     * @throws NullPointerException if {@code buildMetadata} is {@code null}
     *
     * @since 1.0.0
     */
    public SemVer withBuildMetadata(final @NonNull String buildMetadata) {
        return new SemVer(this.major, this.minor, this.patch, this.preRelease, Maybe.some(Objects.requireNonNull(buildMetadata, nullValue("buildMetadata"))));
    }

    /**
     * <div>
     *   <p>
     *     Returns a new version without any build metadata.
     *   </p>
     * </div>
     *
     * @return a new {@link SemVer} instance; never {@code null}
     *
     * @since 1.0.0
     */
    public SemVer withoutBuildMetadata() {
        return new SemVer(this.major, this.minor, this.patch, this.preRelease, none());
    }

    /**
     * <div>
     *   <p>
     *     Compares this version to another {@link SemVer} based on precedence rules.
     *   </p>
     *   <p>
     *     Precedence is determined by the first difference when comparing MAJOR, MINOR,
     *     and PATCH components from left to right. Normal versions have higher precedence
     *     than pre-release versions. Build metadata is ignored.
     *   </p>
     * </div>
     *
     * @param other the version to compare to; must not be {@code null}
     * @return a negative integer, zero, or a positive integer as this version
     *         is less than, equal to, or greater than the specified version
     * @throws NullPointerException if {@code other} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public int compareTo(final @NonNull SemVer other) {
        Objects.requireNonNull(other, nullValue("other"));

        if (this.major != other.major) {
            return Integer.compare(this.major, other.major);
        }
        if (this.minor != other.minor) {
            return Integer.compare(this.minor, other.minor);
        }
        if (this.patch != other.patch) {
            return Integer.compare(this.patch, other.patch);
        }

        // Pre-releases have lower precedence than the same version without pre-release
        if (this.preRelease.isNone() && other.preRelease.isSome()) {
            return 1;
        }
        if (this.preRelease.isSome() && other.preRelease.isNone()) {
            return -1;
        }
        if (this.preRelease.isNone() && other.preRelease.isNone()) {
            return 0;
        }

        // Both have pre-release: lexicographical compare
        return this.preRelease.get().compareTo(other.preRelease.get());
    }

    /**
     * {@inheritDoc}
     * <p>
     *   Two {@link SemVer} instances are equal if and only if all their components
     *   (major, minor, patch, pre-release, and build metadata) are equal.
     * </p>
     * <p>
     *   Note: This differs from {@link #compareTo(SemVer)}, which ignores build metadata
     *   according to the SemVer 2.0.0 specification.
     * </p>
     *
     * @param o the object to compare with
     * @return {@code true} if this version equals the specified object
     *
     * @since 1.0.0
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof SemVer that)) return false;
        return major == that.major
            && minor == that.minor
            && patch == that.patch
            && Objects.equals(preRelease, that.preRelease)
            && Objects.equals(buildMetadata, that.buildMetadata);
    }

    /**
     * {@inheritDoc}
     * <p>
     *   The hash code is calculated based on all version components: major, minor, patch,
     *   pre-release, and build metadata.
     * </p>
     *
     * @return the hash code value for this version
     *
     * @since 1.0.0
     */
    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch, preRelease, buildMetadata);
    }

    @Override
    @NonNull
    public String echo() {
        final var sb = new StringBuilder()
            .append(this.major)
            .append('.')
            .append(this.minor)
            .append('.')
            .append(this.patch);
        if (this.preRelease.isSome()) {
            sb.append('-').append(this.preRelease.get());
        }
        if (this.buildMetadata.isSome()) {
            sb.append('+').append(this.buildMetadata.get());
        }
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     * <p>
     *   Returns a string representation for debugging and logging purposes.
     * </p>
     * <p>
     *   Note: For a valid SemVer 2.0.0 formatted string (e.g., "1.2.3-beta+build"),
     *   use {@link #echo()} instead.
     * </p>
     *
     * @return a debug string representation of this version
     *
     * @since 1.0.0
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", SemVer.class.getSimpleName() + "[", "]")
                .add("major=" + major)
                .add("minor=" + minor)
                .add("patch=" + patch)
                .add("preRelease=" + preRelease)
                .add("buildMetadata=" + buildMetadata)
                .toString();
    }

}
