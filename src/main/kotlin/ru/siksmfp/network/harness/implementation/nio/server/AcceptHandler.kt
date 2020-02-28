package ru.siksmfp.network.harness.implementation.nio.server

import java.nio.channels.SelectionKey
import java.nio.channels.SelectionKey.OP_READ
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel

class AcceptHandler(
        private val pendingData: MutableSet<SocketChannel>
) : SelectionHandler {

    override fun handle(selectionKey: SelectionKey) {
        val ssc = selectionKey.channel() as ServerSocketChannel
        val ss = ssc.accept()
        println("Client connected $ss")
        pendingData.add(ss)
        ss.configureBlocking(false)
        ss.register(selectionKey.selector(), OP_READ)
    }
}