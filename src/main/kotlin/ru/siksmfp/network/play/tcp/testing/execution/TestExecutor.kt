package ru.siksmfp.network.play.tcp.testing.execution

import ru.siksmfp.network.play.api.Client
import ru.siksmfp.network.play.api.Server
import ru.siksmfp.network.play.tcp.testing.file.FileReader
import ru.siksmfp.network.play.tcp.testing.file.FileWriter
import ru.siksmfp.network.play.tcp.testing.support.MessageInterceptor
import ru.siksmfp.network.play.tcp.testing.support.NamedThreadFactory
import ru.siksmfp.network.play.tcp.testing.support.getHomeFolderPath
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
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
        FileComparator(testFile, tempFileName).compare()
    }

    private fun performTest(server: Server<String>, clients: List<Client<String>>) {
        val start = System.nanoTime()
        val fileReader = FileReader(testFile)
        server.setHandler(messageInterceptor)
        executor.execute { server.start() }
        clients.forEach { it.start() }

        var currentClient = 0
        var string = fileReader.nextLine()
        val latch = CountDownLatch(fileReader.lineAmount().toInt())
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
            string = fileReader.nextLine()
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
}
