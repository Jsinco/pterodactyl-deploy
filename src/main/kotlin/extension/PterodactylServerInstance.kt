package dev.jsinco.pterodactyldeploy.extension

import com.mattmalec.pterodactyl4j.client.entities.ClientServer
import com.mattmalec.pterodactyl4j.client.entities.Directory
import com.mattmalec.pterodactyl4j.client.entities.File
import com.mattmalec.pterodactyl4j.client.entities.PteroClient
import com.mattmalec.pterodactyl4j.exceptions.ServerException
import org.gradle.api.Project

class PterodactylServerInstance(val project: Project, val pteroClient: PteroClient, serverId: String) {

    val clientServer: ClientServer = pteroClient.retrieveServerByIdentifier(serverId).execute() ?: run {
        project.logger.error("Server with ID $serverId not found")
        throw IllegalArgumentException("Server with ID $serverId not found")
    }

    fun getOrCreateRemoteDirectory(rootDir: String?, directory: String): Directory {
        val dir = clientServer.retrieveDirectory(rootDir ?: "/").execute()
        clientServer.fileManager.createDirectory().setRoot(dir).setName(directory).execute()
        return this.getRemoteDirectory("${rootDir ?: ""}$directory")!!
    }

    fun getMutableRemoteDirectory(directory: String): Directory? {
        return clientServer.retrieveDirectory(
            clientServer.retrieveDirectory()?.execute(),
            this.getRemoteDirectory(directory)
        ).execute()
    }

    fun getRemoteDirectory(directory: String): Directory? {
        return try {
            clientServer.retrieveDirectory(directory).execute()
        } catch (e: ServerException) {
            // Directory not found
            null
        }
    }

    fun getRemoteFile(directory: String, file: String): File? {
        val remoteDirectory = this.getRemoteDirectory(directory)
        return remoteDirectory?.getFileByName(file)?.orElse(null)
    }
}