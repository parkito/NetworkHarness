package ru.siksmfp.network.harness.implementation.io.simple

import ru.siksmfp.network.harness.api.Handler
import ru.siksmfp.network.harness.api.Server
import ru.siksmfp.network.harness.implementation.io.ServerContext
import java.net.ServerSocket

class IoServer(
        private val port: Int,
        private val threadNumber: Int?
) : Server<String> {
    private lateinit var serverContext: ServerContext

    override fun start() {
        val serverSocket = ServerSocket(port)
        println("Server io started on $port")
        serverContext = ServerContext(serverSocket, threadNumber)
        serverContext.start()
    }

    override fun stop() {
        println("Io stopping io server")
        serverContext.close()
    }

    override fun setHandler(handler: Handler<String>) {
        serverContext.setHandler(handler)
    }
}
