package org.quurz.foomp.base.misc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quurz.foomp.base.misc.SemVer.semVer;
import static org.quurz.foomp.base.misc.SemVer.semVerBuilder;
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
    @DisplayName("Mutators")
    class Mutators {

        @Test
        void withPreRelease_adds_prerelease_component() {
            LOGGER.info("withPreRelease() should add pre-release component");
            var version = semVer(1, 0, 0).withPreRelease("alpha.1");

            assertThat(version.getPreRelease().isSome()).isTrue();
            assertThat(version.getPreRelease().get()).isEqualTo("alpha.1");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void withPreRelease_rejects_null() {
            LOGGER.info("withPreRelease() should reject null");
            assertThatThrownBy(() -> semVer(1, 0, 0).withPreRelease(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void withoutPreRelease_removes_prerelease_component() {
            LOGGER.info("withoutPreRelease() should remove pre-release component");
            var version = semVer(1, 0, 0).withPreRelease("alpha").withoutPreRelease();
            assertThat(version.getPreRelease().isNone()).isTrue();
        }

        @Test
        void withBuildMetadata_adds_build_metadata_component() {
            LOGGER.info("withBuildMetadata() should add build metadata component");
            var version = semVer(1, 0, 0).withBuildMetadata("build.123");

            assertThat(version.getBuildMetadata().isSome()).isTrue();
            assertThat(version.getBuildMetadata().get()).isEqualTo("build.123");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void withBuildMetadata_rejects_null() {
            LOGGER.info("withBuildMetadata() should reject null");
            assertThatThrownBy(() -> semVer(1, 0, 0).withBuildMetadata(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void withoutBuildMetadata_removes_build_metadata_component() {
            LOGGER.info("withoutBuildMetadata() should remove build metadata component");
            var version = semVer(1, 0, 0).withBuildMetadata("build.1").withoutBuildMetadata();
            assertThat(version.getBuildMetadata().isNone()).isTrue();
        }

    }

    @Nested
    @DisplayName("Increment operations")
    class IncrementOperations {

        @Test
        void incrementMajor_increments_major_and_resets_minor_and_patch() {
            LOGGER.info("incrementMajor() should increment major and reset minor and patch to 0");
            var version = semVer(1, 2, 3).incrementMajor();

            assertThat(version.getMajor()).isEqualTo(2);
            assertThat(version.getMinor()).isEqualTo(0);
            assertThat(version.getPatch()).isEqualTo(0);
        }

        @Test
        void incrementMajor_preserves_prerelease_and_metadata() {
            LOGGER.info("incrementMajor() should preserve pre-release and build metadata");
            var version = semVer(1, 2, 3)
                    .withPreRelease("alpha")
                    .withBuildMetadata("build.1")
                    .incrementMajor();

            // Note: Your implementation preserves these, testing actual behavior
            assertThat(version.getPreRelease().isSome()).isTrue();
            assertThat(version.getBuildMetadata().isSome()).isTrue();
        }

        @Test
        void incrementMinor_increments_minor_and_resets_patch() {
            LOGGER.info("incrementMinor() should increment minor and reset patch to 0");
            var version = semVer(1, 2, 3).incrementMinor();

            assertThat(version.getMajor()).isEqualTo(1);
            assertThat(version.getMinor()).isEqualTo(3);
            assertThat(version.getPatch()).isEqualTo(0);
        }

        @Test
        void incrementMinor_preserves_prerelease_and_metadata() {
            LOGGER.info("incrementMinor() should preserve pre-release and build metadata");
            var version = semVer(1, 2, 3)
                    .withPreRelease("beta")
                    .withBuildMetadata("build.2")
                    .incrementMinor();

            assertThat(version.getPreRelease().isSome()).isTrue();
            assertThat(version.getBuildMetadata().isSome()).isTrue();
        }

        @Test
        void incrementPatch_increments_patch_only() {
            LOGGER.info("incrementPatch() should increment patch only");
            var version = semVer(1, 2, 3).incrementPatch();

            assertThat(version.getMajor()).isEqualTo(1);
            assertThat(version.getMinor()).isEqualTo(2);
            assertThat(version.getPatch()).isEqualTo(4);
        }

        @Test
        void incrementPatch_preserves_prerelease_and_metadata() {
            LOGGER.info("incrementPatch() should preserve pre-release and build metadata");
            var version = semVer(1, 2, 3)
                    .withPreRelease("rc.1")
                    .withBuildMetadata("build.3")
                    .incrementPatch();

            assertThat(version.getPreRelease().isSome()).isTrue();
            assertThat(version.getBuildMetadata().isSome()).isTrue();
        }

    }

    @Nested
    @DisplayName("Comparison and ordering")
    class ComparisonAndOrdering {

        @Test
        void compareTo_compares_major_version_first() {
            LOGGER.info("compareTo() should compare major version first");
            var v1 = semVer(1, 0, 0);
            var v2 = semVer(2, 0, 0);

            assertThat(v1.compareTo(v2)).isNegative();
            assertThat(v2.compareTo(v1)).isPositive();
        }

        @Test
        void compareTo_compares_minor_when_major_equal() {
            LOGGER.info("compareTo() should compare minor when major is equal");
            var v1 = semVer(1, 1, 0);
            var v2 = semVer(1, 2, 0);

            assertThat(v1.compareTo(v2)).isNegative();
            assertThat(v2.compareTo(v1)).isPositive();
        }

        @Test
        void compareTo_compares_patch_when_major_and_minor_equal() {
            LOGGER.info("compareTo() should compare patch when major and minor are equal");
            var v1 = semVer(1, 1, 1);
            var v2 = semVer(1, 1, 2);

            assertThat(v1.compareTo(v2)).isNegative();
            assertThat(v2.compareTo(v1)).isPositive();
        }

        @Test
        void compareTo_returns_zero_for_equal_versions() {
            LOGGER.info("compareTo() should return 0 for equal versions");
            var v1 = semVer(1, 2, 3);
            var v2 = semVer(1, 2, 3);

            assertThat(v1.compareTo(v2)).isZero();
        }

        @Test
        void compareTo_returns_zero_for_equal_versions_without_prerelease() {
            LOGGER.info("compareTo() should return 0 for equal versions both without pre-release");
            var v1 = semVer(1, 2, 3).withBuildMetadata("build.1");
            var v2 = semVer(1, 2, 3).withBuildMetadata("build.2");

            // Both have NO pre-release, but different build metadata (which is ignored)
            assertThat(v1.getPreRelease().isNone()).isTrue();
            assertThat(v2.getPreRelease().isNone()).isTrue();
            assertThat(v1.compareTo(v2)).isZero();
        }

        @Test
        void compareTo_prerelease_less_than_release() {
            LOGGER.info("compareTo() should consider pre-release versions less than release versions");
            var v1 = semVer(1, 0, 0).withPreRelease("alpha");
            var v2 = semVer(1, 0, 0);

            assertThat(v1.compareTo(v2)).isNegative();
            assertThat(v2.compareTo(v1)).isPositive();
        }

        @Test
        void compareTo_compares_prerelease_lexicographically() {
            LOGGER.info("compareTo() should compare pre-release versions lexicographically");
            var v1 = semVer(1, 0, 0).withPreRelease("alpha");
            var v2 = semVer(1, 0, 0).withPreRelease("beta");

            assertThat(v1.compareTo(v2)).isNegative();
            assertThat(v2.compareTo(v1)).isPositive();
        }

        @Test
        void compareTo_ignores_build_metadata() {
            LOGGER.info("compareTo() should ignore build metadata");
            var v1 = semVer(1, 0, 0).withBuildMetadata("build.1");
            var v2 = semVer(1, 0, 0).withBuildMetadata("build.2");

            assertThat(v1.compareTo(v2)).isZero();
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void compareTo_throws_on_null() {
            LOGGER.info("compareTo() should throw NullPointerException on null argument");
            var version = semVer(1, 0, 0);

            assertThatThrownBy(() -> version.compareTo(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void compareTo_both_without_prerelease_returns_zero() {
            var v1 = semVer(5, 5, 5);
            var v2 = semVer(5, 5, 5);

            assertThat(v1.compareTo(v2)).isZero();
        }

        @Test
        void compareTo_prerelease_less_than_same_version_without_prerelease() {
            LOGGER.info("compareTo() should return negative when comparing pre-release to same version without pre-release");
            var v1 = semVer(5, 5, 5).withPreRelease("alpha.1");
            var v2 = semVer(5, 5, 5);

            assertThat(v1.compareTo(v2)).isNegative();
        }

        @Test
        void compareTo_release_greater_than_same_version_with_prerelease() {
            LOGGER.info("compareTo() should return positive when comparing release to same version with pre-release");
            var v1 = semVer(5, 5, 5);
            var v2 = semVer(5, 5, 5).withPreRelease("alpha.1");

            assertThat(v1.compareTo(v2)).isPositive();
        }

        @Test
        void compareTo_returns_zero_for_identical_prerelease_versions() {
            LOGGER.info("compareTo() should return 0 for identical pre-release versions");
            var v1 = semVer(5, 5, 5).withPreRelease("alpha.1");
            var v2 = semVer(5, 5, 5).withPreRelease("alpha.1");

            assertThat(v1.compareTo(v2)).isZero();
        }

        @Test
        void compareTo_compares_different_prerelease_identifiers_lexicographically() {
            LOGGER.info("compareTo() should compare different pre-release identifiers lexicographically");
            var v1 = semVer(5, 5, 5).withPreRelease("beta.1");
            var v2 = semVer(5, 5, 5).withPreRelease("alpha.1");

            assertThat(v1.compareTo(v2)).isPositive();
        }

    }

    @Nested
    @DisplayName("Equality and hash code")
    class EqualityAndHashCode {

        @Test
        void equals_returns_true_for_identical_versions() {
            LOGGER.info("equals() should return true for identical versions");
            var v1 = semVer(1, 2, 3);
            var v2 = semVer(1, 2, 3);

            assertThat(v1).isEqualTo(v2);
            assertThat(v2).isEqualTo(v1);
        }

        @Test
        void equals_returns_false_for_different_major() {
            LOGGER.info("equals() should return false for different major version");
            var v1 = semVer(1, 2, 3);
            var v2 = semVer(2, 2, 3);

            assertThat(v1).isNotEqualTo(v2);
        }

        @Test
        void equals_returns_false_for_different_minor() {
            LOGGER.info("equals() should return false for different minor version");
            var v1 = semVer(1, 2, 3);
            var v2 = semVer(1, 3, 3);

            assertThat(v1).isNotEqualTo(v2);
        }

        @Test
        void equals_returns_false_for_different_patch() {
            LOGGER.info("equals() should return false for different patch version");
            var v1 = semVer(1, 2, 3);
            var v2 = semVer(1, 2, 4);

            assertThat(v1).isNotEqualTo(v2);
        }

        @Test
        void equals_considers_prerelease() {
            LOGGER.info("equals() should consider pre-release component");
            var v1 = semVer(1, 0, 0).withPreRelease("alpha");
            var v2 = semVer(1, 0, 0).withPreRelease("beta");
            var v3 = semVer(1, 0, 0);

            assertThat(v1).isNotEqualTo(v2);
            assertThat(v1).isNotEqualTo(v3);
        }

        @Test
        void equals_considers_build_metadata() {
            LOGGER.info("equals() should consider build metadata component");
            var v1 = semVer(1, 0, 0).withBuildMetadata("build.1");
            var v2 = semVer(1, 0, 0).withBuildMetadata("build.2");
            var v3 = semVer(1, 0, 0);

            assertThat(v1).isNotEqualTo(v2);
            assertThat(v1).isNotEqualTo(v3);
        }

        @SuppressWarnings("EqualsWithItself")
        @Test
        void equals_returns_true_for_same_instance() {
            LOGGER.info("equals() should return true for same instance");
            var version = semVer(1, 2, 3);

            assertThat(version).isEqualTo(version);
        }

        @Test
        void equals_returns_false_for_null() {
            LOGGER.info("equals() should return false for null");
            var version = semVer(1, 2, 3);

            assertThat(version).isNotEqualTo(null);
        }

        @Test
        void equals_returns_false_for_different_type() {
            LOGGER.info("equals() should return false for different type");
            var version = semVer(1, 2, 3);

            assertThat(version).isNotEqualTo("1.2.3");
        }

        @Test
        void hashCode_equal_for_equal_versions() {
            LOGGER.info("hashCode() should be equal for equal versions");
            var v1 = semVer(1, 2, 3).withPreRelease("alpha").withBuildMetadata("build.1");
            var v2 = semVer(1, 2, 3).withPreRelease("alpha").withBuildMetadata("build.1");

            assertThat(v1.hashCode()).isEqualTo(v2.hashCode());
        }

        @Test
        void hashCode_different_for_different_versions() {
            LOGGER.info("hashCode() should likely be different for different versions");
            var v1 = semVer(1, 2, 3);
            var v2 = semVer(1, 2, 4);

            // Note: This is not guaranteed by contract, but highly likely
            assertThat(v1.hashCode()).isNotEqualTo(v2.hashCode());
        }

    }

    @Nested
    @DisplayName("String representation")
    class StringRepresentation {

        @Test
        void echo_formats_basic_version() {
            LOGGER.info("echo() should format basic version as major.minor.patch");
            var version = semVer(1, 2, 3);

            assertThat(version.echo()).isEqualTo("1.2.3");
        }

        @Test
        void echo_includes_prerelease() {
            LOGGER.info("echo() should include pre-release component");
            var version = semVer(1, 2, 3).withPreRelease("alpha.1");

            assertThat(version.echo()).isEqualTo("1.2.3-alpha.1");
        }

        @Test
        void echo_includes_build_metadata() {
            LOGGER.info("echo() should include build metadata component");
            var version = semVer(1, 2, 3).withBuildMetadata("build.456");

            assertThat(version.echo()).isEqualTo("1.2.3+build.456");
        }

        @Test
        void echo_includes_both_prerelease_and_metadata() {
            LOGGER.info("echo() should include both pre-release and build metadata");
            var version = semVer(1, 2, 3)
                    .withPreRelease("beta.2")
                    .withBuildMetadata("build.789");

            assertThat(version.echo()).isEqualTo("1.2.3-beta.2+build.789");
        }

        @Test
        void toString_contains_all_components() {
            LOGGER.info("toString() should contain all components");
            var version = semVer(1, 2, 3)
                    .withPreRelease("alpha")
                    .withBuildMetadata("build.1");

            var str = version.toString();
            assertThat(str).contains("major=1");
            assertThat(str).contains("minor=2");
            assertThat(str).contains("patch=3");
            assertThat(str).contains("preRelease");
            assertThat(str).contains("buildMetadata");
        }

    }

}