import org.gradle.api.JavaVersion.VERSION_11
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "ru.siksmfp.network.play"
version = "0.1.1"

val appArchiveName = "rx-$version"

plugins {
//    application
    kotlin("jvm") version "1.3.61"
    java
    id("me.champeau.gradle.jmh") version "0.5.0"
}

java {
    sourceCompatibility = VERSION_11
    targetCompatibility = VERSION_11
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("io.reactivex.rxjava2:rxjava:2.2.8")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.61")
    implementation("org.openjdk.jmh:jmh-core:1.23")
    implementation("org.openjdk.jmh:jmh-generator-annprocess:1.23")
    testCompile("org.junit.jupiter:junit-jupiter-engine:5.4.1")
}

repositories {
    jcenter()
    mavenCentral()
}
//tasks {
//    register("jmh", type = JavaExec::class) {
//        dependsOn("jmhClasses")
//        group = "benchmark"
//        main = "org.openjdk.jmh.Main"
//        classpath = sourceSets["jmh"].runtimeClasspath
//         To pass parameters ("-h" gives a list of possible parameters)
//         args(listOf("-h"))
//    }
//}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "ru.siksmfp.network.play.Main"
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}