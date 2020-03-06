package ru.siksmfp.network.harness.implementation.nio.ssl

import ru.siksmfp.network.harness.api.Handler
import ru.siksmfp.network.harness.api.Server
import ru.siksmfp.network.harness.implementation.nio.simple.server.SSLAcceptHandler
import ru.siksmfp.network.harness.implementation.nio.simple.server.SSLReadHandler
import ru.siksmfp.network.harness.implementation.nio.simple.server.SSLWriteHandler
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.Queue
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.atomic.AtomicBoolean

class NioSSLServer(
        private val port: Int,
        threadNumber: Int?
) : Server<String> {

    private lateinit var serverChannel: ServerSocketChannel

    private val clients = ConcurrentHashMap<SocketChannel, ByteBuffer>()
    private val selectorActions: Queue<Runnable> = ConcurrentLinkedDeque()

    private val acceptHandler = SSLAcceptHandler(clients)
    private val readHandler = SSLReadHandler(clients, selectorActions)
    private val writeHandler = SSLWriteHandler(clients)

    private lateinit var isRunning: AtomicBoolean

    override fun start() {
        isRunning = AtomicBoolean(true)
        serverChannel = ServerSocketChannel.open()
        serverChannel.bind(InetSocketAddress(port))
        serverChannel.configureBlocking(false)
        val selector = Selector.open()
        println("Server nio started on $port")
        serverChannel.register(selector, SelectionKey.OP_ACCEPT)

        while (isRunning.get()) {
            selector.select()
            println("pass block")
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
        clients.forEach { it.key.close() }
        serverChannel.close()
        selectorActions.clear()
    }

    override fun setHandler(handler: Handler<String>) {
        readHandler.setHandler(handler)
    }
}

fun main() {
    NioSSLServer(8081, null).start()
}