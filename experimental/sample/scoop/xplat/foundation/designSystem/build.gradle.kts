plugins {
    id("plugin.scoop.android.library")
    id("plugin.scoop.kotlin.compose.multiplatform")
    alias(libs.plugins.serialization)
    alias(libs.plugins.compose)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(compose.runtime)
                api(compose.ui)
                api(compose.foundation)
                api(compose.material3)
                api(compose.components.resources)
                api(libs.circuit.foundation)
                implementation(libs.kotlinInject.runtime)

                implementation(compose.material3)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinInject.runtime)
                implementation(compose.materialIconsExtended)

                implementation(libs.swipe)
                implementation(compose.components.uiToolingPreview)
            }
        }
    }
}

android {
    namespace = "monster.scoop.xplat.foundation.designSystem"
}
