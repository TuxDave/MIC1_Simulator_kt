package com.tuxdave.mic1_simulator_kt.simulatorgui.project

interface ProjectListener {
    fun mic1ProjectChanged(proj: Mic1Project?){
        anyProjectChanged(proj)
    }
    fun ijvmProjectChanged(proj: Any?){
        anyProjectChanged(proj)
    }
    fun anyProjectChanged(proj: Any?){

    }
}