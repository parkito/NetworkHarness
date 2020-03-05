package ru.siksmfp.network.harness.implementation.io.simple

import org.junit.jupiter.api.Test
import ru.siksmfp.network.harness.api.Client
import ru.siksmfp.network.harness.implementation.io.ssl.IoSSLClient
import ru.siksmfp.network.harness.implementation.io.ssl.IoSSLServer

class IoTests {

    @Test
    fun testIo() {
        val server = IoServer(8081, 5)
        Thread { server.start() }.start()
        testClient(IoClient("localhost", 8081))
        server.stop()
        println("IO successfully tested")
    }

    @Test
    fun testIoSSL() {
        val server = IoSSLServer(8081, 5)
        Thread { server.start() }.start()
        testClient(IoSSLClient("localhost", 8081))
        server.stop()
        println("IO SSL successfully tested")
    }

    private fun testClient(client: Client<String>) {
        client.start()
        client.test()
        client.stop()
    }
}