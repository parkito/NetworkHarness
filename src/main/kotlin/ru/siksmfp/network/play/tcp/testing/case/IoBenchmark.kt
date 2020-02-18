package ru.siksmfp.network.play.tcp.testing.case

import org.openjdk.jmh.annotations.Benchmark
import ru.siksmfp.network.play.tcp.io.simple.IoClient
import ru.siksmfp.network.play.tcp.io.simple.IoServer
import ru.siksmfp.network.play.tcp.testing.execution.TestExecutor
import ru.siksmfp.network.play.tcp.testing.execution.TestProperty

class IoBenchmark {

    @Benchmark
    fun smallFileBenchmark() {

    }
}

fun main() {
    val property = TestProperty(
            serverClass = IoServer::class,
            clientClass = IoClient::class,
            clientTestThreads = 5,
            serverThreads = 7,
            testFileSize = 1_000_000
    )
    TestExecutor(property).executeTest()
}
