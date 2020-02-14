package ru.siksmfp.network.play.tcp.io.simple

import ru.siksmfp.network.play.api.Handler
import ru.siksmfp.network.play.api.Server
import ru.siksmfp.network.play.tcp.testing.NamedThreadFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.Executors

class IoServer(
        private val port: Int
) : Server<String> {
    private var handler: Handler<String>? = null
    private val executor = Executors.newFixedThreadPool(10, NamedThreadFactory("server"))
    private lateinit var serverSocket: ServerSocket

    override fun start() {
        serverSocket = ServerSocket(port)
        println("Server started on $port")
        while (true) {
            val client = serverSocket.accept()
            executor.execute {
                handleClient(client)
            }
        }
    }

    @Synchronized
    override fun stop() {
        println("Stopping server")
        executor.shutdown()
        serverSocket.close()
    }

    override fun setHandler(handler: Handler<String>) {
        this.handler = handler
    }

    private fun handleClient(client: Socket) {
        println("Client connected " + client.port)
        var printWriter: PrintWriter
        val bufferedReader = BufferedReader(InputStreamReader(client.getInputStream()))
        bufferedReader.use {
            while (true) {
                val received = bufferedReader.readLine()
                handler?.handle(received)
                println("IoServer: received $received")

                printWriter = PrintWriter(client.getOutputStream(), false)
                printWriter.println("OK")
                println("IoServer: sending response OK")
                printWriter.flush()
            }
        }
    }
}

fun main() {
    IoServer(8081).start()
}