package com.tuxdave.mic1_simulator_kt.test.component

import com.tuxdave.mic1_simulator_kt.component.ControlStore
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class T_ControlStore {
    @Test
    fun t1(){
        val c = ControlStore()
        assertEquals(c[0, 2], false)
        c[0, 2] = true
        assertEquals(c[0, 2], true)
        c[1] = BooleanArray(ControlStore.DATA_LENGTH) {true}
        assertEquals(c[1][3], true)
    }

    @Test
    fun t2() {
        val c = ControlStore()
        assertEquals(c[600, 100], false)
        c[600, 100] = true
        assertNotEquals(c[600, 100], true)
    }
}