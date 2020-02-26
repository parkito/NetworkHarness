package ru.siksmfp.network.harness.implementation.nio.server

import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.SelectionKey.OP_READ
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.*

class AcceptHandler(
        private val pendingData: MutableMap<SocketChannel, Queue<ByteBuffer>>
) : SelectionHandler {

    override fun handle(selectionKey: SelectionKey) {
        val ssc = selectionKey.channel() as ServerSocketChannel
        val ss = ssc.accept()
        println("Client connected $ss")
        pendingData[ss] = ArrayDeque()
        ss.configureBlocking(false)
        ss.register(selectionKey.selector(), OP_READ)
    }
}