package ru.siksmfp.rx.play.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadedBlockingServer {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8081);
        while (true) {
            handle(ss.accept());
        }
    }

    private static void handle(Socket s) {
        new Thread(() -> {
            System.out.println("Connecting socket " + s);
            try (s;
                 InputStream in = s.getInputStream();
                 OutputStream os = s.getOutputStream()
            ) {
                int data;
                while ((data = in.read()) != -1) {
                    os.write(transmogrify(data));
                }
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
            System.out.println("Disconnecting socket " + s);
        }).start();
    }

    private static int transmogrify(int data) {
        return Character.isLetter(data) ? data ^ ' ' : data;
    }
}
