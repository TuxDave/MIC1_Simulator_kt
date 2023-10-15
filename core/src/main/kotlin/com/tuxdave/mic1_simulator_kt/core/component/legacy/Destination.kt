package com.tuxdave.mic1_simulator_kt.core.component.legacy

interface Destination<T> {
    var inputEnabled: Boolean
    var input: T?
    fun setValueInput(v: T): Unit {
        if(inputEnabled) {
            inputEnabled = false
            input = v
        }
    }
}