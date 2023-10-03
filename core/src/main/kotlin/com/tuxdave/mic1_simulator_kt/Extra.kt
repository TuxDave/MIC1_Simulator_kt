package com.tuxdave.mic1_simulator_kt

import kotlin.math.pow

/**
 * @return the rightest length'th bits of the number, fill with false at left if empty
 * */
fun UByte.toBooleanArray(length: Int = 6): BooleanArray {
//    return if (this >= 2.0.pow(length.toDouble()).toUInt()) booleanArrayOf()
    var self = this.toInt()
    var ret = mutableListOf<Boolean>()
    var c = 0;
    while (self != 0) {
        ret.add(self % 2 == 1)
        self = self shr 1
        c++
    }
    return if (ret.size < length) {
        (0 until (length - ret.size)).map { false }.toBooleanArray() + ret.reversed()
    }
    else if (ret.size > length) {
        ret.drop(ret.size - length).toBooleanArray()
    }
    else {
        ret.reversed().toBooleanArray()
    }
}

fun BooleanArray.toInt(): Int {
    var ret = 0
    for(i in 1 .. size){
        if(this[size - i]){
            ret += 1 shl i - 1
        }
    }
    return ret
}

fun BooleanArray.toLong(): Long {
    var ret = 0L
    for(i in 1 .. size){
        if(this[size - i]){
            ret += 1 shl i - 1
        }
    }
    return ret
}