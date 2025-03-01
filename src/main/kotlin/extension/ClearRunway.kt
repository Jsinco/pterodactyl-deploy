package dev.jsinco.pterodactyldeploy.extension

import com.mattmalec.pterodactyl4j.client.entities.PteroClient
import org.gradle.api.Project

open class ClearRunway(
    val project: Project
) : PteroAction {

    var pteroClient: PteroClient? = null
    var serverId: String? = null

    var clearRemoteDirectories: MutableList<String>? = mutableListOf()
    var removeRemoteDirectories: MutableList<String>? = mutableListOf()
    var removeRemoteFiles: MutableList<String>? = mutableListOf()


    override fun action() {
        val psi = PterodactylServerInstance(project, pteroClient ?: return, serverId ?: return)

        clearRemoteDirectories?.forEach {
            val directory = psi.getRemoteDirectory(it)
            directory?.deleteFiles()?.execute()
        }

        removeRemoteDirectories?.forEach {
            val directory = psi.getRemoteDirectory(it)
            directory?.delete()?.execute()
        }

        removeRemoteFiles?.forEach {
            val parsed = parseForDirectoryAndFile(it)
            val file = psi.getRemoteFile(parsed.first, parsed.second)
            file?.delete()?.execute()
        }

        project.logger.info("Finished ClearRunway job for server: $serverId")
    }

    private fun parseForDirectoryAndFile(path: String): Pair<String, String> {
        val split = path.split("/")
        val directory = split.dropLast(1).joinToString("/")
        val file = split.last()
        return Pair(directory, file)
    }
}