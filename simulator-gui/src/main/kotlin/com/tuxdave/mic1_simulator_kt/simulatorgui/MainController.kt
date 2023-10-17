package com.tuxdave.mic1_simulator_kt.simulatorgui

import javafx.fxml.FXML
import javafx.scene.control.Alert
import kotlin.system.exitProcess

class MainController {
    @FXML
    fun close(): Unit {
        println("we")
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
}