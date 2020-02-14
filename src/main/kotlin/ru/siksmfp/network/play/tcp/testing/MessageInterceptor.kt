package ru.siksmfp.network.play.tcp.testing

import ru.siksmfp.network.play.api.Handler

class MessageInterceptor(
        private val fileWriter: FileWriter
) : Handler<String> {

    override fun handle(t: String) {
        fileWriter.writeRow(t)
    }
}