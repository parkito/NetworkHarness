package ru.siksmfp.network.harness.implementation.io.ssl

import ru.siksmfp.network.harness.api.Client
import ru.siksmfp.network.harness.implementation.io.ClientContext

class IoSSLClient(
        private val host: String,
        private val port: Int
) : Client<String> {
    private lateinit var clientContext: ClientContext

    override fun start() {
        println("Connecting io client to $host:$port")
        val clientSocket = IoSSLUtils.constructSSLClientFactory().createSocket(host, port)
        clientContext = ClientContext(clientSocket)
        println("Connected to $host:$port")
    }

    @Synchronized
    override fun send(message: String) {
        println("Sending $message")
        val response = clientContext.sentAndReceive(message)
        println("Client received a response $response")
    }

    override fun stop() {
        println("Stopping io SSL client")
        clientContext.close()

    }

    override fun test() {
        println("Start io SSL testing")
        clientContext.test()
    }
}