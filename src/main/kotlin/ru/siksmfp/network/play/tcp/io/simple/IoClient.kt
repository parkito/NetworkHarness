package ru.siksmfp.network.play.tcp.io.simple

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

    fun start() {
        println("Connecting client to $host:$port")
        clientSocket = Socket(host, port)
        printWriter = PrintWriter(clientSocket.getOutputStream(), false)
        bufferedReader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
        println("Connected to $host:$port")
    }

    fun send(line: String) {
        println("Sending $line")
        printWriter.println(line)
        printWriter.flush()
        val response = bufferedReader.readLine()
        println("Publisher: received $response")
    }

    fun test() {
        println("Start testing")
        printWriter.println("test")
        printWriter.flush()
        val response = bufferedReader.readLine()
        if (response == "OK") {
            println("Test passed")
        } else {
            throw IllegalStateException("Sending test is failed")
        }
    }

    fun destroy() {
        bufferedReader.close()
        printWriter.close()
        clientSocket.close()
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