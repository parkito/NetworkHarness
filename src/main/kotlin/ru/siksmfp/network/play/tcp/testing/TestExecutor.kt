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
    private val tempFileName = "/Users/parkito/Downloads/temp.txt"
    private val fileWriter = FileWriter(tempFileName)
    private val executor = Executors.newFixedThreadPool(5)
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
        val fileReader = FileReader(testFile)
        server.setHandler(messageInterceptor)
        executor.execute { server.start() }
        clients.forEach { it.start() }

        var currentClient = 0
        var string = fileReader.getString()
        while (string != null) {
            executor.execute {
                clients[currentClient].send(string!!)
                currentClient++
            }

            if (currentClient >= clients.size) {
                currentClient = 0
            }
            string = fileReader.getString()
        }
    }

    private fun compareResult() {
        val testFileReader = FileReader(testFile)
        val testFileRow = testFileReader.getString()
        while (testFileRow != null) {
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
            if (!isTestRowFound) {
                throw IllegalStateException("Files are different")
            }
        }
    }
}
