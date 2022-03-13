plugins {
    `kotlin-dsl`
    alias(libs.plugins.version.check)
}

dependencies {
    implementation(libs.bundles.buildSrc)
}
