package ru.siksmfp.network.harness.benchmarking.benchmark

import org.openjdk.jmh.annotations.Benchmark
import ru.siksmfp.network.harness.benchmarking.api.AbstractBenchmark
import ru.siksmfp.network.harness.benchmarking.execution.BenchmarkExecutor
import ru.siksmfp.network.harness.benchmarking.execution.state.IoState

open class IoBenchmark : AbstractBenchmark() {

    @Benchmark
    fun ioSmallFileBenchmark(state: IoState): Boolean {
        return BenchmarkExecutor(state.getPropertyForSmall())
                .executeTest()
    }

    @Benchmark
    fun ioMiddleFileBenchmark(state: IoState): Boolean {
        return BenchmarkExecutor(state.getPropertyForMiddle())
                .executeTest()
    }

    @Benchmark
    fun ioBigFileBenchmark(state: IoState): Boolean {
        return BenchmarkExecutor(state.getPropertyForBig())
                .executeTest()
    }

    @Benchmark
    fun ioLargeFileBenchmark(state: IoState): Boolean {
        return BenchmarkExecutor(state.getPropertyForLarge())
                .executeTest()
    }
}
