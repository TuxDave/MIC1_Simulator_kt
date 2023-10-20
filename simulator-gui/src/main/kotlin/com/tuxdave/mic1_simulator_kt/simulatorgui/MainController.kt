package com.tuxdave.mic1_simulator_kt.simulatorgui

import com.tuxdave.mic1_simulator_kt.core.Mic1
import com.tuxdave.mic1_simulator_kt.simulatorgui.help.About
import javafx.collections.ListChangeListener.Change
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.KeyEvent
import javafx.stage.Stage
import java.net.URL
import java.sql.Time
import java.util.*
import java.util.function.UnaryOperator
import kotlin.system.exitProcess

class MainController(
    private val mic1Getter: () -> Mic1 = {Mic1()},
    private val reset: () -> Unit = {}
): Initializable {
    private val mic1: Mic1
        get() = mic1Getter()

    var numberBase: Int = 0

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
    @FXML lateinit var waitSpinner: Spinner<Int>
    @FXML lateinit var hexMenuRadio: RadioMenuItem
    @FXML lateinit var decMenuRadio: RadioMenuItem

    //TODO: Fare modifica/aggiornamento dei registri

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        waitSpinner.valueFactory = object : SpinnerValueFactory<Int>() {
            override fun decrement(p0: Int) {
                value = maxOf(value - 1, 0)
            }

            override fun increment(p0: Int) {
                value++
            }
        }
        waitSpinner.valueFactory.value = 0
        changeNumberBase()

        marTF.textFormatter = TextFormatter<String>{ change -> //TODO fix this doing it better and apply at all in functional
            if(change.text.matches("[0-9a-fA-F]".toRegex())) change else null
        }

        //update registers

        reset()
    }

    fun updateUi(): Unit {
        val state = mic1.mic1State

        fun Int.toStringg(radix: Int): String {
            return if (this >= 0) this.toString(radix)
                    else if (radix == 10) this.toString(10)
                else Integer.toHexString(this)
        }

        marTF.text = state.registers["MAR"]?.toStringg(numberBase) ?: "NULL"
        mdrTF.text = state.registers["MDR"]?.toStringg(numberBase) ?: "NULL"
        pcTF.text = state.registers["PC"]?.toStringg(numberBase) ?: "NULL"
        mbrTF.text = state.registers["MBR"]?.toStringg(numberBase) ?: "NULL"
        lvTF.text = state.registers["LV"]?.toStringg(numberBase) ?: "NULL"
        spTF.text = state.registers["SP"]?.toStringg(numberBase) ?: "NULL"
        opcTF.text = state.registers["OPC"]?.toStringg(numberBase) ?: "NULL"
        tosTF.text = state.registers["TOS"]?.toStringg(numberBase) ?: "NULL"
        cppTF.text = state.registers["CPP"]?.toStringg(numberBase) ?: "NULL"
        hTF.text = state.registers["H"]?.toStringg(numberBase) ?: "NULL"
        mirTF.text = state.mir.joinToString("") { if (it) "1" else "0" }
        nextMirTF.text = "TODO"
        mpcTF.text = state.mpc.toString()
    }

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

    @FXML
    fun changeNumberBase(): Unit {
        numberBase = if (hexMenuRadio.isSelected) 16 else 10
        reset()
    }

    @FXML
    fun onRegisterKeyTyped(event: KeyEvent): Unit {
        val tf = event.source as TextField
        val char = event.character
    }
}

private val HEX_CHARSET = arrayOf('0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f')