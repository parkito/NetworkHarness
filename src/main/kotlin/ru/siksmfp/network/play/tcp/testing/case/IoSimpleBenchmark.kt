package ru.siksmfp.network.play.tcp.testing.case

import org.openjdk.jmh.annotations.Benchmark
import ru.siksmfp.network.play.tcp.io.simple.IoClient
import ru.siksmfp.network.play.tcp.io.simple.IoServer
import ru.siksmfp.network.play.tcp.testing.execution.TestExecutor
import ru.siksmfp.network.play.tcp.testing.execution.TestProperty

class IoBenchmark : AbstractBenchmark() {

    @Benchmark
    fun smallFileBenchmark() {

    }

    @Benchmark
    fun middleFileBenchmark() {

    }

    @Benchmark
    fun bigFileBenchmark() {

    }

    @Benchmark
    fun largeFileBenchmark() {

    }
}

fun main() {

//    TestExecutor(property).executeTest()
}
