package com.tuxdave.mic1_simulator_kt.simulatorgui.help

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.Scene
import javafx.stage.Stage

class About {
    @FXML
    fun close(e: ActionEvent): Unit {
        ((e.source as Node).scene.window as Stage).close()
    }
}