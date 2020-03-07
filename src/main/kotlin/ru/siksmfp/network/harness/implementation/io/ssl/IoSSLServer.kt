package ru.siksmfp.network.harness.implementation.io.ssl

import ru.siksmfp.network.harness.api.Handler
import ru.siksmfp.network.harness.api.Server
import ru.siksmfp.network.harness.implementation.SSLUtils
import ru.siksmfp.network.harness.implementation.io.IoServerManager

class IoSSLServer(
        private val port: Int,
        private val threadNumber: Int?
) : Server<String> {
    private lateinit var ioServerManager: IoServerManager

    override fun start() {
        val serverSocket = SSLUtils.constructSSLServerFactory().createServerSocket(port)
        println("Io SSL Server io started on $port")
        ioServerManager = IoServerManager(serverSocket, threadNumber)
        ioServerManager.start()
    }

    override fun stop() {
        println("Stopping io SSL server")
        ioServerManager.close()
    }

    override fun setHandler(handler: Handler<String>) {
        ioServerManager.setHandler(handler)
    }
}