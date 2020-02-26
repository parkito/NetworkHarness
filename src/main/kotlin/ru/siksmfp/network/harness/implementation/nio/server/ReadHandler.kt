package ru.siksmfp.network.harness.implementation.nio.server

import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.SelectionKey.OP_WRITE
import java.nio.channels.SocketChannel
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ReadHandler(
        private val pendingData: MutableMap<SocketChannel, Queue<ByteBuffer>>,
        private val selectorAction: Queue<Runnable>
) : SelectionHandler {

    private var executorService: ExecutorService = Executors.newFixedThreadPool(3)

    override fun handle(selectionKey: SelectionKey) {
        val sc = selectionKey.channel() as SocketChannel
        val bb = ByteBuffer.allocateDirect(80)
        val read = sc.read(bb)
        if (read == -1) {
            pendingData.remove(sc)
            sc.close()
            println("Disconnected from in read $sc")
            return
        }
        if (read > 0) {
            println("Received $bb")
            executorService.submit {
                pendingData[sc]!!.add(bb)
                selectorAction.add(Runnable {
                    selectionKey.interestOps(OP_WRITE)
                    println("Start writing")
                })
                selectionKey.selector().wakeup()
            }
        }
    }
}