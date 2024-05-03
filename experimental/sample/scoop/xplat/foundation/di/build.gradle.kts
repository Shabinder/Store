plugins {
    id("plugin.scoop.android.library")
    id("plugin.scoop.kotlin.multiplatform")
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
    namespace = "monster.scoop.xplat.foundation.di"
}