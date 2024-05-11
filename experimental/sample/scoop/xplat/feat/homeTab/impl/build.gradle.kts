plugins {
    id("plugin.scoop.android.library")
    id("plugin.scoop.kotlin.multiplatform")
    alias(libs.plugins.serialization)
    alias(libs.plugins.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.plugin.parcelize)
}

kotlin {
    androidTarget {
        plugins.apply(libs.plugins.paparazzi.get().pluginId)
    }

    sourceSets {

        commonMain {
            dependencies {
                implementation(compose.material3)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinInject.runtime)
                implementation(compose.materialIconsExtended)

                api(project(":experimental:market:warehouse"))
                api(project(":experimental:sample:scoop:xplat:common:market"))
                api(project(":experimental:sample:scoop:xplat:feat:homeTab:api"))
                api(project(":experimental:sample:scoop:xplat:foundation:di"))
                implementation(libs.coil.compose)
                implementation(libs.coil.network)
                implementation(libs.ktor.core)
                implementation(libs.swipe)
                implementation(compose.components.uiToolingPreview)
            }
        }

        androidUnitTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

dependencies {
    add("kspAndroid", libs.kotlinInject.compiler)
    add("kspIosX64", libs.kotlinInject.compiler)
    add("kspIosArm64", libs.kotlinInject.compiler)
}

android {
    namespace = "monster.scoop.xplat.feat.homeTab.impl"
}