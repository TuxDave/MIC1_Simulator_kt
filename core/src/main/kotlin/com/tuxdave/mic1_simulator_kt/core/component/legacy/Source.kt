package com.tuxdave.mic1_simulator_kt.core.component.legacy

import com.tuxdave.mic1_simulator_kt.core.component.Register8U

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
fun List<Source<Number>>.getOutputValue(): Int? {
    val temp = this.firstOrNull { it.outputEnabled }
    return try {
        temp?.getValueOutput()?.toInt()
    } catch (e: ClassCastException) {
        val temp = (temp as? Register8U)
        temp?.outputEnabled = true
        return temp?.getValueOutput()?.toInt()
    }
}