import nes.gradle.BuildConstants.PROJECT_NAMESPACE
import nes.gradle.BuildConstants.SYSTEM_NAMESPACE

plugins {
    id("com.android.application")

    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

kotlin {
    jvmToolchain(21)
}

android {
    namespace = "nes.app"
    compileSdk = libs.versions.android.sdk.get().toInt()

    signingConfigs {
        val keystoreLocation = providers.gradleProperty("$PROJECT_NAMESPACE.keystoreLocation")
            .orElse(providers.environmentVariable("${SYSTEM_NAMESPACE}_KEYSTORE_LOCATION"))
            .getOrElse("keys/debug.keystore")

        val keystorePassword = providers.gradleProperty("$PROJECT_NAMESPACE.keystorePassword")
            .orElse(providers.environmentVariable("${SYSTEM_NAMESPACE}_KEYSTORE_PASSWORD"))
            .getOrElse("android")

        val storeKeyAlias = providers.gradleProperty("$PROJECT_NAMESPACE.storeKeyAlias")
            .orElse(providers.environmentVariable("${SYSTEM_NAMESPACE}_KEY_ALIAS"))
            .getOrElse("androiddebugkey")


        val aliasKeyPassword = providers.gradleProperty("$PROJECT_NAMESPACE.aliasKeyPassword")
            .orElse(providers.environmentVariable("${SYSTEM_NAMESPACE}_KEY_PASSWORD"))
            .getOrElse("android")

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
        val buildNumber = providers.gradleProperty("$PROJECT_NAMESPACE.buildNumber")
            .orElse(providers.environmentVariable("${SYSTEM_NAMESPACE}_BUILD_NUMBER"))
            .orElse(providers.gradleProperty("$PROJECT_NAMESPACE.defaultBuildNumber"))
            .get()

        val versionNumber = providers.gradleProperty("$PROJECT_NAMESPACE.versionName")
            .get()

        applicationId = "never.ending.splendor"
        minSdk = 28
        targetSdk = libs.versions.android.sdk.get().toInt()
        versionCode = buildNumber.toInt()
        versionName = versionNumber
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    testOptions.unitTests.isReturnDefaultValues = true
    buildFeatures {
        viewBinding = false
        aidl = false
        buildConfig = false
        compose = true
        prefab = false
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

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.coil)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.timber)
    implementation(libs.byteunits)

    implementation(libs.bundles.arrow)
    implementation(libs.markwon)

    debugImplementation(libs.bundles.android.debug.libs)

    testImplementation(libs.bundles.android.test.libs)
    testImplementation(libs.hilt.android.testing)
    kspTest(libs.hilt.android.compiler)
}

val android16LayoutLibVersion = "15.2.2"
configurations.all {
    resolutionStrategy.force(
        "com.android.tools.layoutlib:layoutlib:$android16LayoutLibVersion",
        "com.android.tools.layoutlib:layoutlib-resources:$android16LayoutLibVersion",
        "com.android.tools.layoutlib:layoutlib-runtime:$android16LayoutLibVersion"
    )
}
