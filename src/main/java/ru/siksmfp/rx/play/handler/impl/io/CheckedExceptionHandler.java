package ru.siksmfp.rx.play.handler.impl.io;

import ru.siksmfp.rx.play.handler.api.DecoratedHandler;
import ru.siksmfp.rx.play.handler.api.Handler;

import java.io.IOException;
import java.io.UncheckedIOException;

public class CheckedExceptionHandler<S> extends DecoratedHandler<S> {

    public CheckedExceptionHandler(Handler<S> handler) {
        super(handler);
    }

    @Override
    public void handle(S s) {
        try {
            super.handle(s);
        } catch (IOException ex) {
            System.out.println("Exception occurred " + ex.getMessage());
            throw new UncheckedIOException(ex);
        }
    }
}
