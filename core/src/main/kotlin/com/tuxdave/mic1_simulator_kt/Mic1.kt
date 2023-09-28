package com.tuxdave.mic1_simulator_kt

import com.tuxdave.mic1_simulator_kt.component.*
import com.tuxdave.mic1_simulator_kt.component.legacy.ClockBasedComponent
import com.tuxdave.mic1_simulator_kt.component.legacy.Destination
import com.tuxdave.mic1_simulator_kt.component.legacy.Source
import java.lang.IllegalArgumentException
import kotlin.ClassCastException

/**
 * Mic1, il SOC a forma di emulatore.
 * Non autonomo, necessita che gli venga fornito il clock.
 * Disponibili campi pubblici per la lettura e modifica dei dati.
 * Clock da fornire esternamente chiamando il metodo run() per decidere la velocità di esecuzione
 * */
@Suppress(names = arrayOf("UNCHECKED_CAST", "NAME_SHADOWING"))
class Mic1: ClockBasedComponent(){
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

    // TODO: 2. Creare il metodo di load del file compilato (buona fortuna)

    //Accessibility interface for the manager (probably a UI)
    // TODO: 1. TESTARE

    val mic1State: Mic1StateDTO
        get() = Mic1StateDTO(
            registers.map { Pair(it.key.toString(), it.value.output.toInt()) }.toMap(),
            alu.n,
            alu.z,
            mpc,
            mir.data
        )

    val mic1ControlStoreState: Array<BooleanArray>
        get() = (0 until ControlStore.DATA_LINES).map { controlStore[it] }.toTypedArray()

    fun getMic1MemoryRange(cellsRange: IntRange = 0 until 512): IntArray {
        return cellsRange.map { memory4GB[it] }.toIntArray()
    }

    fun setMemoryValueFromCellNumber(position: Int, value: Int): Boolean {
        return if (value in 0 until Memory4GB.MAX_ADDR) {
            memory4GB[position] = value
            true
        } else false
    }

    fun setRegisterValue(reg: RegNames, value: Int): Boolean {
        var value = value
        if (reg == RegNames.MBRU) {
            return false
        } else if (reg == RegNames.MBR) {
            value = value.toByte().toInt()
        }
        val r = registers[reg] ?: return false
        try {
            (r as Register32).value = value
            return true
        } catch (_: ClassCastException) {}
        return try {
            (r as Register8).value = value.toByte()
            true
        } catch (e: ClassCastException) {
            false
        }
    }
}

data class Mic1StateDTO(
    val registers: Map<String, Int>,
    val aluN: Boolean,
    val aluZ: Boolean,
    val mpc: Int,
    val mir: BooleanArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Mic1StateDTO

        if (registers != other.registers) return false
        if (aluN != other.aluN) return false
        if (aluZ != other.aluZ) return false
        if (mpc != other.mpc) return false
        if (!mir.contentEquals(other.mir)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = registers.hashCode()
        result = 31 * result + aluN.hashCode()
        result = 31 * result + aluZ.hashCode()
        result = 31 * result + mpc
        result = 31 * result + mir.contentHashCode()
        return result
    }
}