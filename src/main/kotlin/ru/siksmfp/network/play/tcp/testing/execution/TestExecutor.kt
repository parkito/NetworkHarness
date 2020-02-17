package ru.siksmfp.network.play.tcp.testing.execution

import ru.siksmfp.network.play.api.Client
import ru.siksmfp.network.play.api.Server
import ru.siksmfp.network.play.tcp.testing.support.MessageInterceptor
import ru.siksmfp.network.play.tcp.testing.support.NamedThreadFactory
import ru.siksmfp.network.play.tcp.testing.file.FileReader
import ru.siksmfp.network.play.tcp.testing.file.FileWriter
import ru.siksmfp.network.play.tcp.testing.support.getHomeFolderPath
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.reflect.KClass

class TestExecutor(
        private val serverClass: KClass<out Any>,
        private val clientClass: KClass<out Any>,
        private val testFile: String
) {
    private val tempFileName = "${getHomeFolderPath()}/Downloads/temp${LocalDateTime.now()}.txt"
    private val fileWriter = FileWriter(tempFileName)
    private val executor = Executors.newFixedThreadPool(5, NamedThreadFactory("client"))
    private val messageInterceptor = MessageInterceptor(fileWriter)

    fun executeTest() {
        val serverClassConstructor = serverClass.constructors.toList()[0]
        val clientClassConstructor = clientClass.constructors.toList()[0]
        val serverInstance = serverClassConstructor.call(8081) as Server<String>
        val clients = IntRange(0, 5)
                .map { clientClassConstructor.call("localhost", 8081) as Client<String> }
                .toList()

        performTest(serverInstance, clients)
        compareResult()
    }

    private fun performTest(server: Server<String>, clients: List<Client<String>>) {
        val start = System.nanoTime()
        val fileReader = FileReader(testFile)
        server.setHandler(messageInterceptor)
        executor.execute { server.start() }
        clients.forEach { it.start() }

        var currentClient = 0
        var string = fileReader.getString()
        val latch = CountDownLatch(fileReader.getLinesNumber().toInt())
        while (string != null) {
            println(string)
            val immutableString = string
            val immutableClientNumber = currentClient
            executor.execute {
                clients[immutableClientNumber].send(immutableString)
                latch.countDown()
            }
            currentClient++

            if (currentClient == clients.size) {
                currentClient = 0
            }
            string = fileReader.getString()
            println(string)
        }
        println("to wait")
        latch.await()
        fileReader.close()
        executor.shutdown()
        server.stop()
        clients.forEach { it.stop() }

        val finish = System.nanoTime()
        println(TimeUnit.NANOSECONDS.toMillis(finish - start))
    }

    private fun compareResult() {
        val executor = Executors.newFixedThreadPool(10, NamedThreadFactory("checker"))
        println("Start file checking")
        val testFileReader = FileReader(testFile)
        val linesNumber = testFileReader.getLinesNumber()
        val latch = CountDownLatch(linesNumber.toInt())
        val noError = AtomicBoolean(true)
        for (i in 0..linesNumber) {
            val testFileRow = testFileReader.getString()
            executor.execute {
                val tempFileReader = FileReader(tempFileName)
                var tempFileRow = tempFileReader.getString()
                var isTestRowFound = false
                while (tempFileRow != null) {
                    if (testFileRow == tempFileRow) {
                        isTestRowFound = true
                        break
                    }
                    tempFileRow = tempFileReader.getString()
                }
                tempFileReader.close()
                latch.countDown()
                if (!isTestRowFound) {
                    noError.set(true)
                    println(testFileRow)
                    throw IllegalStateException("Files are different")
                }
            }
        }

        while (latch.count > 0 && noError.get()) {
            println(latch.count)
        }
        executor.shutdown()
        if (noError.get()) {
            print("Files are equal")
        } else {
            print("Files are different")
        }
    }
}
