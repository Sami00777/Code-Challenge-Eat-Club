plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.androidx.navigation.safe.args)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ktlint.jlleitschuh)
}

android {
    namespace = "com.eatclub.challenge"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.eatclub.challenge"
        minSdk = 26
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
                "proguard-rules.pro",
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
        viewBinding = true
    }
}

ktlint {
    android = true
    ignoreFailures = false
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.core.ktx)
    implementation(libs.legacy.support.v4)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.fragment.ktx)

    // Mockk
    testImplementation(libs.mockk)

    // Arch Core Testing
    testImplementation(libs.androidx.core.testing)

    // Coroutines Testing
    testImplementation(libs.kotlinx.coroutines.test)

    // Unit Testing JUnit
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)

    // Retrofit
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.retrofit)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // gson
    implementation(libs.gson)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // Gson Converter
    implementation(libs.squareup.converter.gson)

    // Glide
    implementation(libs.glide)

    // Jetpack Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
}
