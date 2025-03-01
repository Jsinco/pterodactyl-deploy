package dev.jsinco.pterodactyldeploy

import dev.jsinco.pterodactyldeploy.extension.Bearer
import dev.jsinco.pterodactyldeploy.extension.ClearRunway
import dev.jsinco.pterodactyldeploy.extension.DropIn
import org.gradle.api.Action
import org.gradle.api.Project

open class PteroDeploy(
    private val project: Project
) {

    val bearer: Bearer = Bearer()
    val dropIn: DropIn = DropIn(project)
    val clearRunway: ClearRunway = ClearRunway(project)

    fun bearer(action: Action<in Bearer>) {
        action.execute(bearer)
    }

    fun dropIn(action: Action<in DropIn>) {
        action.execute(dropIn)
    }

    fun clearRunway(action: Action<in ClearRunway>) {
        action.execute(clearRunway)
    }
}