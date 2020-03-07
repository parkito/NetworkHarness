package ru.siksmfp.network.harness.implementation.io

import ru.siksmfp.network.harness.api.Handler
import ru.siksmfp.network.harness.benchmarking.execution.support.createExecutor
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.ExecutorService

class IoServerManager(private val serverSocket: ServerSocket,
                      private val threadNumber: Int?
) {
    private val executor: ExecutorService = createExecutor(threadNumber)
    private var handler: Handler<String>? = null

    fun start() {
        while (true) {
            try {
                val client = serverSocket.accept()
                executor.execute {
                    handleClient(client)
                }
            } catch (ex: IOException) {
                println(ex.message)
                println("Finishing server socket")
                break
            }
        }
    }

    private fun handleClient(client: Socket) {
        println("Client connected " + client.port)
        val printWriter = PrintWriter(client.getOutputStream(), false)
        val bufferedReader = BufferedReader(InputStreamReader(client.getInputStream()))
        printWriter.use {
            bufferedReader.use {
                while (true) {
                    val response = bufferedReader.readLine() ?: break
                    handler?.handle(response)
                    println("IoServer: received $response")

                    printWriter.println("OK")
                    println("IoServer: sending response OK")
                    printWriter.flush()
                }
            }
        }
    }

    fun setHandler(handler: Handler<String>) {
        this.handler = handler
    }

    fun close() {
        executor.shutdown()
        serverSocket.close()
        handler?.close()
    }
}