package ru.siksmfp.network.harness.api

interface Client<T> {

    fun start()

    fun send(message: T)

    fun stop()
}