package org.mobilenativefoundation.sample.octonaut.tooling.extensions
import org.gradle.api.Project
import org.mobilenativefoundation.sample.octonaut.tooling.extensions.configureJava

fun Project.configureKotlin() {
  configureJava()
}