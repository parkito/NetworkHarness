package ru.siksmfp.network.harness.implementation.nio.simple

import ru.siksmfp.network.harness.api.Handler
import ru.siksmfp.network.harness.api.Server
import ru.siksmfp.network.harness.implementation.nio.simple.server.AcceptHandler
import ru.siksmfp.network.harness.implementation.nio.simple.server.ReadHandler
import ru.siksmfp.network.harness.implementation.nio.simple.server.WriteHandler
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey.OP_ACCEPT
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.Queue
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.atomic.AtomicBoolean

class NioServer(
        private val port: Int,
        threadNumber: Int?
) : Server<String> {

    private lateinit var serverChannel: ServerSocketChannel
    private lateinit var selector: Selector

    private val clients = ConcurrentHashMap<SocketChannel, ByteBuffer>()
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
        selector = Selector.open()
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
        isRunning.set(false)
        readHandler.close()
        clients.forEach { it.key.close() }
        serverChannel.close()
        selector.close()
        selectorActions.clear()
        println("Nio server stopped")
    }

    override fun setHandler(handler: Handler<String>) {
        readHandler.setHandler(handler)
    }
}