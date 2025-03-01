package dev.jsinco.pterodactyldeploy.extension

import com.mattmalec.pterodactyl4j.PteroBuilder
import com.mattmalec.pterodactyl4j.client.entities.PteroClient

open class Bearer {

    var url: String? = null
    var apiKey: String? = null
    var serverId: String? = null


    var pteroClient: PteroClient? = null

    fun instantiateClient() {
        pteroClient = PteroBuilder.createClient(url, apiKey)
        if (pteroClient == null) {
            throw IllegalArgumentException("Failed to instantiate PteroClient")
        }
    }
}