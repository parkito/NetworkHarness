package ru.siksmfp.network.harness.implementation.nio.simple.server

import ru.siksmfp.network.harness.implementation.SSLUtils.constructSSLContext
import tlschannel.ServerTlsChannel
import tlschannel.TlsChannel
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.SelectionKey.OP_READ
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel

class SSLAcceptHandler(
        private val clients: MutableMap<SocketChannel, ByteBuffer>
) : SelectionHandler {

    override fun handle(selectionKey: SelectionKey) {
        println("handle")
        val ssc = selectionKey.channel() as ServerSocketChannel
        val serverChannel = ssc.accept()
        serverChannel.configureBlocking(false)
        val tlsChannel: TlsChannel = ServerTlsChannel.newBuilder(serverChannel, constructSSLContext()).build()
        val key = serverChannel.register(selectionKey.selector(), OP_READ)
        key.attach(tlsChannel)
        println("Client connected $serverChannel")
    }

    override fun close() {
        //no shared state
    }
}