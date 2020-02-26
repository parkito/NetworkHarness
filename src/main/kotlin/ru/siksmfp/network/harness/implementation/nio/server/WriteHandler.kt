package ru.siksmfp.network.harness.implementation.nio.server

import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.SelectionKey.OP_READ
import java.nio.channels.SocketChannel
import java.util.*

class WriteHandler(
        private val pendingData: MutableMap<SocketChannel, Queue<ByteBuffer>>
) : SelectionHandler {

    override fun handle(selectionKey: SelectionKey) {
        val sc = selectionKey.channel() as SocketChannel
        val queue = pendingData[sc] ?: ArrayDeque()
        while (!queue.isEmpty()) {
            val bb = queue.peek()
            bb.flip()
            val written = sc.write(bb)
            println("writing $written")
            if (written == -1) {
                sc.close()
                pendingData.remove(sc)
                println("Disconnected from in write $sc")
                return
            }
            if (bb.hasRemaining()) {
                return
            }
            queue.remove()
        }
        selectionKey.interestOps(OP_READ)
    }
}