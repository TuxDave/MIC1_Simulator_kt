package com.tuxdave.mic1_simulator_kt.component

import com.tuxdave.mic1_simulator_kt.component.legacy.Destination
import com.tuxdave.mic1_simulator_kt.component.legacy.Source

abstract class Register<T: Any> : Source<T>, Destination<T> {
    abstract var value: T

    override var outputEnabled: Boolean = false
    override var inputEnabled: Boolean = false

    override var output: T
        get() = value
        set(value) {}
    override var input: T? = null
        set(_n) {
            if (_n != null) {
                value = _n
            }
        }
}

class Register32(override var value: Int = 0) : Register<Int>()
class Register8(override var value: Byte = 0) : Register<Byte>()

enum class RegNames {
    MAR,
    MDR,
    PC,
    MBR,
    SP,
    LV,
    CPP,
    TOS,
    OPC,
    H
}