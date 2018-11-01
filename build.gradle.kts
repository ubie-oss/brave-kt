import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("jacoco")
    id("org.jlleitschuh.gradle.ktlint")
    id("maven")
    id("signing")
    id("org.jetbrains.dokka")
}

group = "app.ubie"
version = "1.0.0"

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
    maxHeapSize = "3g"
}

jacoco {
    toolVersion = "0.8.2"
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    val kotlinVersion: String by project
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("io.zipkin.brave:brave:5.5.0")

    val junitVersion = "5.3.0"
    testCompile("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testCompile("io.mockk:mockk:1.8.7")
    testCompile("org.assertj:assertj-core:3.11.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

val dokka by tasks.getting(DokkaTask::class) {
    outputFormat = "html"
    outputDirectory = "$buildDir/javadoc"
    jdkVersion = 8
}

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    classifier = "javadoc"
    from(dokka)
}

val sourcesJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    classifier = "sources"
    from(sourceSets["main"].allSource)
}

artifacts.add("archives", dokkaJar)
artifacts.add("archives", sourcesJar)

signing {
    isRequired = System.getenv("CI")?.toBoolean()?.not() ?: true
    sign(configurations.archives)
}

val uploadArchives: Upload by tasks
uploadArchives.apply {
    repositories {
        withConvention(MavenRepositoryHandlerConvention::class) {
            mavenDeployer {
                beforeDeployment {
                    signing.signPom(this)
                }

                val username = if(project.hasProperty("sonatypeUsername")) project.properties["sonatypeUsername"] else System.getenv("sonatypeUsername")
                val password = if(project.hasProperty("sonatypePassword")) project.properties["sonatypePassword"] else System.getenv("sonatypePassword")

                withGroovyBuilder {
                    "snapshotRepository"("url" to "https://oss.sonatype.org/content/repositories/snapshots") {
                        "authentication"("userName" to username, "password" to password)
                    }

                    "repository"("url" to "https://oss.sonatype.org/service/local/staging/deploy/maven2") {
                        "authentication"("userName" to username, "password" to password)
                    }
                }

                pom.project {
                    withGroovyBuilder {
                        "name"("brave-kt")
                        "artifactId"("brave-kt")
                        "packaging"("jar")
                        "url"("https://github.com/ubie-inc/brave-kt")
                        "description"("brave-kt is the library that adds Kotlin support to brave")
                        "scm" {
                            "connection"("scm:git:git://github.com/ubie-inc/brave-kt.git")
                            "developerConnection"("scm:git:ssh://git@github.com:ubie-inc/brave-kt.git")
                            "url"("https://github.com/ubie-inc/brave-kt")
                        }
                        "licenses" {
                            "license" {
                                "name"("The Apache Software License, Version 2.0")
                                "url"("http://www.apache.org/licenses/LICENSE-2.0.txt")
                                "distribution"("repo")
                            }
                        }

                        "developers" {
                            "developer" {
                                "id"("isogai-ubie")
                                "name"("shiraji")
                            }
                        }

                        "issueManagement" {
                            "system"("github")
                            "url"("https://github.com/ubie-inc/brave-kt/issues")
                        }
                    }
                }
            }
        }
    }
}