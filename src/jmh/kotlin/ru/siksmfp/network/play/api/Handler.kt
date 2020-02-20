package ru.siksmfp.network.play.api

interface Handler<T> {

    fun handle(t: T)

    fun close()
}
