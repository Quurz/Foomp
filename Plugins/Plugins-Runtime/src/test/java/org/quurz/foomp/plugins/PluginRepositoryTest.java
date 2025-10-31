package org.quurz.foomp.plugins;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.junit.jupiter.api.*;
import org.quurz.foomp.base.misc.SemVer;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;
import static org.quurz.foomp.plugins.PluginCoordinate.pluginCoordinate;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("PluginRepository")
class PluginRepositoryTest {

    private static final Logger LOGGER
        = getLogger(PluginRepositoryTest.class);

    private FileSystem fileSystem;
    private Path baseDirectory;
    private PluginRepository repository;

    @BeforeEach
    void setUp() throws IOException {
        // Create in-memory file system with Jimfs
        fileSystem = Jimfs.newFileSystem(Configuration.unix());
        baseDirectory = fileSystem.getPath("/plugins");
        Files.createDirectories(baseDirectory);
        
        repository = PluginRepository.pluginRepository(baseDirectory);
        
        LOGGER.debug("Created test repository at: {}", baseDirectory);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (fileSystem != null) {
            fileSystem.close();
        }
    }

    @Nested
    @DisplayName("Factory method tests")
    class FactoryMethodTests {

        @Test
        @DisplayName("pluginRepository() creates repository with valid directory")
        void pluginRepositoryCreatesWithValidDirectory() {
            assertThat(repository).isNotNull();
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("pluginRepository() throws on null base directory")
        void pluginRepositoryThrowsOnNullBaseDirectory() {
            assertThatThrownBy(() -> PluginRepository.pluginRepository(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("pluginRepository() throws on non-existent directory")
        void pluginRepositoryThrowsOnNonExistentDirectory() {
            final var nonExistent = fileSystem.getPath("/does-not-exist");
            
            assertThatThrownBy(() -> PluginRepository.pluginRepository(nonExistent))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Not a directory");
        }

        @Test
        @DisplayName("pluginRepository() throws on file instead of directory")
        void pluginRepositoryThrowsOnFile() throws IOException {
            final var file = fileSystem.getPath("/somefile.txt");
            Files.createFile(file);
            
            assertThatThrownBy(() -> PluginRepository.pluginRepository(file))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Not a directory");
        }
    }

    @Nested
    @DisplayName("save() tests")
    class SaveTests {

        @Test
        @DisplayName("save() persists plugin JAR to file system")
        void savePersistsPluginJar() throws Exception {
            final var coordinate = pluginCoordinate("test-plugin", SemVer.semVer(1, 0, 0));
            final var content = "test plugin content".getBytes();
            
            try (InputStream in = new ByteArrayInputStream(content)) {
                repository.save(coordinate, in);
            }
            
            // Verify file exists
            final var expectedPath = baseDirectory
                .resolve("test-plugin")
                .resolve("1.0.0")
                .resolve("plugin.jar");
            
            assertThat(Files.exists(expectedPath)).isTrue();
            assertThat(Files.readAllBytes(expectedPath)).isEqualTo(content);
        }

        @Test
        @DisplayName("save() creates Maven-style directory structure")
        void saveCreatesMavenStyleStructure() throws Exception {
            final var coordinate = pluginCoordinate("my-plugin", SemVer.semVer(2, 3, 4));
            final var content = "content".getBytes();
            
            try (InputStream in = new ByteArrayInputStream(content)) {
                repository.save(coordinate, in);
            }
            
            assertThat(Files.exists(baseDirectory.resolve("my-plugin"))).isTrue();
            assertThat(Files.exists(baseDirectory.resolve("my-plugin/2.3.4"))).isTrue();
            assertThat(Files.exists(baseDirectory.resolve("my-plugin/2.3.4/plugin.jar"))).isTrue();
        }

        @Test
        @DisplayName("save() is idempotent with identical content")
        void saveIsIdempotentWithIdenticalContent() throws Exception {
            final var coordinate = pluginCoordinate("test-plugin", SemVer.semVer(1, 0, 0));
            final var content = "identical content".getBytes();
            
            // Save twice with same content
            try (InputStream in1 = new ByteArrayInputStream(content)) {
                repository.save(coordinate, in1);
            }
            
            assertThatNoException().isThrownBy(() -> {
                try (InputStream in2 = new ByteArrayInputStream(content)) {
                    repository.save(coordinate, in2);
                }
            });
        }

        @Test
        @DisplayName("save() throws PluginAlreadyExistsException with different content")
        void saveThrowsOnDifferentContent() throws Exception {
            final var coordinate = pluginCoordinate("test-plugin", SemVer.semVer(1, 0, 0));
            
            // Save first version
            try (InputStream in1 = new ByteArrayInputStream("content1".getBytes())) {
                repository.save(coordinate, in1);
            }
            
            // Try to save with different content
            assertThatThrownBy(() -> {
                try (InputStream in2 = new ByteArrayInputStream("different content".getBytes())) {
                    repository.save(coordinate, in2);
                }
            })
            .isInstanceOf(PluginAlreadyExistsException.class)
            .hasMessageContaining("already exists with different content");
        }

        @Test
        @DisplayName("save() replaces spaces with underscores in names")
        void saveReplacesSpacesWithUnderscores() throws Exception {
            final var coordinate = pluginCoordinate("my plugin", SemVer.semVer(1, 0, 0));
            final var content = "content".getBytes();
            
            try (InputStream in = new ByteArrayInputStream(content)) {
                repository.save(coordinate, in);
            }
            
            // Name should have underscore instead of space
            assertThat(Files.exists(baseDirectory.resolve("my_plugin"))).isTrue();
        }

        @Test
        @DisplayName("save() handles multiple versions of same plugin")
        void saveHandlesMultipleVersions() throws Exception {
            final var name = "test-plugin";
            final var v1 = pluginCoordinate(name, SemVer.semVer(1, 0, 0));
            final var v2 = pluginCoordinate(name, SemVer.semVer(2, 0, 0));
            
            try (InputStream in1 = new ByteArrayInputStream("v1".getBytes())) {
                repository.save(v1, in1);
            }
            
            try (InputStream in2 = new ByteArrayInputStream("v2".getBytes())) {
                repository.save(v2, in2);
            }
            
            assertThat(Files.exists(baseDirectory.resolve("test-plugin/1.0.0/plugin.jar"))).isTrue();
            assertThat(Files.exists(baseDirectory.resolve("test-plugin/2.0.0/plugin.jar"))).isTrue();
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("save() throws on null coordinate")
        void saveThrowsOnNullCoordinate() {
            assertThatThrownBy(() -> {
                try (InputStream in = new ByteArrayInputStream("test".getBytes())) {
                    repository.save(null, in);
                }
            }).isInstanceOf(NullPointerException.class);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("save() throws on null input stream")
        void saveThrowsOnNullInputStream() {
            final var coordinate = pluginCoordinate("test", SemVer.semVer(1, 0, 0));
            
            assertThatThrownBy(() -> repository.save(coordinate, null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("save() cleans up temp file on failure")
        void saveCleansUpTempFileOnFailure() throws Exception {
            final var coordinate = pluginCoordinate("test", SemVer.semVer(1, 0, 0));
            final var content = "content".getBytes();
            
            // Save successfully first
            try (InputStream in1 = new ByteArrayInputStream(content)) {
                repository.save(coordinate, in1);
            }
            
            // Try to save with different content (will fail)
            try {
                try (InputStream in2 = new ByteArrayInputStream("different".getBytes())) {
                    repository.save(coordinate, in2);
                }
            } catch (PluginAlreadyExistsException e) {
                // Expected
            }
            
            // Verify temp file was cleaned up
            final var versionDir = baseDirectory.resolve("test/1.0.0");
            assertThat(Files.list(versionDir))
                .extracting(Path::getFileName)
                .extracting(Path::toString)
                .containsOnly("plugin.jar")
                .doesNotContain("plugin.jar.tmp");
        }
    }

    @Nested
    @DisplayName("load() tests")
    class LoadTests {

        @Test
        @DisplayName("load() returns input stream for existing plugin")
        void loadReturnsInputStreamForExistingPlugin() throws Exception {
            final var coordinate = pluginCoordinate("test-plugin", SemVer.semVer(1, 0, 0));
            final var content = "test content".getBytes();
            
            // Save plugin first
            try (InputStream in = new ByteArrayInputStream(content)) {
                repository.save(coordinate, in);
            }
            
            // Load it back
            try (InputStream loaded = repository.load(coordinate)) {
                assertThat(loaded).isNotNull();
                assertThat(loaded.readAllBytes()).isEqualTo(content);
            }
        }

        @SuppressWarnings("resource")
        @Test
        @DisplayName("load() throws PluginNotFoundException for non-existent plugin")
        void loadThrowsForNonExistentPlugin() {
            final var coordinate = pluginCoordinate("non-existent", SemVer.semVer(1, 0, 0));
            
            assertThatThrownBy(() -> repository.load(coordinate))
                .isInstanceOf(PluginNotFoundException.class)
                .hasMessageContaining("Plugin not found");
        }

        @SuppressWarnings({"DataFlowIssue", "resource"})
        @Test
        @DisplayName("load() throws on null coordinate")
        void loadThrowsOnNullCoordinate() {
            assertThatThrownBy(() -> repository.load(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("load() can distinguish between different versions")
        void loadCanDistinguishBetweenVersions() throws Exception {
            final var name = "test-plugin";
            final var v1 = pluginCoordinate(name, SemVer.semVer(1, 0, 0));
            final var v2 = pluginCoordinate(name, SemVer.semVer(2, 0, 0));
            final var content1 = "version 1".getBytes();
            final var content2 = "version 2".getBytes();
            
            // Save both versions
            try (InputStream in = new ByteArrayInputStream(content1)) {
                repository.save(v1, in);
            }
            try (InputStream in = new ByteArrayInputStream(content2)) {
                repository.save(v2, in);
            }
            
            // Load and verify each version
            try (InputStream loaded1 = repository.load(v1)) {
                assertThat(loaded1.readAllBytes()).isEqualTo(content1);
            }
            try (InputStream loaded2 = repository.load(v2)) {
                assertThat(loaded2.readAllBytes()).isEqualTo(content2);
            }
        }
    }

    @Nested
    @DisplayName("remove() tests")
    class RemoveTests {

        @Test
        @DisplayName("remove() deletes existing plugin")
        void removeDeletesExistingPlugin() throws Exception {
            final var coordinate = pluginCoordinate("test-plugin", SemVer.semVer(1, 0, 0));
            final var content = "content".getBytes();
            
            // Save and verify it exists
            try (InputStream in = new ByteArrayInputStream(content)) {
                repository.save(coordinate, in);
            }
            
            final var jarPath = baseDirectory.resolve("test-plugin/1.0.0/plugin.jar");
            assertThat(Files.exists(jarPath)).isTrue();
            
            // Remove it
            repository.remove(coordinate);
            
            // Verify it's gone
            assertThat(Files.exists(jarPath)).isFalse();
        }

        @Test
        @DisplayName("remove() throws PluginNotFoundException for non-existent plugin")
        void removeThrowsForNonExistentPlugin() {
            final var coordinate = pluginCoordinate("non-existent", SemVer.semVer(1, 0, 0));
            
            assertThatThrownBy(() -> repository.remove(coordinate))
                .isInstanceOf(PluginNotFoundException.class)
                .hasMessageContaining("Plugin not found");
        }

        @Test
        @DisplayName("remove() does not delete parent directories")
        void removeDoesNotDeleteParentDirectories() throws Exception {
            final var coordinate = pluginCoordinate("test-plugin", SemVer.semVer(1, 0, 0));
            
            try (InputStream in = new ByteArrayInputStream("content".getBytes())) {
                repository.save(coordinate, in);
            }
            
            repository.remove(coordinate);
            
            // Parent directories should still exist
            assertThat(Files.exists(baseDirectory.resolve("test-plugin"))).isTrue();
            assertThat(Files.exists(baseDirectory.resolve("test-plugin/1.0.0"))).isTrue();
        }

        @Test
        @DisplayName("remove() allows removing one version while keeping others")
        void removeAllowsRemovingOneVersion() throws Exception {
            final var name = "test-plugin";
            final var v1 = pluginCoordinate(name, SemVer.semVer(1, 0, 0));
            final var v2 = pluginCoordinate(name, SemVer.semVer(2, 0, 0));
            
            // Save both versions
            try (InputStream in1 = new ByteArrayInputStream("v1".getBytes())) {
                repository.save(v1, in1);
            }
            try (InputStream in2 = new ByteArrayInputStream("v2".getBytes())) {
                repository.save(v2, in2);
            }
            
            // Remove v1
            repository.remove(v1);
            
            // v1 should be gone, v2 should remain
            assertThat(Files.exists(baseDirectory.resolve("test-plugin/1.0.0/plugin.jar"))).isFalse();
            assertThat(Files.exists(baseDirectory.resolve("test-plugin/2.0.0/plugin.jar"))).isTrue();
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("remove() throws on null coordinate")
        void removeThrowsOnNullCoordinate() {
            assertThatThrownBy(() -> repository.remove(null))
                .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Integration tests")
    class IntegrationTests {

        @SuppressWarnings("resource")
        @Test
        @DisplayName("full lifecycle: save, load, remove")
        void fullLifecycleSaveLoadRemove() throws Exception {
            final var coordinate = pluginCoordinate("lifecycle-test", SemVer.semVer(1, 0, 0));
            final var content = "lifecycle test content".getBytes();
            
            // Save
            try (InputStream in = new ByteArrayInputStream(content)) {
                repository.save(coordinate, in);
            }
            LOGGER.debug("Saved plugin: {}", coordinate);
            
            // Load and verify
            try (InputStream loaded = repository.load(coordinate)) {
                assertThat(loaded.readAllBytes()).isEqualTo(content);
            }
            LOGGER.debug("Loaded plugin: {}", coordinate);
            
            // Remove
            repository.remove(coordinate);
            LOGGER.debug("Removed plugin: {}", coordinate);
            
            // Verify it's gone
            assertThatThrownBy(() -> repository.load(coordinate))
                .isInstanceOf(PluginNotFoundException.class);
        }

        @Test
        @DisplayName("multiple plugins coexist in repository")
        void multiplePluginsCoexist() throws Exception {
            final var plugin1 = pluginCoordinate("plugin-one", SemVer.semVer(1, 0, 0));
            final var plugin2 = pluginCoordinate("plugin-two", SemVer.semVer(1, 0, 0));
            final var plugin3 = pluginCoordinate("plugin-three", SemVer.semVer(2, 5, 3));
            
            // Save all
            try (InputStream in = new ByteArrayInputStream("one".getBytes())) {
                repository.save(plugin1, in);
            }
            try (InputStream in = new ByteArrayInputStream("two".getBytes())) {
                repository.save(plugin2, in);
            }
            try (InputStream in = new ByteArrayInputStream("three".getBytes())) {
                repository.save(plugin3, in);
            }
            
            // Verify all can be loaded
            try (InputStream in = repository.load(plugin1)) {
                assertThat(in.readAllBytes()).isEqualTo("one".getBytes());
            }
            try (InputStream in = repository.load(plugin2)) {
                assertThat(in.readAllBytes()).isEqualTo("two".getBytes());
            }
            try (InputStream in = repository.load(plugin3)) {
                assertThat(in.readAllBytes()).isEqualTo("three".getBytes());
            }
        }

        @Test
        @DisplayName("handles special characters in plugin names")
        void handlesSpecialCharactersInNames() throws Exception {
            final var coordinate = pluginCoordinate("my plugin with spaces", SemVer.semVer(1, 0, 0));
            final var content = "content".getBytes();
            
            try (InputStream in = new ByteArrayInputStream(content)) {
                repository.save(coordinate, in);
            }
            
            // Should be able to load it back
            try (InputStream loaded = repository.load(coordinate)) {
                assertThat(loaded.readAllBytes()).isEqualTo(content);
            }
            
            // Can also remove it
            assertThatNoException().isThrownBy(() -> repository.remove(coordinate));
        }
    }

}
