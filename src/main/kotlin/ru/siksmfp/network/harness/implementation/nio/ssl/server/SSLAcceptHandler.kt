package ru.siksmfp.network.harness.implementation.nio.simple.server

import ru.siksmfp.network.harness.implementation.SSLUtils.constructSSLContext
import ru.siksmfp.network.harness.implementation.nio.ssl.server.NioSSLServerContext
import tlschannel.ServerTlsChannel
import tlschannel.TlsChannel
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.SelectionKey.OP_READ
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel

class SSLAcceptHandler(
        private val clients: MutableSet<SocketChannel>
) : SelectionHandler {

    override fun handle(selectionKey: SelectionKey) {
        val ssc = selectionKey.channel() as ServerSocketChannel
        val serverChannel = ssc.accept()
        clients.add(serverChannel)
        serverChannel.configureBlocking(false)
        val tlsChannel: TlsChannel = ServerTlsChannel.newBuilder(serverChannel, constructSSLContext()).build()
        val context = NioSSLServerContext(tlsChannel, ByteBuffer.allocate(10000))
        val key = serverChannel.register(selectionKey.selector(), OP_READ)
        key.attach(context)
        println("Client connected $serverChannel")
    }

    override fun close() {
        //no shared state
    }
}