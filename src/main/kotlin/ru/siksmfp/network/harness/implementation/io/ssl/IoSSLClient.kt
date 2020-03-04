package ru.siksmfp.network.harness.implementation.io.ssl

import ru.siksmfp.network.harness.api.Client
import ru.siksmfp.network.harness.implementation.io.ClientContext
import java.util.Scanner

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

    fun test() {
        println("Start io testing")
        val response = clientContext.sentAndReceive("test")
        if (response == "OK") {
            println("Test passed")
        } else {
            throw IllegalStateException("Sending test is failed")
        }
    }
}

fun main() {
    val client = IoSSLClient("localhost", 8081)
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