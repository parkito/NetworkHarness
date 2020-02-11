package ru.siksmfp.network.play.champ.handler.impl.io;

import ru.siksmfp.network.play.champ.handler.api.Handler;
import ru.siksmfp.network.play.champ.utils.TransmogrifyUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class IoTransmogrifyHandler implements Handler<SocketChannel> {

    public void handle(SocketChannel socket) throws IOException {
        ByteBuffer bb = ByteBuffer.allocateDirect(80);
        int read = socket.read(bb);
        if (read == -1) {
            socket.close();
            return;
        }

        if (read > 0) {
            TransmogrifyUtils.transmogrify(bb);
            while (bb.hasRemaining()) {
                socket.write(bb);
            }
        }
    }
}
