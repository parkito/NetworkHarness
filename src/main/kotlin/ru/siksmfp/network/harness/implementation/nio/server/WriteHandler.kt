package ru.siksmfp.network.harness.implementation.nio.server

import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.SelectionKey.OP_READ
import java.nio.channels.SocketChannel

class WriteHandler(
        private val pendingData: MutableSet<SocketChannel>
) : SelectionHandler {

    override fun handle(selectionKey: SelectionKey) {
        val sc = selectionKey.channel() as SocketChannel
        val written = sc.write(ByteBuffer.wrap("OK".toByteArray()))
        println("NioServer: sending response OK")
        if (written == -1) {
            sc.close()
            pendingData.remove(sc)
            println("Disconnected from in write $sc")
            return
        }
        selectionKey.interestOps(OP_READ)
    }
}