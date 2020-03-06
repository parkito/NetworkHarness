package ru.siksmfp.network.harness.implementation.nio.simple.server

import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.SelectionKey.OP_READ
import java.nio.channels.SocketChannel

class WriteHandler(
        private val clients: MutableMap<SocketChannel, ByteBuffer>
) : SelectionHandler {

    override fun handle(selectionKey: SelectionKey) {
        val socketChannel = selectionKey.channel() as SocketChannel
        val written = socketChannel.write(ByteBuffer.wrap("OK".toByteArray()))
        println("NioServer: sending response OK")
        if (written == -1) {
            socketChannel.close()
            clients.remove(socketChannel)
            println("Disconnected from in write $socketChannel")
            return
        }
        selectionKey.interestOps(OP_READ)
    }

    override fun close() {
        //no shared state
    }
}