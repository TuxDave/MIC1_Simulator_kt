package com.tuxdave.mic1_simulator_kt

fun UByte.toBooleanArray(): BooleanArray {
    return if(this >= 64u) booleanArrayOf()
    else {
        var self = this.toInt()
        var ret = mutableListOf<Boolean>()
        var c = 0;
        while (self != 0) {
            ret.add(self % 2 == 1)
            self = self shr 1
            c++
        }
        if (ret.size < 6) {
            return (0 until (6 - ret.size)).map { false }.toBooleanArray() + ret.reversed()
        }
        ret.reversed().toBooleanArray()
    }
}