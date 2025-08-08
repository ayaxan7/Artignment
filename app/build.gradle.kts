import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    id("kotlin-parcelize")
}
val localPropertiesFile = rootProject.file("local.properties")
val localProperties = Properties().apply {
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { load(it) }
    }
}
val geminiApiKey = localProperties.getProperty("GEMINI_API_KEY") ?: "MISSING_API_KEY"
android {
    namespace = "com.ayaan.artignment"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.ayaan.artignment"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "GEMINI_API_KEY", "\"$geminiApiKey\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig=true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    // Coroutines
    implementation(libs.coroutines.android)

    // Navigation
    implementation(libs.navigation.compose)

    // ExoPlayer
    implementation(libs.exoplayer)
    implementation(libs.exoplayer.ui)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Coil for image loading
    implementation(libs.coil)
    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    // Gemini AI
    implementation("com.google.ai.client.generativeai:generativeai:0.7.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}