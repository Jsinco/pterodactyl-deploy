package dev.jsinco.pterodactyldeploy

import org.gradle.api.Plugin
import org.gradle.api.Project

open class PterodactylDeployGradlePlugin: Plugin<Project> {


    override fun apply(target: Project) {
        val pteroDeploy = target.extensions.create("pterodactylDeploy", PteroDeploy::class.java, target)

        target.tasks.create("pterodactylDeploy") { task ->
            task.group = "pterodactylDeploy"

            task.doLast { lastTask ->
                val bearer = pteroDeploy.bearer
                bearer.instantiateClient()
                val clearRunway = pteroDeploy.clearRunway.apply {
                    pteroClient = bearer.pteroClient
                    serverId = bearer.serverId
                }
                val dropIn = pteroDeploy.dropIn.apply {
                    pteroClient = bearer.pteroClient
                    serverId = bearer.serverId
                }

                clearRunway.action()
                dropIn.action()

                target.logger.lifecycle("Finished actions for server: ${bearer.serverId}")
            }
        }
    }

}