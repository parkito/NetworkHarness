package ru.siksmfp.network.play.benchmarking.benchmark

import org.openjdk.jmh.annotations.Benchmark
import ru.siksmfp.network.play.benchmarking.api.AbstractBenchmark
import ru.siksmfp.network.play.benchmarking.execution.BenchmarkExecutor
import ru.siksmfp.network.play.benchmarking.execution.state.IoSimpleFewThreadsState

open class IoSimpleFewThreadBenchmark : AbstractBenchmark() {

    @Benchmark
    fun smallFileBenchmark(state: IoSimpleFewThreadsState): Boolean {
        return BenchmarkExecutor(state.getPropertyForSmall())
                .executeTest()
    }

    @Benchmark
    fun middleFileBenchmark(state: IoSimpleFewThreadsState): Boolean {
        return BenchmarkExecutor(state.getPropertyForMiddle())
                .executeTest()
    }

    @Benchmark
    fun bigFileBenchmark(state: IoSimpleFewThreadsState): Boolean {
        return BenchmarkExecutor(state.getPropertyForBig())
                .executeTest()
    }

    @Benchmark
    fun largeFileBenchmark(state: IoSimpleFewThreadsState): Boolean {
        return BenchmarkExecutor(state.getPropertyForLarge())
                .executeTest()
    }
}
