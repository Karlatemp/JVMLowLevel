group = "io.github.karlatemp"
version = "1.0-SNAPSHOT"

plugins {
    java
    kotlin("jvm") version "1.3.72"
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("org.ow2.asm:asm-commons:8.0.1")
    implementation("org.ow2.asm:asm-tree:8.0.1")
    implementation("org.jetbrains:annotations:19.0.0")
    implementation(kotlin("stdlib-jdk8", "1.3.72"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}
