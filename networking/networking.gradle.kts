plugins {
    idea
    `java-library`

    kotlin("jvm")
    kotlin("kapt")

    id("api-key-provider")
    id("kotlin-config-writer")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

dependencies {
    implementation(kotlin("stdlib"))

    // api to expose networkingModule.kt to consumers
    api(libs.kodein)

    // api to expose Interceptors and HttpUrl to consumers
    api(libs.okhttp)

    api(libs.result4k)

    implementation(libs.byteunits)

    implementation(libs.okio)

    implementation(libs.bundles.retrofit)

    implementation(libs.bundles.moshi)
    kapt(libs.moshi.codegen)

    testImplementation(kotlin("test"))
    testImplementation(kotlin("reflect"))
    testImplementation(libs.bundles.network.test.libs)
}

kotlinConfigWriter {
    packageName = "nes.networking"

    val phishNetApiKey: String by project
    put("PHISH_NET_API_KEY", phishNetApiKey)

    val phishinApiKey: String by project
    put("PHISH_IN_API_KEY", phishinApiKey)
}

sourceSets {
    create("integrationTest") {
        compileClasspath += sourceSets.main.get().output + configurations.testRuntimeClasspath
        runtimeClasspath += output + compileClasspath
    }
}

// setup integration tests w/ intellij/android studio
// doing it from groovy file because the kotlin dsl is not well equipped to work with this.
apply(from = "idea.gradle")

tasks.register<Test>("integrationTest") {
    val integrationTest by sourceSets
    description = "Runs the integration tests."
    group = "verification"
    testClassesDirs = integrationTest.output.classesDirs
    classpath = integrationTest.runtimeClasspath
    mustRunAfter(tasks.named("compileKotlin").get())
    outputs.upToDateWhen { false }

    useJUnitPlatform()
    testLogging {
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}
