plugins {
    `java-gradle-plugin`
    `maven-publish`
    `signing`
    kotlin("jvm") version "2.0.21"
    id("com.gradle.plugin-publish") version "1.2.1"
    id("com.gradleup.shadow") version "9.0.0-beta9"
}

group = "dev.jsinco.pterodactyldeploy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.mattmalec.com/repository/releases")
}

dependencies {
    compileOnly(gradleApi())
    testImplementation(kotlin("test"))
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
            exclude("org.gradle.api")
            include(dependency("com.mattmalec:Pterodactyl4J:2.BETA_142"))
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