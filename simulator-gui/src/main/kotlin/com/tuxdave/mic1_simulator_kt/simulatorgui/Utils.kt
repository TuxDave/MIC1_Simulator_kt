package com.tuxdave.mic1_simulator_kt.simulatorgui

import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.stage.FileChooser
import java.io.File

fun openFile(title: String, exts: Map<String, String>, initDir: File = File(System.getProperty("user.home"))): File? {
    val fc = FileChooser()
    fc.title = title
    exts.forEach{
        fc.extensionFilters.add(FileChooser.ExtensionFilter(it.key, it.value))
    }
    fc.initialDirectory = initDir
    return fc.showOpenDialog(null)
}

fun notificationAlert(type: AlertType, title: String, header: String, text: String) {
    val alert = Alert(type)
    alert.title = title
    alert.headerText = header
    alert.contentText = text
    alert.showAndWait()
}