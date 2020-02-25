package ru.siksmfp.network.harness.benchmarking.benchmark

import org.openjdk.jmh.annotations.Benchmark
import ru.siksmfp.network.harness.benchmarking.api.AbstractBenchmark
import ru.siksmfp.network.harness.benchmarking.execution.BenchmarkExecutor
import ru.siksmfp.network.harness.benchmarking.execution.state.IoSimpleFewThreadsState

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
