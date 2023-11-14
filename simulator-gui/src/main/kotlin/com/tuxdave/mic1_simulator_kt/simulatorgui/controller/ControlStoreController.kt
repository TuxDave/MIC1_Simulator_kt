package com.tuxdave.mic1_simulator_kt.simulatorgui.controller

import com.tuxdave.mic1_simulator_kt.core.Mic1
import com.tuxdave.mic1_simulator_kt.core.component.ControlStore
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.util.StringConverter
import java.lang.NumberFormatException
import java.net.URL
import java.util.*

class ControlStoreController(
    private val mic1Getter: () -> Mic1
) : Initializable {
    @FXML
    private lateinit var table: TableView<String>

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        updateData()
    }

    fun updateData() {
        val state = mic1Getter.invoke().mic1ControlStoreState
        //fill the table
    }
}
