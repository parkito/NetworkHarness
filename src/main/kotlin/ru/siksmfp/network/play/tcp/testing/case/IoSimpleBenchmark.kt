package ru.siksmfp.network.play.tcp.testing.case

import org.openjdk.jmh.annotations.Benchmark
import ru.siksmfp.network.play.tcp.testing.execution.TestExecutor
import ru.siksmfp.network.play.tcp.testing.state.IoSimpleFewThreadsState

class IoSimpleFewThreadBenchmark : AbstractBenchmark() {

    @Benchmark
    fun smallFileBenchmark(state: IoSimpleFewThreadsState): Boolean {
        return TestExecutor(state.getPropertyForSmall())
                .executeTest()
    }

    @Benchmark
    fun middleFileBenchmark(state: IoSimpleFewThreadsState): Boolean {
        return TestExecutor(state.getPropertyForMiddle())
                .executeTest()
    }

    @Benchmark
    fun bigFileBenchmark(state: IoSimpleFewThreadsState): Boolean {
        return TestExecutor(state.getPropertyForBig())
                .executeTest()
    }

    @Benchmark
    fun largeFileBenchmark(state: IoSimpleFewThreadsState): Boolean {
        return TestExecutor(state.getPropertyForLarge())
                .executeTest()
    }
}
