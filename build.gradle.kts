import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
}

group = "me.nyckb"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    flatDir { dirs("libs") } // Adicionar esta linha
}
dependencies { // Adicionar esta secção “dependencies” caso não exista
    implementation("pt.isel:CanvasLib-jvm:1.0.0") // Adicionar esta linha
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "13"
}