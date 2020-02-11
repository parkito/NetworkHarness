package ru.siksmfp.network.play.tcp.testing

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

class FileWriter(private val fileLimit: Int) {
    private val writeQueue = ConcurrentLinkedQueue<String>()
    private val cretedPath = Files.createFile(Paths.get("~/Downloads/result"))
    private val fileWriter = File(cretedPath.toUri()).bufferedWriter()
    private val threadPool = Executors.newFixedThreadPool(10)
    private val rowCounter = AtomicInteger(0)

    fun writeRow(row: String) {
        writeQueue.add(row)
    }

    private fun writeFile() {
        while (rowCounter.get() < fileLimit) {
            val row = writeQueue.poll()
            if (row != null) {
                threadPool.execute {
                    fileWriter.write(row)
                    rowCounter.incrementAndGet()
                }
            }
        }
    }
}