package com.tuxdave.mic1_simulator_kt.component.legacy

interface Source<T> {
    var outputEnabled: Boolean
    var output: T
    fun getValueOutput(): T? {
        return if (outputEnabled) {
            outputEnabled = false
            output
        } else null
    }
}

/**
 * @return only the value of the first register enabled
 */
fun List<Source<Number>>.getOutputValue(): Number? = this.firstOrNull { it.outputEnabled }?.getValueOutput()?.toInt()