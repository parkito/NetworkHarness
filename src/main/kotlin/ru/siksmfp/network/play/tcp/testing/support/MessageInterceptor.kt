package ru.siksmfp.network.play.tcp.testing.support

import ru.siksmfp.network.play.api.Handler
import ru.siksmfp.network.play.tcp.testing.file.FileWriter

class MessageInterceptor(
        private val fileWriter: FileWriter
) : Handler<String> {

    override fun handle(t: String) {
        fileWriter.writeRow(t)
    }
}
