plugins {
    id("plugin.scoop.android.application")
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose)
}

android {
    namespace = "monster.scoop.android.app"

    defaultConfig {
        applicationId = "monster.scoop"
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
    implementation(libs.coil.compose)
    implementation(libs.coil.network)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.negotiation)

    implementation(libs.compose.webview.multiplatform)

    implementation(project(":experimental:market"))
    implementation(project(":experimental:market:warehouse"))
    implementation(project(":experimental:sample:scoop:xplat:foundation:di"))
    implementation(project(":experimental:sample:scoop:xplat:common:market"))
}

ksp {
    arg("me.tatarka.inject.generateCompanionExtensions", "true")
}