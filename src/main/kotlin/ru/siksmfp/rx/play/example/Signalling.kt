package ru.siksmfp.rx.play.example

import io.reactivex.Observable

//Single is like an Observable that,
// instead of emitting a series of values,
// emits one value or an error notification.

//With this source of data, we can only use two methods to subscribe:
//OnSuccess returns a Single that also calls a method we specify
//OnError also returns a Single that immediately notifies subscribers of an error
fun main() {
    val result = ArrayList<String>()
    val single = Observable.fromArray("Hello", "1")
            .singleOrError()
            .doOnSuccess { e -> result.add(e) }
            .doOnError { error: Throwable -> print(error.message) }

    try {
        single.subscribe { e -> println("On success $e") } //BE CAREFUL. Exception throws in different thread. You can't catch it
    } catch (ex: Exception) {
        println("Exception")
    } finally {
        println("Finally")
    }

    println(result)
}