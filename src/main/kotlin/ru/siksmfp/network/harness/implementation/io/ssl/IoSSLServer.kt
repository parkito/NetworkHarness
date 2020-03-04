package ru.siksmfp.network.harness.implementation.io.ssl

import ru.siksmfp.network.harness.api.Handler
import ru.siksmfp.network.harness.api.Server
import ru.siksmfp.network.harness.benchmarking.execution.support.createExecutor
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.security.KeyStore
import java.util.concurrent.ExecutorService
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory


class IoSSLServer(
        private val port: Int,
        threadNumber: Int?
) : Server<String> {
    private val executor: ExecutorService = createExecutor(threadNumber)
    private var handler: Handler<String>? = null
    private lateinit var serverSocket: ServerSocket

    override fun start() {
        val ks = KeyStore.getInstance("pkcs12")
        ks.load(Thread.currentThread().contextClassLoader.getResourceAsStream("selfsigned.jks"), "11223344".toCharArray())

        val kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, "11223344".toCharArray());

        val tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        val sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null)

        val sslSocketFactory = sslContext.serverSocketFactory

        serverSocket = sslSocketFactory.createServerSocket(port)
        println("Io SSL Server io started on $port")
        while (true) {
            try {
                val client = serverSocket.accept()
                executor.execute {
                    handleClient(client)
                }
            } catch (ex: IOException) {
                println(ex.message)
                println("Finishing server socket")
                break
            }
        }
    }

    override fun stop() {
        println("Stopping io SSL server")
        executor.shutdown()
        serverSocket.close()
        handler?.close()
    }

    override fun setHandler(handler: Handler<String>) {
        this.handler = handler
    }

    private fun handleClient(client: Socket) {
        println("Client connected " + client.port)
        val printWriter = PrintWriter(client.getOutputStream(), false)
        val bufferedReader = BufferedReader(InputStreamReader(client.getInputStream()))
        printWriter.use {
            bufferedReader.use {
                while (true) {
                    val response = bufferedReader.readLine() ?: break
                    handler?.handle(response)
                    println("IoServer: received $response")

                    printWriter.println("OK")
                    println("IoServer: sending response OK")
                    printWriter.flush()
                }
            }
        }
    }
}

fun main() {
    IoSSLServer(8081, 5).start()
}
