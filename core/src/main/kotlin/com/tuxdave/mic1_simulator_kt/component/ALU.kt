package com.tuxdave.mic1_simulator_kt.component

import com.tuxdave.mic1_simulator_kt.component.legacy.ClockBasedComponent
import com.tuxdave.mic1_simulator_kt.component.legacy.Source
import com.tuxdave.mic1_simulator_kt.component.legacy.getOutputValue
import com.tuxdave.mic1_simulator_kt.toInt

class ALU(
    private var a: Source<Int>,
    private var b: List<Source<Number>>
) : ClockBasedComponent(), Source<Int> {
    private var c: Int = 0
    var controlSignal: BooleanArray = (0..5).map { true }.toBooleanArray()
    var n = false
        private set
    var z = false
        private set

    override fun run() {
        val av = a.getValueOutput()
        val bv = b.getOutputValue() as Int?
        c = when (controlSignal.toInt()) {
            24 -> av
            20 -> bv
            26 -> av?.inv()
            44 -> bv?.inv()
            60 -> av?.plus(bv ?: 0)
            61 -> av?.plus(bv ?: 0)?.plus(1)
            57 -> av?.plus(1)
            53 -> bv?.plus(1)
            63 -> bv?.minus(av ?: 0)
            54 -> bv?.minus(1)
            59 -> av?.unaryMinus()
            12 -> av?.and(bv ?: 1)
            28 -> av?.or(bv ?: 0)
            16 -> 0 // already in ELSE
            49, 17 -> 1
            50, 18 -> -1
            else -> 0
        } ?: 0
        n = c < 0
        z = c == 0
        outputEnabled = true
    }

    override var outputEnabled: Boolean = false
    override var output: Int
        get() = c
        set(value) {}
}

val B_REGISTER_BUS: Array<RegNames> = arrayOf(
    RegNames.MDR,
    RegNames.PC,
    RegNames.MBR,
    RegNames.SP,
    RegNames.LV,
    RegNames.CPP,
    RegNames.TOS,
    RegNames.OPC
)