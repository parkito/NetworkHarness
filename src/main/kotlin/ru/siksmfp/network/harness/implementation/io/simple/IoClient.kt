package ru.siksmfp.network.harness.implementation.io.simple

import ru.siksmfp.network.harness.api.Client
import ru.siksmfp.network.harness.implementation.io.ClientContext
import java.net.Socket

open class IoClient(
        private val host: String,
        private val port: Int
) : Client<String> {
    private lateinit var clientContext: ClientContext

    override fun start() {
        println("Connecting io client to $host:$port")
        clientContext = ClientContext(Socket(host, port))
        println("Connected to $host:$port")
    }

    @Synchronized
    override fun send(message: String) {
        println("Sending $message")
        val response = clientContext.sentAndReceive(message)
        println("Client received a response $response")
    }

    override fun stop() {
        println("Stopping io client")
        clientContext.close()
    }

    override fun test() {
        println("Io Start io testing")
        clientContext.test()
    }
}