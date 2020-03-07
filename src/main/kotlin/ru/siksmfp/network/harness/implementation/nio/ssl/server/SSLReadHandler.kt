package ru.siksmfp.network.harness.implementation.nio.simple.server

import ru.siksmfp.network.harness.api.Handler
import ru.siksmfp.network.harness.implementation.nio.ssl.server.NioSSLServerContext
import ru.siksmfp.network.harness.implementation.nio.simple.byteBufferToString
import tlschannel.NeedsReadException
import tlschannel.NeedsWriteException
import java.nio.channels.SelectionKey
import java.nio.channels.SelectionKey.OP_READ
import java.nio.channels.SelectionKey.OP_WRITE
import java.util.Queue
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SSLReadHandler(
        private val selectorActions: Queue<Runnable>
) : SelectionHandler {
    private var handler: Handler<String>? = null
    private var executorService: ExecutorService = Executors.newFixedThreadPool(1)

    override fun handle(selectionKey: SelectionKey) {
        val context = selectionKey.attachment() as NioSSLServerContext
        val tlsChannel = context.tlsChannel
        val bb = context.buffer
        try {
            val read: Int = tlsChannel.read(context.buffer)
            if (read > 0) {
                val response = byteBufferToString(bb, read)
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
        } catch (ex: NeedsReadException) {
            selectionKey.interestOps(OP_READ)
        } catch (ex: NeedsWriteException) {
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