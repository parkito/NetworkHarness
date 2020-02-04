package ru.siksmfp.network.play.tcp.io

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.util.*

class IoClient(
        private val host: String,
        private val port: Int
) {
    private lateinit var printWriter: PrintWriter
    private lateinit var bufferedReader: BufferedReader
    private lateinit var clientSocket: Socket

    fun publish(message: String) {
        printWriter.println(message)

        val response = bufferedReader.readLine()
        println("Publisher: received $response")

        bufferedReader.close()
        printWriter.close()
        clientSocket.close()
    }

    fun start() {
        println("Connecting client to $host:$port")
        printWriter = PrintWriter(clientSocket.getOutputStream(), true)
        bufferedReader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
        clientSocket = Socket(host, port)
        println("Connected to $host:$port")
    }

    fun send(line: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun test() {


    }

    fun destroy() {

    }
}

fun main() {
    val client = IoClient("localhost", 8081)
    client.start()
    client.test()

    val scanner = Scanner(System.`in`)

    while (scanner.hasNext()) {
        val line = scanner.next()
        if (line == "stop") {
            break
        }
        client.send(line)
    }

    client.destroy()
}