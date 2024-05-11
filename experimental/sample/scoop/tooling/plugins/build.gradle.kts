plugins {
    `kotlin-dsl`
}

group = "monster.scoop"

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
            id = "plugin.scoop.android.application"
            implementationClass = "monster.scoop.tooling.plugins.AndroidApplicationConventionPlugin"
        }

        register("androidComposePlugin") {
            id = "plugin.scoop.android.compose"
            implementationClass = "monster.scoop.tooling.plugins.AndroidComposeConventionPlugin"
        }

        register("androidLibraryPlugin") {
            id = "plugin.scoop.android.library"
            implementationClass = "monster.scoop.tooling.plugins.AndroidLibraryConventionPlugin"
        }

        register("kotlinAndroidLibraryPlugin") {
            id = "plugin.scoop.kotlin.android.library"
            implementationClass = "monster.scoop.tooling.plugins.KotlinAndroidLibraryConventionPlugin"
        }

        register("kotlinMultiplatformPlugin") {
            id = "plugin.scoop.kotlin.multiplatform"
            implementationClass = "monster.scoop.tooling.plugins.KotlinMultiplatformConventionPlugin"
        }

        register("kotlinComposeMultiplatformPlugin") {
            id = "plugin.scoop.kotlin.compose.multiplatform"
            implementationClass = "monster.scoop.tooling.plugins.KotlinComposeMultiplatformConventionPlugin"
        }
    }
}
