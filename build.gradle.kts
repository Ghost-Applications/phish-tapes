import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import nes.gradle.isNonStable

plugins {
    id("com.github.ben-manes.versions") version "0.39.0"
}

subprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }

    tasks.withType<KotlinCompile> {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
        kotlinOptions.jvmTarget = "1.8"
    }

    tasks.withType<Test> {
        testLogging.exceptionFormat = TestExceptionFormat.FULL
    }
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}