package ru.siksmfp.network.harness.api

interface Server<T> {

    fun start()

    fun stop()

    fun setHandler(handler: Handler<T>)
}