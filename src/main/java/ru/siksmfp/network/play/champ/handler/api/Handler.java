package ru.siksmfp.network.play.champ.handler.api;

import java.io.IOException;

public interface Handler<S> {

    void handle(S s) throws IOException;
}
