package ru.siksmfp.rx.play.example

import io.reactivex.Observable
import java.util.concurrent.TimeUnit.MILLISECONDS


//A ConnectableObservable resembles an ordinary Observable,
// except that it doesn't begin emitting items when it is subscribed to,
// but only when the connect operator is applied to it.
fun main() {
    val connectable = Observable.interval(200, MILLISECONDS).publish()

    connectable.subscribe { e -> run {
            print(Thread.currentThread().name)
            Thread.sleep(1000)
            println(" Sub1 $e")
        }
    }

    connectable.subscribe { e -> run {
        print(Thread.currentThread().name)
        println(" Sub2 $e") } }

    connectable.connect()
    Thread.sleep(5000)
}