package ru.siksmfp.rx.play.handler.impl.nio;

import ru.siksmfp.rx.play.handler.api.Handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

import static java.nio.channels.SelectionKey.OP_READ;

public class AcceptHandler implements Handler<SelectionKey> {

    private Map<SocketChannel, Queue<ByteBuffer>> pendingData;

    public AcceptHandler(Map<SocketChannel, Queue<ByteBuffer>> pendingData) {
        this.pendingData = pendingData;
    }

    @Override
    public void handle(SelectionKey selectionKey) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
        SocketChannel ss = ssc.accept();
        System.out.println("Connected to " + ss);
        pendingData.put(ss, new ArrayDeque<>());
        ss.configureBlocking(false);
        ss.register(selectionKey.selector(), OP_READ);
    }
}
