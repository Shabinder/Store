plugins {
    id("plugin.ev.android.application")
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose)
}

android {
    namespace = "org.mobilenativefoundation.sample.ev.android.app"

    defaultConfig {
        applicationId = "org.mobilenativefoundation.sample.ev"
        versionCode = 1
        versionName = "1.0"
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            excludes.add("/META-INF/versions/9/previous-compilation-data.bin")
        }
    }
}


dependencies {

    implementation(compose.runtime)
    implementation(compose.material3)
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.compose.activity)
    implementation(libs.androidx.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinInject.runtime)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    api(libs.circuit.foundation)

    ksp(libs.kotlinInject.compiler)

    implementation(project(":experimental:sample:ev:xplat:foundation:di:impl"))
    implementation(project(":experimental:sample:ev:xplat:foundation:networking:impl"))
}

ksp {
    arg("me.tatarka.inject.generateCompanionExtensions", "true")
}