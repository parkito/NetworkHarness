package ru.siksmfp.rx.play.nio.handler.impl.io;

import ru.siksmfp.rx.play.nio.handler.api.Handler;

public class ThreadedHandler<S> extends CheckedExceptionHandler<S> {

    public ThreadedHandler(Handler<S> handler) {
        super(handler);
    }

    @Override
    public void handle(S s) {
        new Thread(() -> super.handle(s)).start();
    }
}
