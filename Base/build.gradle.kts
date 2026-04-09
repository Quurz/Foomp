/*
 * Build-Konfiguration für das 'Base'-Modul.
 */

plugins {
    // Einbinden der gemeinsamen Java-Konventionen aus buildSrc
    id("buildlogic.java-conventions")
}

dependencies {
    // 'api' bedeutet: Diese Abhängigkeit ist auch für Nutzer dieses Moduls sichtbar.
    api(project(":higher"))
    
    // Nutzung des Version Catalogs (libs.versions.toml) für externe Libraries
    api(libs.org.apache.commons.commons.lang3)
    api(libs.org.checkerframework.checker.qual)
    
    // Test-Abhängigkeiten (nur für Tests verfügbar)
    testImplementation(libs.org.slf4j.slf4j.api)
    testImplementation(libs.ch.qos.logback.logback.classic)
}

// Beschreibung des Projekts (wird z.B. in Maven-Pom-Dateien genutzt)
description = "Foomp-Base"

java {
    // Zusätzlich zum normalen JAR auch ein Javadoc-JAR erstellen
    withJavadocJar()
}
