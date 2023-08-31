package com.tuxdave.mic1_simulator_kt.component

import com.tuxdave.mic1_simulator_kt.component.legacy.ClockBasedComponent
import com.tuxdave.mic1_simulator_kt.component.legacy.Source
import com.tuxdave.mic1_simulator_kt.component.legacy.getOutputValue

class ALU(
    private var a: Register32,
    private var b: List<Register<Number>>
): ClockBasedComponent(), Source<Int> {
    private var c: Int = 0
    private var constrolSignal: BooleanArray = (0 .. 5).map { true }.toBooleanArray()

    override fun run() {
        val av = a.getValueOutput()?.toInt()
        val bv = b.getOutputValue() as Int?
        c = when (constrolSignal.toInt()) {
            24 -> av
            20 -> bv
            26 -> av?.inv()
            44 -> bv?.inv()
            60 -> av?.plus(bv?:0)
            61 -> av?.plus(bv?:0)?.plus(1)
            57 -> av?.plus(1)
            53 -> bv?.plus(1)
            63 -> bv?.minus(av?:0)
            54 -> bv?.minus(1)
            59 -> av?.unaryMinus()
            12 -> av?.and(bv?: 1)
            28 -> av?.or(bv?:0)
            16 -> 0 // already in ELSE
            49 -> 1
            50 -> -1
            else -> 0
        } ?: 0
        outputEnabled = true
    }

    override var outputEnabled: Boolean = false
    override var output: Int
        get() = c
        set(value) {}
}

fun BooleanArray.toInt(): Int {
    var ret = 0
    for(i in 1 .. size){
        if(this[size - i]){
            ret += 1 shl i - 1
        }
    }
    return ret
}