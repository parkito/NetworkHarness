package ru.siksmfp.network.harness.implementation.nio.simple.server

import ru.siksmfp.network.harness.api.Handler
import ru.siksmfp.network.harness.implementation.nio.simple.byteBufferToString
import tlschannel.NeedsReadException
import tlschannel.NeedsWriteException
import tlschannel.TlsChannel
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.SelectionKey.OP_READ
import java.nio.channels.SelectionKey.OP_WRITE
import java.nio.channels.SocketChannel
import java.util.Queue
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SSLReadHandler(
        private val clients: MutableMap<SocketChannel, ByteBuffer>,
        private val selectorActions: Queue<Runnable>
) : SelectionHandler {

    private var handler: Handler<String>? = null

    private var executorService: ExecutorService = Executors.newFixedThreadPool(1)

    override fun handle(selectionKey: SelectionKey) {
        val tlsChannel = selectionKey.attachment() as TlsChannel
        val bb = ByteBuffer.allocate(10000) //todo optimize

        try {
            val read: Int = tlsChannel.read(bb)
            if (read > 0) {
                val response = byteBufferToString(bb!!, read)
                println("NioSSLServer: received $response")
                handler?.handle(response)

                executorService.submit {
                    selectorActions.add(Runnable {
                        selectionKey.interestOps(OP_WRITE)
                    })
                    selectionKey.selector().wakeup()
                }
            }
            if (read < 0) {
                tlsChannel.close()
            }
        } catch (e: NeedsReadException) {
            selectionKey.interestOps(OP_READ)
        } catch (e: NeedsWriteException) {
            selectionKey.interestOps(OP_WRITE)
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