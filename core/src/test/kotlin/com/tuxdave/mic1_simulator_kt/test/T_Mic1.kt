package com.tuxdave.mic1_simulator_kt.test

import com.tuxdave.mic1_simulator_kt.Mic1
import com.tuxdave.mic1_simulator_kt.toInt
import com.tuxdave.mic1_simulator_kt.toLong
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters
import java.io.File
import java.net.URI
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertEquals

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class T_Mic1 {
    @Test
    fun t1_tryLoad(){
        val mic = Mic1()
        val res = mic.loadMicroProgram(this.javaClass.classLoader.getResource("resources/ijvm_std_interpreter.mic1"))
//        val res = mic.loadMicroProgram(File("/home/tuxdave/Programmi/mic1simAA1314/mal/mic1ijvm.mic1").toURI().toURL())
        res?.let{println(res)}
        assert(res == null)
        assertEquals(mic.mic1ControlStoreState[0].sliceArray(0..7).toInt(), 0x01)
        assertEquals(mic.mic1ControlStoreState[1].sliceArray(0..7).toInt(), 0x20)
        assertEquals(mic.mic1ControlStoreState[2].sliceArray(0 .. 7).toInt(), 0x00)
        assertEquals(mic.mic1ControlStoreState[2].sliceArray(8 until 16).toInt(), 0x43)
        assertEquals(mic.mic1ControlStoreState[6].sliceArray(0 .. 7).toInt(), 0x01)
//        assertEquals(mic.mic1ControlStoreState[6].sliceArray(16 until 24).toInt(), 0xF2)

//        assertEquals((mic.mic1ControlStoreState[0x13] + BooleanArray(4)).toLong(), 0x1393502110)
        assertEquals(mic.mic1ControlStoreState[0x13].sliceArray(0..7).toInt(), 0x13)
        assertEquals(mic.mic1ControlStoreState[0x13].sliceArray(8 until 16).toInt(), 0x83)
        assertEquals(mic.mic1ControlStoreState[0x13].sliceArray(16 until 24).toInt(), 0x50)
        assertEquals(mic.mic1ControlStoreState[0x13].sliceArray(24 until 32).toInt(), 0x21)
        assertEquals(mic.mic1ControlStoreState[0x13].sliceArray(32 until 36).toInt(), 0x1)
    }
}