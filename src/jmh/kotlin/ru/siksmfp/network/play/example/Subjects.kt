package ru.siksmfp.network.play.example

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

var subscriber1 = 0
var subscriber2 = 0

fun main() {
    val subject = PublishSubject.create<Int>()
    subject.subscribe(getFirstObserver())
    subject.onNext(1)
    subject.onNext(2)
    subject.onNext(3)
    subject.subscribe(getSecondObserver())
    subject.onNext(4)
    subject.onComplete()
}

fun getFirstObserver(): Observer<Int> {
    return object : Observer<Int> {
        override fun onComplete() {
            println("Complete 1")
        }

        override fun onSubscribe(d: Disposable) {
            println("On Subscribe 1")
        }

        override fun onNext(t: Int) {
            println("On next 1")
            subscriber1 += t
        }

        override fun onError(e: Throwable) {
            println("Error 1")
        }
    }
}

fun getSecondObserver(): Observer<Int> {
    return object : Observer<Int> {
        override fun onSubscribe(d: Disposable) {
            println("On subscribe 2")
        }

        override fun onNext(t: Int) {
            println("On next 2")
            subscriber2 += t
        }

        override fun onError(e: Throwable) {
            println("Error 2")
        }

        override fun onComplete() {
            println("Complete 2")
        }
    }
}