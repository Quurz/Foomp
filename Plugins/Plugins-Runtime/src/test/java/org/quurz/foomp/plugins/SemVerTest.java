package org.quurz.foomp.plugins;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quurz.foomp.plugins.SemVer.semVer;
import static org.quurz.foomp.plugins.SemVer.semVerBuilder;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("SemVer")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SemVerTest {

    private static final Logger LOGGER
        = getLogger(SemVerTest.class);

    @Nested
    @DisplayName("Factory")
    class Factory {

        @Test
        void semVer_rejects_negative_major() {
            LOGGER.info("SemVer.semVer(...) should reject negative major version");
            assertThatThrownBy(() -> semVer(-1, 0, 0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("major");
        }

        @Test
        void semVer_rejects_negative_minor() {
            LOGGER.info("SemVer.semVer(...) should reject negative minor version");
            assertThatThrownBy(() -> semVer(0, -1, 0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("minor");
        }

        @Test
        void semVer_rejects_negative_patch() {
            LOGGER.info("SemVer.semVer(...) should reject negative patch version");
            assertThatThrownBy(() -> semVer(0, 0, -1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("patch");
        }

        @Test
        void semVer_accepts_valid_components() {
            LOGGER.info("SemVer.semVer(...) should accept valid non-negative components");
            assertThatNoException().isThrownBy(() -> semVer(1, 2, 3));
            assertThatNoException().isThrownBy(() -> semVer(0, 0, 0));
        }

        @Test
        void semVer_creates_version_without_prerelease_and_metadata() {
            LOGGER.info("SemVer.semVer(...) should create version without pre-release and build metadata");
            var version = semVer(1, 2, 3);

            assertThat(version.getMajor()).isEqualTo(1);
            assertThat(version.getMinor()).isEqualTo(2);
            assertThat(version.getPatch()).isEqualTo(3);
            assertThat(version.getPreRelease().isNone()).isTrue();
            assertThat(version.getBuildMetadata().isNone()).isTrue();
        }

    }

    @Nested
    @DisplayName("Builder")
    class Builder {

        @Test
        void builder_defaults_to_zero_versions() {
            LOGGER.info("SemVerBuilder should default to 0.0.0");
            var version = semVerBuilder().build();

            assertThat(version.getMajor()).isEqualTo(0);
            assertThat(version.getMinor()).isEqualTo(0);
            assertThat(version.getPatch()).isEqualTo(0);
        }

        @Test
        void builder_major_rejects_negative() {
            LOGGER.info("SemVerBuilder.major(...) should reject negative value");
            assertThatThrownBy(() -> semVerBuilder().major(-1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("major");
        }

        @Test
        void builder_minor_rejects_negative() {
            LOGGER.info("SemVerBuilder.minor(...) should reject negative value");
            assertThatThrownBy(() -> semVerBuilder().minor(-1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("minor");
        }

        @Test
        void builder_patch_rejects_negative() {
            LOGGER.info("SemVerBuilder.patch(...) should reject negative value");
            assertThatThrownBy(() -> semVerBuilder().patch(-1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("patch");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void builder_preRelease_rejects_null() {
            LOGGER.info("SemVerBuilder.preRelease(...) should reject null");
            assertThatThrownBy(() -> semVerBuilder().preRelease(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void builder_buildMetadata_rejects_null() {
            LOGGER.info("SemVerBuilder.buildMetadata(...) should reject null");
            assertThatThrownBy(() -> semVerBuilder().buildMetadata(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void builder_fluent_api_works() {
            LOGGER.info("SemVerBuilder should support fluent API");
            var version = semVerBuilder()
                    .major(2)
                    .minor(1)
                    .patch(5)
                    .preRelease("beta.1")
                    .buildMetadata("build.20241029")
                    .build();

            assertThat(version.getMajor()).isEqualTo(2);
            assertThat(version.getMinor()).isEqualTo(1);
            assertThat(version.getPatch()).isEqualTo(5);
            assertThat(version.getPreRelease().get()).isEqualTo("beta.1");
            assertThat(version.getBuildMetadata().get()).isEqualTo("build.20241029");
        }

    }

    @Nested
    @DisplayName("Getters")
    class Getters {

        @Test
        void getters_return_correct_values() {
            LOGGER.info("SemVer getters should return correct values");
            var version = semVerBuilder()
                    .major(3)
                    .minor(4)
                    .patch(5)
                    .preRelease("alpha")
                    .buildMetadata("meta")
                    .build();

            assertThat(version.getMajor()).isEqualTo(3);
            assertThat(version.getMinor()).isEqualTo(4);
            assertThat(version.getPatch()).isEqualTo(5);
            assertThat(version.getPreRelease().isSome()).isTrue();
            assertThat(version.getPreRelease().get()).isEqualTo("alpha");
            assertThat(version.getBuildMetadata().isSome()).isTrue();
            assertThat(version.getBuildMetadata().get()).isEqualTo("meta");
        }

    }

    @Nested
    @DisplayName("Increment Operations")
    class IncrementOperations {

        @Test
        void incrementMajor_resets_minor_and_patch_and_removes_metadata() {
            LOGGER.info("incrementMajor() should reset minor/patch to 0 and remove pre-release and build metadata");
            var version = semVer(1, 2, 3)
                    .withPreRelease("beta")
                    .withBuildMetadata("build.123");

            var incremented = version.incrementMajor();

            assertThat(incremented.getMajor()).isEqualTo(2);
            assertThat(incremented.getMinor()).isEqualTo(0);
            assertThat(incremented.getPatch()).isEqualTo(0);
            assertThat(incremented.getPreRelease().isNone()).isTrue();
            assertThat(incremented.getBuildMetadata().isNone()).isTrue();
        }

        @Test
        void incrementMinor_resets_patch_and_removes_metadata() {
            LOGGER.info("incrementMinor() should reset patch to 0 and remove pre-release and build metadata");
            var version = semVer(1, 2, 3)
                    .withPreRelease("rc.1")
                    .withBuildMetadata("build.456");

            var incremented = version.incrementMinor();

            assertThat(incremented.getMajor()).isEqualTo(1);
            assertThat(incremented.getMinor()).isEqualTo(3);
            assertThat(incremented.getPatch()).isEqualTo(0);
            assertThat(incremented.getPreRelease().isNone()).isTrue();
            assertThat(incremented.getBuildMetadata().isNone()).isTrue();
        }

        @Test
        void incrementPatch_removes_metadata() {
            LOGGER.info("incrementPatch() should remove pre-release and build metadata");
            var version = semVer(1, 2, 3)
                    .withPreRelease("alpha")
                    .withBuildMetadata("build.789");

            var incremented = version.incrementPatch();

            assertThat(incremented.getMajor()).isEqualTo(1);
            assertThat(incremented.getMinor()).isEqualTo(2);
            assertThat(incremented.getPatch()).isEqualTo(4);
            assertThat(incremented.getPreRelease().isNone()).isTrue();
            assertThat(incremented.getBuildMetadata().isNone()).isTrue();
        }

    }

    @Nested
    @DisplayName("With Operations")
    class WithOperations {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void withPreRelease_rejects_null() {
            LOGGER.info("withPreRelease(...) should reject null");
            var version = semVer(1, 0, 0);
            assertThatThrownBy(() -> version.withPreRelease(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void withPreRelease_sets_prerelease_and_removes_buildmetadata() {
            LOGGER.info("withPreRelease(...) should set pre-release and remove build metadata");
            var version = semVer(1, 2, 3).withBuildMetadata("build.1");
            var modified = version.withPreRelease("beta");

            assertThat(modified.getPreRelease().get()).isEqualTo("beta");
            assertThat(modified.getBuildMetadata().isNone()).isTrue();
        }

        @Test
        void withoutPreRelease_removes_prerelease_preserves_buildmetadata() {
            LOGGER.info("withoutPreRelease() should remove pre-release but preserve build metadata");
            var version = semVer(1, 2, 3)
                    .withPreRelease("alpha")
                    .withBuildMetadata("build.2");

            var modified = version.withoutPreRelease();

            assertThat(modified.getPreRelease().isNone()).isTrue();
            assertThat(modified.getBuildMetadata().get()).isEqualTo("build.2");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void withBuildMetadata_rejects_null() {
            LOGGER.info("withBuildMetadata(...) should reject null");
            var version = semVer(1, 0, 0);
            assertThatThrownBy(() -> version.withBuildMetadata(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void withBuildMetadata_sets_buildmetadata_preserves_prerelease() {
            LOGGER.info("withBuildMetadata(...) should set build metadata and preserve pre-release");
            var version = semVer(1, 2, 3).withPreRelease("rc.1");
            var modified = version.withBuildMetadata("build.123");

            assertThat(modified.getBuildMetadata().get()).isEqualTo("build.123");
            assertThat(modified.getPreRelease().get()).isEqualTo("rc.1");
        }

        @Test
        void withoutBuildMetadata_removes_buildmetadata_preserves_prerelease() {
            LOGGER.info("withoutBuildMetadata() should remove build metadata but preserve pre-release");
            var version = semVer(1, 2, 3)
                    .withPreRelease("beta")
                    .withBuildMetadata("build.456");

            var modified = version.withoutBuildMetadata();

            assertThat(modified.getBuildMetadata().isNone()).isTrue();
            assertThat(modified.getPreRelease().get()).isEqualTo("beta");
        }

    }

    @Nested
    @DisplayName("CompareTo")
    class CompareTo {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void compareTo_rejects_null() {
            LOGGER.info("compareTo(...) should reject null");
            var version = semVer(1, 0, 0);
            assertThatThrownBy(() -> version.compareTo(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void compareTo_compares_major_first() {
            LOGGER.info("compareTo(...) should compare MAJOR first");
            assertThat(semVer(2, 0, 0).compareTo(semVer(1, 9, 9))).isPositive();
            assertThat(semVer(1, 0, 0).compareTo(semVer(2, 0, 0))).isNegative();
        }

        @Test
        void compareTo_compares_minor_when_major_equal() {
            LOGGER.info("compareTo(...) should compare MINOR when MAJOR equal");
            assertThat(semVer(1, 2, 0).compareTo(semVer(1, 1, 9))).isPositive();
            assertThat(semVer(1, 1, 0).compareTo(semVer(1, 2, 0))).isNegative();
        }

        @Test
        void compareTo_compares_patch_when_major_and_minor_equal() {
            LOGGER.info("compareTo(...) should compare PATCH when MAJOR and MINOR equal");
            assertThat(semVer(1, 2, 3).compareTo(semVer(1, 2, 2))).isPositive();
            assertThat(semVer(1, 2, 1).compareTo(semVer(1, 2, 3))).isNegative();
        }

        @Test
        void compareTo_prerelease_has_lower_precedence_than_release() {
            LOGGER.info("compareTo(...) should treat pre-release versions as lower than release versions");
            var release = semVer(1, 0, 0);
            var prerelease = semVer(1, 0, 0).withPreRelease("alpha");

            assertThat(prerelease.compareTo(release)).isNegative();
            assertThat(release.compareTo(prerelease)).isPositive();
        }

        @Test
        void compareTo_prereleases_are_compared_lexically() {
            LOGGER.info("compareTo(...) should compare pre-releases lexically");
            var alpha = semVer(1, 0, 0).withPreRelease("alpha");
            var beta = semVer(1, 0, 0).withPreRelease("beta");

            assertThat(alpha.compareTo(beta)).isNegative();
            assertThat(beta.compareTo(alpha)).isPositive();
        }

        @Test
        void compareTo_ignores_build_metadata() {
            LOGGER.info("compareTo(...) should ignore build metadata per SemVer spec");
            var v1 = semVer(1, 0, 0).withBuildMetadata("build.1");
            var v2 = semVer(1, 0, 0).withBuildMetadata("build.2");

            assertThat(v1.compareTo(v2)).isZero();
            assertThat(v2.compareTo(v1)).isZero();
        }

        @Test
        void compareTo_equal_versions_return_zero() {
            LOGGER.info("compareTo(...) should return zero for equal versions");
            var v1 = semVer(1, 2, 3);
            var v2 = semVer(1, 2, 3);

            assertThat(v1.compareTo(v2)).isZero();
        }

    }

    @Nested
    @DisplayName("Equals and HashCode")
    class EqualsAndHashCode {

        @SuppressWarnings("EqualsWithItself")
        @Test
        void equals_is_reflexive() {
            LOGGER.info("equals(...) should be reflexive");
            var version = semVer(1, 2, 3);
            assertThat(version).isEqualTo(version);
        }

        @Test
        void equals_is_symmetric() {
            LOGGER.info("equals(...) should be symmetric");
            var v1 = semVer(1, 2, 3).withPreRelease("beta");
            var v2 = semVer(1, 2, 3).withPreRelease("beta");

            assertThat(v1).isEqualTo(v2);
            assertThat(v2).isEqualTo(v1);
        }

        @Test
        void equals_is_transitive() {
            LOGGER.info("equals(...) should be transitive");
            var v1 = semVer(1, 2, 3);
            var v2 = semVer(1, 2, 3);
            var v3 = semVer(1, 2, 3);

            assertThat(v1).isEqualTo(v2);
            assertThat(v2).isEqualTo(v3);
            assertThat(v1).isEqualTo(v3);
        }

        @Test
        void equals_handles_null() {
            LOGGER.info("equals(...) should handle null");
            var version = semVer(1, 2, 3);
            assertThat(version).isNotEqualTo(null);
        }

        @Test
        void equals_includes_build_metadata() {
            LOGGER.info("equals(...) should include build metadata (unlike compareTo)");
            var v1 = semVer(1, 0, 0).withBuildMetadata("build.1");
            var v2 = semVer(1, 0, 0).withBuildMetadata("build.2");

            assertThat(v1).isNotEqualTo(v2);
        }

        @Test
        void equals_includes_prerelease() {
            LOGGER.info("equals(...) should include pre-release");
            var v1 = semVer(1, 0, 0).withPreRelease("alpha");
            var v2 = semVer(1, 0, 0).withPreRelease("beta");
            var v3 = semVer(1, 0, 0);

            assertThat(v1).isNotEqualTo(v2);
            assertThat(v1).isNotEqualTo(v3);
        }

        @Test
        void hashCode_equal_objects_have_same_hash() {
            LOGGER.info("hashCode() should be equal for equal objects");
            var v1 = semVer(1, 2, 3).withPreRelease("beta").withBuildMetadata("build.1");
            var v2 = semVer(1, 2, 3).withPreRelease("beta").withBuildMetadata("build.1");

            assertThat(v1).isEqualTo(v2);
            assertThat(v1.hashCode()).isEqualTo(v2.hashCode());
        }

        @Test
        void hashCode_is_consistent() {
            LOGGER.info("hashCode() should be consistent");
            var version = semVer(1, 2, 3);
            assertThat(version.hashCode()).isEqualTo(version.hashCode());
        }

    }

    @Nested
    @DisplayName("Echo")
    class EchoMethod {

        @Test
        void echo_formats_basic_version() {
            LOGGER.info("echo() should format basic version as MAJOR.MINOR.PATCH");
            var version = semVer(1, 2, 3);
            assertThat(version.echo()).isEqualTo("1.2.3");
        }

        @Test
        void echo_includes_prerelease() {
            LOGGER.info("echo() should include pre-release with hyphen");
            var version = semVer(2, 0, 0).withPreRelease("beta.1");
            assertThat(version.echo()).isEqualTo("2.0.0-beta.1");
        }

        @Test
        void echo_includes_buildmetadata() {
            LOGGER.info("echo() should include build metadata with plus");
            var version = semVer(1, 5, 0).withBuildMetadata("build.456");
            assertThat(version.echo()).isEqualTo("1.5.0+build.456");
        }

        @Test
        void echo_includes_both_prerelease_and_buildmetadata() {
            LOGGER.info("echo() should include both pre-release and build metadata");
            var version = semVerBuilder()
                    .major(2)
                    .minor(1)
                    .patch(0)
                    .preRelease("rc.1")
                    .buildMetadata("20241029")
                    .build();

            assertThat(version.echo()).isEqualTo("2.1.0-rc.1+20241029");
        }

        @Test
        void echo_handles_zero_version() {
            LOGGER.info("echo() should handle 0.0.0 version");
            var version = semVer(0, 0, 0);
            assertThat(version.echo()).isEqualTo("0.0.0");
        }

    }

    @Nested
    @DisplayName("ToString")
    class ToStringMethod {

        @Test
        void toString_contains_all_fields() {
            LOGGER.info("toString() should contain all fields for debugging");
            var version = semVerBuilder()
                    .major(1)
                    .minor(2)
                    .patch(3)
                    .preRelease("beta")
                    .buildMetadata("build.1")
                    .build();

            String str = version.toString();
            assertThat(str).contains("major=1");
            assertThat(str).contains("minor=2");
            assertThat(str).contains("patch=3");
            assertThat(str).contains("preRelease");
            assertThat(str).contains("buildMetadata");
        }

    }

}