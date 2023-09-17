package com.tuxdave.mic1_simulator_kt.component

import com.tuxdave.mic1_simulator_kt.component.legacy.ClockBasedComponent

class Memory4GB (
    private val mar: Register32,
    private val mdr: Register32,
    private val mbr: Register8,
    private val pc: Register32
): ClockBasedComponent() {
    //TODO: Optimize with another data struct
    private val data: MutableList<Int> = mutableListOf()
    private var queue = mutableListOf<Pair<Int, Transition>>()
    var wrfSignal: BooleanArray = Array(3){ _ -> false}.toBooleanArray()
        set(value) {
            if (value.size == 3 && value.count {it} in 0..1) {
                field = value
            }
        }

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

    companion object {
        val MAX_ADDR = Math.pow(2.0, 30.0).toInt()
    }

    private enum class Transition {
        READ,
        WRITE,
        FETCH
    }

    override fun run() {
        if (wrfSignal.count { it } == 1) {
            queue.add(if(wrfSignal[0]) {
                Pair(0, Transition.WRITE)
            } else if (wrfSignal[1]) {
                Pair(0, Transition.READ)
            } else Pair(0, Transition.FETCH))
            wrfSignal = BooleanArray(3) {false}
        }

        queue.filter { it.first == 1 }.forEach {
            when (it.second) {
                Transition.WRITE -> {
                    this[mar.output] = mdr.output
                }
                Transition.READ -> {
                    mdr.inputEnabled = true
                    mdr.setValueInput(this[mar.output])
                }
                Transition.FETCH -> { //01234567 -> 0=01, 1=23, 2=45, 3=67 .... <- that endian
                    val wordAddr = pc.output
                    mbr.inputEnabled = true
                    mbr.setValueInput((this[wordAddr shr 2] shr ((3 - (wordAddr % 4)) * 8)).toByte())
                }
            }
        }
        queue.removeAll(queue.filter { it.first == 1 })
        queue.replaceAll { Pair(1, it.second) }
    }
}