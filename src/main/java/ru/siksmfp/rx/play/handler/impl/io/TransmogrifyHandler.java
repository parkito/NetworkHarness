package ru.siksmfp.rx.play.handler.impl.io;

import ru.siksmfp.rx.play.handler.api.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static ru.siksmfp.rx.play.utils.TransmogrifyUtils.transmogrify;

public class TransmogrifyHandler implements Handler<Socket> {

    public void handle(Socket socket) throws IOException {
        try (socket;
             InputStream in = socket.getInputStream();
             OutputStream os = socket.getOutputStream()
        ) {
            int data;
            while ((data = in.read()) != -1) {
                os.write(transmogrify(data));
            }
        }
    }
}
