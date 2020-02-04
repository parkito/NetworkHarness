package ru.siksmfp.network.play.temp.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

import static java.nio.channels.SelectionKey.OP_ACCEPT;

public class NioConnector {

    public static void main(String[] args) throws IOException {
        new NioConnector().start();
    }

    public void start() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8081));
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, OP_ACCEPT);

        AcceptHandler acceptHandler = new AcceptHandler();
        ReadHandler readHandler = new ReadHandler();
        WriteHandler writeHandler = new WriteHandler();

        while (AppState.isWorking) {
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

        System.out.println("Finishing nio connector");
    }
}
