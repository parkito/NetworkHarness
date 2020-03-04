package ru.siksmfp.network.harness.implementation.io.ssl

import ru.siksmfp.network.harness.api.Handler
import ru.siksmfp.network.harness.api.Server
import ru.siksmfp.network.harness.implementation.io.ServerContext

class IoSSLServer(
        private val port: Int,
        private val threadNumber: Int?
) : Server<String> {
    private lateinit var serverContext: ServerContext

    override fun start() {
        val serverSocket = IoSSLUtils.constructSSLServerFactory().createServerSocket(port)
        println("Io SSL Server io started on $port")
        serverContext = ServerContext(serverSocket, threadNumber)
        serverContext.start()
    }

    override fun stop() {
        println("Stopping io SSL server")
        serverContext.close()
    }

    override fun setHandler(handler: Handler<String>) {
        serverContext.setHandler(handler)
    }
}