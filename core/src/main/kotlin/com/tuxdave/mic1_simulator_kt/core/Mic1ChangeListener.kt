package com.tuxdave.mic1_simulator_kt.core

import com.tuxdave.mic1_simulator_kt.core.component.RegNames

interface Mic1ChangeListener {
    /**called when the edit is done before running anything*/
    fun regChanged(reg: RegNames)

    /**called when the edit is done before running anything*/
    fun memChanged(addr: Int)
}