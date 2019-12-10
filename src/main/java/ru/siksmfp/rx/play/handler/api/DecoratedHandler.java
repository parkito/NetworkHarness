package ru.siksmfp.rx.play.handler.api;

import java.io.IOException;

abstract public class DecoratedHandler<S> implements Handler<S> {
    private Handler<S> handler;

    public DecoratedHandler(Handler<S> handler) {
        this.handler = handler;
    }

    @Override
    public void handle(S s) throws IOException {
        handler.handle(s);
    }
}
