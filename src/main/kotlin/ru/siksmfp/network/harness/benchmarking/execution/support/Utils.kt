package ru.siksmfp.network.harness.benchmarking.execution.support

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

fun createExecutor(threadNumber: Int?): ExecutorService {
    return if (threadNumber != null) {
        Executors.newFixedThreadPool(threadNumber, NamedThreadFactory("server"))
    } else {
        Executors.newCachedThreadPool()
    }
}