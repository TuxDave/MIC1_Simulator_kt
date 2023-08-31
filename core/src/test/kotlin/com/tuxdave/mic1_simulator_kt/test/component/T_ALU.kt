package com.tuxdave.mic1_simulator_kt.test.component

import com.tuxdave.mic1_simulator_kt.component.Register
import com.tuxdave.mic1_simulator_kt.component.Register32
import com.tuxdave.mic1_simulator_kt.component.Register8
import com.tuxdave.mic1_simulator_kt.component.legacy.Source
import com.tuxdave.mic1_simulator_kt.component.legacy.getOutputValue
import com.tuxdave.mic1_simulator_kt.component.toInt
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class T_ALU {

    private var rs: List<Register<Number>> = listOf()

    @BeforeTest
    fun setup(){
        rs = listOf(Register32(1), Register8(128.toByte())) as? List<Register<Number>> ?: listOf()
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
        assertEquals(rs.getOutputValue(), 1)
    }
}