plugins {
    `kotlin-dsl`
}

group = "org.mobilenativefoundation.sample.octonaut"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplicationPlugin") {
            id = "plugin.octonaut.android.application"
            implementationClass = "org.mobilenativefoundation.sample.octonaut.tooling.plugins.AndroidApplicationConventionPlugin"
        }

        register("androidComposePlugin") {
            id = "plugin.octonaut.android.compose"
            implementationClass = "org.mobilenativefoundation.sample.octonaut.tooling.plugins.AndroidComposeConventionPlugin"
        }

        register("androidLibraryPlugin") {
            id = "plugin.octonaut.android.library"
            implementationClass = "org.mobilenativefoundation.sample.octonaut.tooling.plugins.AndroidLibraryConventionPlugin"
        }

        register("kotlinAndroidLibraryPlugin") {
            id = "plugin.octonaut.kotlin.android.library"
            implementationClass = "org.mobilenativefoundation.sample.octonaut.tooling.plugins.KotlinAndroidLibraryConventionPlugin"
        }

        register("kotlinMultiplatformPlugin") {
            id = "plugin.octonaut.kotlin.multiplatform"
            implementationClass = "org.mobilenativefoundation.sample.octonaut.tooling.plugins.KotlinMultiplatformConventionPlugin"
        }
    }
}
