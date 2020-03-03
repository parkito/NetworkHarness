package ru.siksmfp.network.harness.implementation.nio

import java.nio.ByteBuffer

fun byteBufferToString(bb: ByteBuffer, size: Int): String {
    bb.flip()
    val arr = ByteArray(size)
    bb[arr]
    bb.clear()
    return String(arr)
}