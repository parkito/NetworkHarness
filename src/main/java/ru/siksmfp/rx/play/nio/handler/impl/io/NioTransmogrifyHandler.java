package ru.siksmfp.rx.play.nio.handler.impl.io;

import ru.siksmfp.rx.play.nio.handler.api.Handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static ru.siksmfp.rx.play.nio.utils.TransmogrifyUtils.transmogrify;

public class NioTransmogrifyHandler implements Handler<SocketChannel> {

    public void handle(SocketChannel socket) throws IOException {
        ByteBuffer bb = ByteBuffer.allocateDirect(80);
        int read = socket.read(bb);
        if (read == -1) {
            socket.close();
            return;
        }

        if (read > 0) {
            transmogrify(bb);
            while (bb.hasRemaining()) {
                socket.write(bb);
            }
        }
    }
}
