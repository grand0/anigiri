plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "tech.bnuuy.anigiri.feature.search"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "ANILIBRIA_BASE_URL", "\"https://anilibria.top/api/v1/\"")
        buildConfigField("String", "ANILIBRIA_STORAGE_BASE_URL", "\"https://anilibria.top\"")
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
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:nav"))
    implementation(project(":core:network"))
    implementation(project(":core:db"))
    implementation(project(":feature:search:api"))

    implementation(libs.bundles.koin)
    implementation(libs.bundles.voyager)
    implementation(libs.bundles.orbit)
    implementation(libs.bundles.coil)
    implementation(libs.bundles.paging)
    implementation(libs.kotlinx.datetime)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
