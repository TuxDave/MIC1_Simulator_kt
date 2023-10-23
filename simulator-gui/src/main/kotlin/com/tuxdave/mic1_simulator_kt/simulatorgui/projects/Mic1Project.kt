package com.tuxdave.mic1_simulator_kt.simulatorgui.projects

import com.tuxdave.mic1_simulator_kt.core.component.ControlStore
import com.tuxdave.mic1_simulator_kt.core.component.RegNames
import java.io.File

data class Mic1Project(
    val exec: File,
    var hexNumberFormat: Boolean,
    val startValues: Map<RegNames, Int>
    // TODO: add some and maybe serialize
)
