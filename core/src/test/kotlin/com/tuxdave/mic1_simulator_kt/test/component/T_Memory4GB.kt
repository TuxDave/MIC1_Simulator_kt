package com.tuxdave.mic1_simulator_kt.test.component

import com.tuxdave.mic1_simulator_kt.core.component.Memory4GB
import com.tuxdave.mic1_simulator_kt.core.component.Register32
import com.tuxdave.mic1_simulator_kt.core.component.Register8
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.isAccessible
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class T_Memory4GB {

    lateinit var mem: Memory4GB
    lateinit var mar: Register32
    lateinit var mdr: Register32
    lateinit var mbr: Register8
    lateinit var pc: Register32

    @BeforeTest
    fun setup() {
        Memory4GB::class.declaredFunctions.first { it.name == "get" }
            .isAccessible = true
        Memory4GB::class.declaredFunctions.first { it.name == "set" }
            .isAccessible = true

        pc = Register32()
        mar = Register32()
        mdr = Register32()
        mbr = Register8()

        mem = Memory4GB(
            mar = mar,
            mbr = mbr,
            pc = pc,
            mdr = mdr
        )
    }

    @Test
    fun t1(){
        assertEquals(
            Memory4GB::class.declaredFunctions
                .first { it.name == "get" }
                .call(mem, 0) as Int,
            0
        )
        Memory4GB::class.declaredFunctions
            .first { it.name == "set" }
            .call(mem, 0, 10)
        assertEquals(
            Memory4GB::class.declaredFunctions
                .first { it.name == "get" }
                .call(mem, 0) as Int,
            10
        )
        Memory4GB::class.declaredFunctions
            .first { it.name == "set" }
            .call(mem, 9, 9)
        assertEquals(
            Memory4GB::class.declaredFunctions
                .first { it.name == "get" }
                .call(mem, 9) as Int,
            9
        )
        assertEquals(
            Memory4GB::class.declaredFunctions
                .first { it.name == "get" }
                .call(mem, 5) as Int,
            0
        )
    }

    @Test
    fun t2() {
        assertEquals(
            Memory4GB::class.declaredFunctions
                .first { it.name == "get" }
                .call(mem, -1) as Int,
            0
        )
        Memory4GB::class.declaredFunctions
            .first { it.name == "set" }
            .call(mem, -1, 10)
        assertEquals(
            Memory4GB::class.declaredFunctions
                .first { it.name == "get" }
                .call(mem, -1) as Int,
            0
        )
    }

    @Test
    fun t3_write() {
        mar.inputEnabled = true
        mar.setValueInput(0)
        mdr.inputEnabled = true
        mdr.setValueInput(1000)
        mem.wrfSignal = booleanArrayOf(true, false, false)
        assertEquals(
            Memory4GB::class.declaredFunctions
                .first { it.name == "get" }
                .call(mem, 0) as Int,
            0
        )
        mem.run()
        assertEquals(
            Memory4GB::class.declaredFunctions
                .first { it.name == "get" }
                .call(mem, 0) as Int,
            0
        )
        mem.run()
        assertEquals(
            Memory4GB::class.declaredFunctions
                .first { it.name == "get" }
                .call(mem, 0) as Int,
            1000
        )
    }

    @Test
    fun t4_read() {
        val ADDR = 9
        mar.inputEnabled = true
        mar.setValueInput(ADDR)
        mem.wrfSignal = booleanArrayOf(false, true, false)

        Memory4GB::class.declaredFunctions
            .first { it.name == "set" }
            .call(mem, ADDR, 9)
        mem.wrfSignal = booleanArrayOf(false, true, false)
        mem.run()
        assertEquals(mdr.output, 0)
        mem.run()
        assertEquals(mdr.output, 9)
    }

    @Test
    fun t5_fetch() {
        pc.inputEnabled = true
        pc.setValueInput(9)
        mem.wrfSignal = booleanArrayOf(false, false, true)

        Memory4GB::class.declaredFunctions
            .first { it.name == "set" }
            .call(mem, 2, 0x05b75487)
        mem.run()
        assertEquals(mbr.output, 0)
        mem.run()
        assert(mbr.output  == 0xb7.toByte())
    }

    @Test
    fun t6_write_differito() {
        mar.inputEnabled = true
        mar.setValueInput(0)
        mdr.inputEnabled = true
        mdr.setValueInput(1000)
        mem.wrfSignal = booleanArrayOf(true, false, false)
        assertEquals(
            Memory4GB::class.declaredFunctions
                .first { it.name == "get" }
                .call(mem, 0) as Int,
            0
        )

        mem.run()
        assertEquals(
            Memory4GB::class.declaredFunctions
                .first { it.name == "get" }
                .call(mem, 0) as Int,
            0
        )

        //modifico il valore del registro prima che la scrittura venga effettivamente effettuata
        mdr.inputEnabled = true
        mdr.setValueInput(500)

        mem.run()
        assertEquals(
            Memory4GB::class.declaredFunctions
                .first { it.name == "get" }
                .call(mem, 0) as Int,
            1000
        )
    }
}