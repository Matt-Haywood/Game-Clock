plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.mhappening.gameclock"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mhappening.gameclock"
        minSdk = 33
        targetSdk = 34
        versionCode = 10
        versionName = "1.07"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isDebuggable = false
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
            ndk {
                debugSymbolLevel =  "FULL"
            }
        }
        debug {
            isDebuggable = true
//            ndk {
//                debugSymbolLevel =  "FULL"
//            }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.5"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    implementation(libs.androidx.compose.animation)

    implementation(libs.androidx.material3)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout.compose)


    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.androidx.ui.test.junit4)
    testImplementation(libs.androidx.navigation.testing)
    testImplementation(libs.androidx.espresso.core)
    testImplementation(libs.androidx.espresso.intents)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.androidx.espresso.intents)

    androidTestImplementation(libs.androidx.junit)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    //Permissions
    implementation(libs.accompanist.permissions)

    //View Model
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    //Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material3.window.size)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    //Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.core.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.room.ktx)

    //Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    androidTestImplementation(libs.hilt.android.testing)
    kspTest(libs.hilt.compiler)
    testImplementation(libs.hilt.android.testing)

    // work-manager
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.hilt.work)

    // Admob
    implementation(libs.play.services.ads)

    // Splash Screen
    implementation(libs.core.splashscreen)


}

