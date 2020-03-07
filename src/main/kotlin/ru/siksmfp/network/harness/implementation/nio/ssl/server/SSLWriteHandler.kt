package ru.siksmfp.network.harness.implementation.nio.simple.server

import tlschannel.TlsChannel
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.SelectionKey.OP_READ
import java.nio.channels.SocketChannel

class SSLWriteHandler(
        private val clients: MutableSet<SocketChannel>
) : SelectionHandler {

    override fun handle(selectionKey: SelectionKey) {
        val tlsChannel = selectionKey.attachment() as TlsChannel

        val written = tlsChannel.write(ByteBuffer.wrap("OK".toByteArray()))
        println("NioServer: sending response OK")
        if (written == -1) {
            tlsChannel.close()
            println("Disconnected from in write")
            return
        }
        selectionKey.interestOps(OP_READ)
    }

    override fun close() {
        //no shared state
    }
}