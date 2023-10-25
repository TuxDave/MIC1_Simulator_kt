package com.tuxdave.mic1_simulator_kt.simulatorgui.projects

import com.tuxdave.mic1_simulator_kt.core.Mic1
import com.tuxdave.mic1_simulator_kt.core.component.RegNames
import kotlinx.serialization.Serializable
import java.io.File

@Serializable
data class Mic1Project(
    val relExecPath: String? = null,
    val relSrcPath: String? = null,
    var hexNumberFormat: Boolean = true,
    val startValues: MutableMap<RegNames, Int> = Mic1().mic1State.registers.map {
        Pair(RegNames.getFromName(it.key), it.value)
    }.toMap().toMutableMap(),
    val mpcBreakPoints: MutableList<Int> = mutableListOf()
)
