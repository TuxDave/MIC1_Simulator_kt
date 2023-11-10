package com.tuxdave.mic1_simulator_kt.simulatorgui.controller

import com.tuxdave.mic1_simulator_kt.simulatorgui.notificationAlert
import com.tuxdave.mic1_simulator_kt.simulatorgui.project.Mic1Project
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.stage.FileChooser
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

const val MIC1_EXEC_NAME = "exec.mic1"
const val MIC1_SRC_NAME = "src.mal"

fun askForSave(msg: String): Boolean {
    val ask = Alert(Alert.AlertType.CONFIRMATION)
    ask.title = "Richiesta di salvataggio..."
    ask.title = "Salvare il progetto?"
    ask.contentText = msg
    ask.showAndWait()
    ask.isResizable = false
    return ask.result == ButtonType.OK
}

fun saveAs(proj: Mic1Project, destFile: String) {
    try {
        val dest = File(destFile)

        val folder = dest.parentFile
        if (proj.relExecPath != null && (folder.listFiles()?.filter { it.name == MIC1_EXEC_NAME }?.size ?: 0) == 0) {
            if(File("${MainController.currentDir}/${proj.relExecPath}").exists())
                File("${MainController.currentDir}/${proj.relExecPath}").copyTo(File(folder.absolutePath + "/" + "exec.mic1"), overwrite = true)
        }
        proj.relExecPath = MIC1_EXEC_NAME

        if (proj.relSrcPath != null && (folder.listFiles()?.filter { it.name == MIC1_SRC_NAME }?.size ?: 0) == 0) {
            if(File("${MainController.currentDir}/${proj.relSrcPath}").exists())
                File("${MainController.currentDir}/${proj.relSrcPath}").copyTo(File(folder.absolutePath + "/" + File(proj.relSrcPath).name), overwrite = true)
        }
        proj.relSrcPath = "src.mal"

        //TODO: decompile if source missing

        dest.writeText(Json.encodeToString(proj))
    } catch (e: Exception) {
        e.printStackTrace()
        notificationAlert(
            Alert.AlertType.ERROR,
            "Errore di scrittura",
            "Errore!",
            "Si è verificato un errore durante la scrittura, verifica che la posizione sia scrivibile"
        )
    }
}