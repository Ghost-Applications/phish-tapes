import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import nes.gradle.isNonStable
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.version.check)
}

subprojects {
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
