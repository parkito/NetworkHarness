package ru.siksmfp.network.harness.api

interface Handler<T> {

    fun handle(t: T)

    fun close()
}
