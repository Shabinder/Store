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
                api(libs.kotlinInject.runtime)
                api(project(":experimental:sample:scoop:xplat:foundation:networking:api"))
                api(project(":experimental:market"))
                api(project(":experimental:market:warehouse"))
                api(project(":experimental:sample:scoop:xplat:domain:story:api"))
            }
        }
    }
}

android {
    namespace = "monster.scoop.xplat.common.market"
}
