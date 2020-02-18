package ru.siksmfp.network.play.tcp.testing.execution

import ru.siksmfp.network.play.api.Client
import ru.siksmfp.network.play.api.Server
import ru.siksmfp.network.play.tcp.testing.file.FileComparator
import ru.siksmfp.network.play.tcp.testing.file.FileReader
import ru.siksmfp.network.play.tcp.testing.support.MessageInterceptor
import ru.siksmfp.network.play.tcp.testing.support.NamedThreadFactory
import ru.siksmfp.network.play.tcp.testing.support.getHomeFolderPath
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit.NANOSECONDS
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class TestExecutor(
        private val property: TestProperty
) {
    private val tempFileName = "${getHomeFolderPath()}/Downloads/temp${LocalDateTime.now()}.txt"
    private val executor = Executors.newFixedThreadPool(property.clientTestThreads, NamedThreadFactory("client"))

    fun executeTest() {
        val server = constructServer(property)
        val clients = constructClients(property)
        performTest(server, clients)
        FileComparator(property.testFile, tempFileName).compare()
    }

    private fun constructServer(property: TestProperty): Server<String> {
        return getConstructor(property.serverClass).call(8081, property.serverThreads) as Server<String>
    }

    private fun constructClients(property: TestProperty): List<Client<String>> {
        val constructor = getConstructor(property.clientClass)
        return IntRange(0, 5)
                .map { constructor.call("localhost", 8081) as Client<String> }
                .toList()
    }

    private fun getConstructor(clazz: KClass<out Any>): KFunction<Any> {
        return clazz.constructors.toList()[0]
    }

    private fun performTest(server: Server<String>, clients: List<Client<String>>) {
        val start = System.nanoTime()

        prepareTest(server, clients)
        runTest(clients)
        finishTest(server, clients)

        val finish = System.nanoTime()
        println(NANOSECONDS.toMillis(finish - start))
    }

    private fun prepareTest(server: Server<String>, clients: List<Client<String>>) {
        val messageInterceptor = MessageInterceptor(tempFileName)
        server.setHandler(messageInterceptor)
        executor.execute { server.start() }
        clients.forEach { it.start() }
    }

    private fun runTest(clients: List<Client<String>>) {
        val fileReader = FileReader(property.testFile)
        fileReader.use {
            val linesNumber = fileReader.lineAmount()
            val latch = CountDownLatch(linesNumber.toInt())
            var currentClient = 0
            for (currentLine in 0 until linesNumber) {
                val string = fileReader.nextLine()
                val immutableClientNumber = currentClient
                executor.execute {
                    clients[immutableClientNumber].send(string!!)
                    latch.countDown()
                }
                currentClient++

                if (currentClient == clients.size) {
                    currentClient = 0
                }
            }
            latch.await()
        }
    }

    private fun finishTest(server: Server<String>, clients: List<Client<String>>) {
        executor.shutdown()
        server.stop()
        clients.forEach { it.stop() }
    }
}
