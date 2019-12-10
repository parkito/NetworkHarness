package ru.siksmfp.rx.play.handler.impl;

import ru.siksmfp.rx.play.handler.api.Handler;

import java.io.IOException;
import java.io.UncheckedIOException;

public class CheckedExceptionHandler<S> implements Handler<S> {

    private Handler<S> handler;

    public CheckedExceptionHandler(Handler<S> handler) {
        this.handler = handler;
    }

    @Override
    public void handle(S s) {
        try {
            handler.handle(s);
        } catch (IOException ex) {
            System.out.println("Exception occurred " + ex.getMessage());
            throw new UncheckedIOException(ex);
        }
    }
}
