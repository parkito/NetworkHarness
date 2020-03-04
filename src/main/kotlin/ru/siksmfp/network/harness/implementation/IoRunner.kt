package ru.siksmfp.network.harness.implementation

import ru.siksmfp.network.harness.api.Client
import ru.siksmfp.network.harness.implementation.io.simple.IoClient
import ru.siksmfp.network.harness.implementation.io.simple.IoServer
import ru.siksmfp.network.harness.implementation.io.ssl.IoSSLClient
import ru.siksmfp.network.harness.implementation.io.ssl.IoSSLServer
import java.util.Scanner

fun startIoServer() {
    IoServer(8081, 5).start()
}

fun startIoClient() {
    val client = IoClient("localhost", 8081)
    processClientInput(client)
    client.stop()
}

fun startIoSSLClient() {
    val client = IoClient("localhost", 8081)
    processClientInput(client)
    client.stop()
}

val scanner = Scanner(System.`in`)

fun startIoSSLServer() {
    IoSSLServer(8081, 5).start()
}

fun testIo() {
    val server = IoServer(8081, 5)
    testClient(IoClient("localhost", 8081))
    server.stop()
    println("IO successfully tested")
}

fun testIoSSL() {
    val server = IoSSLServer(8081, 5)
    testClient(IoSSLClient("localhost", 8081))
    server.stop()
    println("IO SSL successfully tested")
}

private fun processClientInput(client: Client<String>) {
    while (scanner.hasNext()) {
        val line = scanner.next()
        if (line == "stop") {
            break
        }
        client.send(line)
    }
}

private fun testClient(client: Client<String>) {
    client.start()
    client.test()
    client.stop()
}
