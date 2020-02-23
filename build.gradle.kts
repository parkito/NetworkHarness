group = "ru.siksmfp.network.harness"
version = "0.1.1"

plugins {
    kotlin("jvm") version "1.3.61"
    id("me.champeau.gradle.jmh") version "0.5.0"
    idea
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation("org.openjdk.jmh:jmh-core:1.23")
    implementation("org.openjdk.jmh:jmh-generator-annprocess:1.23")
    implementation(kotlin("stdlib", "1.2.31"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.61")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.4.1")
}

repositories {
    jcenter()
    mavenCentral()
}
