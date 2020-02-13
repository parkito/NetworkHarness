package ru.siksmfp.network.play.tcp.testing

import ru.siksmfp.network.play.api.Client
import ru.siksmfp.network.play.api.Server
import java.util.concurrent.Executors
import kotlin.reflect.KClass

class TestExecutor(
        private val serverClass: KClass<out Any>,
        private val clientClass: KClass<out Any>,
        private val testFile: String
) {
    private val fileReader = FileReader(testFile)
    private val fileWriter = FileWriter("/Users/parkito/Downloads/temp.txt")
    private val executor = Executors.newFixedThreadPool(15)
    private val messageInterceptor = MessageInterceptor(fileWriter)

    fun executeTest() {
        val serverClassConstructor = serverClass.constructors.toList()[0]
        val clientClassConstructor = clientClass.constructors.toList()[0]
        val serverInstance = serverClassConstructor.call(8081) as Server<String>
        val clients = IntRange(0, 100)
                .map { clientClassConstructor.call("localhost", 8081) as Client<String> }
                .toList()

        performTest(serverInstance, clients)
    }

    private fun performTest(server: Server<String>, clients: List<Client<String>>) {
        executor.execute { server.start() }
        clients.forEach { it.start() }
        var currentClient = 0
        do {
            val string = fileReader.getString()
            if (string != null) {
                executor.execute {
                    clients[currentClient].send(string)
                }
                currentClient++
            }

            if (currentClient >= clients.size) {
                currentClient = 0
            }

        } while (string != null)

    }

    private fun compareResult() {

    }
}