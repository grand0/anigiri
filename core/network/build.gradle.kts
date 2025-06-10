plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.detekt)
}

android {
    namespace = "tech.bnuuy.anigiri.core.network"
    compileSdk = 35

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.bundles.ktor)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.datetime)
    implementation(libs.brotli.dec)
    implementation(libs.datastore.preferences.core)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
}

detekt {
    toolVersion = libs.versions.detekt.get()
    val configFile = File(project.rootDir, "config/detekt/detekt.yml")
    config.setFrom(configFile)
    buildUponDefaultConfig = true
}
