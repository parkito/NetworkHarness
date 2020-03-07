package ru.siksmfp.network.harness.implementation.nio.simple.server

import ru.siksmfp.network.harness.implementation.nio.ssl.server.NioSSLServerContext
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.SelectionKey.OP_READ
import java.nio.channels.SocketChannel

class SSLWriteHandler(
        private val clients: MutableSet<SocketChannel>
) : SelectionHandler {

    override fun handle(selectionKey: SelectionKey) {
        val context = selectionKey.attachment() as NioSSLServerContext
        val written = context.tlsChannel.write(ByteBuffer.wrap("OK".toByteArray()))
        println("NioServer: sending response OK")
        if (written == -1) {
            context.tlsChannel.close()
            println("Disconnected from in write")
            return
        }
        selectionKey.interestOps(OP_READ)
    }

    override fun close() {
        //no shared state
    }
}