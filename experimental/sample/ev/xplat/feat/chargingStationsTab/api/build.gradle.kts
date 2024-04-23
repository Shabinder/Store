plugins {
    id("plugin.ev.android.library")
    id("plugin.ev.kotlin.multiplatform")
    alias(libs.plugins.serialization)
    alias(libs.plugins.compose)
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(compose.runtime)
                api(compose.components.resources)
                api(libs.circuit.foundation)
                implementation(libs.kotlinInject.runtime)

                api(project(":experimental:market:warehouse"))
                api(project(":experimental:market"))
                api(project(":experimental:sample:ev:xplat:common:market"))
            }
        }
    }
}

android {
    namespace = "org.mobilenativefoundation.sample.ev.xplat.feat.chargingStationsTab.api"
}

dependencies {
    add("kspAndroid", libs.kotlinInject.compiler)
    add("kspIosX64", libs.kotlinInject.compiler)
    add("kspIosArm64", libs.kotlinInject.compiler)
}
