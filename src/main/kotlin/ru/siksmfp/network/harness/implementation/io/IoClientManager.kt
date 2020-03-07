package ru.siksmfp.network.harness.implementation.io

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class IoClientManager(private val clientSocket: Socket) {
    private var printWriter: PrintWriter = PrintWriter(clientSocket.getOutputStream(), false)
    private var bufferedReader: BufferedReader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))

    fun sentAndReceive(message: String): String {
        printWriter.println(message)
        printWriter.flush()
        return bufferedReader.readLine()
    }

    fun test() {
        val response = sentAndReceive("test")
        if (response == "OK") {
            println("Test passed")
        } else {
            throw IllegalStateException("Sending test is failed")
        }
    }

    fun close() {
        bufferedReader.close()
        printWriter.close()
        clientSocket.close()
    }
}

