package ru.siksmfp.network.play.tcp.testing.case

import ru.siksmfp.network.play.tcp.io.simple.IoClient
import ru.siksmfp.network.play.tcp.io.simple.IoServer
import ru.siksmfp.network.play.tcp.testing.execution.TestExecutor
import ru.siksmfp.network.play.tcp.testing.execution.TestProperty
import ru.siksmfp.network.play.tcp.testing.support.getHomeFolderPath

fun main() {
    val property = TestProperty(
            serverClass = IoServer::class,
            clientClass = IoClient::class,
            testFile = "${getHomeFolderPath()}/Downloads/test.txt",
            clientTestThreads = 5,
            serverThreads = 7
    )
    TestExecutor(property).executeTest()
}
