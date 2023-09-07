package com.tuxdave.mic1_simulator_kt

import com.tuxdave.mic1_simulator_kt.component.*
import com.tuxdave.mic1_simulator_kt.component.legacy.ClockBasedComponent
import com.tuxdave.mic1_simulator_kt.component.legacy.Destination
import com.tuxdave.mic1_simulator_kt.component.legacy.Source

class Controller {
    private val registers: Map<RegNames, Register<Number>> = mapOf(
        Pair(RegNames.MAR, Register32() as Register<Number>),
        Pair(RegNames.MDR, Register32() as Register<Number>),
        Pair(RegNames.PC, Register32() as Register<Number>),
        Pair(RegNames.MBR, Register8() as Register<Number>),
        Pair(RegNames.SP, Register32() as Register<Number>),
        Pair(RegNames.LV, Register32() as Register<Number>),
        Pair(RegNames.CPP, Register32() as Register<Number>),
        Pair(RegNames.TOS, Register32() as Register<Number>),
        Pair(RegNames.OPC, Register32() as Register<Number>),
        Pair(RegNames.H, Register32() as Register<Number>)
    )

    private val alu: ALU = ALU(
        registers.getOrElse(RegNames.H) {Register32()} as Source<Int>,
        registers.filter { it.key in B_REGISTER_BUS }.values.toList()
    )

    private val shifter = Shifter(
        alu
    )

    private val cBus = OneToMoreBus(
        from = shifter,
        to = registers.filter { it.key in C_BUS_DESTINATIONS }.values.toList() as List<Destination<Int>>
    )

    //TODO: write memory and dispatcher (with MIR and MPC composition for jumps), than add here
    private val clockCycle: Array<ClockBasedComponent> = arrayOf(alu,shifter, cBus)
}