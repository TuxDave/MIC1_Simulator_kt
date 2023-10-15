package com.tuxdave.mic1_simulator_kt.test.component

import com.tuxdave.mic1_simulator_kt.core.component.Register32
import com.tuxdave.mic1_simulator_kt.core.component.Register8
import com.tuxdave.mic1_simulator_kt.core.component.Register8U
import junit.framework.TestCase.assertEquals
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotEquals

class T_Register {

    var r8 = Register8()
    var r32 = Register32()

    @BeforeTest
    fun setUp() {
        r8 = Register8()
        r32 = Register32()
    }

    @Test
    fun t1(){
        r8.setValueInput(13)
        r8.outputEnabled = true
        assertNotEquals(13, r8.getValueOutput())
        r8.inputEnabled = true
        r8.setValueInput(13)
        r8.outputEnabled = true
        assert(13.toByte() == r8.getValueOutput())
        assertEquals(r8.inputEnabled, false)
        assertEquals(null, r8.getValueOutput())
    }

    @Test
    fun t2(){
        val r8 = Register8(10)
        val r8u = Register8U(r8)
        assertEquals(null, r8u.getValueOutput())
        r8u.outputEnabled = true
        assertEquals(10u.toUByte(), r8u.getValueOutput())
        r8.inputEnabled = true
        r8.setValueInput(-10)
        r8u.outputEnabled = true
        assertEquals(246u.toUByte(), r8u.getValueOutput())
    }
}