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
                api(libs.kotlinInject.runtime)
                api(project(":experimental:sample:octonaut:xplat:foundation:networking:api"))
                api(project(":experimental:market"))
                api(project(":experimental:market:warehouse"))
                api(project(":experimental:sample:octonaut:xplat:domain:user:api"))
            }
        }
    }
}

android {
    namespace = "org.mobilenativefoundation.sample.octonaut.xplat.common.market"
}