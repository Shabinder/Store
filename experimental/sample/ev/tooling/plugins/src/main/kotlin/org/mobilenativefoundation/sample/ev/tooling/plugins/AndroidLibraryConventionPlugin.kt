package org.mobilenativefoundation.sample.ev.tooling.plugins

import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.mobilenativefoundation.sample.ev.tooling.extensions.Versions
import org.mobilenativefoundation.sample.ev.tooling.extensions.configureAndroid
import org.mobilenativefoundation.sample.ev.tooling.extensions.configureFlavors

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
            }

            extensions.configure<LibraryExtension> {
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }

                configureAndroid()
                configureFlavors(this)
                defaultConfig.targetSdk = Versions.TARGET_SDK
            }
        }
    }
}
