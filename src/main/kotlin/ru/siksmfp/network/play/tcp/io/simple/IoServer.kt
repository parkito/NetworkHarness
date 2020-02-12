package ru.siksmfp.network.play.tcp.io.simple

import ru.siksmfp.network.play.api.Handler
import ru.siksmfp.network.play.api.Server
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

class IoServer(
        private val port: Int
) : Server<String> {
    private val handler: Handler<String>? = null

    override fun start() {
        val serverSocket = ServerSocket(port)
        println("Server started on $port")
        while (true) {
            val client = serverSocket.accept()
            Thread {
                handleClient(client)
            }.start()
        }
    }

    override fun stop() {
        //todo  implement
    }

    override fun setHandler(handler: Handler<String>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

            println("IoServer: sending response OK")
            printWriter = PrintWriter(client.getOutputStream(), false)
            printWriter.println("OK")
            printWriter.flush()
        }
    }
}

fun main() {
    IoServer(8081).start()
}