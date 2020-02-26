package ru.siksmfp.network.harness.implementation.nio

import ru.siksmfp.network.harness.api.Handler
import ru.siksmfp.network.harness.api.Server
import ru.siksmfp.network.harness.implementation.nio.server.AcceptHandler
import ru.siksmfp.network.harness.implementation.nio.server.ReadHandler
import ru.siksmfp.network.harness.implementation.nio.server.SelectionHandler
import ru.siksmfp.network.harness.implementation.nio.server.WriteHandler
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey.OP_ACCEPT
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.*
import java.util.concurrent.ConcurrentLinkedDeque

class NioServer(
        private val port: Int
) : Server<String> {

    override fun start() {
        val pendingData: MutableMap<SocketChannel, Queue<ByteBuffer>> = HashMap()
        val selectorAction: Queue<Runnable> = ConcurrentLinkedDeque()

        val acceptHandler: SelectionHandler = AcceptHandler(pendingData)
        val readHandler: SelectionHandler = ReadHandler(pendingData, selectorAction)
        val writeHandler: SelectionHandler = WriteHandler(pendingData)

        val ss = ServerSocketChannel.open()
        ss.bind(InetSocketAddress(port))
        ss.configureBlocking(false)
        val selector = Selector.open()
        ss.register(selector, OP_ACCEPT)

        println("Server started on $port")

        while (true) {
            selector.select()
            processSelectorAction(selectorAction)
            val keys = selector.selectedKeys()
            val it = keys.iterator()
            while (it.hasNext()) {
                val key = it.next()
                it.remove()
                if (key.isValid) {
                    if (key.isAcceptable) {
                        acceptHandler.handle(key)
                    } else if (key.isReadable) {
                        readHandler.handle(key)
                    } else if (key.isWritable) {
                        writeHandler.handle(key)
                    }
                }
            }
        }
    }

    private fun processSelectorAction(selectorAction: Queue<Runnable>) {
        println("Woke up ${selectorAction.size}")
        var task: Runnable? = selectorAction.peek()
        while (task != null) {
            task.run();
            task = selectorAction.poll()
            println("in loop")
        }
    }

    override fun stop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setHandler(handler: Handler<String>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

fun main() {
    NioServer(8081).start()
}