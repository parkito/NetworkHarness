package ru.siksmfp.network.play.tcp.io

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

class IoServer(
        private val port: Int
) {
    private lateinit var clientSocket: Socket
    private lateinit var printWriter: PrintWriter
    private lateinit var bufferedReader: BufferedReader
    private val serverSocket: ServerSocket = ServerSocket(port)

    fun start() {
        println("Server started on $port")
        while (true) {
            clientSocket = serverSocket.accept()
            bufferedReader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))

            val received = bufferedReader.readLine()
            println("IoServer: received $received")

            println("IoServer: sending response OK")
            printWriter = PrintWriter(clientSocket.getOutputStream(), true)
            printWriter.println("OK")
        }
    }
}

fun main() {
    IoServer(8081).start()
}