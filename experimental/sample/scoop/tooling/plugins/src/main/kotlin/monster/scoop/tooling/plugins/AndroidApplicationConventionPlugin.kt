package monster.scoop.tooling.plugins


import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import monster.scoop.tooling.extensions.BuildFlavor
import monster.scoop.tooling.extensions.BuildType
import monster.scoop.tooling.extensions.FlavorDimension
import monster.scoop.tooling.extensions.Versions
import monster.scoop.tooling.extensions.configureAndroid
import monster.scoop.tooling.extensions.configureAndroidCompose
import monster.scoop.tooling.extensions.configureFlavors

class AndroidApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<ApplicationExtension> {
                defaultConfig {
                    targetSdk = Versions.TARGET_SDK
                    missingDimensionStrategy(
                        FlavorDimension.contentType.name,
                        BuildFlavor.demo.name
                    )
                }

                buildFeatures {
                    buildConfig = true
                }

                configureAndroid()
                configureAndroidCompose(this)
                configureFlavors(this)

                buildTypes {
                    getByName(BuildType.DEBUG.applicationIdSuffix) {
                    }

                    getByName(BuildType.RELEASE.applicationIdSuffix) {
                    }
                }
            }
        }
    }
}
