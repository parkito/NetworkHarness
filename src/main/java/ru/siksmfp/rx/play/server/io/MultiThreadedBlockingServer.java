package ru.siksmfp.rx.play.server.io;

import ru.siksmfp.rx.play.handler.impl.io.PrintableHandler;
import ru.siksmfp.rx.play.handler.impl.io.ThreadedHandler;
import ru.siksmfp.rx.play.handler.impl.io.TransmogrifyHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadedBlockingServer {

    public static void main(String[] args) throws IOException {
        ThreadedHandler<Socket> handler = new ThreadedHandler<>(
                new PrintableHandler<>(
                        new TransmogrifyHandler()
                )
        );
        ServerSocket ss = new ServerSocket(8081);
        while (true) {
            handler.handle(ss.accept());
        }
    }
}
