package ru.siksmfp.network.play.champ.server.nio;

import ru.siksmfp.network.play.champ.handler.impl.io.IoTransmogrifyHandler;
import ru.siksmfp.network.play.champ.handler.impl.io.PrintableHandler;
import ru.siksmfp.network.play.champ.handler.impl.io.ThreadedHandler;
import ru.siksmfp.network.play.champ.handler.impl.nio.NioBlockingConnectionHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NioBlockingServer {

    public static void main(String[] args) throws IOException {
        ThreadedHandler<SocketChannel> handler = new ThreadedHandler<>(
                new PrintableHandler<>(
                        new NioBlockingConnectionHandler(
                                new IoTransmogrifyHandler()
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
