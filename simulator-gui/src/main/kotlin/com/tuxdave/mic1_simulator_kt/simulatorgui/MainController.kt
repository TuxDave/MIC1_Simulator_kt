package com.tuxdave.mic1_simulator_kt.simulatorgui

import com.tuxdave.mic1_simulator_kt.core.Mic1
import com.tuxdave.mic1_simulator_kt.core.component.RegNames
import com.tuxdave.mic1_simulator_kt.simulatorgui.help.About
import com.tuxdave.mic1_simulator_kt.simulatorgui.projects.Mic1Project
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory
import javafx.scene.input.InputMethodEvent
import javafx.scene.input.KeyEvent
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import javafx.stage.Stage
import java.io.File
import java.net.URL
import java.util.*
import kotlin.system.exitProcess

class MainController(
    private val mic1Getter: () -> Mic1 = { Mic1() },
    private val reset: () -> Unit = {}
) : Initializable {
    private val mic1: Mic1
        get() = mic1Getter()

    private var numberBase: Int = 0

    private var mic1Project: Mic1Project? = null
    private var ijvmProject: Any? = null

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

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        waitSpinner.valueFactory = IntegerSpinnerValueFactory(0, 20, 0, 1)
        waitSpinner.promptText = "sec"
        waitSpinner.focusedProperty().addListener { _, _, f ->
            if (!f && waitSpinner.valueFactory.value == null) waitSpinner.valueFactory.value = 0
        }
        waitSpinner.valueFactory.value = 0
        changeNumberBase()

        mapOf(
            Pair(marTF, RegNames.MAR),
            Pair(mdrTF, RegNames.MDR),
            Pair(pcTF, RegNames.PC),
            Pair(mbrTF, RegNames.MBR),
            Pair(opcTF, RegNames.OPC),
            Pair(cppTF, RegNames.CPP),
            Pair(lvTF, RegNames.LV),
            Pair(spTF, RegNames.SP),
            Pair(tosTF, RegNames.TOS),
            Pair(hTF, RegNames.H)
        ).forEach {
            val (it, value) = it
            it.textProperty().addListener { _, old, new ->
                if (numberBase == 16) {
                    if (!new.matches("[0-9a-fA-F]{0,8}".toRegex())) {
                        it.text = old ?: ""
                    }
                } else {
                    if (!new.matches("-?[0-9]*".toRegex())) {
                        it.text = old ?: ""
                    }
                }
            }
            it.focusedProperty().addListener { _, _, new ->
                if (!new) {
                    if (it.text == "") it.text = "0"
                    mic1.setRegisterValue(
                        value,
                        if (numberBase == 16) Integer.parseUnsignedInt(it.text, 16) else it.text.toInt()
                    )
                    println(mic1.mic1State.registers[value.toString()])
                }
                updateUi()
            }
        }
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

    private fun loadMic1Project(proj: Mic1Project): Unit {
        //TODO: Pensarci bene che c'era casino e non riuscivo
        ijvmProject = null
        reset()
        proj.relExecPath?.let{
            mic1.loadMicroProgram(File(it).toURI().toURL())
            proj.startValues.forEach { (regNames, value) ->
                mic1.setRegisterValue(regNames, value)
            }
            if(proj.hexNumberFormat) {
                hexMenuRadio.isSelected = true
            }else {
                hexMenuRadio.toggleGroup.toggles[1].isSelected = true
            }
            changeNumberBase()
        }
    }

    @FXML
    fun reset(): Unit {
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
        alert.contentText =
            "La funzione selezionata non è ancora implementata...\nRiprova dopo un futuro aggiornamento."
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
        updateUi()
    }

    @FXML
    fun onRegistryAction(): Unit {
        nextMirTF.requestFocus()
    }
    
    @FXML
    fun openMicroprogram(): Unit {
        val fc = FileChooser()
        fc.title = "Apri un microprogramma"
        fc.extensionFilters.add(ExtensionFilter("Microprogramma", "*.mic1"))
        fc.initialDirectory = File(System.getProperty("user.home"))
        val file: File? = fc.showOpenDialog(null)
        file?.let {
            loadMic1Project(Mic1Project(
                relExecPath = it.toRelativeString(File(".").canonicalFile)
            ))
        }
    }
}