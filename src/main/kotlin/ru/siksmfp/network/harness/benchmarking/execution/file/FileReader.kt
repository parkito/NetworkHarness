package ru.siksmfp.network.harness.benchmarking.execution.file

import java.io.Closeable
import java.nio.file.Files
import java.nio.file.Paths

class FileReader(
        private val filename: String
) : Closeable {
    private val reader = Files.newBufferedReader(Paths.get(filename))

    fun nextLine(): String? {
        return reader.readLine()
    }

    fun lineAmount(): Long {
        return Files.newBufferedReader(Paths.get(filename)).use {
            it.lines().count()
        }
    }

    override fun close() {
        reader.close()
    }
}
