package ru.siksmfp.network.play.api

interface Server {

    fun start(host:String, port:String)

    fun stop()
}