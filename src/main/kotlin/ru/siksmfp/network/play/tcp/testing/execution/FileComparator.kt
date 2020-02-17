package ru.siksmfp.network.play.tcp.testing.execution

import ru.siksmfp.network.play.tcp.testing.file.FileReader

class FileComparator(
        private val baseFile: String,
        private val comparableFile: String
) {

    fun compare() {
        val comparableLines = FileReader(comparableFile).lineAmount()
        val baselineLines = FileReader(baseFile).lineAmount()
        if (baselineLines != comparableLines) {
            println("Base lines length $baselineLines VS Comparable lines length  $comparableLines")
            throw IllegalStateException("Files are different")
        }

        "abcdefghijklmnopqrstuvwxyz".forEach {
            val baseOccurrences = compareStringInclude(baseFile, it.toString())
            val comparableOccurrences = compareStringInclude(comparableFile, it.toString())
            print("Character $it. Base number $baseOccurrences VS Comparable number $comparableOccurrences")
            throw IllegalStateException("Files are different")
        }
    }

    private fun compareStringInclude(filename: String, string: String): Long {
        var counter: Long = 0
        val reader = FileReader(filename)
        for (i in 0 until reader.lineAmount()) {
            val str = reader.nextLine()
            var lastIndex = 0
            while (lastIndex != -1) {
                lastIndex = str!!.indexOf(string, lastIndex)
                if (lastIndex != -1) {
                    counter++
                    lastIndex += string.length
                }
            }
        }
        return counter
    }
}


