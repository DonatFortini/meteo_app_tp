plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
    id("com.github.ben-manes.versions") version "0.51.0"
}

android {
    namespace = "com.example.meteo_app_tp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.meteo_app_tp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
}



dependencies {
    implementation(libs.androidx.room.runtime.v261)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation (libs.play.services.location.v2101)
    implementation (libs.androidx.lifecycle.runtime.compose)
    implementation (libs.androidx.navigation.compose.v276)
    implementation (libs.material3)
    implementation(libs.androidx.material.icons.extended.v154)
}