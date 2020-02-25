package ru.siksmfp.network.harness.implementation.nio

import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel

@Throws(IOException::class)
fun handle(socket: SocketChannel): String {
    val stringBuilder = StringBuilder()
    val bb = ByteBuffer.allocateDirect(80)
    var read: Int

    do {
        read = socket.read(bb)
        if (read > 0) {
            while (bb.hasRemaining()) {
                socket.write(bb)//todo send ok
            }
        }
    } while (read != -1)

    socket.close()
    return stringBuilder.toString()
}