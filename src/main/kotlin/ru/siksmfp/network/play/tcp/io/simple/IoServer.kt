package ru.siksmfp.network.play.tcp.io.simple

import ru.siksmfp.network.play.api.Handler
import ru.siksmfp.network.play.api.Server
import ru.siksmfp.network.play.tcp.testing.support.NamedThreadFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class IoServer(
        private val port: Int,
        private val threadNumber: Int?
) : Server<String> {
    private val executor: ExecutorService = if (threadNumber != null) {
        Executors.newFixedThreadPool(threadNumber, NamedThreadFactory("server"))
    } else {
        Executors.newCachedThreadPool()
    }

    private var handler: Handler<String>? = null
    private lateinit var serverSocket: ServerSocket

    override fun start() {
        serverSocket = ServerSocket(port)
        println("Server started on $port")
        while (true) {
            try {
                val client = serverSocket.accept()
                executor.execute {
                    handleClient(client)
                }
            } catch (ex: IOException) {
                println("Finishing server socket")
                break
            }
        }
    }

    override fun stop() {
        println("Stopping server")
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
                    val received = bufferedReader.readLine() ?: break
                    handler?.handle(received)
                    println("IoServer: received $received")

                    printWriter.println("OK")
                    println("IoServer: sending response OK")
                    printWriter.flush()
                }
            }
        }
    }
}

fun main() {
    IoServer(8081, 5).start()
}
