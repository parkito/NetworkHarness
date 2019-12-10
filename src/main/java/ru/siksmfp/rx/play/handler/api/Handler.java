package ru.siksmfp.rx.play.handler.api;

import java.io.IOException;

public interface Handler<S> {

    void handle(S s) throws IOException;
}
