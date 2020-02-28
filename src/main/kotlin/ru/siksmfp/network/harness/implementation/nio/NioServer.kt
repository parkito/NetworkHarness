package ru.siksmfp.network.harness.implementation.nio

import ru.siksmfp.network.harness.api.Handler
import ru.siksmfp.network.harness.api.Server
import ru.siksmfp.network.harness.implementation.nio.server.AcceptHandler
import ru.siksmfp.network.harness.implementation.nio.server.ReadHandler
import ru.siksmfp.network.harness.implementation.nio.server.WriteHandler
import java.net.InetSocketAddress
import java.nio.channels.SelectionKey.OP_ACCEPT
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque


class NioServer(
        private val port: Int
) : Server<String> {

    private var serverChannel: ServerSocketChannel? = null

    private val clients = Collections.newSetFromMap<SocketChannel>(ConcurrentHashMap())
    private val selectorActions: Queue<Runnable> = ConcurrentLinkedDeque()

    private val acceptHandler = AcceptHandler(clients)
    private val readHandler = ReadHandler(clients, selectorActions)
    private val writeHandler = WriteHandler(clients)

    override fun start() {
        serverChannel = ServerSocketChannel.open()
        serverChannel!!.bind(InetSocketAddress(port))
        serverChannel!!.configureBlocking(false)
        val selector = Selector.open()
        serverChannel!!.register(selector, OP_ACCEPT)

        println("Server started on $port")

        while (true) {
            selector.select()
            processSelectorAction(selectorActions)
            val keys = selector.selectedKeys()
            val it = keys.iterator()
            while (it.hasNext()) {
                val key = it.next()
                it.remove()
                if (key.isValid) {
                    when {
                        key.isAcceptable -> {
                            acceptHandler.handle(key)
                        }
                        key.isReadable -> {
                            readHandler.handle(key)
                        }
                        key.isWritable -> {
                            writeHandler.handle(key)
                        }
                    }
                }
            }
        }
    }

    private fun processSelectorAction(selectorAction: Queue<Runnable>) {
        var task: Runnable? = selectorAction.peek()
        while (task != null) {
            task.run();
            task = selectorAction.poll()
        }
    }

    override fun stop() {
        println("Stopping server")
        readHandler.close()
        serverChannel!!.close()
    }

    override fun setHandler(handler: Handler<String>) {
        readHandler.setHandler(handler)
    }
}

fun main() {
    NioServer(8081).start()
}