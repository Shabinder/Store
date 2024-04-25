plugins {
    id("plugin.octonaut.android.library")
    id("plugin.octonaut.kotlin.multiplatform")
    alias(libs.plugins.serialization)
    alias(libs.plugins.compose)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(compose.runtime)
                api(compose.components.resources)
                api(libs.circuit.foundation)
                api(libs.kotlinx.serialization.core)
                implementation(libs.ktor.serialization.xml)
                api(project(":experimental:sample:octonaut:xplat:foundation:di:api"))
                implementation(libs.compose.webview.multiplatform)
            }
        }
    }
}

android {
    namespace = "org.mobilenativefoundation.sample.octonaut.xplat.foundation.webview"
}