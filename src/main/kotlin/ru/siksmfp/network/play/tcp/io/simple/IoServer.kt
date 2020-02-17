package ru.siksmfp.network.play.tcp.io.simple

import ru.siksmfp.network.play.api.Handler
import ru.siksmfp.network.play.api.Server
import ru.siksmfp.network.play.tcp.testing.NamedThreadFactory
import ru.siksmfp.network.play.tcp.testing.getServerServerThreads
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.Executors

class IoServer(
        private val port: Int
) : Server<String> {
    private var handler: Handler<String>? = null
    private val executor = Executors.newFixedThreadPool(getServerServerThreads(), NamedThreadFactory("server"))
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
                val received = bufferedReader.readLine() ?: break
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
