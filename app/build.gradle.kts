plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.varswatch"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.varswatch"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.firestore)
    implementation(libs.androidx.media3.session)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.fragment.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //custom youtube_player
    implementation (libs.core)

    implementation (libs.gson)

    //image loading library
    implementation (libs.glide)

    //CircleIndicator
    implementation (libs.circleindicator)

    //airbnb recycler view
    implementation (libs.epoxy)

    //api calling library
    implementation (libs.retrofit)
    implementation (libs.converter.moshi)
    implementation(libs.okhttp)

    //dependency injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    //Permissions asking library
    implementation (libs.permissionx)

    // Room Database for Offline Storage
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    implementation ("androidx.media:media:1.6.0")

    // Firebase UI
    implementation ("com.firebaseui:firebase-ui-auth:8.0.2")

}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}