package ru.siksmfp.network.harness.implementation.nio

import ru.siksmfp.network.harness.api.Client
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.util.*


class NioClient(
        private val host: String,
        private val port: Int
) : Client<String> {

    private var client: SocketChannel? = null

    override fun start() {
        client = SocketChannel.open(InetSocketAddress(host, port))
    }

    @Synchronized
    override fun send(message: String) {
        client!!.write(ByteBuffer.wrap(message.toByteArray()))
        val bb = ByteBuffer.allocate(2)
        val read = client!!.read(bb)
        val response = byteBufferToString(bb, read)
        println("Client received a response $response")
    }

    fun test() {
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
        client!!.close()
    }
}

fun main() {
    val client = NioClient("localhost", 8081)
    client.start()
    client.test()

    val scanner = Scanner(System.`in`)

    while (scanner.hasNext()) {
        val line = scanner.next()
        if (line == "stop") {
            break
        }
        client.send(line)
    }

    client.stop()
}