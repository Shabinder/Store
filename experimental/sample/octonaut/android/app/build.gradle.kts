plugins {
    id("plugin.octonaut.android.application")
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose)
}

android {
    namespace = "org.mobilenativefoundation.sample.octonaut.android.app"

    defaultConfig {
        applicationId = "org.mobilenativefoundation.sample.octonaut"
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

    implementation(project(":experimental:sample:octonaut:xplat:foundation:di:api"))
    implementation(project(":experimental:sample:octonaut:xplat:foundation:networking:impl"))
    implementation(project(":experimental:sample:octonaut:xplat:foundation:webview"))
    implementation(project(":experimental:sample:octonaut:xplat:common:market"))
    implementation(project(":experimental:sample:octonaut:xplat:domain:user:impl"))
    implementation(project(":experimental:sample:octonaut:xplat:domain:feed:impl"))
    implementation(project(":experimental:sample:octonaut:xplat:domain:notifications:impl"))
    implementation(project(":experimental:sample:octonaut:xplat:feat:homeTab:impl"))
    implementation(project(":experimental:sample:octonaut:xplat:feat:notificationsTab:impl"))
    implementation(project(":experimental:sample:octonaut:xplat:feat:exploreTab:impl"))
    implementation(project(":experimental:sample:octonaut:xplat:feat:profileTab:impl"))
    implementation(libs.compose.webview.multiplatform)
}

ksp {
    arg("me.tatarka.inject.generateCompanionExtensions", "true")
}