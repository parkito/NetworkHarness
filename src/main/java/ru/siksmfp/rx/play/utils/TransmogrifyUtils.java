package ru.siksmfp.rx.play.utils;

import java.nio.ByteBuffer;

public class TransmogrifyUtils {

    public static int transmogrify(int data) {
        if (data == '$') {
            throw new IllegalStateException("Error");
        }
        return Character.isLetter(data) ? data ^ ' ' : data;
    }

    public static void transmogrify(ByteBuffer bb) {
        bb.flip();
        for (int i = 0; i < bb.limit(); i++) {
            int data = bb.get(i);
            bb.put(i, (byte) transmogrify(data));
        }
    }
}
