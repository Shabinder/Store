plugins {
    id("plugin.ev.android.library")
    id("plugin.ev.kotlin.multiplatform")
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
                implementation(libs.kotlinInject.runtime)
            }
        }
    }
}

android {
    namespace = "org.mobilenativefoundation.sample.ev.xplat.foundation.di.api"
}