package ru.siksmfp.network.play.champ.server.io;

import ru.siksmfp.network.play.champ.handler.api.Handler;
import ru.siksmfp.network.play.champ.handler.impl.io.PrintableHandler;
import ru.siksmfp.network.play.champ.handler.impl.io.TransmogrifyHandler;

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
