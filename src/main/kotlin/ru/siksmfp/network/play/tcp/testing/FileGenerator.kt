package ru.siksmfp.network.play.tcp.testing

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.security.SecureRandom

class FileGenerator {

    companion object {
        private const val CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz"
        private val CHAR_UPPER = CHAR_LOWER.toUpperCase()
        private const val NUMBER = "0123456789"
        private val random: SecureRandom = SecureRandom()
        private val DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER
        private const val STRING_MAX_SIZE = 100_000
        private const val STRING_NUMBER = 100_000
    }

    fun generateFile(filename: String) {
        Files.deleteIfExists(Paths.get(filename))
        val testFile = File(filename)
        if (!testFile.createNewFile()) {
            throw IllegalStateException("Can't create a file with tests")
        }

        val fileWriter = testFile.printWriter()

        for (i in 0 until STRING_NUMBER) {
            val row = "$i.) ${generateRandomString()}\n"
            fileWriter.write(row)
            println(row)
        }

        fileWriter.close()
        print("File was populated")
    }

    fun generateRandomString(): String {
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

fun main() {
    FileGenerator().generateFile("test.txt")
}