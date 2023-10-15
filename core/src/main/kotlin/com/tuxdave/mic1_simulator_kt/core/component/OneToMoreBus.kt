package com.tuxdave.mic1_simulator_kt.core.component

import com.tuxdave.mic1_simulator_kt.core.component.legacy.ClockBasedComponent
import com.tuxdave.mic1_simulator_kt.core.component.legacy.Destination
import com.tuxdave.mic1_simulator_kt.core.component.legacy.Source

class OneToMoreBus(
    private val from: Source<Int>,
    private val to: List<Destination<Int>>
) : ClockBasedComponent() {
    override fun run() {
        from.outputEnabled = true
        val v = from.getValueOutput()!!
        to.forEach {
            it.setValueInput(v)
        }
    }
}

val C_BUS_DESTINATIONS = arrayOf(
    RegNames.H,
    RegNames.OPC,
    RegNames.TOS,
    RegNames.CPP,
    RegNames.LV,
    RegNames.SP,
    RegNames.PC,
    RegNames.MDR,
    RegNames.MAR
)