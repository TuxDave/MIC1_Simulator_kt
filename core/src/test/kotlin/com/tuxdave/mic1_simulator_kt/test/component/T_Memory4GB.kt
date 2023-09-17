package com.tuxdave.mic1_simulator_kt.test.component

import com.tuxdave.mic1_simulator_kt.component.Memory4GB
import com.tuxdave.mic1_simulator_kt.component.Register32
import com.tuxdave.mic1_simulator_kt.component.Register8
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.isAccessible
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class T_Memory4GB {

    lateinit var mem: Memory4GB
    lateinit var mar: Register32
    lateinit var mdr: Register32
    lateinit var mbr: Register8
    lateinit var pc: Register32

    @BeforeTest
    fun setup() {
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
        Memory4GB::class.declaredFunctions.first { it.name == "get" }
            .isAccessible = true
        Memory4GB::class.declaredFunctions.first { it.name == "set" }
            .isAccessible = true
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
        Memory4GB::class.declaredFunctions.first { it.name == "get" }
            .isAccessible = true
        Memory4GB::class.declaredFunctions.first { it.name == "set" }
            .isAccessible = true
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
}