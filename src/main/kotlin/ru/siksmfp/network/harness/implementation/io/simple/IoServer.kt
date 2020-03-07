package ru.siksmfp.network.harness.implementation.io.simple

import ru.siksmfp.network.harness.api.Handler
import ru.siksmfp.network.harness.api.Server
import ru.siksmfp.network.harness.implementation.io.IoServerManager
import java.net.ServerSocket

class IoServer(
        private val port: Int,
        private val threadNumber: Int?
) : Server<String> {
    private lateinit var ioServerManager: IoServerManager

    override fun start() {
        val serverSocket = ServerSocket(port)
        println("Server io started on $port")
        ioServerManager = IoServerManager(serverSocket, threadNumber)
        ioServerManager.start()
    }

    override fun stop() {
        println("Io stopping io server")
        ioServerManager.close()
    }

    override fun setHandler(handler: Handler<String>) {
        ioServerManager.setHandler(handler)
    }
}
