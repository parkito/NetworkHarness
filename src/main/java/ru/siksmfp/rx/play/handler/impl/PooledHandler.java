package ru.siksmfp.rx.play.handler.impl;

import ru.siksmfp.rx.play.handler.api.DecoratedHandler;
import ru.siksmfp.rx.play.handler.api.Handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

public class PooledHandler<S> extends DecoratedHandler<S> {
    private ExecutorService executorService;
    private Thread.UncaughtExceptionHandler exceptionHandler;

    public PooledHandler(Handler<S> handler,
                         ExecutorService executorService,
                         Thread.UncaughtExceptionHandler exceptionHandler) {
        super(handler);
        this.executorService = executorService;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void handle(S s) {
        executorService.submit(
                new FutureTask<>(
                        () -> {
                            super.handle(s);
                            return null;
                        }) {
                    @Override
                    protected void setException(Throwable t) {
                        exceptionHandler.uncaughtException(Thread.currentThread(), t);
                    }
                }
        )
        ;
    }
}
