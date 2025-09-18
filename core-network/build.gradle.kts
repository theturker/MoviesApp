plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.alperenturker.core.network"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        buildConfigField("String", "BASE_URL", "\"https://www.omdbapi.com/\"")
        val omdbKey = (project.findProperty("OMDB_API_KEY") as String?) ?: ""
        buildConfigField("String", "OMDB_API_KEY", "\"$omdbKey\"")
        // BuildConfig.GROQ_API_KEY --> local.properties'ten
        val groqKey = project.findProperty("GROQ_API_KEY") as String? ?: ""
        buildConfigField("String", "GROQ_API_KEY", "\"$groqKey\"")
    }

    buildFeatures { buildConfig = true }   // <— BU ŞART
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions { jvmTarget = "11" }
}


dependencies {
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
}
