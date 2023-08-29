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