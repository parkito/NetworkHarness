package ru.siksmfp.network.play.tcp.testing

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class FileWriter(
        private val filename: String
) {
    private val createdPath = Files.createFile(Paths.get(filename))
    private val fileWriter = File(createdPath.toUri()).bufferedWriter()

    fun writeRow(row: String) {
        fileWriter.write(row)
    }
}