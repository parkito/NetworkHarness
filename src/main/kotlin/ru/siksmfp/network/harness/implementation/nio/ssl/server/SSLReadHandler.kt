package ru.siksmfp.network.harness.implementation.nio.simple.server

import ru.siksmfp.network.harness.api.Handler
import ru.siksmfp.network.harness.implementation.nio.simple.byteBufferToString
import tlschannel.NeedsReadException
import tlschannel.NeedsWriteException
import tlschannel.TlsChannel
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
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
        println("reding")
        val tlsChannel = selectionKey.attachment() as TlsChannel
        val bb = ByteBuffer.allocate(10000) //todo optimize

        try {
            // write received bytes in stdout
            val c: Int = tlsChannel.read(bb)
            if (c > 0) {
                bb.flip()
//                print(tlschannel.example.NonBlockingServer.utf8.decode(buffer))
            }
            if (c < 0) {
                tlsChannel.close()
            }
        } catch (e: NeedsReadException) {
//            key.interestOps(SelectionKey.OP_READ) // overwrites previous value
        } catch (e: NeedsWriteException) {
//            key.interestOps(OP_WRITE) // overwrites previous value
        }
        println("red")
//        val response = byteBufferToString(bb!!, read)
//        handler?.handle(response)
        println("NioServer: received $response")

//        if (read > 0) {
//            executorService.submit {
//                selectorActions.add(Runnable {
//                    selectionKey.interestOps(OP_WRITE)
//                })
//                selectionKey.selector().wakeup()
//            }
//        }
    }

    override fun close() {
        handler?.close()
        executorService.shutdown()
    }

    fun setHandler(handler: Handler<String>) {
        this.handler = handler
    }
}