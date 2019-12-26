package ru.siksmfp.rx.play.nio.server.nio;

import ru.siksmfp.rx.play.nio.handler.api.Handler;
import ru.siksmfp.rx.play.nio.handler.impl.io.NioTransmogrifyHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collection;

public class SingleThreadedPollingServer {

    public static void main(String[] args) throws IOException {
        Handler<SocketChannel> handler = new NioTransmogrifyHandler();

        ServerSocketChannel ss = ServerSocketChannel.open();
        ss.bind(new InetSocketAddress(8081));
        ss.configureBlocking(false);

        Collection<SocketChannel> sockets = new ArrayList<>();
        while (true) {
            SocketChannel socket = ss.accept();
            if (socket != null) {
                sockets.add(socket);
                System.out.println("Connected to " + socket);
                socket.configureBlocking(false);
            }

            for (SocketChannel sock : sockets) {
                if (sock.isConnected()) {
                    handler.handle(sock);
                }
            }

            sockets.removeIf(s -> !s.isConnected());
        }
    }
}
