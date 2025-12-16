apply {
    from("../secret.gradle")
}
val devConfig =
    (project.extra["devConfig"] as Map<*, *>).map { it.key.toString() to it.value.toString() }
        .toMap()
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.example.ibanking_kltn"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.ibanking_kltn"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            //noinspection WrongGradleMethod
            devConfig.forEach { (key, value) ->
                buildConfigField("String", key, "\"${value}\"")
            }
        }
        debug {
            isMinifyEnabled = false
            //noinspection WrongGradleMethod
            devConfig.forEach { (key, value) ->
                buildConfigField("String", key, "\"${value}\"")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.browser)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.navigation:navigation-compose:2.9.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose-android:2.9.2")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //DI
    implementation("com.google.dagger:hilt-android:2.57.2")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    ksp("com.google.dagger:hilt-android-compiler:2.57.2")

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:34.4.0"))
    implementation("com.google.firebase:firebase-analytics")
    // https://mvnrepository.com/artifact/com.google.firebase/firebase-messaging-ktx
    implementation("com.google.firebase:firebase-messaging-ktx:24.1.1")

    //biometric
    implementation("androidx.biometric:biometric:1.1.0")
    implementation("androidx.biometric:biometric-ktx:1.2.0-alpha05")

    //QR
    implementation("com.google.zxing:core:3.5.1")

    //CameraX
    implementation("androidx.camera:camera-core:1.4.2")
    implementation("androidx.camera:camera-camera2:1.4.2")
    implementation("androidx.camera:camera-lifecycle:1.4.2")
    implementation("androidx.camera:camera-view:1.4.2")
    implementation("com.google.mlkit:barcode-scanning:17.3.0")

    //Json to Object
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    //pagination
    implementation("androidx.paging:paging-runtime:3.3.6")
    implementation("androidx.paging:paging-compose:3.3.6")

}