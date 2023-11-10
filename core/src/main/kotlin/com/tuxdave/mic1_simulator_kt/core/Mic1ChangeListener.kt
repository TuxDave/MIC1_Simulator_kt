package com.tuxdave.mic1_simulator_kt.core

import com.tuxdave.mic1_simulator_kt.core.component.RegNames

interface Mic1ChangeListener {
    fun regChanged(reg: RegNames)
    fun memChanged(addr: Int)
}