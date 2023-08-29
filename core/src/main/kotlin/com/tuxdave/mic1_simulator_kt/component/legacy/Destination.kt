package com.tuxdave.mic1_simulator_kt.component.legacy

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