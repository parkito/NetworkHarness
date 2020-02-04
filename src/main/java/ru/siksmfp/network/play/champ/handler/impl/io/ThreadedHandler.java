package ru.siksmfp.network.play.champ.handler.impl.io;

import ru.siksmfp.network.play.champ.handler.api.Handler;

public class ThreadedHandler<S> extends CheckedExceptionHandler<S> {

    public ThreadedHandler(Handler<S> handler) {
        super(handler);
    }

    @Override
    public void handle(S s) {
        new Thread(() -> super.handle(s)).start();
    }
}
