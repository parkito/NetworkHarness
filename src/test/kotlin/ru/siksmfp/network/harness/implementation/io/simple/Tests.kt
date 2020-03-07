package ru.siksmfp.network.harness.implementation.io.simple

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.siksmfp.network.harness.api.Client
import ru.siksmfp.network.harness.implementation.io.ssl.IoSSLClient
import ru.siksmfp.network.harness.implementation.io.ssl.IoSSLServer
import ru.siksmfp.network.harness.implementation.nio.simple.NioClient
import ru.siksmfp.network.harness.implementation.nio.simple.NioServer
import ru.siksmfp.network.harness.implementation.nio.ssl.NioSSLClient
import ru.siksmfp.network.harness.implementation.nio.ssl.NioSSLServer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Tests {
    private lateinit var executorService: ExecutorService

    @BeforeEach
    fun before() {
        executorService = Executors.newFixedThreadPool(1)
    }

    @AfterEach
    fun after() {
        executorService.shutdown()
    }

    @Test
    fun testIo() {
        val server = IoServer(8081, 5)
        executorService.execute { server.start() }
        Thread.sleep(1000)
        testClient(IoClient("localhost", 8081))
        server.stop()
        println("Io successfully tested")
    }

    @Test
    fun testIoSSL() {
        val server = IoSSLServer(8081, 5)
        executorService.execute { server.start() }
        Thread.sleep(1000)
        testClient(IoSSLClient("localhost", 8081))
        server.stop()
        println("Io SSL successfully tested")
    }

    @Test
    fun testNio() {
        val server = NioServer(8081, 5)
        executorService.execute { server.start() }
        Thread.sleep(1000)
        testClient(NioClient("localhost", 8081))
        server.stop()
        Thread.sleep(1000)
        println("Nio successfully tested")
    }

        @Test
    fun testNioSSL() {
        val server = NioSSLServer(8081, 5)
        executorService.execute { server.start() }
        Thread.sleep(1000)
        testClient(NioSSLClient("localhost", 8081))
        server.stop()
        println("Nio SSL successfully tested")
    }

    private fun testClient(client: Client<String>) {
        client.start()
        client.test()
        client.stop()
    }
}