package ru.siksmfp.network.harness.implementation.io.ssl

import ru.siksmfp.network.harness.api.Handler
import ru.siksmfp.network.harness.api.Server
import ru.siksmfp.network.harness.implementation.SSLUtils
import ru.siksmfp.network.harness.implementation.io.IoServerContext

class IoSSLServer(
        private val port: Int,
        private val threadNumber: Int?
) : Server<String> {
    private lateinit var ioServerContext: IoServerContext

    override fun start() {
        val serverSocket = SSLUtils.constructSSLServerFactory().createServerSocket(port)
        println("Io SSL Server io started on $port")
        ioServerContext = IoServerContext(serverSocket, threadNumber)
        ioServerContext.start()
    }

    override fun stop() {
        println("Stopping io SSL server")
        ioServerContext.close()
    }

    override fun setHandler(handler: Handler<String>) {
        ioServerContext.setHandler(handler)
    }
}