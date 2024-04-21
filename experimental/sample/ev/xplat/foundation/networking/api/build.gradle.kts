plugins {
    id("plugin.ev.android.library")
    id("plugin.ev.kotlin.multiplatform")
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
            }
        }
    }
}

android {
    namespace = "org.mobilenativefoundation.sample.ev.xplat.foundation.networking.api"
}

apollo {
    service("service") {
        packageName.set("org.mobilenativefoundation.sample.ev.xplat.foundation.networking.api")
        schemaFiles.from(file("src/commonMain/graphql/schema.graphqls"))
    }
}