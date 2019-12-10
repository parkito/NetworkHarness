package ru.siksmfp.rx.play.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SingleThreadedBlockingServer {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8081);
        while (true) {
            try (Socket s = ss.accept();
                 InputStream in = s.getInputStream();
                 OutputStream os = s.getOutputStream()
            ) {
                int data;
                while ((data = in.read()) != -1) {
                    os.write(transmogrify(data));
                }
            }
        }
    }

    private static int transmogrify(int data) {
        return Character.isLetter(data) ? data ^ ' ' : data;
    }
}
