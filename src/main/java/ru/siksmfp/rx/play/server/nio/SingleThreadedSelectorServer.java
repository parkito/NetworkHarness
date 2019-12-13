package ru.siksmfp.rx.play.server.nio;

import ru.siksmfp.rx.play.handler.api.Handler;
import ru.siksmfp.rx.play.handler.impl.nio.AcceptHandler;
import ru.siksmfp.rx.play.handler.impl.nio.ReadHandler;
import ru.siksmfp.rx.play.handler.impl.nio.WriteHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import static java.nio.channels.SelectionKey.OP_ACCEPT;

public class SingleThreadedSelectorServer {

    public static void main(String[] args) throws IOException {
        Map<SocketChannel, Queue<ByteBuffer>> pendingData = new HashMap<>();

        Handler<SelectionKey> acceptHandler = new AcceptHandler(pendingData);
        Handler<SelectionKey> readHandler = new ReadHandler(pendingData);
        Handler<SelectionKey> writeHandler = new WriteHandler(pendingData);

        ServerSocketChannel ss = ServerSocketChannel.open();
        ss.bind(new InetSocketAddress(8081));
        ss.configureBlocking(false);
        Selector selector = Selector.open();
        ss.register(selector, OP_ACCEPT);

        while (true) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            for (Iterator<SelectionKey> it = keys.iterator(); it.hasNext(); ) {
                SelectionKey key = it.next();
                it.remove();
                if (key.isValid()) {
                    if (key.isAcceptable()) {
                        acceptHandler.handle(key);
                    } else if (key.isReadable()) {
                        readHandler.handle(key);
                    } else if (key.isWritable()) {
                        writeHandler.handle(key);
                    }
                }
            }
        }
    }
}
