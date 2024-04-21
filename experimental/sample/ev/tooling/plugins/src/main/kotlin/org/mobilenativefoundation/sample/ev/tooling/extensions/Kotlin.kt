package org.mobilenativefoundation.sample.ev.tooling.extensions
import org.gradle.api.Project
import org.mobilenativefoundation.sample.ev.tooling.extensions.configureJava

fun Project.configureKotlin() {
  configureJava()
}