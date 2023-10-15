package com.tuxdave.mic1_simulator_kt.test.component

import com.tuxdave.mic1_simulator_kt.core.component.legacy.Source
import com.tuxdave.mic1_simulator_kt.core.component.legacy.getOutputValue
import com.tuxdave.mic1_simulator_kt.core.component.ALU
import com.tuxdave.mic1_simulator_kt.core.component.Register
import com.tuxdave.mic1_simulator_kt.core.component.Register32
import com.tuxdave.mic1_simulator_kt.core.component.Register8
import com.tuxdave.mic1_simulator_kt.core.toBooleanArray
import com.tuxdave.mic1_simulator_kt.core.toInt
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class T_ALU {

    private lateinit var a: Register32
    private var rs: List<Register<Number>> = listOf()
    private lateinit var alu: ALU

    @BeforeTest
    fun setup(){
        a = Register32(1)
        rs = listOf(Register32(10), Register8(128.toByte())) as? List<Register<Number>> ?: listOf()
        alu = ALU(a, rs as List<Source<Number>>)
    }

    @Test
    fun t_toInt(){
        val a = booleanArrayOf(false, false, false, false, false, false)
        assertEquals(a.toInt(), 0)
        a[5] = true
        assertEquals(a.toInt(), 1)
        a[4] = true
        assertEquals(a.toInt(), 3)
        a[0] = true
        assertEquals(a.toInt(), 35)
    }

    @Test
    fun t_registers_getValueOutput1(){
        rs[1].outputEnabled = true
        assertEquals(rs.getOutputValue(), -128)
    }

    @Test
    fun t_registers_getValueOutput2(){
        rs[0].outputEnabled = true
        assertEquals(rs.getOutputValue(), 10)
    }

    @Test
    fun t_znfalse(){
        a.outputEnabled = true
        rs[0].outputEnabled = true
        val f = 63u.toUByte().toBooleanArray()
        alu.controlSignal = f
        alu.run()
        alu.outputEnabled = true
        assertEquals(alu.output, 9)
        assertEquals(alu.z, false)
        assertEquals(alu.n, false)
    }

    @Test
    fun t_z(){
        a.outputEnabled = true
        rs[0].outputEnabled = true
        val f = 63u.toUByte().toBooleanArray()
        a.value = 10
        rs[0].value = 1
        alu.controlSignal = f
        alu.run()
        alu.outputEnabled = true
        assertEquals(alu.output, -9)
        assertEquals(alu.z, false)
        assertEquals(alu.n, true)
    }

    @Test
    fun t_n(){
        a.outputEnabled = true
        rs[0].outputEnabled = true
        val f = 63u.toUByte().toBooleanArray()
        a.value = 1
        rs[0].value = 1
        alu.controlSignal = f
        alu.run()
        alu.outputEnabled = true
        assertEquals(alu.output, 0)
        assertEquals(alu.z, true)
        assertEquals(alu.n, false)
    }
}