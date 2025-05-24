import java.util.Properties

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        load(file.inputStream())
    }
}

val apiBaseUrl: String = localProperties["API_BASE_URL"] as String
val apiToken: String = localProperties["API_TOKEN"] as String
val apiBaseUrlScoreboard: String = localProperties["SCOREBOARD_BASE_URL_API"] as String
val apiTokenScoreboard: String = localProperties["SCOREBOARD_TOKEN_API"] as String


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp") version "2.0.0-1.0.22"
}

android {
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    namespace = "com.baliproject.scoreboardtennis"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.baliproject.scoreboardtennis"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "API_BASE_URL", "\"$apiBaseUrl\"")
        buildConfigField("String", "API_TOKEN", "\"$apiToken\"")
        buildConfigField("String", "SCOREBOARD_BASE_URL_API", "\"$apiBaseUrlScoreboard\"")
        buildConfigField("String", "SCOREBOARD_TOKEN_API", "\"$apiTokenScoreboard\"")
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
}

dependencies {
    val room_version = "2.7.1"
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Retrofit

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp Logging Interceptor (opsional, untuk debugging)
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
//    implementation("androidx.gridlayout:gridlayout:1.0.0")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

// Kotlin Extensions and Coroutines support



    // Gson Converter untuk Retrofit


    // OkHttp logging interceptor (untuk debugging)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

