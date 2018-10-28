rootProject.name = "brave-kt"

pluginManagement {
    resolutionStrategy {
        val kotlinVersion: String by settings
        eachPlugin {
            when (requested.id.id) {
                "org.jetbrains.kotlin.jvm" -> useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
                "org.jlleitschuh.gradle.ktlint" -> useModule("gradle.plugin.org.jlleitschuh.gradle:ktlint-gradle:4.1.0")
            }
        }
    }

    repositories {
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
    }
}