plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)

    id("api-key-provider")
    id("signing-config")
    id("build-number")

    alias(libs.plugins.paparazzi)
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "nes.app"
    compileSdk = libs.versions.android.sdk.get().toInt()

    signingConfigs {
        val keystoreLocation: String by project
        val keystorePassword: String by project
        val storeKeyAlias: String by project
        val aliasKeyPassword: String by project

        getByName("debug") {
            storeFile = rootProject.file("keys/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
        create("release") {
            storeFile = rootProject.file(keystoreLocation)
            storePassword = keystorePassword
            keyAlias = storeKeyAlias
            keyPassword = aliasKeyPassword
        }
    }

    defaultConfig {
        val buildNumber: String by project
        applicationId = "never.ending.splendor"
        minSdk = 23
        targetSdk = libs.versions.android.sdk.get().toInt()
        versionCode = buildNumber.toInt()
        versionName = properties["phish.tapes.versionName"] as String
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        val debug by getting {
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        val release by getting {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    testOptions.unitTests.isReturnDefaultValues = true
    buildFeatures {
        viewBinding = false
        aidl = false
        buildConfig = false
        compose = true
        prefab = false
        renderScript = false
        resValues = false
        shaders = false
    }

    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
}

dependencies {
    implementation(projects.networking)
    implementation(kotlin("stdlib"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.serialization)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.guava)

    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.bundles.navigation)

    implementation(libs.bundles.hilt)
    ksp(libs.hilt.android.compiler)

    implementation(libs.android.material)

    implementation(libs.bundles.media3)
    implementation(libs.androidx.mediarouter)

    implementation(libs.bundles.androidx)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.navigation)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.coil)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.timber)
    implementation(libs.byteunits)

    implementation(libs.bundles.arrow)
    implementation(libs.markwon)

    debugImplementation(libs.bundles.android.debug.libs)
    releaseImplementation(libs.bundles.android.release.libs)

    testImplementation(libs.bundles.android.test.libs)
    testImplementation(libs.hilt.android.testing)
    kspTest(libs.hilt.android.compiler)
}

tasks.named("build") {
    dependsOn("verifyPaparazziRelease")
}

val android16LayoutLibVersion = "15.2.2"
configurations.all {
    resolutionStrategy.force(
        "com.android.tools.layoutlib:layoutlib:$android16LayoutLibVersion",
        "com.android.tools.layoutlib:layoutlib-resources:$android16LayoutLibVersion",
        "com.android.tools.layoutlib:layoutlib-runtime:$android16LayoutLibVersion"
    )
}
