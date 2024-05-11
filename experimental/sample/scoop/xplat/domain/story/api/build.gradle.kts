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
                api(project(":experimental:market"))
                api(project(":experimental:paging"))
                api(project(":experimental:sample:scoop:xplat:foundation:networking:api"))
                api(project(":experimental:sample:scoop:xplat:foundation:di"))
                api(project(":store"))
            }
        }
    }
}

android {
    namespace = "monster.scoop.xplat.domain.story.api"
}
