package com.tuxdave.mic1_simulator_kt.component

import java.lang.IllegalArgumentException

enum class MirRange private constructor(val bitRange: IntRange) {
    NEXT_ADDRESS(0 until 9),
    JAM(9 until 12),
    ALU(12 until 20),
    C(20 until 29),
    MEM(29 until 32),
    B(32 until 36)
}

class MicroIstructionRegister(data: BooleanArray) {
    var data = BooleanArray(36)
        set(value) {
            if (value.size == 36){
                field = value
            } else throw IllegalArgumentException("MIC1's data format is ${ControlStore.DATA_LENGTH} bits.")
        }

    operator fun get(r: MirRange): BooleanArray {
        return data.sliceArray(r.bitRange)
    }
}