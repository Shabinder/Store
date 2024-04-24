plugins {
    id("plugin.octonaut.android.library")
    id("plugin.octonaut.kotlin.multiplatform")
    alias(libs.plugins.serialization)
    alias(libs.plugins.compose)
    id("com.apollographql.apollo3") version "3.8.2"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(compose.runtime)
                api(compose.components.resources)
                api(libs.circuit.foundation)
                api(libs.apollo.runtime)
                api(libs.kotlinx.serialization.core)
                api(project(":experimental:sample:octonaut:xplat:foundation:di:api"))
            }
        }
    }
}

android {
    namespace = "org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api"
}

apollo {
    service("service") {
        packageName.set("org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api")
        schemaFiles.from(file("src/commonMain/graphql/schema.graphqls"))
    }
}