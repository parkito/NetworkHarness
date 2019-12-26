package ru.siksmfp.rx.play.nio.server.nio;

import ru.siksmfp.rx.play.nio.handler.impl.nio.NioBlockingConnectionHandler;
import ru.siksmfp.rx.play.nio.handler.impl.io.NioTransmogrifyHandler;
import ru.siksmfp.rx.play.nio.handler.impl.io.PrintableHandler;
import ru.siksmfp.rx.play.nio.handler.impl.io.ThreadedHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NioBlockingServer {

    public static void main(String[] args) throws IOException {
        ThreadedHandler<SocketChannel> handler = new ThreadedHandler<>(
                new PrintableHandler<>(
                        new NioBlockingConnectionHandler(
                                new NioTransmogrifyHandler()
                        )
                )
        );

        ServerSocketChannel ss = ServerSocketChannel.open();
        ss.bind(new InetSocketAddress(8081));
        while (true) {
            handler.handle(ss.accept());
        }
    }
}
