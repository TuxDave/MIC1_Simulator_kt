package com.tuxdave.mic1_simulator_kt.test.component

import com.tuxdave.mic1_simulator_kt.component.Register32
import com.tuxdave.mic1_simulator_kt.component.Register8
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
}