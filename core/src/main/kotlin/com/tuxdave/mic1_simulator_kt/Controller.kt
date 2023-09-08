package com.tuxdave.mic1_simulator_kt

import com.tuxdave.mic1_simulator_kt.component.*
import com.tuxdave.mic1_simulator_kt.component.legacy.ClockBasedComponent
import com.tuxdave.mic1_simulator_kt.component.legacy.Destination
import com.tuxdave.mic1_simulator_kt.component.legacy.Source

class Controller: ClockBasedComponent(){
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

    private lateinit var controlStore: ControlStore
    private var mpc = 0
    private var mir = MicroIstructionRegister(BooleanArray(ControlStore.DATA_LENGTH))

    //TODO: write memory and dispatcher (with MIR and MPC composition for jumps), than add here
    private val clockCycle: Array<ClockBasedComponent> = arrayOf(alu,shifter, cBus)

    private fun dispatch() {
        alu.controlSignal = mir[MirRange.ALU]
        shifter.shiftLeft8 = if (mir[MirRange.SHIFTER].none { it }) null else mir[MirRange.SHIFTER][0]

        run {
            val b = mir[MirRange.B].toInt()
            val which = RegNames.getFromDecodeUnit(b.toUByte())
            //TODO: Capire come gestire il fatto di MBRU | TESTAREEEE
        }
    }

    override fun run() {
        mir.data = controlStore[mpc]
        dispatch()
        //FETCH: dispatch the current control store microistruction throught the components
        //EXECUTE: doing clockCycle
        //WRITEBACK TODO: Manage the memory writes
        //JUMP: compute the MPC from the nextAddr, JAM and NZ
        //LOOP
    }
}