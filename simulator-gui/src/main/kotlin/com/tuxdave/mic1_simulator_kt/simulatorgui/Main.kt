package com.tuxdave.mic1_simulator_kt.simulatorgui

import com.tuxdave.mic1_simulator_kt.core.Mic1
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class Main : Application() {
    var mic1 = Mic1()

    val mainController: MainController

    init {
        mainController = MainController(
            mic1_getter = { mic1 },
            reset = { mic1 = Mic1() }
        )
    }

    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("Main-view.fxml"))
        fxmlLoader.setController(mainController)
        val scene = Scene(fxmlLoader.load())
        stage.title = "Mic1 Simulator"
        stage.scene = scene
        stage.sizeToScene()
        stage.isResizable = false
        stage.show()
    }
}

fun main() {
    Application.launch(Main::class.java)
}