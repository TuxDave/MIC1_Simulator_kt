package com.tuxdave.mic1_simulator_kt.simulatorgui

import com.tuxdave.mic1_simulator_kt.core.Mic1
import com.tuxdave.mic1_simulator_kt.simulatorgui.controller.MainController
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class Main : Application() {
    private var mic1 = Mic1()

    private lateinit var mainController: MainController

    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("Main-view.fxml"))
        fxmlLoader.setControllerFactory {
            MainController(
                mic1Getter = { mic1 },
                reset = { mic1 = Mic1() }
            )
        }
        val scene = Scene(fxmlLoader.load())
        mainController = fxmlLoader.getController()
        stage.title = "Mic1 Simulator"
        stage.scene = scene
        stage.sizeToScene()
        stage.isResizable = false
        mainController.reset()
        stage.show()
    }
}

fun main() {
    Application.launch(Main::class.java)
}