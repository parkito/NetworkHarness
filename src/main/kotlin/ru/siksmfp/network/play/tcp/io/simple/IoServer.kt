package ru.siksmfp.network.play.tcp.io.simple

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

class IoServer(
        private val port: Int
) {
    fun start() {
        val serverSocket = ServerSocket(port)
        println("Server started on $port")
        while (true) {
            val client = serverSocket.accept()
            Thread {
                handleClient(client)
            }.start()
        }
    }

    private fun handleClient(client: Socket) {
        println("Client connected " + client.port)
        var printWriter: PrintWriter
        var bufferedReader: BufferedReader
        while (true) {
            bufferedReader = BufferedReader(InputStreamReader(client.getInputStream()))

            val received = bufferedReader.readLine()
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