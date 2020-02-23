package ru.siksmfp.network.play.benchmarking.execution.genrerator

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.security.SecureRandom

object FileGenerator {

    private const val CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz"
    private const val NUMBER = "0123456789"
    private val CHAR_UPPER = CHAR_LOWER.toUpperCase()
    private const val STRING_MAX_SIZE = 1000
    private val random: SecureRandom = SecureRandom()

    val DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER

    fun generateFile(filename: String, linesNumber: Int) {
        Files.deleteIfExists(Paths.get(filename))
        val testFile = File(filename)
        if (!testFile.createNewFile()) {
            throw IllegalStateException("Can't create a file with tests")
        }

        val fileWriter = testFile.bufferedWriter()
        fileWriter.use {
            for (i in 0 until linesNumber) {
                val row = "$i.) ${generateRandomString()}\n"
                fileWriter.write(row)
            }

            print("File was populated")
        }
    }

    private fun generateRandomString(): String {
        val length = random.nextInt(STRING_MAX_SIZE)
        val sb = StringBuilder(length)
        for (i in 0 until length) {
            val rndCharAt: Int = random.nextInt(DATA_FOR_RANDOM_STRING.length)
            val rndChar: Char = DATA_FOR_RANDOM_STRING[rndCharAt]
            sb.append(rndChar)
        }
        return sb.toString()
    }
}
