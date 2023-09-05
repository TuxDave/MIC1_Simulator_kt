package com.tuxdave.mic1_simulator_kt

import com.tuxdave.mic1_simulator_kt.component.*
import com.tuxdave.mic1_simulator_kt.component.legacy.ClockBasedComponent
import com.tuxdave.mic1_simulator_kt.component.legacy.Source

class Controller {
    private val registers: Map<RegNames, Source<Number>> = mapOf(
        Pair(RegNames.MAR, Register32() as Source<Number>),
        Pair(RegNames.MDR, Register32() as Source<Number>),
        Pair(RegNames.PC, Register32() as Source<Number>),
        Pair(RegNames.MBR, Register8() as Source<Number>),
        Pair(RegNames.SP, Register32() as Source<Number>),
        Pair(RegNames.LV, Register32() as Source<Number>),
        Pair(RegNames.CPP, Register32() as Source<Number>),
        Pair(RegNames.TOS, Register32() as Source<Number>),
        Pair(RegNames.OPC, Register32() as Source<Number>),
        Pair(RegNames.H, Register32() as Source<Number>)
    )

    private val alu: ALU = ALU(
        registers.getOrElse(RegNames.H) {Register32()} as Source<Int>,
        registers.filter { it.key in ALU.bRegisterBus }.values.toList()
    )

    private val shifter = Shifter(
        alu
    )

    private val clockCycle: Array<ClockBasedComponent> = arrayOf(alu,shifter)
}