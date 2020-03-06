package ru.siksmfp.network.harness.implementation.io.simple

import ru.siksmfp.network.harness.api.Handler
import ru.siksmfp.network.harness.api.Server
import ru.siksmfp.network.harness.implementation.io.IoServerContext
import java.net.ServerSocket

class IoServer(
        private val port: Int,
        private val threadNumber: Int?
) : Server<String> {
    private lateinit var ioServerContext: IoServerContext

    override fun start() {
        val serverSocket = ServerSocket(port)
        println("Server io started on $port")
        ioServerContext = IoServerContext(serverSocket, threadNumber)
        ioServerContext.start()
    }

    override fun stop() {
        println("Io stopping io server")
        ioServerContext.close()
    }

    override fun setHandler(handler: Handler<String>) {
        ioServerContext.setHandler(handler)
    }
}
