package dev.jsinco.pterodactyldeploy

import com.mattmalec.pterodactyl4j.PteroBuilder
import com.mattmalec.pterodactyl4j.client.entities.PteroClient
import dev.jsinco.pterodactyldeploy.extension.ClearRunway
import dev.jsinco.pterodactyldeploy.extension.DropIn
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import javax.inject.Inject

open class PteroDeploy : DefaultTask() {

    @Input
    var url: String? = null
    @Input
    var apiKey: String? = null
    @Input
    var serverId: String? = null
    @Internal
    var pteroClient: PteroClient? = null
    @Internal
    val dropIn: DropIn = DropIn(project)
    @Internal
    val clearRunway: ClearRunway = ClearRunway(project)

    init {
        group = "pterodactylDeploy"
    }

    fun dropIn(action: Action<in DropIn>) {
        action.execute(dropIn)
    }

    fun clearRunway(action: Action<in ClearRunway>) {
        action.execute(clearRunway)
    }


    fun instantiateClient() {
        if (url == null || apiKey == null) {
            throw IllegalArgumentException("url and apiKey must be set")
        }
        pteroClient = PteroBuilder.createClient(url, apiKey)
        if (pteroClient == null) {
            throw IllegalArgumentException("Failed to instantiate PteroClient")
        }
    }
}