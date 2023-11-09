package com.tuxdave.mic1_simulator_kt.simulatorgui

class Runner(
    var waitTimeSeconds: Int,
    var running: Boolean = false,
    private val exec: () -> Unit
): Thread() {
    override fun run() {
        super.run()
        while (running){
            exec()
            try{
                sleep(maxOf(waitTimeSeconds * 1000L, 1))
            } catch (_: Exception) {}
        }
    }
}