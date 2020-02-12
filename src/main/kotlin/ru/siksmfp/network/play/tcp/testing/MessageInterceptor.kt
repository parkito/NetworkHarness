package ru.siksmfp.network.play.tcp.testing

import ru.siksmfp.network.play.champ.handler.api.Handler

class MessageInterceptor(
        private val fileWriter: FileWriter
) : Handler<String> {

    override fun handle(message: String) {
        fileWriter.writeRow(message)
    }
}