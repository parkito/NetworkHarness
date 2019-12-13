package ru.siksmfp.rx.play.server.io;

import ru.siksmfp.rx.play.handler.api.Handler;
import ru.siksmfp.rx.play.handler.impl.io.PrintableHandler;
import ru.siksmfp.rx.play.handler.impl.io.TransmogrifyHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SingleThreadedBlockingServer {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8081);
        Handler<Socket> handler = new PrintableHandler(
                new TransmogrifyHandler()
        );

        while (true) {
            handler.handle(ss.accept());
        }
    }
}
