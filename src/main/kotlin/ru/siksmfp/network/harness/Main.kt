package ru.siksmfp.network.harness

import ru.siksmfp.network.harness.benchmarking.benchmark.NioBenchmark
import ru.siksmfp.network.harness.benchmarking.execution.state.NioState

fun main(args: Array<String>) {
//    org.openjdk.jmh.Main.main(arrayOf("nioSmallFileBenchmark"));
    NioBenchmark().nioSmallFileBenchmark(NioState())
}
