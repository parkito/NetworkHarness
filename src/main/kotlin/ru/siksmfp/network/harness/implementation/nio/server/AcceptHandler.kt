package ru.siksmfp.network.harness.implementation.nio.server

import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.SelectionKey.OP_READ
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel

class AcceptHandler(
        private val clients: MutableMap<SocketChannel, ByteBuffer>
) : SelectionHandler {

    override fun handle(selectionKey: SelectionKey) {
        val ssc = selectionKey.channel() as ServerSocketChannel
        val serverChannel = ssc.accept()
        println("Client connected $serverChannel")
        clients.putIfAbsent(serverChannel, ByteBuffer.allocate(10000))
        serverChannel.configureBlocking(false)
        serverChannel.register(selectionKey.selector(), OP_READ)
    }

    override fun close() {
        //no shared state
    }
}