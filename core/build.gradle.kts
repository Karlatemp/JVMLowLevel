/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/06/05 18:02:46
 *
 * JVMLowLevel/JVM-LowLevel/build.gradle.kts
 */


plugins {
    java
    kotlin("jvm") version "1.3.72"
}

group = "io.github.karlatemp"
version = "unspecified"

dependencies {
    implementation("org.ow2.asm:asm-commons:8.0.1")
    implementation("org.ow2.asm:asm-tree:8.0.1")
    implementation("org.jetbrains:annotations:19.0.0")
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation(project(":accessor"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

