package monster.scoop.tooling.plugins

import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import monster.scoop.tooling.extensions.Versions
import monster.scoop.tooling.extensions.configureAndroid
import monster.scoop.tooling.extensions.configureFlavors

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
            }

            extensions.configure<LibraryExtension> {
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }

                configureAndroid()
                configureFlavors(this)
                defaultConfig.targetSdk = Versions.TARGET_SDK
            }
        }
    }
}
