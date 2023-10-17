package com.tuxdave.mic1_simulator_kt.simulatorgui

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class Main : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("hello-view.fxml"))
        val scene = Scene(fxmlLoader.load())
        stage.title = "Hello!"
        stage.scene = scene
        stage.sizeToScene()
        stage.isResizable = false
        stage.show()
    }
}

fun main() {
    Application.launch(Main::class.java)
}