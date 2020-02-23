package ru.siksmfp.network.play.benchmarking.execution.support

import ru.siksmfp.network.play.api.Handler
import java.io.File

class MessageInterceptor(
        filename: String
) : Handler<String> {

    private val fileWriter = File(filename).bufferedWriter()

    override fun handle(t: String) {
        fileWriter.write("$t\n")
    }

    override fun close() {
        fileWriter.close()
    }
}
