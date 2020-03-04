package ru.siksmfp.network.harness.implementation.io.simple

import ru.siksmfp.network.harness.api.Handler
import ru.siksmfp.network.harness.api.Server
import ru.siksmfp.network.harness.benchmarking.execution.support.createExecutor
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.ExecutorService

class IoServer(
        private val port: Int,
        threadNumber: Int?
) : Server<String> {
    private val executor: ExecutorService = createExecutor(threadNumber)
    private var handler: Handler<String>? = null
    private lateinit var serverSocket: ServerSocket

    override fun start() {
        serverSocket = ServerSocket(port)
        println("Server io started on $port")
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

    override fun stop() {
        println("Io stopping io server")
        executor.shutdown()
        serverSocket.close()
        handler?.close()
    }

    override fun setHandler(handler: Handler<String>) {
        this.handler = handler
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
}
