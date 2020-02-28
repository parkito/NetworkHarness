package ru.siksmfp.network.harness.benchmarking.benchmark

import org.openjdk.jmh.annotations.Benchmark
import ru.siksmfp.network.harness.benchmarking.api.AbstractBenchmark
import ru.siksmfp.network.harness.benchmarking.execution.BenchmarkExecutor
import ru.siksmfp.network.harness.benchmarking.execution.state.NioState

open class NioBenchmark : AbstractBenchmark() {

    @Benchmark
    fun nioSmallFileBenchmark(state: NioState): Boolean {
        return BenchmarkExecutor(state.getPropertyForSmall())
                .executeTest()
    }

    @Benchmark
    fun nioMiddleFileBenchmark(state: NioState): Boolean {
        return BenchmarkExecutor(state.getPropertyForMiddle())
                .executeTest()
    }

    @Benchmark
    fun nioBigFileBenchmark(state: NioState): Boolean {
        return BenchmarkExecutor(state.getPropertyForBig())
                .executeTest()
    }

    @Benchmark
    fun nioLargeFileBenchmark(state: NioState): Boolean {
        return BenchmarkExecutor(state.getPropertyForLarge())
                .executeTest()
    }
}
