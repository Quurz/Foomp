/*
 * Die settings.gradle.kts ist der Einstiegspunkt für Gradle.
 * Hier wird der Projektname definiert und festgelegt, welche Submodule (Projekte)
 * Teil dieses Multi-Project-Builds sind.
 */

// Name des Hauptprojekts (Root)
rootProject.name = "Foomp"

/*
 * Definition der Submodule. Jedes Modul wird mit 'include' angemeldet.
 * Der Doppelpunkt ':' ist der Pfad-Trennner in Gradle-Projektstrukturen.
 */
include(":plugins-test-implementation1")
include(":plugins")
include(":base")
include(":plugins-test-shared")
include(":automata")
include(":plugins-test-implementation2")
include(":higher")
include(":plugins-runtime")

/*
 * Standardmäßig erwartet Gradle die Submodule in Ordnern, die genau so heißen wie das Projekt.
 * Da hier eine verschachtelte Ordnerstruktur (z.B. unter 'Plugins/') genutzt wird,
 * müssen die Pfade zu den Projektverzeichnissen manuell zugewiesen werden.
 */
project(":plugins-test-implementation1").projectDir = file("Plugins/Plugins-Test-Implementation1")
project(":plugins").projectDir = file("Plugins")
project(":base").projectDir = file("Base")
project(":plugins-test-shared").projectDir = file("Plugins/Plugins-Test-Shared")
project(":automata").projectDir = file("Automata")
project(":plugins-test-implementation2").projectDir = file("Plugins/Plugins-Test-Implementation2")
project(":higher").projectDir = file("Higher")
project(":plugins-runtime").projectDir = file("Plugins/Plugins-Runtime")
