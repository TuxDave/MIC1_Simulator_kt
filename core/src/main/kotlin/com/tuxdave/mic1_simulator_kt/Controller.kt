package com.tuxdave.mic1_simulator_kt

import com.tuxdave.mic1_simulator_kt.component.*
import com.tuxdave.mic1_simulator_kt.component.legacy.ClockBasedComponent
import com.tuxdave.mic1_simulator_kt.component.legacy.Destination
import com.tuxdave.mic1_simulator_kt.component.legacy.Source
import java.lang.IllegalArgumentException

class Controller: ClockBasedComponent(){
    private var registers: Map<RegNames, Register<Number>> = mapOf(
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

    private val memory4GB: Memory4GB

    private val alu: ALU

    private val shifter: Shifter

    private val cBus: OneToMoreBus

    private lateinit var controlStore: ControlStore
    private var mpc: Int = 0
    private var mir = MicroIstructionRegister(BooleanArray(ControlStore.DATA_LENGTH))

    private val clockCycle: Array<ClockBasedComponent>

    var running = true

    init {
        registers += Pair(RegNames.MBRU, Register8U(registers[RegNames.MBR]!! as Register8) as Register<Number>)

        alu = ALU(
            registers.getOrElse(RegNames.H) {Register32()} as Source<Int>,
            registers.filter { it.key in B_REGISTER_BUS }.values.toList()
        )

        shifter = Shifter(alu)

        cBus = OneToMoreBus(
            from = shifter,
            to = registers.filter { it.key in C_BUS_DESTINATIONS }.values.toList() as List<Destination<Int>>
        )

        memory4GB = Memory4GB(
            registers[RegNames.MAR] as? Register32 ?: throw IllegalArgumentException("MEMORY: MAR non trovato"),
            registers[RegNames.MDR] as? Register32 ?: throw IllegalArgumentException("MEMORY: MDR non trovato"),
            registers[RegNames.MBR] as? Register8 ?: throw IllegalArgumentException("MEMORY: MBR non trovato"),
            registers[RegNames.PC] as? Register32 ?: throw IllegalArgumentException("MEMORY: PC non trovato")
        )

        clockCycle = arrayOf(alu,shifter, cBus, memory4GB)
    }

    private fun dispatch() {
        alu.controlSignal = mir[MirRange.ALU]
        shifter.shiftLeft8 = if (mir[MirRange.SHIFTER].none { it }) null else mir[MirRange.SHIFTER][0]

        run { // enabling the output from 1 b bus register connected
            val b = mir[MirRange.B].toInt()
            val which = RegNames.getFromDecodeUnit(b.toUByte())
            registers[which]?.outputEnabled = true
        }

        run{ // enabling the input on the C receiver registers
            val cs = mir[MirRange.C]
            cs.zip(C_SEQUENCE).forEach{
                if (it.first) {
                    registers[it.second]?.inputEnabled = true
                }
            }
        }
    }

    private fun jump() {
        val jamcnz = mir[MirRange.JAM]
        val nextAddr = mir[MirRange.NEXT_ADDRESS]
        if (jamcnz[0]) { //salto a molte vie
            val mbr: Int = (registers[RegNames.MBR] as? Register8)?.output?.toInt() ?: 0
            for (i in 8 downTo 1) { //dalla posizione più a destra alla penultima a sinistra
                nextAddr[i] = ((mbr shr (8-i)) % 2 == 1) || nextAddr[i]
            }
        } else if (jamcnz[1]) { //salto se ultima operazione avesse risultato negativo
            nextAddr[0] = nextAddr[0] || alu.n
        } else if (jamcnz[2]) { //risultato == 0
            nextAddr[0] = nextAddr[0] || alu.z
        }
        mpc = nextAddr.toInt()
    }

    override fun run() {
        mir.data = controlStore[mpc]
        dispatch()
        clockCycle.forEach { it.run() }
        jump()
    }
}