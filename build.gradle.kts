import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.serialization) apply false
}

subprojects {
    tasks.withType<Test> {
        testLogging.exceptionFormat = TestExceptionFormat.FULL
    }
}
