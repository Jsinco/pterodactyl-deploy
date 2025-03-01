plugins {
    `java-gradle-plugin`
    `maven-publish`
    kotlin("jvm") version "2.0.21"
    id("com.gradle.plugin-publish") version "1.2.1"
    id("com.gradleup.shadow") version "9.0.0-beta9"
}

group = "dev.jsinco.pterodactyldeploy"
version = "1.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.mattmalec.com/repository/releases")
}

dependencies {
    compileOnly(gradleApi())
    implementation("com.mattmalec:Pterodactyl4J:2.BETA_142")
}


kotlin {
    jvmToolchain(17)
}

gradlePlugin {
    website.set("https://github.com/Jsinco/pterodactyl-deploy")
    vcsUrl.set("https://github.com/Jsinco/pterodactyl-deploy")

    plugins {
        create("pterodactylDeploy") {
            id = "dev.jsinco.pterodactyldeploy"
            displayName = "Pterodactyl Deploy Plugin"
            description = "A plugin to deploy to a Pterodactyl server"
            implementationClass = "dev.jsinco.pterodactyldeploy.PterodactylDeployGradlePlugin"
            tags.set(listOf("deployment", "deploy", "minecraft", "pterodactyl", "ci", "server"))
        }
    }
}


tasks {
    shadowJar {
        dependencies {

        }

        archiveClassifier.set("")
    }

    jar {

    }

    build {
        dependsOn(shadowJar)
    }

    test {
        useJUnitPlatform()
    }
}

publishing {
    repositories {
        maven {
            name = "jsinco-repo"
            url = uri("https://repo.jsinco.dev/releases")
            credentials(PasswordCredentials::class) {
                // get from environment
                username = System.getenv("repo_username")
                password = System.getenv("repo_secret")
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            //artifactId = project.name
            version = project.version.toString()
            artifact(tasks.shadowJar.get().archiveFile) {
                builtBy(tasks.shadowJar)
            }
        }
    }
}