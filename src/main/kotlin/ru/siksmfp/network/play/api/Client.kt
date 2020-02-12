package ru.siksmfp.network.play.api

interface Client<T> {

    fun start()

    fun send(message: T)
}