package ru.siksmfp.network.play.tcp.io.simple

import ru.siksmfp.network.play.api.Client
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.util.*

class IoClient(
        private val host: String,
        private val port: Int
) : Client<String> {
    private lateinit var printWriter: PrintWriter
    private lateinit var bufferedReader: BufferedReader
    private lateinit var clientSocket: Socket

    override fun start() {
        println("Connecting client to $host:$port")
        clientSocket = Socket(host, port)
        printWriter = PrintWriter(clientSocket.getOutputStream(), false)
        bufferedReader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
        println("Connected to $host:$port")
    }

    @Synchronized
    override fun send(message: String) {
        println("Sending $message")
        printWriter.println(message)
        printWriter.flush()
        val response = bufferedReader.readLine()
        println("Publisher: received $response")
    }

    override fun stop() {
        println("Stopping client")
        bufferedReader.close()
        printWriter.close()
        clientSocket.close()
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