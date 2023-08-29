package com.tuxdave.mic1_simulator_kt.test.component

import com.tuxdave.mic1_simulator_kt.component.*
import com.tuxdave.mic1_simulator_kt.component.legacy.Source
import kotlin.test.Test
import kotlin.test.assertEquals

class T_ALU {
    @Test
    fun t_toInt(){
        var a = booleanArrayOf(false, false, false, false, false, false)
        assertEquals(a.toInt(), 0)
        a[5] = true
        assertEquals(a.toInt(), 1)
        a[4] = true
        assertEquals(a.toInt(), 3)
        a[0] = true
        assertEquals(a.toInt(), 35)
    }

    @Test
    fun t_registers_getValueOutput(){
        val rs: List<Source<Number>> = listOf(Register32(1) as Register<Number>, Register8(128.toByte()) as Register<Number>)
        rs[1].outputEnabled = true
        assertEquals(rs.getOutputValue(),128)
        //TODO: think about this fail
    }
}