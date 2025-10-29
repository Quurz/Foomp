package org.quurz.foomp.plugins;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quurz.foomp.plugins.PluginCoordinate.pluginCoordinate;
import static org.quurz.foomp.plugins.SemVer.semVer;
import static org.quurz.foomp.plugins.SemVer.semVerBuilder;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("PluginCoordinate")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PluginCoordinateTest {

    private static final Logger LOGGER
        = getLogger(PluginCoordinateTest.class);

    @Nested
    @DisplayName("Factory")
    class Factory {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void pluginCoordinate_rejects_null_name() {
            LOGGER.info("PluginCoordinate.pluginCoordinate(...) should reject null name");
            assertThatThrownBy(() -> pluginCoordinate(null, semVer(1, 0, 0)))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("name");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void pluginCoordinate_rejects_null_version() {
            LOGGER.info("PluginCoordinate.pluginCoordinate(...) should reject null version");
            assertThatThrownBy(() -> pluginCoordinate("my-plugin", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("version");
        }

        @Test
        void pluginCoordinate_accepts_valid_inputs() {
            LOGGER.info("PluginCoordinate.pluginCoordinate(...) should accept valid name and version");
            assertThatNoException().isThrownBy(() -> 
                pluginCoordinate("my-plugin", semVer(1, 2, 3))
            );
        }

        @Test
        void pluginCoordinate_creates_coordinate_with_given_values() {
            LOGGER.info("PluginCoordinate.pluginCoordinate(...) should create coordinate with given values");
            var version = semVer(1, 2, 3);
            var coord = pluginCoordinate("database-connector", version);

            assertThat(coord.getName()).isEqualTo("database-connector");
            assertThat(coord.getVersion()).isEqualTo(version);
        }

    }

    @Nested
    @DisplayName("Getters")
    class Getters {

        @Test
        void getName_returns_plugin_name() {
            LOGGER.info("getName() should return the plugin name");
            var coord = pluginCoordinate("test-plugin", semVer(1, 0, 0));
            assertThat(coord.getName()).isEqualTo("test-plugin");
        }

        @Test
        void getVersion_returns_semantic_version() {
            LOGGER.info("getVersion() should return the semantic version");
            var version = semVer(2, 3, 4);
            var coord = pluginCoordinate("plugin", version);
            assertThat(coord.getVersion()).isEqualTo(version);
        }

        @Test
        void getters_preserve_original_values() {
            LOGGER.info("Getters should preserve original values across multiple calls");
            var version = semVerBuilder()
                .major(1)
                .minor(5)
                .patch(2)
                .preRelease("beta")
                .build();
            var coord = pluginCoordinate("my-plugin", version);

            assertThat(coord.getName()).isEqualTo("my-plugin");
            assertThat(coord.getVersion()).isEqualTo(version);
            // Second call
            assertThat(coord.getName()).isEqualTo("my-plugin");
            assertThat(coord.getVersion()).isEqualTo(version);
        }

    }

    @Nested
    @DisplayName("Equals and HashCode")
    class EqualsAndHashCode {

        @Test
        void equals_is_reflexive() {
            LOGGER.info("equals(...) should be reflexive");
            var coord = pluginCoordinate("plugin", semVer(1, 0, 0));
            assertThat(coord).isEqualTo(coord);
        }

        @Test
        void equals_is_symmetric() {
            LOGGER.info("equals(...) should be symmetric");
            var coord1 = pluginCoordinate("plugin", semVer(1, 2, 3));
            var coord2 = pluginCoordinate("plugin", semVer(1, 2, 3));

            assertThat(coord1).isEqualTo(coord2);
            assertThat(coord2).isEqualTo(coord1);
        }

        @Test
        void equals_is_transitive() {
            LOGGER.info("equals(...) should be transitive");
            var coord1 = pluginCoordinate("plugin", semVer(1, 0, 0));
            var coord2 = pluginCoordinate("plugin", semVer(1, 0, 0));
            var coord3 = pluginCoordinate("plugin", semVer(1, 0, 0));

            assertThat(coord1).isEqualTo(coord2);
            assertThat(coord2).isEqualTo(coord3);
            assertThat(coord1).isEqualTo(coord3);
        }

        @Test
        void equals_handles_null() {
            LOGGER.info("equals(...) should handle null");
            var coord = pluginCoordinate("plugin", semVer(1, 0, 0));
            assertThat(coord).isNotEqualTo(null);
        }

        @Test
        void equals_differs_on_different_name() {
            LOGGER.info("equals(...) should return false for different names");
            var coord1 = pluginCoordinate("plugin-a", semVer(1, 0, 0));
            var coord2 = pluginCoordinate("plugin-b", semVer(1, 0, 0));

            assertThat(coord1).isNotEqualTo(coord2);
        }

        @Test
        void equals_differs_on_different_version() {
            LOGGER.info("equals(...) should return false for different versions");
            var coord1 = pluginCoordinate("plugin", semVer(1, 0, 0));
            var coord2 = pluginCoordinate("plugin", semVer(2, 0, 0));

            assertThat(coord1).isNotEqualTo(coord2);
        }

        @Test
        void equals_differs_on_both_name_and_version() {
            LOGGER.info("equals(...) should return false when both name and version differ");
            var coord1 = pluginCoordinate("plugin-a", semVer(1, 0, 0));
            var coord2 = pluginCoordinate("plugin-b", semVer(2, 0, 0));

            assertThat(coord1).isNotEqualTo(coord2);
        }

        @Test
        void equals_considers_version_details() {
            LOGGER.info("equals(...) should consider pre-release and build metadata in version");
            var coord1 = pluginCoordinate("plugin", semVer(1, 0, 0).withPreRelease("alpha"));
            var coord2 = pluginCoordinate("plugin", semVer(1, 0, 0).withPreRelease("beta"));
            var coord3 = pluginCoordinate("plugin", semVer(1, 0, 0));

            assertThat(coord1).isNotEqualTo(coord2);
            assertThat(coord1).isNotEqualTo(coord3);
        }

        @Test
        void hashCode_equal_objects_have_same_hash() {
            LOGGER.info("hashCode() should be equal for equal objects");
            var coord1 = pluginCoordinate("plugin", semVer(1, 2, 3));
            var coord2 = pluginCoordinate("plugin", semVer(1, 2, 3));

            assertThat(coord1).isEqualTo(coord2);
            assertThat(coord1.hashCode()).isEqualTo(coord2.hashCode());
        }

        @Test
        void hashCode_is_consistent() {
            LOGGER.info("hashCode() should be consistent");
            var coord = pluginCoordinate("plugin", semVer(1, 0, 0));
            assertThat(coord.hashCode()).isEqualTo(coord.hashCode());
        }

    }

    @Nested
    @DisplayName("Usage as Map Key")
    class UsageAsMapKey {

        @Test
        void can_be_used_as_map_key() {
            LOGGER.info("PluginCoordinate should work as HashMap key");
            Map<PluginCoordinate, String> map = new HashMap<>();
            var coord1 = pluginCoordinate("plugin-a", semVer(1, 0, 0));
            var coord2 = pluginCoordinate("plugin-b", semVer(2, 0, 0));

            map.put(coord1, "Plugin A");
            map.put(coord2, "Plugin B");

            assertThat(map.get(coord1)).isEqualTo("Plugin A");
            assertThat(map.get(coord2)).isEqualTo("Plugin B");
        }

        @Test
        void equal_coordinates_retrieve_same_map_value() {
            LOGGER.info("Equal coordinates should retrieve same value from map");
            Map<PluginCoordinate, String> map = new HashMap<>();
            var coord1 = pluginCoordinate("plugin", semVer(1, 2, 3));
            var coord2 = pluginCoordinate("plugin", semVer(1, 2, 3));

            map.put(coord1, "Test Plugin");

            assertThat(map.get(coord2)).isEqualTo("Test Plugin");
        }

        @Test
        void different_coordinates_are_distinct_keys() {
            LOGGER.info("Different coordinates should be distinct map keys");
            Map<PluginCoordinate, String> map = new HashMap<>();
            var coord1 = pluginCoordinate("plugin", semVer(1, 0, 0));
            var coord2 = pluginCoordinate("plugin", semVer(2, 0, 0));

            map.put(coord1, "Version 1");
            map.put(coord2, "Version 2");

            assertThat(map).hasSize(2);
            assertThat(map.get(coord1)).isEqualTo("Version 1");
            assertThat(map.get(coord2)).isEqualTo("Version 2");
        }

    }

    @Nested
    @DisplayName("ToString")
    class ToStringMethod {

        @Test
        void toString_contains_name_and_version() {
            LOGGER.info("toString() should contain name and version");
            var coord = pluginCoordinate("my-plugin", semVer(1, 2, 3));
            String str = coord.toString();

            assertThat(str).contains("name='my-plugin'");
            assertThat(str).contains("version=");
        }

        @Test
        void toString_format_is_stable() {
            LOGGER.info("toString() format should be stable");
            var coord = pluginCoordinate("test", semVer(1, 0, 0));
            assertThat(coord.toString()).startsWith("PluginCoordinate[");
            assertThat(coord.toString()).endsWith("]");
        }

    }

    @Nested
    @DisplayName("Immutability")
    class Immutability {

        @Test
        void coordinate_is_immutable() {
            LOGGER.info("PluginCoordinate should be immutable");
            var version = semVer(1, 0, 0);
            var coord = pluginCoordinate("plugin", version);

            // No setters exist, values should not change
            assertThat(coord.getName()).isEqualTo("plugin");
            assertThat(coord.getVersion()).isEqualTo(version);

            // Creating a "new" coordinate with same values should be equal but potentially different instance
            var coord2 = pluginCoordinate("plugin", version);
            assertThat(coord).isEqualTo(coord2);
        }

        @Test
        void version_modifications_dont_affect_coordinate() {
            LOGGER.info("Modifying the version object should not affect existing coordinates");
            var version1 = semVer(1, 0, 0);
            var coord = pluginCoordinate("plugin", version1);

            // Create modified version
            var version2 = version1.incrementMajor();

            // Original coordinate should still have original version
            assertThat(coord.getVersion()).isEqualTo(version1);
            assertThat(coord.getVersion()).isNotEqualTo(version2);
        }

    }

}
