package com.tuxdave.mic1_simulator_kt

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class T_Extra {

    @Test
    fun t1(): Unit {
        assertContentEquals(booleanArrayOf(false, false, true, false, true, false), 10u.toUByte().toBooleanArray())
        assertContentEquals(booleanArrayOf(true, true, true, true, true, true), 63u.toUByte().toBooleanArray())
    }
}
