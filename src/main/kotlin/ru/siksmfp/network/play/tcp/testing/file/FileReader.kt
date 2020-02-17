package ru.siksmfp.network.play.tcp.testing.file

import java.nio.file.Files
import java.nio.file.Paths

class FileReader(
        private val filename: String
) {
    private val reader = Files.newBufferedReader(Paths.get(filename))

    fun nextLine(): String? {
        return reader.readLine()
    }

    fun lineAmount(): Long {
        return Files.newBufferedReader(Paths.get(filename))
                .lines()
                .count()
    }

    fun close() {
        reader.close()
    }
}
