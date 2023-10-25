package com.tuxdave.mic1_simulator_kt.simulatorgui.projects

interface ProjectListener {
    fun mic1ProjectChanged(proj: Mic1Project?)
    fun ijvmProjectChanged()
}