package ru.siksmfp.network.play.api

interface Client {

    fun start()

    fun send(message: String)
}