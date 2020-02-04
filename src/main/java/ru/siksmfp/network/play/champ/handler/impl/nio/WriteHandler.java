package ru.siksmfp.network.play.champ.handler.impl.nio;

import ru.siksmfp.network.play.champ.handler.api.Handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

import static java.nio.channels.SelectionKey.OP_READ;

public class WriteHandler implements Handler<SelectionKey> {

    private Map<SocketChannel, Queue<ByteBuffer>> pendingData;

    public WriteHandler(Map<SocketChannel, Queue<ByteBuffer>> pendingData) {
        this.pendingData = pendingData;
    }

    @Override
    public void handle(SelectionKey selectionKey) throws IOException {
        SocketChannel sc = (SocketChannel) selectionKey.channel();
        Queue<ByteBuffer> queue = pendingData.getOrDefault(sc, new ArrayDeque<>());
        while (!queue.isEmpty()) {
            ByteBuffer bb = queue.peek();
            int written = sc.write(bb);

            if (written == -1) {
                sc.close();
                pendingData.remove(sc);
                System.out.println("Disconnected from in write" + sc);
                return;
            }

            if (bb.hasRemaining()) {
                return;
            }

            queue.remove();
        }
        selectionKey.interestOps(OP_READ);
    }
}
