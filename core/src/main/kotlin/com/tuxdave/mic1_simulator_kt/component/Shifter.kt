package com.tuxdave.mic1_simulator_kt.component

import com.tuxdave.mic1_simulator_kt.component.legacy.ClockBasedComponent
import com.tuxdave.mic1_simulator_kt.component.legacy.Source

/**
 * for each clock if shiftLeft assigned will shift left or right, else it will be automatically null and will not shift
 */
class Shifter(val alu: Source<Int>): ClockBasedComponent(), Source<Int> {
    private var value: Int = 0
    /*
    * true  = SHL8
    * false = SHR1
    * */
    var shiftLeft8: Boolean? = null
    override fun run() {
        alu.outputEnabled = true
        if (shiftLeft8 != null) {
            value = if (shiftLeft8!!) alu.output shl 8 else alu.output shr 1
            shiftLeft8 = null
        }
    }

    override var outputEnabled: Boolean = false
    override var output: Int
        get() = value
        set(value) {}
}