package dev.jsinco.pterodactyldeploy.extension

import com.mattmalec.pterodactyl4j.client.entities.PteroClient
import org.gradle.api.Project
import java.io.File
import java.nio.file.Path

open class DropIn(
    val project: Project
) : PteroAction {
    var pteroClient: PteroClient? = null
    var serverId: String? = null

    var deployDirectory: String? = null // Remote directory to deploy to
    var deployCommands: MutableList<String>? = mutableListOf() // Commands to run after deploying the files

    var uploadDirectories: MutableList<Path>? = mutableListOf() // Local directories to upload
    var uploadFiles: MutableList<File>? = mutableListOf() // Local files to upload


    override fun action() {
        val psi = PterodactylServerInstance(project, pteroClient ?: return, serverId ?: return)

        val remoteDir = psi.getRemoteDirectory(deployDirectory ?: "/") ?: return run {
            project.logger.error("Remote directory $deployDirectory not found")
        }

       if (uploadFiles?.isNotEmpty() == true) {
           val fileUpload = remoteDir.upload()
           uploadFiles!!.forEach { file ->
               if (!file.exists()) {
                   project.logger.error("File ${file.absolutePath} does not exist!")
                   return@forEach
               }
               fileUpload.addFile(file)
           }
           fileUpload.execute()
       }

        for (localDir in uploadDirectories ?: emptyList()) {
            uploadLocalDirectory(localDir, psi)
        }

        deployCommands?.forEach { command ->
            psi.clientServer.sendCommand(command).execute()
        }

        project.logger.info("Finished DropIn job for server: $serverId")
    }

    // helper func
    private fun uploadLocalDirectory(localDir: Path, psi: PterodactylServerInstance, parent: String? = null) {
        val dir = localDir.toFile()
        if (!dir.exists()) {
            project.logger.error("Directory ${dir.absolutePath} does not exist!")
            return
        }

        val dirName = if (parent == null) {
            dir.name
        } else {
            "$parent/${dir.name}"
        }
        val localRemoteDir =  psi.getOrCreateRemoteDirectory(deployDirectory, dirName)

        val localFileUpload = localRemoteDir.upload()
        val ref = dir.listFiles() ?: return
        val allDirectories = ref.filter { it.isDirectory }
        val allFiles = ref.filter { it.isFile }

        if (allDirectories.isNotEmpty()) {
            allDirectories.forEach { directory ->
                uploadLocalDirectory(directory.toPath(), psi, dirName)
            }
        }

        if (allFiles.isNotEmpty()) {
            allFiles.forEach { file ->
                localFileUpload.addFile(file)
            }
            localFileUpload.execute()
        }
    }

}