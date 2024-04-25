plugins {
    id("plugin.octonaut.android.library")
    id("plugin.octonaut.kotlin.multiplatform")
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
                implementation(libs.ktor.core)
                implementation(libs.ktor.negotiation)
                implementation(libs.ktor.serialization.json)
                implementation(libs.ktor.serialization.xml)
                implementation(libs.kotlinx.serialization.core)
                api(project(":experimental:sample:octonaut:xplat:foundation:networking:api"))
                api(project(":experimental:sample:octonaut:xplat:foundation:di:api"))
                implementation("io.github.pdvrieze.xmlutil:serialization:0.86.3")
            }
        }

        androidUnitTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        androidMain {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp-jvm:2.3.7")
            }
        }
        nativeMain {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:2.3.7")
            }
        }
        jvmMain {
            dependencies {
                implementation("io.ktor:ktor-client-apache5:2.3.7")
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
    namespace = "org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.impl"
}