    plugins {
        alias(libs.plugins.android.application)
        alias(libs.plugins.kotlin.android)
        alias(libs.plugins.kotlin.compose)
    }

    android {
        namespace = "com.example.actividad2"
        compileSdk = 36

        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = "1.1.1"
        }

        kotlinOptions {
            jvmTarget = "1.8"
        }
        defaultConfig {
            applicationId = "com.example.actividad2"
            minSdk = 24
            targetSdk = 36
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
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
        kotlinOptions {
            jvmTarget = "11"
        }
        buildFeatures {
            compose = true
        }
    }

    dependencies {
        implementation("androidx.navigation:navigation-compose:2.7.7")
        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.7")
        implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.6" + "")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.runtime.ktx)
        implementation(libs.androidx.activity.compose)
        implementation(platform(libs.androidx.compose.bom))
        implementation(libs.androidx.ui)
        implementation(libs.androidx.ui.graphics)
        implementation(libs.androidx.ui.tooling.preview)
        implementation(libs.androidx.material3)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(platform(libs.androidx.compose.bom))
        androidTestImplementation(libs.androidx.ui.test.junit4)
        debugImplementation(libs.androidx.ui.tooling)
        debugImplementation(libs.androidx.ui.test.manifest)
        implementation("androidx.compose.material3:material3:1.4.0")
        implementation("androidx.compose.material3:material3-window-size-class:1.4.0")
        implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.5.0-alpha04")
        implementation "androidx.room:room-runtime:2.6.0"
        implementation "androidx.room:room-ktx:2.6.0"
        kapt "androidx.room:room-compiler:2.6.0"
        // Room Database
        implementation "androidx.room:room-runtime:2.6.0"
        implementation "androidx.room:room-ktx:2.6.0"
        kapt "androidx.room:room-compiler:2.6.0"

        // Coroutines
        implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3"

        // Lifecycle (para ViewModel)
        implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0"
        implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.7.0"

        // Compose
        implementation "androidx.compose.ui:ui:1.5.4"
        implementation "androidx.compose.material3:material3:1.1.2"
        implementation "androidx.activity:activity-compose:1.8.0"
    }