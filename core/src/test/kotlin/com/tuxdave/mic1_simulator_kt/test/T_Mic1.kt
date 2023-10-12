package com.tuxdave.mic1_simulator_kt.test

import com.tuxdave.mic1_simulator_kt.Mic1
import com.tuxdave.mic1_simulator_kt.component.RegNames
import com.tuxdave.mic1_simulator_kt.toInt
import com.tuxdave.mic1_simulator_kt.toLong
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters
import java.io.File
import java.net.URI
import java.net.URL
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class T_Mic1 {
    lateinit var mic: Mic1

    @BeforeTest
    fun setup(){
        mic = Mic1()
    }
    @Test
    fun t0_tryLoad(){
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

    @Test
    fun t1_Registerassigning(){
        val res = mic.loadMicroProgram(this
            .javaClass
            .classLoader
            .getResource("resources/mal_examples/ex1.mic1"))
        res?.let{println(res)}
        assert(res == null)
        repeat(3){
            mic.run()
        }
        assertEquals(mic.mic1State.registers["PC"], 1)
    }

    @Test
    fun t2_RegisterEditing(){
        val res = mic.loadMicroProgram(this
            .javaClass
            .classLoader
            .getResource("resources/mal_examples/ex2.mic1"))
        res?.let{println(res)}
        assert(res == null)
        repeat(10){
            mic.run()
        }
        assertEquals(mic.mic1State.registers["PC"], 1)
        assertEquals(mic.mic1State.registers["CPP"], 1)
        assertEquals(mic.mic1State.registers["LV"], 3)
    }

    @Test
    fun t3_RAM_WRITE(){
        val res = mic.loadMicroProgram(this
            .javaClass
            .classLoader
            .getResource("resources/mal_examples/ex3.mic1"))
        res?.let{println(res)}
        assert(res == null)
        mic.setRegisterValue(RegNames.TOS, 0x8000)
        mic.setRegisterValue(RegNames.PC, 0x5)
        repeat(100){
            mic.run()
        }
        assertContentEquals(mic.getMic1MemoryRange(0x8000..0x8004), IntArray(5) {0x8000 + it})
    }

    @Test
    fun t4_RAM_READ(){
        val res = mic.loadMicroProgram(this
            .javaClass
            .classLoader
            .getResource("resources/mal_examples/ex4.mic1"))
        res?.let{println(res)}
        assert(res == null)
        mic.setRegisterValue(RegNames.TOS, 0x100)
        mic.setMemoryValueFromCellNumber(0x100, 1234)
        repeat(10){
            mic.run()
        }
        assertEquals(mic.mic1State.registers["TOS"], 1234)
    }

    @Test
    fun t5_RAM_FETCH(){
        val res = mic.loadMicroProgram(this
            .javaClass
            .classLoader
            .getResource("resources/mal_examples/ex5.mic1"))
        res?.let{println(res)}
        assert(res == null)
        mic.setRegisterValue(RegNames.TOS, 0x100*4 + 1)
        mic.setMemoryValueFromCellNumber(0x100, 0x00EE0000)
        repeat(10){
            mic.run()
        }
        assertEquals(mic.mic1State.registers["TOS"], 0xEE.toUByte().toInt())
        assertEquals(mic.mic1State.registers["OPC"], 0xEE.toByte().toInt())
    }
}