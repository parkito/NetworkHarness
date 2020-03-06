package ru.siksmfp.network.harness.implementation

import ru.siksmfp.network.harness.api.Client
import ru.siksmfp.network.harness.implementation.io.simple.IoClient
import ru.siksmfp.network.harness.implementation.io.simple.IoServer
import ru.siksmfp.network.harness.implementation.io.ssl.IoSSLServer
import java.util.Scanner

fun startIoServer() {
    IoServer(8081, 5).start()
}

fun startIoSSLServer() {
    IoSSLServer(8081, 5).start()
}

fun startIoClient() {
    val client = IoClient("localhost", 8081)
    client.start()
    processClientInput(client)
    client.stop()
}

fun startIoSSLClient() {
    val client = IoClient("localhost", 8081)
    client.start()
    processClientInput(client)
    client.stop()
}

fun processClientInput(client: Client<String>) {
    val scanner = Scanner(System.`in`)
    while (scanner.hasNext()) {
        val line = scanner.next()
        if (line == "stop") {
            break
        }
        client.send(line)
    }
}

fun main() {
//    Thread { startIoServer() }.start()
    startIoClient()
}
