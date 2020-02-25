package ru.siksmfp.network.harness.benchmarking.execution.file

import ru.siksmfp.network.harness.benchmarking.execution.genrerator.FileGenerator.DATA_FOR_RANDOM_STRING

class FileComparator(
        private val baseFile: String,
        private val comparableFile: String
) {

    fun compare() {
        var comparableLines: Long = 0
        var baselineLines: Long = -1

        FileReader(comparableFile).use {
            comparableLines = it.lineAmount()
        }

        FileReader(baseFile).use {
            baselineLines = it.lineAmount()
        }

        if (baselineLines != comparableLines) {
            println("Base lines length $baselineLines VS Comparable lines length  $comparableLines")
            throw IllegalStateException("Files are different")
        }

        DATA_FOR_RANDOM_STRING.forEach {
            val baseOccurrences = compareStringInclude(baseFile, it.toString())
            val comparableOccurrences = compareStringInclude(comparableFile, it.toString())
            if (baseOccurrences != comparableOccurrences) {
                print("Character $it. Base number $baseOccurrences VS Comparable number $comparableOccurrences")
                throw IllegalStateException("Files are different")
            }
        }
    }

    private fun compareStringInclude(filename: String, string: String): Long {
        var counter: Long = 0
        val reader = FileReader(filename)
        reader.use {
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
}


