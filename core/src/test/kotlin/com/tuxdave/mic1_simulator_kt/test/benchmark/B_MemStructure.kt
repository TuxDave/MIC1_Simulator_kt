package com.tuxdave.mic1_simulator_kt.test.benchmark

import com.tuxdave.mic1_simulator_kt.component.OptimizedRamStruct4GB
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


var hm: HashMap<Int, Int> = hashMapOf<Int, Int>()
var om: OptimizedRamStruct4GB = OptimizedRamStruct4GB()
const val to =   100000000
const val step = 100000

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class B_MemStructure {

    @Test
    fun b1_HashMap_1_write(){
        for (i in 0..to step step) {
            hm[i] = i
        }
    }

    @Test
    fun b1_HashMap_2_read(){
        for (i in 0..to step step) {
            assertEquals(i, hm[i])
        }
    }

    @Test
    fun b1_OptimizedMap_1_write(){
        for (i in 0..to step step) {
            om[i] = i
        }
    }

    @Test
    fun b1_OptimizedMap_2_read(){
        for (i in 0..to step step) {
            assertEquals(i, om[i])
        }
    }
}