import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "ru.siksmfp.network.play"
version = "0.1.1"

val appArchiveName = "rx-$version"

plugins {
    application
    kotlin("jvm") version "1.3.21"
    java
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    compile(kotlin("stdlib"))
    compile("javax.annotation:javax.annotation-api:1.3.2")
    compile("io.reactivex.rxjava2:rxjava:2.2.8")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.20")
    implementation("org.openjdk.jmh:jmh-core:1.23")
    implementation("org.openjdk.jmh:jmh-generator-annprocess:1.23")
    testCompile("org.junit.jupiter:junit-jupiter-engine:5.4.1")
}

repositories {
    jcenter()
    mavenCentral()
}

val jar by tasks.getting(Jar::class) {
    archiveName = "$appArchiveName.jar"
    into("META-INF") {
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}