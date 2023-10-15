package com.tuxdave.mic1_simulator_kt.test.component

import com.tuxdave.mic1_simulator_kt.core.component.*
import com.tuxdave.mic1_simulator_kt.core.component.legacy.Destination
import com.tuxdave.mic1_simulator_kt.core.component.legacy.Source
import org.junit.Assert.assertNotEquals
import kotlin.test.Test
import kotlin.test.assertEquals

class T_OneToMoreBus {
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
        Pair(RegNames.H, Register32(10) as Register<Number>)
    )

    @Test
    fun t1() {
        val oneToMoreBus = OneToMoreBus(
            from = registers.getOrElse(RegNames.H){ Register32(0) } as Source<Int>,
            to = registers.filter { it.key in B_REGISTER_BUS }.values.toList() as List<Destination<Int>>
        )
        registers.filter { it.key in listOf(RegNames.SP, RegNames.LV) }.values.forEach { it.inputEnabled = true }
        oneToMoreBus.run()
        registers[RegNames.H]?.outputEnabled = true
        registers[RegNames.LV]?.outputEnabled = true
        registers[RegNames.SP]?.outputEnabled = true
        registers[RegNames.CPP]?.outputEnabled = true
        assertEquals(registers[RegNames.H]!!.output, registers[RegNames.LV]!!.output)
        registers[RegNames.H]?.outputEnabled = true
        assertEquals(registers[RegNames.H]!!.output, registers[RegNames.SP]!!.output)
        registers[RegNames.H]?.outputEnabled = true
        assertNotEquals(registers[RegNames.H]!!.output, registers[RegNames.CPP]!!.output)
    }
}
