package com.tuxdave.mic1_simulator_kt.simulatorgui

import com.tuxdave.mic1_simulator_kt.simulatorgui.help.About
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.stage.Stage
import kotlin.system.exitProcess

class MainController {
    @FXML
    fun close(): Unit {
        exitProcess(0)
    }
    
    @FXML
    fun todo(): Unit {
        val alert = Alert(Alert.AlertType.INFORMATION)
        alert.title = "Non ancora implementato."
        alert.headerText = "Mi dispiace!"
        alert.contentText = "La funzione selezionata non è ancora implementata...\nRiprova dopo un futuro aggiornamento."
        alert.showAndWait()
    }

    @FXML
    fun about(): Unit {
        val fxmlLoader = FXMLLoader(About::class.java.getResource("About-view.fxml"))
        val scene = Scene(fxmlLoader.load())
        val stage = Stage()
        stage.title = "About this software..."
        stage.scene = scene
        stage.sizeToScene()
        stage.isResizable = false
        stage.show()
    }
}