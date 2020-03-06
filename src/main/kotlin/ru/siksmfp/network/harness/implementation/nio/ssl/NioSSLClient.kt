package ru.siksmfp.network.harness.implementation.nio.ssl

import ru.siksmfp.network.harness.api.Client
import ru.siksmfp.network.harness.implementation.SSLUtils.constructSSLContext
import ru.siksmfp.network.harness.implementation.nio.simple.byteBufferToString
import tlschannel.ClientTlsChannel
import tlschannel.TlsChannel
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel

class NioSSLClient(
        private val host: String,
        private val port: Int
) : Client<String> {

    private var client: SocketChannel? = null
    private var tlsClient: TlsChannel? = null

    override fun start() {
        println("Connecting nio client to $host:$port")
        client = SocketChannel.open()
        client!!.connect(InetSocketAddress(host, port))
        tlsClient = ClientTlsChannel.newBuilder(client, constructSSLContext()).build()
        println("Connected")
    }

    @Synchronized
    override fun send(message: String) {
        tlsClient!!.write(ByteBuffer.wrap(message.toByteArray()))
        val bb = ByteBuffer.allocate(2)
        val read = tlsClient!!.read(bb)
        if (read > 0) {
            val response = byteBufferToString(bb, read)
            println("Client received a response $response")
        }
    }

    override fun test() {
        println("Testing")
        tlsClient!!.write(ByteBuffer.wrap("test".toByteArray()))
        println("Sent")
        val bb = ByteBuffer.allocate(2)
        val read = tlsClient!!.read(bb)
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
    val client = NioSSLClient("localhost", 8081)
    client.start()
    client.test()
}