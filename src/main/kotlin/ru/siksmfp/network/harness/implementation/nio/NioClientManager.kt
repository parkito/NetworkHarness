package ru.siksmfp.network.harness.implementation.nio

import ru.siksmfp.network.harness.implementation.nio.simple.byteBufferToString
import java.nio.ByteBuffer
import java.nio.channels.ByteChannel

class NioClientManager(private val client: ByteChannel) {

    fun sentAndReceive(message: String): String? {
        client.write(ByteBuffer.wrap(message.toByteArray()))
        val bb = ByteBuffer.allocate(2)
        val read = client.read(bb)
        if (read > 0) {
            return byteBufferToString(bb, read)
        }
        return null
    }

    fun test() {
        client.write(ByteBuffer.wrap("test".toByteArray()))
        val bb = ByteBuffer.allocate(2)
        val read = client.read(bb)
        val response = byteBufferToString(bb, read)
        if (response == "OK") {
            println("Test passed")
        } else {
            throw IllegalStateException("Sending test is failed")
        }
    }

    fun close() {
        client.close()
    }
}