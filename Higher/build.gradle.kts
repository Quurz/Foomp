/*
 * Build-Konfiguration für das 'Higher'-Modul (höherwertige Typen).
 */

plugins {
    // Gemeinsame Java-Konventionen
    id("buildlogic.java-conventions")
    // AsciiDoctor für die Dokumentation
    id("org.asciidoctor.jvm.convert") version "4.0.5"
}

description = "Foomp-Higher-Types"

java {
    withJavadocJar()
}

/*
 * Konfiguration des Asciidoctor-Tasks.
 */
tasks.named<org.asciidoctor.gradle.jvm.AsciidoctorTask>("asciidoctor") {
    setSourceDir(file("src/site/adoc"))
    setOutputDir(layout.buildDirectory.dir("docs/asciidoc").get().asFile)
}

// Hilfsvariable für den Zielordner der Projekthomepage (Site)
val moduleSiteDir = layout.buildDirectory.dir("site")

/*
 * Task 'moduleSite' führt die Dokumentationen zusammen.
 */
tasks.register<Copy>("moduleSite") {
    group = "documentation"
    description = "Erzeugt die Modul-Site (Asciidoc, JavaDoc, Coverage)."

    dependsOn(
        "asciidoctor",
        "javadoc",
        "test",
        "jacocoTestReport"
    )

    into(moduleSiteDir)

    from(layout.buildDirectory.dir("docs/asciidoc")) {
        into("docs")
    }
    from(layout.buildDirectory.dir("docs/javadoc")) {
        into("javadoc")
    }
    from(layout.buildDirectory.dir("reports/jacoco/test/html")) {
        into("jacoco")
    }
}