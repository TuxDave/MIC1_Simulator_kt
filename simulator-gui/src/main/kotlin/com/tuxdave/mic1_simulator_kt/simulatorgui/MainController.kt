package com.tuxdave.mic1_simulator_kt.simulatorgui

import com.tuxdave.mic1_simulator_kt.core.Mic1
import com.tuxdave.mic1_simulator_kt.simulatorgui.help.About
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.TextField
import javafx.stage.Stage
import kotlin.system.exitProcess

class MainController(
    private val mic1Getter: () -> Mic1 = {Mic1()},
    private val reset: () -> Unit = {}
) {
    private val mic1: Mic1
        get() = mic1Getter()

    fun updateUi(): Unit {
        val state = mic1.mic1State
        marTF.text = state.registers["MAR"]?.toString(16) ?: ""
        mdrTF.text = state.registers["MDR"]?.toString(16) ?: ""
        pcTF.text = state.registers["PC"]?.toString(16) ?: ""
        mbrTF.text = state.registers["MBR"]?.toString(16) ?: ""
        lvTF.text = state.registers["LV"]?.toString(16) ?: ""
        spTF.text = state.registers["SP"]?.toString(16) ?: ""
        opcTF.text = state.registers["OPC"]?.toString(16) ?: ""
        tosTF.text = state.registers["TOS"]?.toString(16) ?: ""
        cppTF.text = state.registers["CPP"]?.toString(16) ?: ""
        hTF.text = state.registers["H"]?.toString(16) ?: ""
        mirTF.text = state.mir.joinToString("") { if (it) "1" else "0" }
        nextMirTF.text = "TODO"
        mpcTF.text = state.mpc.toString()
    }

    @FXML lateinit var marTF: TextField
    @FXML lateinit var mdrTF: TextField
    @FXML lateinit var pcTF: TextField
    @FXML lateinit var mbrTF: TextField
    @FXML lateinit var opcTF: TextField
    @FXML lateinit var cppTF: TextField
    @FXML lateinit var lvTF: TextField
    @FXML lateinit var spTF: TextField
    @FXML lateinit var tosTF: TextField
    @FXML lateinit var hTF: TextField
    @FXML lateinit var mirTF: TextField
    @FXML lateinit var nextMirTF: TextField
    @FXML lateinit var mpcTF: TextField

    @FXML
    fun reset(): Unit  {
        reset.invoke()
        updateUi()
    }

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