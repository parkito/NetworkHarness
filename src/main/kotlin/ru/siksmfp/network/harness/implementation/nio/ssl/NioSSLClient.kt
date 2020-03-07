package ru.siksmfp.network.harness.implementation.nio.ssl

import ru.siksmfp.network.harness.api.Client
import ru.siksmfp.network.harness.implementation.SSLUtils.constructSSLContext
import ru.siksmfp.network.harness.implementation.nio.NioClientManager
import tlschannel.ClientTlsChannel
import java.net.InetSocketAddress
import java.nio.channels.SocketChannel

class NioSSLClient(
        private val host: String,
        private val port: Int
) : Client<String> {
    private lateinit var clientManager: NioClientManager

    override fun start() {
        println("Connecting nio ssl client to $host:$port")
        val client = SocketChannel.open(InetSocketAddress(host, port))
        val tlsClient = ClientTlsChannel.newBuilder(client, constructSSLContext()).build()
        clientManager = NioClientManager(tlsClient)
        println("Connected")
    }

    @Synchronized
    override fun send(message: String) {
        println("Nio ssl sending $message")
        val response = clientManager.sentAndReceive(message)
        println("Client nio ssl received a response $response")
    }

    override fun test() {
        clientManager.test()
    }

    override fun stop() {
        println("Stopping nio ssl client")
        clientManager
    }
}
