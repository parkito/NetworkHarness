package ru.siksmfp.network.harness.implementation.nio.server

import ru.siksmfp.network.harness.api.Handler
import ru.siksmfp.network.harness.implementation.nio.byteBufferToString
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.SelectionKey.OP_WRITE
import java.nio.channels.SocketChannel
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ReadHandler(
        private val clients: MutableSet<SocketChannel>,
        private val selectorActions: Queue<Runnable>
) : SelectionHandler {

    private var handler: Handler<String>? = null

    private var executorService: ExecutorService = Executors.newFixedThreadPool(1)

    override fun handle(selectionKey: SelectionKey) {
        val sc = selectionKey.channel() as SocketChannel
        val bb = ByteBuffer.allocateDirect(10000)//todo improve memory management
        val read = sc.read(bb)
        if (read == -1) {
            clients.remove(sc)
            sc.close()
            println("Disconnected from in read $sc")
            return
        }
        val response = byteBufferToString(bb, read)
        handler?.handle(response)
        println("NioServer: received $response")

        if (read > 0) {
            executorService.submit {
                selectorActions.add(Runnable {
                    selectionKey.interestOps(OP_WRITE)
                })
                selectionKey.selector().wakeup()
            }
        }
    }

    override fun close() {
        handler?.close()
        executorService.shutdown()
    }

    fun setHandler(handler: Handler<String>) {
        this.handler = handler
    }
}