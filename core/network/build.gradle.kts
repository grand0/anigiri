plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.buildconfig)
}

buildConfig {
    packageName("tech.bnuuy.anigiri.core.network")
    
    buildConfigField("ANILIBRIA_BASE_URL", "https://anilibria.top/api/v1/")
    buildConfigField("ANILIBRIA_STORAGE_BASE_URL", "https://anilibria.top")
}

dependencies {
    implementation(libs.bundles.ktor)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.datetime)
    implementation(libs.brotli.dec)
    implementation(libs.datastore.preferences.core)
}
