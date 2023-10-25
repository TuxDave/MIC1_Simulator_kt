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
            sleep(maxOf(waitTimeSeconds * 1000L, 100))
        }
    }
}