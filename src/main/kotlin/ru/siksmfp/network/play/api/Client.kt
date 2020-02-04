package ru.siksmfp.network.play.api

interface Client {

    fun start(host:String, port:String)

    fun sendMessage(message:String)
}