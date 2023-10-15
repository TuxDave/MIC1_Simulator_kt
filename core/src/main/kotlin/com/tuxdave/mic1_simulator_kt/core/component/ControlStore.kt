package com.tuxdave.mic1_simulator_kt.core.component

class ControlStore {
    companion object {
        const val DATA_LENGTH: Int = 36
        const val DATA_LINES: Int = 512
    }

    private val data = Array(DATA_LINES) { BooleanArray(DATA_LENGTH) }

    operator fun get(line: Int): BooleanArray {
        return if (line in 0 until DATA_LINES) {
            data[line]
        } else BooleanArray(DATA_LENGTH)
    }

    operator fun set(line: Int, value: BooleanArray): Unit {
        if (line in 0 until DATA_LINES) {
            data[line] = value
        }
    }

    operator fun get(line: Int, index: Int): Boolean {
        return if (index in 0 until DATA_LENGTH) {
            this[line][index]
        } else false
    }

    operator fun set(line: Int, index: Int, value: Boolean) {
        if (index in 0 until DATA_LENGTH) {
            this[line][index] = value
        }
    }

    fun reset() {
        for (i in 0 until DATA_LINES) {
            this[i] = BooleanArray(DATA_LENGTH)
        }
    }
}



/**
 * @return: A boolean array with a true for each number != 0
 * */
fun BooleanArray.fromIntArray(intArray: IntArray) = intArray.map { it != 0 }.toBooleanArray()

/**
 * @return: A boolean array with a true for each number != 0 (char != 48)
 * */
fun BooleanArray.fromIntString(intString: String) = intString.map { it.code != 48 }.toBooleanArray()