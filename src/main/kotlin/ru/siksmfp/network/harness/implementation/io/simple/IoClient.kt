package ru.siksmfp.network.harness.implementation.io.simple

import ru.siksmfp.network.harness.api.Client
import ru.siksmfp.network.harness.implementation.io.ClientContext
import java.net.Socket
import java.util.Scanner

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

    fun test() {
        println("Io Start io testing")
        val response = clientContext.sentAndReceive("test")
        if (response == "OK") {
            println("Test passed")
        } else {
            throw IllegalStateException("Sending test is failed")
        }
    }
}

fun main() {
    val client = IoClient("localhost", 8081)
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