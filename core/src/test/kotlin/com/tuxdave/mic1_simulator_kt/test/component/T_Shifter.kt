package com.tuxdave.mic1_simulator_kt.test.component

import com.tuxdave.mic1_simulator_kt.component.Register32
import com.tuxdave.mic1_simulator_kt.component.Shifter
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class T_Shifter {

    private lateinit var fakeAlu: Register32

    private lateinit var shifter: Shifter
    @BeforeTest
    fun setup(): Unit {
        fakeAlu = Register32(2)
        shifter = Shifter(fakeAlu)
    }

    @Test
    fun t1(){
        shifter.shiftLeft8 = false
        shifter.run()
        shifter.outputEnabled = true
        assertEquals(shifter.output, 1)
    }

    @Test
    fun t2() {
        shifter.shiftLeft8 = true
        shifter.run()
        shifter.outputEnabled = true
        assertEquals(shifter.output, 512)
    }

    @Test
    fun t3() {
        shifter.shiftLeft8 = null
        shifter.run()
        shifter.outputEnabled = true
        assertEquals(shifter.output, 2)
    }
}