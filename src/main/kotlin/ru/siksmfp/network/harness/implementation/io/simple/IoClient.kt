package ru.siksmfp.network.harness.implementation.io.simple

import ru.siksmfp.network.harness.api.Client
import ru.siksmfp.network.harness.implementation.io.IoClientContext
import java.net.Socket

open class IoClient(
        private val host: String,
        private val port: Int
) : Client<String> {
    private lateinit var ioClientContext: IoClientContext

    override fun start() {
        println("Connecting io client to $host:$port")
        ioClientContext = IoClientContext(Socket(host, port))
        println("Connected to $host:$port")
    }

    @Synchronized
    override fun send(message: String) {
        println("Sending $message")
        val response = ioClientContext.sentAndReceive(message)
        println("Client received a response $response")
    }

    override fun stop() {
        println("Stopping io client")
        ioClientContext.close()
    }

    override fun test() {
        println("Io Start io testing")
        ioClientContext.test()
    }
}