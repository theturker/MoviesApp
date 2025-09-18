plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.alperenturker.core.ui"
    compileSdk = 36

    defaultConfig { minSdk = 24 }

    buildFeatures { compose = true }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions { jvmTarget = "11" }
}

dependencies {
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.ui)
    api(libs.androidx.foundation)
    api(libs.androidx.material3)
    api(libs.androidx.runtime)

    debugApi(libs.androidx.ui.tooling)
    implementation(libs.coil.compose)

    implementation(libs.androidx.core.ktx)
    implementation(libs.coil.compose)

}
