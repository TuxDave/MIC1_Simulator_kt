package com.tuxdave.mic1_simulator_kt.simulatorgui.controller

import com.tuxdave.mic1_simulator_kt.core.Mic1
import com.tuxdave.mic1_simulator_kt.core.component.RegNames
import com.tuxdave.mic1_simulator_kt.simulatorgui.Runner
import com.tuxdave.mic1_simulator_kt.simulatorgui.help.About
import com.tuxdave.mic1_simulator_kt.simulatorgui.project.Mic1Project
import com.tuxdave.mic1_simulator_kt.simulatorgui.project.ProjectListener
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory
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
) : Initializable, ProjectListener {
    private val mic1: Mic1
        get() = mic1Getter()

    private lateinit var runner: Runner

    private var numberBase: Int = 0

    private var mic1Project: Mic1Project? = null
        set(value) {
            field = value
            projectListeners.forEach {it.mic1ProjectChanged(field)}
        }
    private var ijvmProject: Any? = null
        set(value) {
            field = value
            //TODO: Call listeners
        }
    private val projectListeners: MutableList<ProjectListener> = mutableListOf()

    @FXML
    lateinit var tabPane: TabPane
    @FXML
    lateinit var mic1Tab: Tab

    @FXML
    lateinit var runButton: Button
    @FXML
    lateinit var stopButton: Button
    @FXML
    lateinit var microStepButton: Button
    @FXML
    lateinit var macroStepButton: Button

    @FXML
    lateinit var marTF: TextField
    @FXML
    lateinit var mdrTF: TextField
    @FXML
    lateinit var pcTF: TextField
    @FXML
    lateinit var mbrTF: TextField
    @FXML
    lateinit var opcTF: TextField
    @FXML
    lateinit var cppTF: TextField
    @FXML
    lateinit var lvTF: TextField
    @FXML
    lateinit var spTF: TextField
    @FXML
    lateinit var tosTF: TextField
    @FXML
    lateinit var hTF: TextField
    @FXML
    lateinit var mirTF: TextField
    @FXML
    lateinit var nextMirTF: TextField
    @FXML
    lateinit var mpcTF: TextField
    @FXML
    lateinit var waitSpinner: Spinner<Int>
    @FXML
    lateinit var hexMenuRadio: RadioMenuItem
    @FXML
    lateinit var decMenuRadio: RadioMenuItem
    @FXML
    lateinit var controlStoreToggle: CheckMenuItem

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        waitSpinner.valueFactory = IntegerSpinnerValueFactory(0, 20, 0, 1)
        waitSpinner.promptText = "sec"
        waitSpinner.focusedProperty().addListener { _, _, f ->
            if (!f && waitSpinner.valueFactory.value == null) waitSpinner.valueFactory.value = 0
            if(!f) {
                try {
                    runner.waitTimeSeconds = waitSpinner.valueFactory.value
                } catch (_: UninitializedPropertyAccessException){}
            }
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
                updateMic1Ui()
            }
        }
        projectListeners.add(this)
        reset()
    }

    fun updateMic1Ui(): Unit {
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
        nextMirTF.text = mic1.mic1ControlStoreState[state.mpc].joinToString("") {if (it) "1" else "0"}
        mpcTF.text = state.mpc.toString()
    }

    @FXML
    fun reset(): Unit {
        fun loadMic1Project(proj: Mic1Project): Unit {
            ijvmProject = null
            proj.relExecPath?.let {
                mic1.loadMicroProgram(File(it).toURI().toURL())
                proj.startValues.forEach { (regNames, value) ->
                    mic1.setRegisterValue(regNames, value)
                }
                if (proj.hexNumberFormat) {
                    hexMenuRadio.isSelected = true
                } else {
                    decMenuRadio.isSelected = true
                }
                changeNumberBase()
                mic1Tab.tabPane.selectionModel.select(mic1Tab)
            }
        }

        stop()
        reset.invoke()
        if (mic1Project != null) {
            loadMic1Project(mic1Project!!)
        } else if (false) {
            //TODO: Load ijvmProject
        } else {
            runButton.isDisable = true
            stopButton.isDisable = true
            microStepButton.isDisable = true
            macroStepButton.isDisable = true
        }
        updateMic1Ui()
    }

    @FXML
    fun closeProject(): Unit {
        //TODO: chiedi di salvare se modificato prima di chiudere
        mic1Project = null
        //TODO: close ijvmProject
        reset()
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
        updateMic1Ui()
    }

    @FXML
    private fun onRegistryAction(): Unit {
        nextMirTF.requestFocus()
    }

    @FXML
    private fun runMicroStep(): Unit{
        mic1.run()
        Platform.runLater(::updateMic1Ui)
    }

    @FXML
    fun start() {
        if (mic1Project != null){
            tabPane.isDisable = true
            runner = Runner(waitSpinner.value, exec = ::runMicroStep)
        } else if (ijvmProject != null) {//TODO: Crea il runner
        } else return
        runner.running = true
        runner.start()

        runButton.isDisable = true
        stopButton.isDisable = false
    }

    fun stop(): Unit {
        try{
            runner.running = false
            runButton.isDisable = false
            stopButton.isDisable = true
            tabPane.isDisable = false
        } catch (_: UninitializedPropertyAccessException) {}
    }

    @FXML
    fun openMicroprogram(): Unit {
        closeProject() //close all opened

        val fc = FileChooser()
        fc.title = "Apri un microprogramma"
        fc.extensionFilters.add(ExtensionFilter("Microprogramma", "*.mic1"))
        fc.initialDirectory = File(System.getProperty("user.home"))
        val file: File? = fc.showOpenDialog(null)
        file?.let {
            mic1Project = Mic1Project(
                relExecPath = it.toRelativeString(File(".").canonicalFile)
            )
            reset()
        }
    }

    override fun mic1ProjectChanged(proj: Mic1Project?) {
        proj?.let {
            runButton.isDisable = false
            stopButton.isDisable = true
            microStepButton.isDisable = false
            macroStepButton.isDisable = true
        }
        super.mic1ProjectChanged(proj)
    }

    override fun ijvmProjectChanged(proj: Any?) {
        //TODO("Not yet implemented")
        super.ijvmProjectChanged(proj)
    }

    @FXML
    @JvmOverloads
    fun controlStoreVisibilityChange(newState: Boolean? = null) {
        if(newState != null) {
            controlStoreToggle.isVisible = newState
            controlStoreVisibilityChange()
        } else {
            todo() //TODO: Implementare la finestra del control store
        }
    }
}