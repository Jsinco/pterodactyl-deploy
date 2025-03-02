package dev.jsinco.pterodactyldeploy

import org.gradle.api.Plugin
import org.gradle.api.Project

open class PterodactylDeployGradlePlugin: Plugin<Project> {


    override fun apply(target: Project) {
        val pteroDeploy = target.tasks.create("pterodactylDeploy", PteroDeploy::class.java) as PteroDeploy
        pteroDeploy.doFirst {
            pteroDeploy.instantiateClient()
        }

        pteroDeploy.doLast { lastTask ->
            val clearRunway = pteroDeploy.clearRunway.apply {
                pteroClient = pteroDeploy.pteroClient
                serverId = pteroDeploy.serverId
            }
            val dropIn = pteroDeploy.dropIn.apply {
                pteroClient = pteroDeploy.pteroClient
                serverId = pteroDeploy.serverId
            }

            clearRunway.action()
            dropIn.action()

            target.logger.lifecycle("Finished actions for server: ${pteroDeploy.serverId}")
        }

//        target.tasks.create("pterodactylDeploy") { task ->
//            task.group = "pterodactylDeploy"
//
//            task.doFirst {
//                pteroDeploy.instantiateClient()
//            }
//
//            task.doLast { lastTask ->
//                val clearRunway = pteroDeploy.clearRunway.apply {
//                    pteroClient = pteroDeploy.pteroClient
//                    serverId = pteroDeploy.serverId
//                }
//                val dropIn = pteroDeploy.dropIn.apply {
//                    pteroClient = pteroDeploy.pteroClient
//                    serverId = pteroDeploy.serverId
//                }
//
//                clearRunway.action()
//                dropIn.action()
//
//                target.logger.lifecycle("Finished actions for server: ${pteroDeploy.serverId}")
//            }
//        }
    }

}