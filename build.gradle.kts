plugins {
    kotlin("jvm") version "2.2.20"
}

group = "kzs.th000"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()

    testLogging{
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showExceptions = true
        showCauses = true
    }
}
kotlin {
    jvmToolchain(17)
}

(1..3).forEach { day ->
    val dayId = String.format("%02d", day)

    tasks.register<JavaExec>("day$dayId") {
        classpath = sourceSets.main.get().runtimeClasspath
        mainClass.set("aoc.day${dayId}.Day${dayId}Kt")
        group = "AOC"
    }
}
