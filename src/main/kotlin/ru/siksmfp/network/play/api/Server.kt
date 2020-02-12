package ru.siksmfp.network.play.api

interface Server<T> {

    fun start()

    fun stop()

    fun setHandler(handler: Handler<T>)
}