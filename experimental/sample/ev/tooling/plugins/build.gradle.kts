plugins {
    `kotlin-dsl`
}

group = "org.mobilenativefoundation.sample.ev"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplicationPlugin") {
            id = "plugin.ev.android.application"
            implementationClass = "org.mobilenativefoundation.sample.ev.tooling.plugins.AndroidApplicationConventionPlugin"
        }

        register("androidComposePlugin") {
            id = "plugin.ev.android.compose"
            implementationClass = "org.mobilenativefoundation.sample.ev.tooling.plugins.AndroidComposeConventionPlugin"
        }

        register("androidLibraryPlugin") {
            id = "plugin.ev.android.library"
            implementationClass = "org.mobilenativefoundation.sample.ev.tooling.plugins.AndroidLibraryConventionPlugin"
        }

        register("kotlinAndroidLibraryPlugin") {
            id = "plugin.ev.kotlin.android.library"
            implementationClass = "org.mobilenativefoundation.sample.ev.tooling.plugins.KotlinAndroidLibraryConventionPlugin"
        }

        register("kotlinMultiplatformPlugin") {
            id = "plugin.ev.kotlin.multiplatform"
            implementationClass = "org.mobilenativefoundation.sample.ev.tooling.plugins.KotlinMultiplatformConventionPlugin"
        }
    }
}
