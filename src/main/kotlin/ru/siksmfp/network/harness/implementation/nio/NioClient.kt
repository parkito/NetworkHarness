package ru.siksmfp.network.harness.implementation.nio

import ru.siksmfp.network.harness.api.Client
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel

class NioClient(
        private val host: String,
        private val port: Int
) : Client<String> {

    private var client: SocketChannel? = null

    override fun start() {
        println("Connecting nio client to $host:$port")
        client = SocketChannel.open(InetSocketAddress(host, port))
    }

    @Synchronized
    override fun send(message: String) {
        client!!.write(ByteBuffer.wrap(message.toByteArray()))
        val bb = ByteBuffer.allocate(2)
        val read = client!!.read(bb)
        if (read > 0) {
            val response = byteBufferToString(bb, read)
            println("Client received a response $response")
        }
    }

    override fun test() {
        client!!.write(ByteBuffer.wrap("test".toByteArray()))
        val bb = ByteBuffer.allocate(2)
        val read = client!!.read(bb)
        val response = byteBufferToString(bb, read)
        if (response == "OK") {
            println("Test passed")
        } else {
            throw IllegalStateException("Sending test is failed")
        }
    }

    override fun stop() {
        println("Stopping nio client")
        client!!.close()
    }
}

fun main() {

}