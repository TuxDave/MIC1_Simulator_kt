package com.tuxdave.mic1_simulator_kt.component

import com.tuxdave.mic1_simulator_kt.component.legacy.ClockBasedComponent
import kotlin.math.pow

class Memory4GB(
    private val mar: Register32,
    private val mdr: Register32,
    private val mbr: Register8,
    private val pc: Register32
) : ClockBasedComponent() {
    private val data = OptimizedRamStruct4GB()
    private var queue = mutableListOf<Pair<Int, Transition>>()
    var wrfSignal: BooleanArray = Array(3) { _ -> false }.toBooleanArray()
        set(value) {
            if (value.size == 3 && value.count { it } in 0..1) {
                field = value
            }
        }

    private operator fun get(addr: Int): Int  = data[addr]

    private operator fun set(addr: Int, value: Int) {
        data[addr] = value
    }

    companion object {
        val MAX_ADDR = 2.0.pow(30.0).toInt()
    }

    private enum class Transition {
        READ,
        WRITE,
        FETCH
    }

    override fun run() {
        if (wrfSignal.count { it } == 1) {
            queue.add(
                if (wrfSignal[0]) {
                    Pair(0, Transition.WRITE)
                } else if (wrfSignal[1]) {
                    Pair(0, Transition.READ)
                } else Pair(0, Transition.FETCH)
            )
            wrfSignal = BooleanArray(3) { false }
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

/**
 * A more stable implementation of a HashMap
 *
 * This is about quick equals if it is strongly full or empty, opposite than a HashMap so slowly when almost full (1G of keys)
 */
class OptimizedRamStruct4GB {
    private val data: HashMap<UShort, HashMap<UShort, HashMap<UByte, HashMap<UByte, Int>>>> = hashMapOf()

    operator fun get(addr: Int): Int {
        val addrs: Addr
        try {
            addrs = Addr(addr)
        } catch (e: IllegalArgumentException) {
            return 0
        }
        return data[addrs.addrFirst]?.let { a ->
            a[addrs.addrSecond]?.let { b ->
                b[addrs.addrThird]?.let { c ->
                    c[addrs.addr]
                }
            }
        } ?: let {
            this[addr] = 0
            this[addr]
        }
    }
    @Suppress("NAME_SHADOWING")
    operator fun set(addr: Int, value: Int) {
        val addrs: Addr
        try {
            addrs = Addr(addr)
        } catch (e: IllegalArgumentException) {
            return
        }
        if (addrs.addrFirst in data.keys) {
            val data = data[addrs.addrFirst]!!
            if (addrs.addrSecond in data.keys) {
                val data = data[addrs.addrSecond]!!
                if (addrs.addrThird in data.keys) {
                    data[addrs.addrThird]!![addrs.addr] = value
                } else {
                    data[addrs.addrThird] = hashMapOf()
                    this[addr] = value
                }
            } else {
                data[addrs.addrSecond] = hashMapOf()
                this[addr] = value
            }
        } else {
            data[addrs.addrFirst] = hashMapOf()
            this[addr] = value
        }
    }

    class Addr(addr: Int) {
        val addrFirst: UShort
        val addrSecond: UShort
        val addrThird: UByte
        val addr: UByte

        init {
            if (addr !in 0 until Memory4GB.MAX_ADDR) {
                throw IllegalArgumentException()
            } else {
                addrFirst = ((addr shr 20) % 1024).toUShort()    //which MEGA
                addrSecond = ((addr shr 10) % 1024).toUShort()   //which KILO
                addrThird = ((addr shr 7) % 8).toUByte()         //which 128-group
                this.addr = (addr % 128).toUByte()               //which cell of a 128-group
            }
        }
    }
}