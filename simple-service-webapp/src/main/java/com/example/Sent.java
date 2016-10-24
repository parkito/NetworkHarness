package com.example;

import java.io.Serializable;

/**
 * Created by Artyom Karnov on 10/25/16.
 * artyom-karnov@yandex.ru
 **/
public class Sent implements Serializable {
    String data;

    public Sent(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
