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
class Register8U(private val register8: Register8): Register<UByte>(){
    override var value: UByte = 0u
    override var output: UByte
        get() {
            return register8.output.toUByte()
        }
        set(value) {}
}

enum class RegNames(private val decodeIndex: UByte?) {
    MAR(null),
    MDR(0u),
    PC(1u),
    MBR(2u),
    MBRU(3u),
    SP(4u),
    LV(5u),
    CPP(6u),
    TOS(7u),
    OPC(8u),
    H(null);

    companion object {
        fun getFromDecodeUnit(i: UByte): RegNames? {
            return RegNames.entries.find {it.decodeIndex == i}
        }
    }
}

val C_SEQUENCE = arrayOf(
    RegNames.H,
    RegNames.OPC,
    RegNames.TOS,
    RegNames.CPP,
    RegNames.LV,
    RegNames.SP,
    RegNames.PC,
    RegNames.MDR,
    RegNames.MAR
)