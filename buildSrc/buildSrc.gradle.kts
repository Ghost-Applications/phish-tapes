plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
    maven("https://maven.fabric.io/public")
    jcenter()
}

dependencies {
    implementation("com.android.tools.build:gradle:4.2.1")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.10")
    implementation("com.google.gms:google-services:4.3.8")
    implementation("com.google.firebase:firebase-crashlytics-gradle:2.7.0")

    implementation("com.squareup:kotlinpoet:1.8.0")
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}
