package com.tuxdave.mic1_simulator_kt.component
class Memory4GB (
    private val mar: Register32,
    private val mdr: Register32,
    private val mbr: Register8,
    private val pc: Register32
) {
    //TODO: Optimize with another data struct
    private val data: MutableList<Int> = mutableListOf()

    private operator fun get(addr: Int): Int {
        if (addr !in 0..MAX_ADDR) {
            return 0
        }
        if (data.size > addr) {
            return data[addr]
        } else {
            extend(addr + 1)
            return this[addr]
        }
    }
    private operator fun set(addr: Int, value: Int) {
        if (addr < 0) return
        if(addr < data.size) {
            data[addr] = value
        } else {
            extend(addr + 1)
            this[addr] = value
        }
    }

    private fun extend(ln: Int) {
        if (ln > data.size) {
            val n = ln - data.size
            for (it in 0 until n) {
                data.add(0)
            }
        }
    }

    //TODO: Write the methods to write read and fetch

    companion object {
        val MAX_ADDR = Math.pow(2.0, 30.0).toInt()
    }
}