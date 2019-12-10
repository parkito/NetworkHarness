package ru.siksmfp.rx.play.handler.impl;

import ru.siksmfp.rx.play.handler.api.Handler;

import java.io.IOException;

public class PrintableHandler<S> implements Handler<S> {

    private Handler<S> handler;

    public PrintableHandler(Handler<S> handler) {
        this.handler = handler;
    }

    @Override
    public void handle(S s) throws IOException {
        try {
            System.out.println("Connecting socket " + s);
            handler.handle(s);
        } finally {
            System.out.println("Disconnecting socket " + s);
        }
    }
}
