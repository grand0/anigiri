plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.gms)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.detekt)
}

android {
    namespace = "tech.bnuuy.anigiri"
    compileSdk = 35

    defaultConfig {
        applicationId = "tech.bnuuy.anigiri"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "0.1"

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
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:db"))
    implementation(project(":core:designsystem"))
    implementation(project(":feature:home:impl"))
    implementation(project(":feature:release:impl"))
    implementation(project(":feature:search:impl"))
    implementation(project(":feature:profile:impl"))
    implementation(project(":feature:favorites:impl"))
    implementation(project(":feature:player:impl"))
    implementation(project(":feature:collections:impl"))

    implementation(libs.bundles.voyager)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.coil)
    implementation(libs.datastore.preferences)
    
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

detekt {
    toolVersion = libs.versions.detekt.get()
    val configFile = File(project.rootDir, "config/detekt/detekt.yml")
    config.setFrom(configFile)
    buildUponDefaultConfig = true
}
