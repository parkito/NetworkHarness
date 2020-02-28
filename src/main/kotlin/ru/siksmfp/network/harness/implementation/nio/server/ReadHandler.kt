package ru.siksmfp.network.harness.implementation.nio.server

import ru.siksmfp.network.harness.implementation.nio.byteBufferToString
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.SelectionKey.OP_WRITE
import java.nio.channels.SocketChannel
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ReadHandler(
        private val pendingData: MutableSet<SocketChannel>,
        private val selectorAction: Queue<Runnable>
) : SelectionHandler {

    private var executorService: ExecutorService = Executors.newFixedThreadPool(3)

    override fun handle(selectionKey: SelectionKey) {
        val sc = selectionKey.channel() as SocketChannel
        val bb = ByteBuffer.allocateDirect(80)
        val read = sc.read(bb)
        val response = byteBufferToString(bb, read)
        println("NioServer: received $response")
        if (read == -1) {
            pendingData.remove(sc)
            sc.close()
            println("Disconnected from in read $sc")
            return
        }
        if (read > 0) {
            executorService.submit {
                selectorAction.add(Runnable {
                    selectionKey.interestOps(OP_WRITE)
                })
                selectionKey.selector().wakeup()
            }
        }
    }
}