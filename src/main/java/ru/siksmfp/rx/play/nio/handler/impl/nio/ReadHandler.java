package ru.siksmfp.rx.play.nio.handler.impl.nio;

import ru.siksmfp.rx.play.nio.handler.api.Handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Queue;

import static java.nio.channels.SelectionKey.OP_WRITE;
import static ru.siksmfp.rx.play.nio.utils.TransmogrifyUtils.transmogrify;

public class ReadHandler implements Handler<SelectionKey> {

    private Map<SocketChannel, Queue<ByteBuffer>> pendingData;

    public ReadHandler(Map<SocketChannel, Queue<ByteBuffer>> pendingData) {
        this.pendingData = pendingData;
    }

    @Override
    public void handle(SelectionKey selectionKey) throws IOException {
        SocketChannel sc = (SocketChannel) selectionKey.channel();
        ByteBuffer bb = ByteBuffer.allocateDirect(80);
        int read = sc.read(bb);

        if (read == -1) {
            pendingData.remove(sc);
            sc.close();
            System.out.println("Disconnected from in read" + sc);
            return;
        }

        if (read > 0) {
            transmogrify(bb);
            pendingData.get(sc).add(bb);
            selectionKey.interestOps(OP_WRITE);
        }
    }
}
