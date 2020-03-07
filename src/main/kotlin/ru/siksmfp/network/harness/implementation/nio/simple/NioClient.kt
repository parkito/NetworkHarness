package ru.siksmfp.network.harness.implementation.nio.simple

import ru.siksmfp.network.harness.api.Client
import ru.siksmfp.network.harness.implementation.nio.NioClientManager
import java.net.InetSocketAddress
import java.nio.channels.SocketChannel

class NioClient(
        private val host: String,
        private val port: Int
) : Client<String> {
    private lateinit var clientManager: NioClientManager

    override fun start() {
        println("Connecting nio client to $host:$port")
        clientManager = NioClientManager(SocketChannel.open(InetSocketAddress(host, port)))
    }

    @Synchronized
    override fun send(message: String) {
        println("Nio sending $message")
        val response = clientManager.sentAndReceive(message)
        println("Client nio received a response $response")
    }

    override fun test() {
        clientManager.test()
    }

    override fun stop() {
        println("Stopping nio client")
        clientManager.close()
    }
}