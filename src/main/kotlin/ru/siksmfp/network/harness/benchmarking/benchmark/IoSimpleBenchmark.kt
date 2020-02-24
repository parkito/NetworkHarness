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
}
