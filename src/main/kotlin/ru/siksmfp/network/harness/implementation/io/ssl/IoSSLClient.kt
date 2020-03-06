package ru.siksmfp.network.harness.implementation.io.ssl

import ru.siksmfp.network.harness.api.Client
import ru.siksmfp.network.harness.implementation.SSLUtils
import ru.siksmfp.network.harness.implementation.io.IoClientContext

class IoSSLClient(
        private val host: String,
        private val port: Int
) : Client<String> {
    private lateinit var ioClientContext: IoClientContext

    override fun start() {
        println("Connecting io client to $host:$port")
        val clientSocket = SSLUtils.constructSSLClientFactory().createSocket(host, port)
        ioClientContext = IoClientContext(clientSocket)
        println("Connected to $host:$port")
    }

    @Synchronized
    override fun send(message: String) {
        println("Sending $message")
        val response = ioClientContext.sentAndReceive(message)
        println("Client received a response $response")
    }

    override fun stop() {
        println("Stopping io SSL client")
        ioClientContext.close()

    }

    override fun test() {
        println("Start io SSL testing")
        ioClientContext.test()
    }
}