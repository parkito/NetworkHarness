package ru.siksmfp.network.play.tcp.io.simple

import ru.siksmfp.network.play.api.Handler
import ru.siksmfp.network.play.api.Server
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
    private val executor = Executors.newFixedThreadPool(10)

    override fun start() {
        val serverSocket = ServerSocket(port)
        println("Server started on $port")
        while (true) {
            val client = serverSocket.accept()
            executor.submit {
                handleClient(client)
            }
        }
    }

    override fun stop() {
        //todo  implement
    }

    override fun setHandler(handler: Handler<String>) {
        this.handler = handler
    }

    private fun handleClient(client: Socket) {
        println("Client connected " + client.port)
        var printWriter: PrintWriter
        var bufferedReader: BufferedReader
        while (true) {
            bufferedReader = BufferedReader(InputStreamReader(client.getInputStream()))

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

fun main() {
    IoServer(8081).start()
}