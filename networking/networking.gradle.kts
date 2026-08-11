import nes.gradle.BuildConstants.PROJECT_NAMESPACE
import nes.gradle.BuildConstants.SYSTEM_NAMESPACE

plugins {
    idea
    `java-library`

    kotlin("jvm")

    alias(libs.plugins.serialization)
    alias(libs.plugins.ksp)

    id("kotlin-config-writer")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

kotlin {
    jvmToolchain(21)
}

sourceSets {
    create("integrationTest") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.kotlinx.coroutines)

    api(libs.okhttp)
    api(libs.bundles.arrow)

    implementation(libs.okio)
    api(libs.bundles.retrofit)

    implementation(libs.kotlinx.serialization)

    implementation(libs.dagger)
    ksp(libs.dagger.compiler)

    testImplementation(kotlin("test"))
    testImplementation(kotlin("reflect"))
    testImplementation(libs.bundles.network.test.libs)
    kspTest(libs.dagger.compiler)

    "kspIntegrationTest"(libs.dagger.compiler)
}

kotlinConfigWriter {
    packageName = "nes.networking"

    val phishNetApiKey = providers.gradleProperty("$PROJECT_NAMESPACE.phishNetApiKey")
        .orElse(providers.environmentVariable("$SYSTEM_NAMESPACE.PHISH_NET_API_KEY"))
        .getOrElse("")
    put("PHISH_NET_API_KEY", phishNetApiKey)

    val phishinApiKey = providers.gradleProperty("$PROJECT_NAMESPACE.phishinApiKey")
        .orElse(providers.environmentVariable("$SYSTEM_NAMESPACE.PHISHIN_API_KEY"))
        .getOrElse("")
    put("PHISH_IN_API_KEY", phishinApiKey)
}

configurations.getByName("integrationTestImplementation") {
    extendsFrom(configurations.implementation.get())
    extendsFrom(configurations.testImplementation.get())
}
configurations.getByName("integrationTestRuntimeOnly") {
    extendsFrom(configurations.runtimeOnly.get())
}

val integrationTest = tasks.register<Test>("integrationTest") {
    val integrationTest = sourceSets.getByName("integrationTest")
    description = "Runs the integration tests."
    group = "verification"

    testClassesDirs = integrationTest.output.classesDirs
    classpath = integrationTest.runtimeClasspath
    shouldRunAfter("test")

    useJUnitPlatform()
    testLogging {
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

tasks.check { dependsOn(integrationTest) }

idea {
    module {
        testSources.from(sourceSets["integrationTest"].allSource.srcDirs)
    }
}
