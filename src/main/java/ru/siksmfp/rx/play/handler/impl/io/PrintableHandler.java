package ru.siksmfp.rx.play.handler.impl.io;

import ru.siksmfp.rx.play.handler.api.DecoratedHandler;
import ru.siksmfp.rx.play.handler.api.Handler;

import java.io.IOException;

public class PrintableHandler<S> extends DecoratedHandler<S> {

    public PrintableHandler(Handler<S> handler) {
        super(handler);
    }

    @Override
    public void handle(S s) throws IOException {
        try {
            System.out.println("Connecting socket " + s);
            super.handle(s);
        } finally {
            System.out.println("Disconnecting socket " + s);
        }
    }
}
