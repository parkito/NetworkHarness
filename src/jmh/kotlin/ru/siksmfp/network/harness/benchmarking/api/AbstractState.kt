package ru.siksmfp.network.harness.benchmarking.api

import ru.siksmfp.network.harness.api.Client
import ru.siksmfp.network.harness.api.Server
import ru.siksmfp.network.harness.benchmarking.execution.model.BenchmarkProperty
import kotlin.reflect.KClass

abstract class AbstractState(
        private val serverClass: KClass<out Server<*>>,
        private val clientClass: KClass<out Client<*>>,
        private val clientTestThreads: Int,
        private val serverThreads: Int
) {

    fun getPropertyForSmall(): BenchmarkProperty {
        return BenchmarkProperty(
                serverClass,
                clientClass,
                clientTestThreads,
                serverThreads,
                testFileSize = 10
        )
    }

    fun getPropertyForMiddle(): BenchmarkProperty {
        return BenchmarkProperty(
                serverClass,
                clientClass,
                clientTestThreads,
                serverThreads,
                testFileSize = 1_000
        )
    }

    fun getPropertyForBig(): BenchmarkProperty {
        return BenchmarkProperty(
                serverClass,
                clientClass,
                clientTestThreads,
                serverThreads,
                testFileSize = 100_000
        )
    }

    fun getPropertyForLarge(): BenchmarkProperty {
        return BenchmarkProperty(
                serverClass,
                clientClass,
                clientTestThreads,
                serverThreads,
                testFileSize = 1_000_000
        )
    }
}