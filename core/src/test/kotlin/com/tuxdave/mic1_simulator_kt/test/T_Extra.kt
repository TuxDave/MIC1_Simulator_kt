package com.tuxdave.mic1_simulator_kt.test

import com.tuxdave.mic1_simulator_kt.toBooleanArray
import kotlin.test.Test
import kotlin.test.assertContentEquals

class T_Extra {

    @Test
    fun t1(): Unit {
        assertContentEquals(booleanArrayOf(false, false, true, false, true, false), 10u.toUByte().toBooleanArray())
        assertContentEquals(booleanArrayOf(true, true, true, true, true, true), 63u.toUByte().toBooleanArray())
        assertContentEquals(booleanArrayOf(false, false, true, true, true, true, true, true), 63u.toUByte().toBooleanArray(8))
        assertContentEquals(booleanArrayOf(true, true, true, true), 63u.toUByte().toBooleanArray(4))
    }
}
