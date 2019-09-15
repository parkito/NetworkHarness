package ru.siksmfp.rx.play.example

import io.reactivex.Observable

fun main() {
    val result = arrayListOf<Char>()
    val values = Observable.using(
            { "MyResource" },
            { r ->
                Observable.create<Char> { o ->
                    for (c in r.toCharArray()) {
                        o.onNext(c)
                    }
                    o.onComplete()
                }
            },
            { r -> println("Disposed: $r") }
    )
    values.subscribe(
            { v -> result.add(v) },
            { e -> println(e.message) }
    )

    println(result)
}