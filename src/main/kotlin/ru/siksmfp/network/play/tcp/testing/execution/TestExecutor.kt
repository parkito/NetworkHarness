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
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class TestExecutor(
        private val property: TestProperty
) {
    private val tempFileName = "${getHomeFolderPath()}/Downloads/temp${LocalDateTime.now()}.txt"
    private val executor = Executors.newFixedThreadPool(5, NamedThreadFactory("client"))
    private val messageInterceptor = MessageInterceptor(tempFileName)

    fun executeTest() {
        val server = constructServer(property)
        val clients = constructClients(property)
        performTest(server, clients)
        FileComparator(property.testFile, tempFileName).compare()
    }

    private fun constructServer(property: TestProperty): Server<String> {
        return getConstructor(property.serverClass).call(8081) as Server<String>
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
        val fileReader = FileReader(property.testFile)
        server.setHandler(messageInterceptor)
        executor.execute { server.start() }
        clients.forEach { it.start() }

        var currentClient = 0
        fileReader.use {
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
            latch.await()
        }
        executor.shutdown()
        server.stop()
        clients.forEach { it.stop() }

        val finish = System.nanoTime()
        println(TimeUnit.NANOSECONDS.toMillis(finish - start))
    }
}
