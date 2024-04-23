plugins {
    id("plugin.ev.android.library")
    id("plugin.ev.kotlin.multiplatform")
    alias(libs.plugins.serialization)
    alias(libs.plugins.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.plugin.parcelize)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(compose.runtime)
                api(compose.components.resources)
                api(libs.circuit.foundation)
                implementation(libs.kotlinInject.runtime)
                api(compose.material3)

                api(project(":experimental:sample:ev:xplat:domain:chargingStations:api"))
                api(project(":experimental:market:warehouse"))
                api(project(":experimental:market"))
                api(project(":experimental:sample:ev:xplat:common:market"))
                api(project(":experimental:sample:ev:xplat:feat:chargingStationsTab:api"))
                api(project(":experimental:sample:ev:xplat:foundation:di:api"))
            }
        }
    }
}

android {
    namespace = "org.mobilenativefoundation.sample.ev.xplat.feat.chargingStationsTab.impl"
}

dependencies {
    add("kspAndroid", libs.kotlinInject.compiler)
    add("kspIosX64", libs.kotlinInject.compiler)
    add("kspIosArm64", libs.kotlinInject.compiler)
}
