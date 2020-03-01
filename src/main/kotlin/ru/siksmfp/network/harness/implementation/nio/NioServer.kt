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
import java.util.concurrent.atomic.AtomicBoolean


class NioServer(
        private val port: Int,
        threadNumber: Int?
) : Server<String> {

    private lateinit var serverChannel: ServerSocketChannel

    private val clients = Collections.newSetFromMap<SocketChannel>(ConcurrentHashMap())
    private val selectorActions: Queue<Runnable> = ConcurrentLinkedDeque()

    private val acceptHandler = AcceptHandler(clients)
    private val readHandler = ReadHandler(clients, selectorActions)
    private val writeHandler = WriteHandler(clients)

    private lateinit var isRunning: AtomicBoolean

    override fun start() {
        isRunning = AtomicBoolean(true)
        serverChannel = ServerSocketChannel.open()
        serverChannel.bind(InetSocketAddress(port))
        serverChannel.configureBlocking(false)
        val selector = Selector.open()
        serverChannel.register(selector, OP_ACCEPT)

        println("Server nio started on $port")

        while (isRunning.get()) {
            selector.select()
            processSelectorAction(selectorActions)
            val keys = selector.selectedKeys()
            val keysIterator = keys.iterator()
            while (keysIterator.hasNext()) {
                val key = keysIterator.next()
                keysIterator.remove()
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
        println("Stopping nio server")
        isRunning.set(false)
        readHandler.close()
        clients.forEach { it.close() }
        serverChannel.close()
        selectorActions.clear()
    }

    override fun setHandler(handler: Handler<String>) {
        readHandler.setHandler(handler)
    }
}

fun main() {
    NioServer(8081, null).start()
}